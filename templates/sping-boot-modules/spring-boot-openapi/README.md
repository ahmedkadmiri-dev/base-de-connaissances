# Spring Boot OpenAPI

Module Maven réutilisable pour centraliser la documentation des API REST avec **OpenAPI 3** et **Swagger UI**.

## Fonctionnalités

- Configuration automatique de Springdoc.
- Centralisation des informations générales de l'API.
- Ajout automatique des réponses HTTP communes.
- Réutilisation du modèle `ErrorResponse` du module `spring-boot-exception`.
- Détection des endpoints annotés avec `@ApiSecured`.
- Conservation des réponses explicitement déclarées.

## Réponses HTTP

| Code HTTP | Gestion | Description |
|---|---|---|
| 500 | Automatique | Erreur interne du serveur, pour tous les endpoints |
| 401 | Automatique | Non authentifié, pour les endpoints `@ApiSecured` |
| 403 | Automatique | Accès interdit, pour les endpoints `@ApiSecured` |
| 200 | Manuelle | Succès |
| 201 | Manuelle | Ressource créée |
| 202 | Manuelle | Requête acceptée |
| 204 | Manuelle | Succès sans contenu |
| 400 | Manuelle | Requête invalide |
| 404 | Manuelle | Ressource introuvable |
| 409 | Manuelle | Conflit métier |

Les réponses métier et situationnelles doivent être déclarées manuellement sur les endpoints concernés.

Les réponses déjà déclarées ne sont jamais remplacées par le customizer.

## Dépendances

- Spring Boot 4.x
- Java 21
- Springdoc OpenAPI
- Lombok
- `spring-boot-exception` pour le modèle `ErrorResponse`

## Configuration

Dans `application.yml` :

```yaml
spring:
  openapi:
    title: Bank API
    description: Bank REST API documentation
    version: 1.0.0
```

## Utilisation

### Sécurité documentaire

L'annotation `@ApiSecured` permet d'indiquer qu'un endpoint nécessite une authentification.

Sur une classe :

```java
@RestController
@RequestMapping("/accounts")
@ApiSecured
public class AccountController {
}
```

Sur une méthode :

```java
@ApiSecured
@GetMapping("/{id}")
public Account findById(@PathVariable Long id) {
    // ...
}
```

Les réponses 401 et 403 sont alors ajoutées automatiquement.

**Remarque :** `@ApiSecured` sert uniquement à documenter les réponses de sécurité dans OpenAPI. La protection réelle est assurée par Spring Security.

### Déclaration des réponses spécifiques

Les réponses propres à chaque opération restent déclarées manuellement :

```java
@Operation(
    responses = {
        @ApiResponse(responseCode = "200", description = "Account found"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    }
)
@GetMapping("/{id}")
public Account findById(@PathVariable Long id) {
    // ...
}
```

## Accès à la documentation

Après démarrage de l'application :

- Swagger UI : `/swagger-ui/index.html`
- OpenAPI JSON : `/v3/api-docs`

## Auto-configuration

Le module est détecté automatiquement par Spring Boot grâce au fichier :

```text
META-INF/spring/
└── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Contenu :

```text
com.example.config.OpenApiAutoConfiguration
```