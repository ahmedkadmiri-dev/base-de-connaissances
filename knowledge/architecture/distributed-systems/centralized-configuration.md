# Centralized Configuration

## Définition

La **Centralized Configuration** (configuration centralisée) est un mécanisme qui permet de centraliser la gestion des configurations de plusieurs applications dans un référentiel commun.

Dans une architecture microservices, chaque service possède généralement ses propres paramètres : URL des API, ports, paramètres de connexion, timeouts, fonctionnalités activées, etc.

Plutôt que de maintenir ces configurations individuellement dans chaque application, un serveur de configuration centralisé permet de les externaliser et de les distribuer aux services concernés.

## Fonctionnement

```text
                 Configuration Repository
                       (Git)
                         |
                         v
                 Config Server
                         |
              +----------+----------+
              |          |          |
              v          v          v
           Service A  Service B  Service C
              |          |          |
              v          v          v
          Config A   Config B   Config C
```

1. Les configurations sont stockées dans un référentiel centralisé, généralement Git.
2. Le Config Server récupère les configurations depuis ce référentiel.
3. Chaque microservice contacte le Config Server au démarrage.
4. Le Config Server fournit la configuration correspondant au nom de l'application et à son environnement.
5. Le microservice utilise les paramètres récupérés pour démarrer son fonctionnement.

## Concepts principaux

- **Configuration Repository** : référentiel centralisé contenant les fichiers de configuration, souvent un dépôt Git.
- **Config Server** : serveur responsable de récupérer et de distribuer les configurations.
- **Config Client** : application cliente qui récupère sa configuration auprès du Config Server.
- **Environment Profile** : environnement d'exécution de l'application, par exemple `dev`, `uat` ou `prod`.
- **Configuration Refresh** : mécanisme permettant de mettre à jour la configuration d'une application sans reconstruire son artefact. Son application dépend du framework et des mécanismes de rafraîchissement utilisés.

## Exemple avec Spring Cloud Config

Spring Cloud Config fournit une solution de configuration centralisée pour les applications Spring Boot.

### Architecture

```text
                  Git Repository
                        |
                        v
                Spring Cloud Config
                     Server
                        |
             +----------+----------+
             |          |          |
             v          v          v
        Config Client  Config Client  Config Client
        Service A      Service B      Service C
```

### 1. Repository Git

Exemple de structure du dépôt contenant les configurations :

```text
config-repository/
├── application.yml
├── application-dev.yml
├── application-uat.yml
├── application-prod.yml
│
├── account-service.yml
├── account-service-uat.yml
├── account-service-prod.yml
│
├── payment-service.yml
├── payment-service-uat.yml
└── payment-service-prod.yml
```

Spring Cloud Config combine les configurations communes et spécifiques à chaque application et à son profil actif.

Par exemple :
- `application.yml` : configuration commune à toutes les applications.
- `account-service.yml` : configuration spécifique à `account-service`.
- `account-service-uat.yml` : configuration spécifique à `account-service` en UAT.

### 2. Config Server

**Dépendance Maven :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

**Classe principale :**

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

**application.yml :**

```yaml
server:
  port: 8888

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/company/config-repository
          default-label: main
```

Le Config Server expose les configurations via des endpoints HTTP.

Exemple :

```http
GET http://localhost:8888/account-service/uat
```

Cet endpoint retourne la configuration de `account-service` pour le profil `uat`.

### 3. Config Client

**Dépendance Maven :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**application.yml :**

```yaml
spring:
  application:
    name: account-service
  config:
    import: configserver:http://localhost:8888
  profiles:
    active: uat
```

Au démarrage, `account-service` contacte le Config Server pour récupérer sa configuration.

## Mise à jour des configurations

Modifier un fichier dans Git ne met pas automatiquement à jour tous les microservices déjà démarrés.

Plusieurs mécanismes sont possibles :

- **Restart** : redémarrer le microservice pour récupérer la nouvelle configuration.
- **Actuator Refresh** : utiliser Spring Boot Actuator et `@RefreshScope` pour rafraîchir certains beans compatibles.
- **Spring Cloud Bus** : diffuser un événement de rafraîchissement à plusieurs instances via un broker de messages comme RabbitMQ ou Kafka.
- **CI/CD** : déclencher automatiquement un déploiement ou un redémarrage après une modification de configuration.

## Gestion des secrets

Les configurations peuvent contenir des informations sensibles : mots de passe, tokens, clés API et identifiants de connexion.

Bonnes pratiques :

- Ne pas stocker les secrets en clair dans Git, même dans un dépôt privé.
- Utiliser un gestionnaire de secrets comme HashiCorp Vault ou un service équivalent.
- Limiter les accès aux configurations selon les applications et les environnements.
- Sécuriser les échanges entre Config Server et Config Clients avec TLS et authentification.

## Avantages

- Centralisation des configurations.
- Séparation entre le code applicatif et la configuration.
- Gestion simplifiée des environnements `dev`, `uat` et `prod`.
- Cohérence des paramètres entre les différentes instances d'un service.
- Modification des configurations sans reconstruire les applications.
- Historisation et traçabilité des changements grâce à Git.

## Inconvénients

- Dépendance au Config Server pour récupérer les configurations au démarrage.
- Ajout d'un composant supplémentaire à maintenir et superviser.
- Risque de panne centralisée si aucune haute disponibilité n'est mise en place.
- Nécessité de sécuriser les configurations et les accès.

## Technologies

- **Spring Cloud Config** : serveur et client de configuration centralisée pour Spring Boot.
- **HashiCorp Vault** : gestion centralisée des secrets.
- **Consul** : service discovery et stockage de configurations clé-valeur.
- **AWS Systems Manager Parameter Store** : stockage et gestion des paramètres applicatifs.
- **Azure App Configuration** : gestion centralisée des configurations applicatives.

## Concepts associés

- Service Discovery
- API Gateway
- Spring Cloud Bus
- Externalized Configuration
- Secrets Management
- HashiCorp Vault
- GitOps