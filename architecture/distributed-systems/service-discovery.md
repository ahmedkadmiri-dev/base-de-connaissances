# Service Discovery

## Définition

Le **Service Discovery** (découverte de services) est un mécanisme qui permet aux applications de découvrir automatiquement les adresses réseau des services disponibles dans une architecture distribuée.

Dans une architecture microservices, les services peuvent être déployés sur plusieurs instances dont les adresses IP et les ports peuvent changer dynamiquement.

Plutôt que de configurer manuellement les adresses de chaque service, le Service Discovery permet de localiser automatiquement les instances disponibles.

## Fonctionnement

Le Service Discovery repose généralement sur deux composants :

- **Service Registry** : registre centralisé qui conserve les informations des services et de leurs instances.
- **Service Instance** : instance d'un microservice enregistrée dans le registre avec son adresse IP, son port et son état de disponibilité.

```text
                  Service Registry
                         |
              +----------+----------+
              |          |          |
              v          v          v
          Service A  Service B  Service C
              |          |          |
           Register   Register   Register
              |          |          |
              +----------+----------+
                         |
                         v
                  Service Consumer
                  (Discovery Client)
                         |
                  Discover Service B
                         |
                         v
                  Instance de B
```

1. Un microservice démarre et s'enregistre auprès du Service Registry.
2. Le registre conserve son adresse et ses informations.
3. Un autre microservice souhaite communiquer avec lui.
4. Le client interroge le registre pour découvrir les instances disponibles.
5. Il sélectionne une instance et lui envoie sa requête.

## Types de Service Discovery

### Client-Side Discovery

Dans le modèle *Client-Side Discovery*, c'est le service consommateur qui interroge directement le Service Registry pour obtenir les instances disponibles.

Il sélectionne ensuite une instance, généralement à l'aide d'un mécanisme de Load Balancing.

```text
          Service Consumer
                 |
                 v
          Service Registry
                 |
          Liste des instances
                 |
                 v
          Load Balancer
                 |
                 v
          Service Provider
```

**Exemple :** Spring Cloud Netflix Eureka avec Spring Cloud LoadBalancer.

### Server-Side Discovery

Dans le modèle *Server-Side Discovery*, le client envoie sa requête à un intermédiaire qui se charge de découvrir une instance disponible et de transmettre la requête.

```text
          Service Consumer
                 |
                 v
          Load Balancer
                 |
                 v
          Service Registry
                 |
          Instance disponible
                 |
                 v
          Service Provider
```

**Exemple :** Kubernetes Service, qui fournit un point d'accès stable et répartit le trafic vers les Pods disponibles.

## Enregistrement et disponibilité des services

Le Service Discovery doit maintenir une vision actualisée des instances disponibles.

Plusieurs mécanismes sont utilisés :

- **Registration** : enregistrement d'une instance lors de son démarrage.
- **Heartbeat** : envoi périodique de signaux pour confirmer que l'instance est toujours active.
- **Health Check** : vérification de l'état de santé d'une instance.
- **Deregistration** : suppression d'une instance lorsqu'elle s'arrête ou devient indisponible.
- **TTL (Time To Live)** : durée de validité d'une inscription ou d'un heartbeat avant expiration.

## Exemple avec Spring Cloud Netflix Eureka

Eureka permet de mettre en place un Service Registry et des clients capables d'enregistrer et de découvrir les microservices.

### 1. Eureka Server

**Dépendance Maven :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**Classe principale :**

```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

**application.yml :**

```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: true
```

Le serveur Eureka expose une interface d'administration permettant de visualiser les instances enregistrées.

### 2. Service Provider

Un microservice doit intégrer le client Eureka pour s'enregistrer automatiquement.

**Dépendance Maven :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**application.yml :**

```yaml
spring:
  application:
    name: account-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

Au démarrage, `account-service` s'enregistre automatiquement auprès du serveur Eureka.

### 3. Service Consumer

Un autre microservice peut appeler `account-service` en utilisant son nom logique plutôt que son adresse IP.

Exemple avec `WebClient` et Spring Cloud LoadBalancer :

```java
@Bean
@LoadBalanced
public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
}
```

Utilisation :

```java
@Service
@RequiredArgsConstructor
public class AccountClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<String> getAccount(Long id) {
        return webClientBuilder.build()
                .get()
                .uri("http://account-service/accounts/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }
}
```

Le nom `account-service` est résolu grâce au Service Discovery. Spring Cloud LoadBalancer sélectionne ensuite une instance disponible.

## Service Discovery dans Kubernetes

Kubernetes intègre nativement un mécanisme de découverte des services.

Chaque Service Kubernetes fournit un nom DNS stable permettant aux applications de communiquer sans connaître les adresses IP individuelles des Pods.

Exemple :

```text
http://account-service:8080/accounts/123
```

Kubernetes assure la résolution DNS et la distribution du trafic vers les Pods correspondant au Service.

Dans ce contexte, il n'est généralement pas nécessaire d'ajouter Eureka pour découvrir les services internes au cluster.

## Service Discovery vs Load Balancing

Ces deux mécanismes sont complémentaires, mais différents.

| Service Discovery | Load Balancing |
|---|---|
| Découvre les instances disponibles | Répartit les requêtes entre les instances |
| Fournit les adresses des services | Sélectionne une instance cible |
| Maintient les informations des instances | Applique une stratégie de répartition |
| Exemple : Eureka, Consul | Exemple : Spring Cloud LoadBalancer, HAProxy |

## Avantages

- Découverte automatique des services.
- Réduction du couplage entre les microservices.
- Adaptation aux déploiements dynamiques.
- Facilitation de la montée en charge horizontale.
- Suppression de la configuration manuelle des adresses IP.
- Intégration avec les mécanismes de Load Balancing.

## Inconvénients

- Ajout d'un composant supplémentaire à maintenir.
- Dépendance au registre de services selon l'architecture choisie.
- Nécessité de gérer les instances indisponibles et les informations obsolètes.
- Complexité supplémentaire dans la supervision et le diagnostic des communications.

## Technologies

- **Eureka** : Service Registry de l'écosystème Spring Cloud Netflix.
- **Consul** : solution de découverte de services et de gestion de configuration.
- **Kubernetes DNS** : découverte native des services dans Kubernetes.
- **Apache ZooKeeper** : coordination distribuée et stockage de métadonnées.
- **etcd** : stockage clé-valeur distribué utilisé notamment par Kubernetes.

## Concepts associés

- Service Registry
- Client-Side Discovery
- Server-Side Discovery
- Load Balancing
- API Gateway
- Centralized Configuration
- Health Check
- Kubernetes Service