# Programmation fonctionnelle (Functional Programming)

## 1. Définition

La programmation fonctionnelle est un paradigme de programmation qui consiste à construire un logiciel à partir de fonctions, en privilégiant l'immutabilité, la composition et la limitation des effets de bord.

## 2. Principes fondamentaux

| Principe | Définition |
|---|---|
| **Fonction pure (Pure Function)** | Fonction qui retourne toujours le même résultat pour les mêmes entrées et qui ne produit aucun effet de bord. |
| **Immutabilité (Immutability)** | État d'une donnée qui ne peut pas être modifié après sa création. Toute transformation produit une nouvelle valeur. |
| **Limitation des effets de bord (Side Effects)** | Réduire les modifications de l'état externe, les entrées/sorties et les interactions qui rendent le comportement moins prévisible. |
| **Fonctions de première classe (First-Class Functions)** | Les fonctions peuvent être affectées à des variables, transmises en paramètres et retournées par d'autres fonctions. |
| **Fonctions d'ordre supérieur (Higher-Order Functions)** | Fonctions qui acceptent d'autres fonctions en paramètres ou retournent des fonctions. |
| **Composition de fonctions (Function Composition)** | Construction de fonctions complexes à partir de fonctions plus simples. |
| **Déclarativité (Declarative Programming)** | Décrire le résultat attendu plutôt que détailler toutes les étapes nécessaires à son obtention. |

## 3. Fonctionnalités Java permettant la programmation fonctionnelle

Java est un langage multi-paradigme. Depuis Java 8, il intègre plusieurs fonctionnalités permettant d'adopter les principes de la programmation fonctionnelle.

### 3.1. Expressions lambda (Lambda Expressions)

Permettent de représenter une fonction de manière concise et de transmettre un comportement comme paramètre.

### 3.2. Interfaces fonctionnelles (Functional Interfaces)

Interfaces contenant une seule méthode abstraite, pouvant être implémentées par une expression lambda.

Principales interfaces de `java.util.function` :

| Interface | Rôle |
|---|---|
| `Predicate<T>` | Tester une condition et retourner un booléen. |
| `Function<T, R>` | Transformer une valeur de type T en une valeur de type R. |
| `Consumer<T>` | Consommer une valeur sans retourner de résultat. |
| `Supplier<T>` | Fournir une valeur sans recevoir de paramètre. |
| `BiFunction<T, U, R>` | Accepter deux paramètres et retourner un résultat. |
| `UnaryOperator<T>` | Transformer une valeur en une autre valeur du même type. |
| `BinaryOperator<T>` | Combiner deux valeurs du même type pour produire une valeur de ce type. |

### 3.3. Références de méthodes (Method References)

Permettent de référencer directement une méthode existante au lieu d'écrire une expression lambda équivalente.

### 3.4. Stream API

Permet de traiter des collections de données sous forme de séquences d'opérations déclaratives.

Principales opérations :

| Opération | Rôle |
|---|---|
| `filter()` | Filtrer les éléments selon une condition. |
| `map()` | Transformer les éléments. |
| `flatMap()` | Transformer et aplatir plusieurs flux. |
| `sorted()` | Trier les éléments. |
| `distinct()` | Éliminer les doublons. |
| `limit()` | Limiter le nombre d'éléments. |
| `reduce()` | Agréger les éléments en un résultat. |
| `collect()` | Rassembler les éléments dans une collection ou un résultat. |
| `forEach()` | Appliquer une action à chaque élément. |

### 3.5. Optional

Représente une valeur potentiellement absente et permet de traiter explicitement les cas de présence ou d'absence, en limitant les risques de `NullPointerException`.

### 3.6. Records

Introduits en Java 16, les `records` permettent de déclarer des classes destinées principalement à transporter des données, avec des champs privés et finaux et des accesseurs générés automatiquement.

Ils favorisent l'immutabilité des objets de données et s'intègrent naturellement dans une approche fonctionnelle.

### 3.7. Collections immuables

Java propose des méthodes de création de collections non modifiables, notamment :

- `List.of()`
- `Set.of()`
- `Map.of()`

Elles facilitent la manipulation de données sans modification de l'état initial.

## 4. Synthèse

| Principe fonctionnel | Fonctionnalités Java associées |
|---|---|
| Fonctions de première classe | Expressions lambda, interfaces fonctionnelles |
| Fonctions d'ordre supérieur | Interfaces fonctionnelles, Stream API |
| Composition de fonctions | `andThen()`, `compose()`, Stream API |
| Immutabilité | Records, collections immuables |
| Limitation des effets de bord | Stream API, collections immuables |
| Déclarativité | Stream API |
| Gestion des valeurs absentes | Optional |

**À retenir :** Java ne constitue pas un langage purement fonctionnel. Il permet de combiner programmation impérative, orientée objet et fonctionnelle au sein d'une même application.