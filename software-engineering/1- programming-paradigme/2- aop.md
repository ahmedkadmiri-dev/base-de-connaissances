# Aspect-Oriented Programming (AOP)

La **Programmation Orientée Aspect (AOP)** est un paradigme qui permet de séparer les préoccupations transversales (*cross-cutting concerns*) de la logique métier.

Exemples de préoccupations transversales :
- Logging
- Gestion des transactions
- Sécurité et autorisation
- Monitoring et mesure du temps d'exécution
- Gestion des exceptions

## 1. Concepts fondamentaux

| Concept | Description |
|---|---|
| Aspect | Classe qui regroupe une préoccupation transversale |
| Advice | Action exécutée à un moment précis |
| Join Point | Point d'exécution pouvant être intercepté |
| Pointcut | Expression qui sélectionne les points d'interception |
| Target | Objet dont l'exécution est interceptée |
| Weaving | Mécanisme d'association de l'aspect au code cible |
| Proxy | Objet intermédiaire qui intercepte les appels vers la cible dans Spring AOP |

## 2. Types d'Advice

| Advice | Description |
|---|---|
| `@Before` | Exécuté avant la méthode |
| `@After` | Exécuté après la méthode, qu'elle réussisse ou échoue |
| `@AfterReturning` | Exécuté après une exécution réussie |
| `@AfterThrowing` | Exécuté lorsqu'une exception est levée |
| `@Around` | Encadre l'exécution : avant et après, avec contrôle de `proceed()` |

## 3. Exemple avec Spring AOP

**Dépendance Maven :**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

**Service métier :**

```java
@Service
public class PaymentService {

    public void pay() {
        System.out.println("Paiement effectué");
    }
}
```

**Aspect de logging :**

```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.example.service..*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();

        long start = System.currentTimeMillis();

        log.info("Début : {}", method);

        try {
            Object result = joinPoint.proceed();

            log.info("Fin : {} - {} ms",
                    method, System.currentTimeMillis() - start);

            return result;
        } catch (Throwable ex) {
            log.error("Erreur dans : {}", method, ex);
            throw ex;
        }
    }
}
```

**Utilisation :**

```java
paymentService.pay();
```

L'appel passe par le proxy Spring, qui exécute l'aspect avant et après la méthode métier.

## 4. Pointcut : sélectionner les méthodes

Les expressions Pointcut permettent de cibler les méthodes à intercepter.

```java
// Toutes les méthodes des services
execution(* com.example.service..*(..))

// Toutes les méthodes publiques
execution(public * com.example..*(..))

// Méthodes annotées avec @Loggable
@annotation(com.example.annotation.Loggable)

// Toutes les classes annotées avec @Loggable
@within(com.example.annotation.Loggable)
```

## 5. Limitations de Spring AOP

Spring AOP repose principalement sur des proxies :

- Seuls les appels passant par le proxy sont interceptés.
- L'auto-invocation (`this.maMethode()`) contourne le proxy.
- Les méthodes privées ne sont pas interceptées.
- Les méthodes finales ne peuvent pas être interceptées avec les proxies basés sur l'héritage (CGLIB).
- Spring AOP intercepte principalement les appels de méthodes des beans Spring.

## 6. AOP vs OOP

| OOP | AOP |
|---|---|
| Organise le code autour des objets | Sépare les préoccupations transversales |
| Encapsulation et héritage | Aspects et Advice |
| Logique métier | Logging, transactions, sécurité, etc. |

**À retenir :** AOP permet d'éviter de répéter le même code technique dans plusieurs classes métier. Spring utilise notamment AOP pour implémenter `@Transactional`, le logging et certaines fonctionnalités de sécurité.