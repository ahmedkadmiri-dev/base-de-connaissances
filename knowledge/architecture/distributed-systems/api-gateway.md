# API Gateway

## Définition

Un **API Gateway** est un point d'entrée unique (*Single Entry Point*) qui permet aux clients d'accéder aux API exposées par plusieurs services backend.

Il se positionne entre les clients et les microservices. Il reçoit les requêtes, applique les politiques nécessaires et les redirige vers les services concernés.

Il évite aux clients de connaître directement les adresses et les détails techniques des différents microservices.

## Fonctionnement

```text
                  Clients
                     |
                     v
                API Gateway
                     |
          +----------+----------+
          |          |          |
          v          v          v
       Service A  Service B  Service C
          |          |          |
          v          v          v
       Database  Database   Database
```

1. Le client envoie une requête à l'API Gateway.
2. L'API Gateway identifie le service cible.
3. Il applique les politiques de sécurité et les règles de gestion des requêtes.
4. Il transmet la requête au service backend.
5. Il retourne la réponse au client.

## Fonctionnalités principales

- **Routing** : rediriger les requêtes vers les microservices appropriés en fonction du chemin, de la méthode HTTP ou d'autres critères.
- **Authentication** : vérifier l'identité du client à l'aide de mécanismes comme OAuth2, OpenID Connect ou JWT.
- **Authorization** : contrôler les droits d'accès aux API.
- **Rate Limiting** : limiter le nombre de requêtes autorisées pour un client pendant une période donnée.
- **Request/Response Transformation** : modifier les requêtes et les réponses avant leur transmission.
- **API Aggregation** : regrouper les réponses de plusieurs microservices dans une seule réponse destinée au client.
- **Load Balancing** : répartir les requêtes entre plusieurs instances d'un service.
- **Circuit Breaker** : limiter les appels vers un service défaillant afin d'éviter la propagation des erreurs.
- **Logging and Monitoring** : centraliser les logs, métriques et traces des appels API.

## Exemple avec Spring Cloud Gateway

Spring Cloud Gateway permet de construire un API Gateway avec Spring Boot.

### Dépendance Maven

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway-server-webflux</artifactId>
</dependency>
```

### Configuration

Exemple de configuration dans `application.yml` :

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: account-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/accounts/**
          filters:
            - StripPrefix=1

        - id: payment-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/payments/**
          filters:
            - StripPrefix=1
```

Avec cette configuration :

| Requête client | Service cible |
|---|---|
| `/api/accounts/123` | `http://localhost:8081/accounts/123` |
| `/api/payments/456` | `http://localhost:8082/payments/456` |

Le filtre `StripPrefix=1` supprime le premier segment du chemin avant de transmettre la requête au service backend.

## API Gateway dans une architecture microservices

L'API Gateway peut également s'intégrer avec d'autres composants de l'écosystème Spring Cloud :

- **Service Discovery** : découvrir dynamiquement les instances des microservices avec Eureka ou Consul.
- **Centralized Configuration** : centraliser la configuration avec Spring Cloud Config.
- **Load Balancing** : répartir les requêtes entre les instances disponibles avec Spring Cloud LoadBalancer.
- **Resilience** : gérer les défaillances avec Circuit Breaker et Resilience4j.

## Avantages

- Point d'entrée unique pour les clients.
- Simplification de la communication avec les microservices.
- Centralisation des préoccupations transversales : sécurité, routage et monitoring.
- Réduction du couplage entre les clients et les services backend.
- Possibilité d'adapter les API aux différents types de clients.

## Inconvénients

- Point de défaillance potentiel si aucune haute disponibilité n'est mise en place.
- Ajout d'un composant supplémentaire dans le parcours des requêtes.
- Risque de devenir un composant trop complexe si toute la logique métier y est concentrée.
- Coût supplémentaire en matière de maintenance et de supervision.

## Bonnes pratiques

- Garder l'API Gateway léger et éviter d'y placer la logique métier.
- Mettre en place plusieurs instances pour garantir la haute disponibilité.
- Centraliser les politiques de sécurité sans négliger leur application au niveau des services sensibles.
- Configurer des timeouts et des mécanismes de résilience.
- Surveiller les performances et les erreurs.
- Éviter de transformer l'API Gateway en orchestrateur de workflows métier complexes.

## Technologies

- **Spring Cloud Gateway** : API Gateway basé sur Spring.
- **Kong Gateway** : API Gateway open source et plateforme extensible.
- **Apigee** : plateforme de gestion et de sécurisation des API.
- **AWS API Gateway** : service managé de création et de gestion des API.
- **Azure API Management** : plateforme de gestion des API.
- **Envoy Proxy** : proxy utilisé notamment dans les architectures microservices et Service Mesh.

## Concepts associés

- Reverse Proxy
- API Management
- Service Discovery
- Load Balancing
- OAuth2 / OpenID Connect
- Rate Limiting
- Circuit Breaker
- Microservices Architecture