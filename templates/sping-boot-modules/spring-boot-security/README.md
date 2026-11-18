# Spring Boot Security

Module Maven réutilisable pour centraliser la configuration de Spring Security, l'intégration OAuth2 Resource Server / JWT, la gestion des rôles et les réponses d'erreurs de sécurité.

## Fonctionnalités

- Configuration automatique de Spring Security.
- Authentification JWT via OAuth2 Resource Server (Keycloak par défaut).
- Extraction des rôles Realm et Client depuis le JWT.
- Conversion des rôles en `GrantedAuthority`.
- Protection des endpoints avec `@PreAuthorize`.
- Gestion personnalisée des erreurs `401 Unauthorized` et `403 Forbidden`.
- Identification configurable du fournisseur d'identité dans les réponses d'erreur.
- Configuration CORS.
- Accès aux informations du principal connecté via `CurrentPrincipal`.
- Activation / désactivation de la sécurité par propriété.

## Dépendance Maven

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-security</artifactId>
</dependency>
```

## Configuration

### application.yml

```yaml
security:
  enabled: true
  provider: KEYCLOAK

  public-endpoints:
    - /actuator/health
    - /api/public/**

  jwt:
    roles-claims:
      - realm_access.roles
      - resource_access.my-client.roles
    authority-prefix: ROLE_

  cors:
    enabled: false
    allowed-origins:
      - https://example.com
    allowed-methods:
      - GET
      - POST
      - PUT
      - PATCH
      - DELETE
      - OPTIONS
    allowed-headers:
      - Authorization
      - Content-Type
    allow-credentials: false

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.example.com/realms/my-realm
```

### Propriétés disponibles

| Propriété | Défaut | Description |
|---|---|---|
| `security.enabled` | `true` | Active la configuration Spring Security |
| `security.provider` | `KEYCLOAK` | Nom du fournisseur d'identité affiché dans les réponses d'erreur |
| `security.public-endpoints` | `[]` | Endpoints accessibles sans authentification |
| `security.jwt.roles-claims` | `realm_access.roles` | Chemins des claims contenant les rôles |
| `security.jwt.authority-prefix` | `ROLE_` | Préfixe des autorités Spring Security |
| `security.cors.enabled` | `false` | Active la configuration CORS |
| `security.cors.allowed-origins` | `[]` | Origines autorisées |
| `security.cors.allowed-methods` | `[]` | Méthodes HTTP autorisées |
| `security.cors.allowed-headers` | `[]` | Headers autorisés |
| `security.cors.allow-credentials` | `false` | Autorise les credentials CORS |

### Propriétés Spring Boot

| Propriété | Description |
|---|---|
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | URI de l'issuer utilisée par Spring Boot pour configurer le `JwtDecoder` et valider les JWT |

## Authentification OAuth2 / JWT

Le module utilise le mécanisme OAuth2 Resource Server de Spring Security.

La configuration native Spring Boot `issuer-uri` permet :

- La découverte des métadonnées OIDC du Realm.
- La récupération des clés publiques de signature.
- La validation de la signature du JWT.
- La validation de l'issuer et des claims temporels.

Spring Boot configure automatiquement le `JwtDecoder`. Aucune configuration manuelle supplémentaire n'est nécessaire dans le module.

## Identification du fournisseur d'identité

La propriété `security.provider` permet de personnaliser le nom du fournisseur d'identité affiché dans les réponses d'erreur de sécurité.

Valeur par défaut :

```yaml
security:
  provider: KEYCLOAK
```

Exemples :

```yaml
security:
  provider: AUTH0
```

```yaml
security:
  provider: OKTA
```

Cette propriété est utilisée par les handlers `CustomAuthenticationEntryPoint` et `CustomAccessDeniedHandler`, dans le champ `details.provider`.

Elle permet de conserver un module indépendant du fournisseur d'identité utilisé par chaque microservice.

## Gestion des rôles JWT

Le `JwtAuthenticationConverter` récupère les rôles à partir des chemins configurés dans `roles-claims`.

Exemple de JWT Keycloak :

```json
{
  "realm_access": {
    "roles": ["ADMIN", "USER"]
  },
  "resource_access": {
    "my-client": {
      "roles": ["DOSSIER_READ", "DOSSIER_WRITE"]
    }
  }
}
```

Configuration :

```yaml
security:
  jwt:
    roles-claims:
      - realm_access.roles
      - resource_access.my-client.roles
    authority-prefix: ROLE_
```

Les rôles sont convertis en autorités Spring Security :

```text
ROLE_ADMIN
ROLE_USER
ROLE_DOSSIER_READ
ROLE_DOSSIER_WRITE
```

Les rôles Realm et Client sont fusionnés, sans doublons.

## Protection des endpoints

La configuration active les annotations de sécurité avec `@EnableMethodSecurity`.

### hasRole

```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser() {
    // ...
}
```

### hasAuthority

```java
@PreAuthorize("hasAuthority('ROLE_DOSSIER_READ')")
public void getDossier() {
    // ...
}
```

`hasRole('ADMIN')` ajoute automatiquement le préfixe `ROLE_`, contrairement à `hasAuthority()`.

## CurrentPrincipal

La classe utilitaire `CurrentPrincipal` permet d'accéder au principal authentifié et aux claims de son JWT.

```java
import com.example.context.CurrentPrincipal;
```

### Récupérer le subject

```java
Optional<String> subject = CurrentPrincipal.getUserId();
```

Récupère le claim `sub`, identifiant du principal.

### Récupérer le username

```java
Optional<String> username = CurrentPrincipal.getUsername();
```

Récupère le claim `preferred_username`.

### Récupérer un claim

```java
Optional<String> email = CurrentPrincipal.getClaim("email");
```

### Récupérer tous les claims

```java
Optional<Map<String, Object>> claims = CurrentPrincipal.getClaims();
```

**Remarque :** `sub` identifie le principal du token. En Client Credentials, il peut représenter un client technique et non un utilisateur humain. De même, `preferred_username` n'est pas nécessairement présent dans un token de service.

## Gestion des erreurs

Le module fournit deux handlers personnalisés qui utilisent le `ErrorResponse` du module `spring-boot-exception`.

### 401 – Unauthorized

`CustomAuthenticationEntryPoint`

Déclenché lorsqu'une authentification est absente ou invalide.

Exemple de réponse :

```json
{
  "timestamp": "2026-11-18T13:48:08.0581217",
  "errorCode": "UNAUTHORIZED",
  "message": "Authentification requise",
  "details": {
    "source": "SPRING_SECURITY",
    "provider": "KEYCLOAK"
  }
}
```

### 403 – Forbidden

`CustomAccessDeniedHandler`

Déclenché lorsqu'un utilisateur authentifié ne dispose pas des autorisations nécessaires.

Exemple de réponse :

```json
{
  "timestamp": "2026-11-18T13:48:08.0581217",
  "errorCode": "FORBIDDEN",
  "message": "Accès refusé",
  "details": {
    "source": "SPRING_SECURITY",
    "provider": "KEYCLOAK"
  }
}
```

Le champ `source` identifie l'origine de l'erreur. Le champ `provider` est configurable via `security.provider`.

## Désactivation de la sécurité

Pour désactiver la configuration Spring Security du module :

```yaml
security:
  enabled: false
```

La configuration automatique du module est alors désactivée.