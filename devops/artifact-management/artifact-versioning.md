# Artifact Versioning

## Définition

L'**Artifact Versioning** (versionnement des artefacts) consiste à attribuer une version unique à chaque publication d'un composant logiciel.

Il permet d'identifier les différentes évolutions d'un artefact, de gérer les dépendances entre applications et de garantir la traçabilité des composants utilisés lors des builds et des déploiements.

Les versions sont généralement définies dans les outils de build comme Maven ou Gradle, puis stockées et distribuées par un Artifact Repository comme Nexus.

## Semantic Versioning (SemVer)

Le **Semantic Versioning** est une convention de numérotation des versions qui suit le format :

```text
MAJOR.MINOR.PATCH
```

Exemple :

```text
2.4.1
```

| Partie | Signification | Exemple |
|---|---|---|
| MAJOR | Changement incompatible avec les versions précédentes | 2.0.0 |
| MINOR | Ajout de fonctionnalités compatibles | 2.1.0 |
| PATCH | Correction de bugs compatible | 2.1.1 |

### Règles principales

- **MAJOR** : incrémenter lorsque des changements incompatibles sont introduits.
- **MINOR** : incrémenter lorsqu'une nouvelle fonctionnalité compatible est ajoutée.
- **PATCH** : incrémenter lorsqu'une correction compatible est publiée.

Exemple d'évolution :

```text
1.0.0
  |
  |-- Correction de bug
  v
1.0.1
  |
  |-- Ajout de fonctionnalité compatible
  v
1.1.0
  |
  |-- Changement incompatible
  v
2.0.0
```

## Types de versions Maven

Maven distingue notamment les versions de développement et les versions stables.

### 1. Release

Une Release représente une version publiée et destinée à être utilisée de manière stable.

Exemples :

```text
1.0.0
1.1.0
2.0.0
```

Une Release ne doit normalement pas être remplacée par un autre contenu sous la même coordonnée de version.

### 2. Snapshot

Une Snapshot représente une version de développement susceptible d'évoluer.

Exemples :

```text
1.0.0-SNAPSHOT
1.1.0-SNAPSHOT
2.0.0-SNAPSHOT
```

Maven peut récupérer une nouvelle publication Snapshot lorsque la dépendance est résolue à nouveau.

Contrairement à une Release, une Snapshot peut être republiée sous la même version logique.

## Exemple avec Maven

### 1. Définir la version du projet

Dans le `pom.xml` :

```xml
<groupId>com.company</groupId>
<artifactId>common-library</artifactId>
<version>1.0.0-SNAPSHOT</version>
<packaging>jar</packaging>
```

La coordonnée complète de l'artefact est appelée **Maven Coordinates** :

```text
groupId:artifactId:version
```

Exemple :

```text
com.company:common-library:1.0.0-SNAPSHOT
```

### 2. Configurer les dépôts Nexus

```xml
<distributionManagement>
    <repository>
        <id>nexus-releases</id>
        <url>https://nexus.example.com/repository/maven-releases/</url>
    </repository>

    <snapshotRepository>
        <id>nexus-snapshots</id>
        <url>https://nexus.example.com/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

### 3. Publier une version Snapshot

Dans le `pom.xml` :

```xml
<version>1.0.0-SNAPSHOT</version>
```

Commande :

```bash
mvn clean deploy
```

Maven publie automatiquement l'artefact dans `maven-snapshots`.

Nexus peut stocker plusieurs publications physiques de cette même version logique avec des timestamps et numéros de build.

### 4. Publier une Release

Modifier la version dans le `pom.xml` :

```xml
<version>1.0.0</version>
```

Puis exécuter :

```bash
mvn clean deploy
```

Maven publie l'artefact dans `maven-releases`.

## Versioning et gestion des dépendances

Une application peut dépendre d'une version précise d'une bibliothèque interne.

Exemple :

```xml
<dependency>
    <groupId>com.company</groupId>
    <artifactId>common-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

Cette déclaration permet de contrôler exactement quelle version de la bibliothèque est utilisée lors du build.

Il est préférable de privilégier des versions explicites plutôt que des versions dynamiques comme `LATEST` ou `RELEASE`.

## Bonnes pratiques

- Utiliser Semantic Versioning pour les bibliothèques et API lorsque cela est pertinent.
- Séparer les dépôts Releases et Snapshots.
- Ne jamais remplacer le contenu d'une Release déjà publiée.
- Publier les Snapshots uniquement pour les besoins de développement et d'intégration.
- Utiliser des versions explicites dans les dépendances.
- Associer chaque publication à un commit Git ou à un tag permettant de retrouver le code source correspondant.
- Conserver le même artefact entre les environnements DEV, UAT et PROD.
- Automatiser la publication des versions dans les pipelines CI/CD.

## Versioning des artefacts vs Versioning du code source

| Git | Maven | Nexus |
|---|---|---|
| Versionne le code source | Définit les coordonnées et versions des artefacts | Stocke et distribue les artefacts |
| Gère les commits et branches | Gère les dépendances et le cycle de build | Conserve les publications |
| Gère les tags | Produit les packages | Héberge les Releases et Snapshots |

## Concepts associés

- Semantic Versioning
- Artifact Repository
- Maven Coordinates
- Release Management
- Snapshot
- Dependency Management
- CI/CD
- Git Tagging