# Programmation orientée objet (POO)

## 1. Définition

La programmation orientée objet (POO) est un paradigme de programmation qui organise le logiciel autour d'objets regroupant des attributs représentant leur état et des méthodes définissant leurs comportements.

Elle repose sur la modélisation des entités métier et de leurs interactions afin de concevoir des applications modulaires, extensibles et maintenables.

## 2. Concepts fondamentaux

| Concept | Définition |
|---|---|
| Classe (Class) | Modèle définissant la structure et le comportement des objets. |
| Objet (Object) | Instance d'une classe possédant un état et un comportement. |
| Attribut (Attribute) | Donnée déclarée dans une classe et représentant une caractéristique d'un objet. |
| Méthode (Method) | Opération définissant le comportement d'un objet. |
| Constructeur (Constructor) | Mécanisme permettant d'initialiser un objet lors de sa création. |
| Interface (Interface) | Contrat définissant les opérations que les classes implémentantes doivent respecter. |
| Classe abstraite (Abstract Class) | Classe non instanciable pouvant contenir des attributs, des constructeurs, des méthodes concrètes et des méthodes abstraites. |

## 3. Les quatre piliers de la POO

| Principe | Définition |
|---|---|
| Encapsulation | Protection de l'état interne d'un objet en contrôlant l'accès à ses attributs et en exposant des opérations maîtrisées. |
| Héritage | Mécanisme permettant à une classe de récupérer et de spécialiser les caractéristiques d'une autre classe. |
| Polymorphisme | Capacité à manipuler différents types d'objets à travers une même abstraction, tout en bénéficiant de comportements spécifiques. |
| Abstraction | Définition des fonctionnalités essentielles en masquant les détails de leur implémentation. |

### 3.1. Abstraction et implémentation

L'abstraction définit ce qu'un objet doit faire, tandis que l'implémentation définit comment ce comportement est réalisé.

En Java, l'abstraction est principalement mise en œuvre à travers les interfaces et les classes abstraites.

| Notion | Rôle |
|---|---|
| Abstraction | Expose les fonctionnalités essentielles sans révéler les détails internes. |
| Interface | Définit un contrat que les classes implémentantes doivent respecter. |
| Classe abstraite | Définit une abstraction pouvant combiner comportements communs et méthodes abstraites. |
| Implémentation | Fournit la logique concrète des fonctionnalités définies par une abstraction. |

**Exemple :**

```java
public interface Payment {
    void pay(double amount);
}

public class CardPayment implements Payment {

    @Override
    public void pay(double amount) {
        // Implémentation du paiement par carte
    }
}
```

Le code client utilise le contrat `Payment` sans dépendre des détails de l'implémentation concrète.

## 4. Les formes de polymorphisme

### 4.1. Polymorphisme par surcharge (Overloading)

Plusieurs méthodes portent le même nom, mais possèdent des signatures différentes (nombre, type ou ordre des paramètres).

- Résolution statique à la compilation.
- Exemple : plusieurs méthodes `calculate()` avec des paramètres différents.

### 4.2. Polymorphisme par redéfinition (Overriding)

Une sous-classe fournit une implémentation spécifique d'une méthode héritée.

- Résolution dynamique à l'exécution.
- Basé sur l'héritage.
- Exemple : une méthode `draw()` redéfinie dans les classes `Circle` et `Rectangle`.

### 4.3. Polymorphisme par sous-typage (Subtype Polymorphism)

Permet de manipuler différents objets à travers un type parent commun (classe parente ou interface).

- Le comportement effectif dépend du type réel de l'objet.
- Permet de remplacer une implémentation par une autre sans modifier le code client.
- Exemple : `CardPayment` et `CashPayment` manipulés à travers l'interface `Payment`.

### 4.4. Polymorphisme paramétrique (Parametric Polymorphism)

Permet de définir des classes, interfaces et méthodes génériques capables de fonctionner avec différents types de données sans dupliquer le code.

- Utilise les génériques (Generics).
- Le type est défini lors de l'utilisation.
- Exemple : `List<String>`, `List<Integer>` ou une méthode générique `identity(T value)`.

```java
public class Box<T> {

    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
```

## 5. Relations entre objets

| Relation | Définition | Exemple |
|---|---|---|
| Association | Relation entre deux objets pouvant interagir. | `Customer` utilise `BankAccount`. |
| Agrégation | Relation HAS-A où les objets conservent des cycles de vie indépendants. | `Department` possède des `Employee`. |
| Composition | Relation HAS-A forte où le cycle de vie de la partie dépend du tout. | `House` contient des `Room`. |
| Héritage | Relation de spécialisation IS-A. | `Dog extends Animal`. |
| Dépendance | Relation d'utilisation temporaire d'un autre objet. | `OrderService` utilise `EmailService` dans une méthode. |

**À retenir :**
- Association : interagit avec.
- Agrégation : possède indépendamment.
- Composition : contient avec dépendance du cycle de vie.
- Héritage : est un.
- Dépendance : utilise temporairement.

## 6. Composition et héritage

Deux mécanismes permettent de structurer et de réutiliser les comportements :

- **Héritage (IS-A)** : spécialisation d'une classe existante.
- **Composition (HAS-A)** : construction d'un objet à partir d'autres objets qui collaborent entre eux.

La composition est généralement à privilégier lorsque la relation de spécialisation n'est pas justifiée.

## 7. Bonnes pratiques en POO

- Respecter l'encapsulation et contrôler l'accès à l'état interne des objets.
- Privilégier la composition lorsque l'héritage n'est pas nécessaire.
- Programmer contre des interfaces plutôt que contre des implémentations concrètes.
- Utiliser les classes abstraites pour factoriser les comportements communs.
- Favoriser des classes cohérentes avec des responsabilités bien définies.
- Éviter les hiérarchies d'héritage trop profondes.
- Limiter le couplage entre les objets.
- Favoriser l'immuabilité lorsque cela est pertinent.
- Garantir la cohérence de l'état des objets.

## 8. Avantages et limites

| Avantages | Limites |
|---|---|
| Modularité | Risque de sur-ingénierie |
| Réutilisabilité | Complexité des hiérarchies d'héritage |
| Maintenabilité | Multiplication des abstractions |
| Extensibilité | Coût de conception initial |
| Encapsulation | Risque de couplage excessif |

## Références

- [Documentation officielle Java](https://docs.oracle.com/en/java/)
- [Java Language Specification](https://docs.oracle.com/javase/specs/)