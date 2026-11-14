# Spring Boot Logging

## Présentation

`spring-boot-logging` est une bibliothèque permettant de centraliser la journalisation des applications Spring Boot grâce à des aspects AOP.

Elle fournit automatiquement :
- **Controller** : journalisation des requêtes HTTP, headers, request body et réponses.
- **Service** : journalisation des appels, paramètres et résultats.
- **Repository** : journalisation des opérations d'accès aux données.
- **Traçabilité** : intégration du `traceId` et de l'`idempotencyKey`.

## Dépendance Maven

À ajouter dans le `pom.xml` du microservice :

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-logging</artifactId>
</dependency>
```

Adapter la version à celle publiée sur le repository Maven.

## Configuration

À ajouter dans `application.yml` du microservice :

```yaml
logging:
  aspect:
    controller:
      enabled: true
      include-request-body: true
      include-response-body: true
      include-headers: false
      max-body-length: 5000

    service:
      enabled: true
      include-parameters: true
      include-result: true
      max-value-length: 2000

    repository:
      enabled: true
      include-parameters: true
      include-result: true
      max-value-length: 1000
```

## Activation

Après ajout de la dépendance et de la configuration, les aspects de journalisation sont activés automatiquement via l'auto-configuration Spring Boot.

Chaque couche peut être activée ou désactivée indépendamment.