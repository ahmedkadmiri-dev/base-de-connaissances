# Spring Boot Exception

Bibliothèque Java réutilisable permettant de centraliser la gestion des exceptions REST dans les applications Spring Boot.

## Objectif

`spring-boot-exception` fournit un mécanisme commun de gestion des exceptions afin d'harmoniser les réponses d'erreur des API REST.

Le module permet notamment de :

- Centraliser la gestion des exceptions avec `@RestControllerAdvice`.
- Fournir une exception de base (`ApiException`) pour les exceptions métier.
- Uniformiser le format des réponses d'erreur.
- Gérer les messages internationalisés avec `MessageSource`.
- Éviter la duplication des handlers d'exception dans les microservices.

## Technologies

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Spring Web MVC | 4.1.2 |
| Maven | 3.x |
| Lombok | Compatible |

## Architecture

```text
spring-boot-exception
│
├── src/main/java/com/example/exception
│   │
│   ├── config
│   │   ├── ExceptionConfiguration.java
│   │   └── ExceptionProperties.java
│   │
│   ├── model
│   │   └── ErrorResponse.java
│   │
│   ├── handler
│   │   └── GlobalExceptionHandler.java
│   │
│   └── ApiException.java
│
├── src/main/resources
│
├── src/test/java
│
└── pom.xml
```

## Fonctionnalités

### 1. Exception de base : ApiException

`ApiException` est une classe abstraite de référence pour les exceptions métier des microservices consommateurs.

Elle permet de définir :

- Le statut HTTP.
- Le code d'erreur.
- Les arguments éventuels du message.
- Les informations complémentaires (`details`).

Les microservices peuvent créer leurs propres exceptions en héritant de `ApiException`.

### 2. Exceptions métier personnalisées

Exemple dans un microservice consommateur :

```java
public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "ACCOUNT_NOT_FOUND",
                new Object[]{id},
                Map.of("accountId", id)
        );
    }
}
```

Une autre exception métier peut être définie de la même manière :

```java
public class InsufficientBalanceException extends ApiException {

    public InsufficientBalanceException(Long accountId) {
        super(
                HttpStatus.BAD_REQUEST,
                "INSUFFICIENT_BALANCE",
                null,
                Map.of("accountId", accountId)
        );
    }
}
```

### 3. Réponse d'erreur standardisée

Toutes les exceptions prises en charge retournent une structure homogène :

```json
{
  "timestamp": "2026-11-02T21:30:00",
  "errorCode": "ACCOUNT_NOT_FOUND",
  "message": "Account with id 100 not found",
  "details": {
    "accountId": 100
  }
}
```

### 4. Gestion centralisée des exceptions

`GlobalExceptionHandler` étend `ResponseEntityExceptionHandler` et centralise le traitement des exceptions.

Il prend notamment en charge :

- `ApiException` et toutes ses sous-classes : exceptions métier personnalisées.
- `Exception` : exceptions techniques non prévues.
- Les exceptions Spring MVC prises en charge par `ResponseEntityExceptionHandler`, selon les méthodes surchargées.

Il n'est donc pas nécessaire de créer un handler spécifique pour chaque exception métier.

### 5. Internationalisation

Les messages sont résolus à partir de `MessageSource`.

Exemple de fichier `messages_fr.properties` :

```properties
ACCOUNT_NOT_FOUND=Compte avec l'identifiant {0} introuvable
INSUFFICIENT_BALANCE=Solde insuffisant pour le compte {0}
INTERNAL_SERVER_ERROR=Une erreur interne est survenue
```

Exemple de fichier `messages_en.properties` :

```properties
ACCOUNT_NOT_FOUND=Account with id {0} not found
INSUFFICIENT_BALANCE=Insufficient balance for account {0}
INTERNAL_SERVER_ERROR=An unexpected internal error occurred
```

La langue de réponse dépend de la locale résolue par Spring.

## Installation

### Installation locale

Compiler et installer le module dans le repository Maven local :

```bash
mvn clean install
```

### Publication sur Nexus

Après configuration du repository Maven dans le `pom.xml` :

```bash
mvn clean deploy
```

## Intégration dans un microservice

### 1. Ajouter la dépendance Maven

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-exception</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Importer le handler

Le module utilise une déclaration manuelle. Il faut donc importer le handler dans l'application consommatrice.

```java
@Configuration
@Import(GlobalExceptionHandler.class)
public class ExceptionConfig {
}
```

Ou directement dans la classe principale :

```java
@SpringBootApplication
@Import(GlobalExceptionHandler.class)
public class TestingPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingPocApplication.class, args);
    }
}
```

### 3. Créer une exception métier

Dans le package `exception` du microservice :

```java
public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "ACCOUNT_NOT_FOUND",
                new Object[]{id},
                Map.of("accountId", id)
        );
    }
}
```

### 4. Utiliser l'exception dans un service

```java
public Account getAccount(Long id) {

    return accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException(id));
}
```

Le `GlobalExceptionHandler` intercepte automatiquement l'exception et construit la réponse HTTP correspondante.

## Tests

Exécuter les tests :

```bash
mvn test
```

Les tests doivent notamment couvrir :

- Les exceptions métier et leurs sous-classes.
- Les exceptions techniques.
- Les statuts HTTP retournés.
- Les codes d'erreur.
- Les messages internationalisés.
- Le format de la réponse JSON.
- L'absence d'informations techniques sensibles dans les réponses.

## Versioning

Le projet suit le versioning Maven.

| Version | Description |
|---|---|
| `1.0-SNAPSHOT` | Version de développement initiale |
| `1.0.0` | Première version stable |

## Évolutions possibles

- Auto-configuration Spring Boot pour supprimer l'import manuel.
- Configuration personnalisable des réponses d'erreur.
- Gestion centralisée des erreurs de validation.
- Support de nouveaux types d'exceptions.

---

**Spring Boot Exception** — Centralized and consistent exception handling for Spring Boot REST APIs.

Developed by **Ahmed Kadmiri** © 2026