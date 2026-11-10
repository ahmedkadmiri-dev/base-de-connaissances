# Artifact Repository

## Définition

Un **Artifact Repository** (dépôt d'artefacts) est un système permettant de stocker, organiser, versionner et distribuer les artefacts logiciels produits lors du développement et du build d'une application.

Un artefact peut être :
- Une bibliothèque Java (`JAR`).
- Une application Java (`WAR`).
- Une image Docker.
- Un package npm.
- Un package Python.
- Un composant logiciel réutilisable.

Un Artifact Repository centralise les composants logiciels afin de faciliter leur partage, leur réutilisation et leur distribution entre les équipes et les pipelines CI/CD.

## Fonctionnement

```text
       Développeurs / CI Pipeline
                  |
                  v
             Build Tool
          (Maven / Gradle)
                  |
          +-------+-------+
          |               |
          v               v
      Download         Publish
      Dependencies     Artifacts
          |               |
          v               v
          +---------------+
                  |
                  v
          Artifact Repository
                  |
          +-------+-------+
          |               |
          v               v
       Internal        External
       Artifacts       Repositories
```

1. Le build tool recherche les dépendances nécessaires dans les dépôts configurés.
2. Le repository fournit les artefacts demandés.
3. L'application est compilée et packagée.
4. Le pipeline publie l'artefact généré dans un dépôt interne.
5. Les autres applications ou pipelines peuvent récupérer cet artefact.

## Types de repositories

### 1. Hosted Repository

Un dépôt Hosted stocke les artefacts produits par l'organisation.

Exemples :
- Bibliothèques Java internes.
- Modules Maven partagés.
- Images Docker internes.
- Packages développés en interne.

### 2. Proxy Repository

Un dépôt Proxy sert d'intermédiaire vers un dépôt externe.

Il télécharge les artefacts distants et les met en cache localement.

Exemples :
- Maven Central.
- npm Registry.
- Docker Hub.

Avantages :
- Réduction des téléchargements externes.
- Accélération des builds.
- Centralisation de l'accès aux dépendances.
- Réduction de la dépendance directe aux dépôts externes.

### 3. Group Repository

Un dépôt Group regroupe plusieurs dépôts sous une seule URL.

Exemple :

```text
maven-public/
    |
    +-- maven-releases/
    |
    +-- maven-snapshots/
    |
    +-- maven-central-proxy/
```

Le développeur utilise une seule URL pour accéder aux différents dépôts Maven.

## Exemple avec Nexus Repository

**Nexus Repository** est un gestionnaire de dépôts permettant de stocker et distribuer différents types d'artefacts.

Exemple d'organisation des dépôts Maven dans Nexus :

| Repository | Type | Utilisation |
|---|---|---|
| `maven-releases` | Hosted | Stockage des versions stables |
| `maven-snapshots` | Hosted | Stockage des versions de développement |
| `maven-central-proxy` | Proxy | Cache des dépendances Maven Central |
| `maven-public` | Group | Regroupement des dépôts précédents |

### Configuration Maven

Pour publier des artefacts dans Nexus, configurer les dépôts dans le `pom.xml` :

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

Configurer les identifiants dans le fichier Maven `settings.xml` :

```xml
<settings>
    <servers>
        <server>
            <id>nexus-releases</id>
            <username>${env.NEXUS_USERNAME}</username>
            <password>${env.NEXUS_PASSWORD}</password>
        </server>

        <server>
            <id>nexus-snapshots</id>
            <username>${env.NEXUS_USERNAME}</username>
            <password>${env.NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
```

Les identifiants doivent correspondre aux `id` définis dans `distributionManagement`.

### Publication d'un artefact

```bash
mvn clean deploy
```

Maven sélectionne automatiquement le dépôt cible en fonction de la version du projet.

### Consommation d'un artefact

Un autre projet peut récupérer une bibliothèque publiée dans Nexus :

```xml
<dependency>
    <groupId>com.company</groupId>
    <artifactId>common-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

Le dépôt Nexus doit être accessible depuis la configuration Maven du consommateur, généralement via un miroir configuré dans `settings.xml`.

## Artifact Repository dans une chaîne CI/CD

```text
       Git Repository
             |
             v
         CI Pipeline
             |
             v
        Maven Build
             |
             v
         Unit Tests
             |
             v
        Package JAR
             |
             v
     Nexus Repository
             |
             v
      Deployment Pipeline
             |
             v
        Environments
        DEV / UAT / PROD
```

L'artefact est construit et publié une seule fois, puis récupéré pour les différents environnements.

Cela permet de garantir que le même binaire est déployé en DEV, UAT et PROD, sans reconstruire l'application à chaque déploiement.

## Avantages

- Centralisation des artefacts logiciels.
- Réutilisation des bibliothèques internes.
- Accélération des builds grâce au cache.
- Contrôle des accès aux dépendances.
- Distribution standardisée des composants.
- Intégration avec les pipelines CI/CD.
- Traçabilité des artefacts publiés.

## Technologies

- **Sonatype Nexus Repository** : gestionnaire de dépôts d'artefacts.
- **JFrog Artifactory** : solution de gestion des artefacts.
- **GitHub Packages** : hébergement de packages logiciels.
- **GitLab Package Registry** : registre de packages intégré à GitLab.
- **Apache Maven** : gestion des dépendances et publication des artefacts.
- **Gradle** : outil de build et gestion des dépendances.

## Concepts associés

- Artifact Versioning
- Dependency Management
- Maven Repository
- CI/CD
- Software Supply Chain
- Release Management