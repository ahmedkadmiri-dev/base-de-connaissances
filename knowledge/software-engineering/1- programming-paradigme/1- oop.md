# Programmation orientée objet (POO)

## 1. Définition

La programmation orientée objet (POO) est un paradigme de programmation qui organise le logiciel autour d'objets regroupant des attributs représentant leur état et des méthodes définissant leurs comportements.

## 2. Concepts fondamentaux

| Concept | Définition |
|---|---|
| Classe (Class) | Modèle définissant la structure et le comportement des objets. |
| Objet (Object) | Instance d'une classe possédant un état et un comportement. |
| Attribut (Attribute) | Donnée représentant une caractéristique d'un objet. |
| Méthode (Method) | Opération définissant le comportement d'un objet. |
| Constructeur (Constructor) | Mécanisme d'initialisation d'un objet. |
| Interface (Interface) | Contrat définissant les comportements à implémenter. |
| Classe abstraite (Abstract Class) | Classe non instanciable pouvant définir des attributs et des méthodes abstraites ou concrètes. |

## 3. Les quatre piliers de la POO

| Principe | Définition |
|---|---|
| Encapsulation | Protection de l'état interne d'un objet en contrôlant l'accès à ses attributs. |
| Héritage | Mécanisme permettant à une classe de récupérer et spécialiser les caractéristiques d'une autre classe. |
| Polymorphisme | Capacité à manipuler différents types d'objets à travers une même abstraction. |
| Abstraction | Exposition des fonctionnalités essentielles en masquant les détails d'implémentation. |

### 3.1. Abstraction et implémentation

L'abstraction définit **ce qui doit être fait**, tandis que l'implémentation définit **comment cela est réalisé**.

En Java :
- **Interface :** définit un contrat à respecter.
- **Classe abstraite :** définit un contrat et peut fournir des comportements communs.
- **Implémentation :** fournit la logique concrète des comportements définis.

## 4. Les formes de polymorphisme

| Forme | Définition |
|---|---|
| Surcharge (Overloading) | Plusieurs méthodes portent le même nom avec des signatures différentes. Résolution statique à la compilation. |
| Redéfinition (Overriding) | Une sous-classe fournit une implémentation spécifique d'une méthode héritée. Résolution dynamique à l'exécution. |
| Sous-typage (Subtype Polymorphism) | Manipulation de différents objets à travers un type parent commun (classe ou interface). |
| Paramétrique (Parametric Polymorphism) | Utilisation de paramètres de type (Generics) pour concevoir des classes, interfaces et méthodes compatibles avec différents types. |

## 5. Relations entre objets

| Relation | Définition | Exemple |
|---|---|---|
| Association | Relation entre deux objets pouvant interagir. | `Customer` utilise `BankAccount`. |
| Agrégation | Relation HAS-A avec des cycles de vie indépendants. | `Department` possède des `Employee`. |
| Composition | Relation HAS-A forte avec dépendance du cycle de vie. | `House` contient des `Room`. |
| Héritage | Relation de spécialisation IS-A. | `Dog` est un `Animal`. |
| Dépendance | Utilisation ponctuelle d'un autre objet. | `OrderService` utilise `EmailService`. |

## 6. Composition et héritage

- **Héritage (IS-A) :** spécialisation d'une classe existante.
- **Composition (HAS-A) :** assemblage d'objets collaborant entre eux.

Privilégier la composition lorsque la relation de spécialisation n'est pas justifiée.

## 7. Bonnes pratiques

- Respecter l'encapsulation et contrôler l'accès aux attributs.
- Privilégier la composition plutôt que l'héritage lorsque pertinent.
- Programmer contre des interfaces plutôt que des implémentations concrètes.
- Favoriser des classes cohérentes et faiblement couplées.
- Éviter les hiérarchies d'héritage trop profondes.
- Favoriser l'immuabilité lorsque cela est pertinent.
- Garantir la cohérence de l'état des objets.

## 8. Avantages et limites

| Avantages | Limites |
|---|---|
| Modularité | Risque de sur-ingénierie |
| Réutilisabilité | Complexité des hiérarchies |
| Maintenabilité | Multiplication des abstractions |
| Extensibilité | Coût de conception initial |
| Encapsulation | Risque de couplage excessif |