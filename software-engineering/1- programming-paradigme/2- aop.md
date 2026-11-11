# Programmation orientée aspect (POA)

## 1. Définition

La programmation orientée aspect (POA ou AOP – Aspect-Oriented Programming) est un paradigme qui permet de séparer les préoccupations transversales (*Cross-Cutting Concerns*) de la logique métier.

Elle complète la POO en centralisant les traitements communs à plusieurs composants.

## 2. Préoccupations transversales (Cross-Cutting Concerns)

Ce sont des fonctionnalités qui concernent plusieurs parties d'une application et qui ne relèvent pas directement de la logique métier.

Exemples :
- Logging et traçabilité.
- Gestion des transactions.
- Sécurité et contrôle d'accès.
- Mesure des performances.
- Gestion des exceptions.

## 3. Concepts fondamentaux

### 3.1. Aspect

Un Aspect est un module qui regroupe un ou plusieurs traitements transversaux.

En Spring AOP, une classe est déclarée comme aspect avec `@Aspect`.

```java
@Aspect
@Component
public class LoggingAspect {
    // Définition des Pointcuts et Advices
}
```

### 3.2. Join Point (point de jonction)

Un Join Point représente un point potentiel d'exécution du programme auquel un aspect peut intervenir.

En Spring AOP, les Join Points correspondent principalement aux exécutions de méthodes.

Exemple :

```java
@Service
public class ClientService {

    public void saveClient() { }

    public void updateClient() { }

    public void deleteClient() { }
}
```

Chaque exécution de `saveClient()`, `updateClient()` ou `deleteClient()` constitue un Join Point potentiel.

### 3.3. Pointcut (point de coupe)

Un Pointcut est une expression qui permet de sélectionner les Join Points sur lesquels un aspect doit intervenir.

Exemple : sélectionner toutes les méthodes de `ClientService`.

```java
@Pointcut("execution(* com.example.service.ClientService.*(..))")
public void clientServiceMethods() {}
```

Autres exemples :

```java
// Méthodes dont le nom commence par save
@Pointcut("execution(* com.example.service.*.save*(..))")
public void saveMethods() {}

// Toutes les méthodes du package service et de ses sous-packages
@Pointcut("execution(* com.example.service..*(..))")
public void serviceMethods() {}
```

Expressions courantes :
- `*` : n'importe quel type ou nom selon sa position.
- `..` : zéro ou plusieurs paramètres ou sous-packages selon sa position.
- `execution(...)` : sélectionne les exécutions de méthodes.
- `within(...)` : sélectionne les méthodes appartenant à un type ou package.

### 3.4. Advice (conseil)

Un Advice définit l'action à exécuter et le moment où elle doit être exécutée lorsqu'un Join Point sélectionné est atteint.

Il existe cinq types principaux d'Advice en Spring AOP :

| Type | Fonctionnement |
|---|---|
| `@Before` | Exécute un traitement avant la méthode ciblée. |
| `@After` | Exécute un traitement après la méthode, qu'elle se termine normalement ou avec une exception. |
| `@AfterReturning` | Exécute un traitement uniquement après une exécution réussie. Permet de récupérer le résultat retourné. |
| `@AfterThrowing` | Exécute un traitement lorsqu'une exception est levée. Permet de récupérer l'exception. |
| `@Around` | Encadre l'exécution de la méthode et permet de contrôler son déroulement, son résultat ou sa durée. |

### 3.5. Target Object (objet cible)

Le Target Object est l'objet métier sur lequel l'aspect intervient.

Exemple :

```java
@Service
public class ClientService {

    public void saveClient() {
        System.out.println("Sauvegarde du client");
    }
}
```

Ici, `ClientService` est le Target Object.

### 3.6. Weaving (tissage)

Le Weaving est le mécanisme qui permet d'associer les aspects au programme afin d'appliquer les traitements transversaux aux points sélectionnés.

Il peut être réalisé :
- À la compilation (*Compile-Time Weaving*).
- Après compilation (*Post-Compile-Time Weaving*).
- Au chargement des classes (*Load-Time Weaving*).
- À l'exécution via des proxies, comme dans Spring AOP.

### 3.7. Proxy

Un Proxy est un objet intermédiaire qui intercepte les appels vers l'objet cible afin d'appliquer les traitements définis par les aspects.

Spring AOP utilise principalement deux mécanismes :
- JDK Dynamic Proxy : basé sur les interfaces.
- CGLIB Proxy : basé sur la création de sous-classes.

Le proxy permet notamment d'appliquer les Advices avant de déléguer l'appel à la méthode de l'objet cible.

## 4. Exemple complet avec Spring AOP

L'objectif est de mesurer automatiquement le temps d'exécution des méthodes du `ClientService`.

**Service métier :**

```java
@Service
public class ClientService {

    public void saveClient() {
        System.out.println("Sauvegarde du client");
    }

    public void updateClient() {
        System.out.println("Modification du client");
    }
}
```

**Aspect :**

```java
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.service.ClientService.*(..))")
    public void clientServiceMethods() {}

    @Around("clientServiceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println("Durée : " + duration + " ms");
        }
    }
}
```

**Utilisation :**

```java
clientService.saveClient();
```

L'appel passe par le proxy Spring, qui applique l'Advice associé au Pointcut avant et après l'exécution de la méthode cible.

## 5. Spring AOP vs AspectJ

| Spring AOP | AspectJ |
|---|---|
| Basé principalement sur des proxies. | Basé sur le weaving. |
| Intercepte principalement les exécutions de méthodes. | Offre des points d'interception plus étendus. |
| Intégration native avec Spring. | Fonctionne indépendamment de Spring. |
| Plus simple à mettre en œuvre. | Plus puissant et plus flexible. |

**Limites de Spring AOP :**
- Les appels internes entre méthodes d'un même objet (*Self-Invocation*) ne passent généralement pas par le proxy.
- Les méthodes privées ne sont pas interceptées par les proxies Spring AOP.
- L'interception dépend du passage de l'appel par le proxy.

## 6. Synthèse des concepts

| Concept | Rôle |
|---|---|
| Aspect | Regroupe les traitements transversaux. |
| Join Point | Point d'exécution potentiel auquel intervenir. |
| Pointcut | Sélectionne les Join Points concernés. |
| Advice | Définit l'action et le moment de son exécution. |
| Before / After / Around | Types d'Advice. |
| Target Object | Objet métier ciblé par l'aspect. |
| Proxy | Intermédiaire qui intercepte les appels. |
| Weaving | Mécanisme d'intégration des aspects. |

**À retenir :**
- Le Join Point représente le point d'exécution.
- Le Pointcut choisit où intervenir.
- L'Advice définit quoi faire et quand.
- L'Aspect regroupe les Pointcuts et les Advices.