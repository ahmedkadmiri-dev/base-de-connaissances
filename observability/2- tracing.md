# Distributed Tracing

## 1. Définition

Le **Distributed Tracing** (traçage distribué) est une technique d'observabilité permettant de suivre le parcours d'une requête à travers plusieurs services d'une architecture distribuée ou microservices.

Il permet de comprendre :
- Le parcours d'une requête entre les différents services.
- Le temps d'exécution de chaque opération.
- L'identification du service responsable d'une erreur ou d'une latence.
- La propagation du contexte de tracing entre les services.

## 2. Concepts fondamentaux

### TraceId

Identifiant unique permettant de suivre une requête de bout en bout à travers plusieurs services.

Tous les spans appartenant à une même trace partagent le même TraceId.

### SpanId

Identifiant unique représentant une opération précise au sein d'une trace.

Chaque service ou opération instrumentée peut générer son propre SpanId.

### ParentSpanId

Identifie le span parent à l'origine d'une opération.

Il permet de reconstruire la hiérarchie des opérations.

### Span

Représente une unité de travail dans une trace : appel HTTP, requête SQL, traitement métier, etc.

## 3. Architecture du Distributed Tracing

Exemple d'une requête traversant trois microservices :

```text
Client
  |
  v
API Gateway
  | TraceId: abc123
  | SpanId: 001
  |
  v
Simulation Service
  | TraceId: abc123
  | SpanId: 002
  |
  v
Tarification Service
    TraceId: abc123
    SpanId: 003
```

Le TraceId reste identique tout au long du parcours, tandis que chaque opération possède son propre SpanId.

## 4. Spring Boot 3 : Micrometer Tracing

Depuis Spring Boot 3, Spring Cloud Sleuth n'est plus compatible avec la nouvelle génération Spring.

Son successeur est **Micrometer Tracing**, qui s'intègre à l'écosystème Spring Boot Actuator.

Les principales technologies :

| Technologie | Rôle |
|---|---|
| Micrometer Tracing | API de tracing |
| Brave | Implémentation du tracing |
| OpenTelemetry | Standard et écosystème d'observabilité |
| Zipkin | Collecte et visualisation des traces |
| Jaeger | Collecte et visualisation des traces |

## 5. Implémentation avec Spring Boot 3

### Dépendances Maven

Exemple avec Micrometer Tracing, Brave et Zipkin :

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing-bridge-brave</artifactId>
    </dependency>

    <dependency>
        <groupId>io.zipkin.reporter2</groupId>
        <artifactId>zipkin-reporter-brave</artifactId>
    </dependency>
</dependencies>
```

Les versions sont gérées par le BOM Spring Boot.

### Configuration application.yml

```yaml
spring:
  application:
    name: simulation-service

management:
  tracing:
    sampling:
      probability: 1.0

  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans

logging:
  pattern:
    level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"
```

**Explications :**

- `sampling.probability` : définit la proportion de requêtes à tracer.
- `zipkin.endpoint` : définit l'URL du collecteur Zipkin.
- `traceId` et `spanId` : ajoutés automatiquement aux logs via le MDC.

En production, adapter le taux d'échantillonnage pour maîtriser le volume de traces.

## 6. Propagation des headers HTTP

La propagation permet de transmettre le contexte de tracing entre les microservices.

Spring Boot 3 prend en charge le standard W3C Trace Context.

### Header traceparent

Exemple :

```http
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
```

Structure :

```text
version-traceId-parentId-traceFlags
```

| Élément | Description |
|---|---|
| Version | Version du protocole |
| TraceId | Identifiant de la trace |
| ParentId | Identifiant du span parent |
| TraceFlags | Indique notamment si la trace est échantillonnée |

### Propagation automatique avec WebClient

Utiliser le `WebClient.Builder` auto-configuré par Spring Boot pour bénéficier de l'instrumentation et de la propagation automatique.

```java
@Service
@RequiredArgsConstructor
public class TarificationClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<TarificationResponse> getTarification(Long id) {

        return webClientBuilder
                .baseUrl("http://tarification-service")
                .build()
                .get()
                .uri("/tarifications/{id}", id)
                .retrieve()
                .bodyToMono(TarificationResponse.class);
    }
}
```

Le contexte de tracing est propagé dans les headers HTTP sortants lorsque le client est correctement instrumenté.

Éviter de créer un client indépendant avec `WebClient.create()` si l'on souhaite bénéficier de l'instrumentation automatique.

## 7. Création de spans personnalisés

Spring Boot instrumente automatiquement plusieurs opérations HTTP.

Pour tracer une opération métier spécifique, utiliser l'API Micrometer Observation.

```java
@Service
@RequiredArgsConstructor
public class CapaciteService {

    private final ObservationRegistry observationRegistry;

    public void calculerCapacite() {

        Observation.createNotStarted(
                "capacite.calculation",
                observationRegistry
        ).observe(() -> {

            // Logique métier

        });
    }
}
```

L'observation peut produire un span de tracing lorsque le tracing est configuré.

## 8. Visualisation avec Zipkin

Démarrer Zipkin avec Docker :

```bash
docker run -d \
  --name zipkin \
  -p 9411:9411 \
  openzipkin/zipkin
```

Interface de visualisation :

```text
http://localhost:9411
```

Zipkin permet de :
- Rechercher une trace par TraceId.
- Visualiser les spans.
- Identifier les dépendances entre services.
- Analyser les temps d'exécution.
- Localiser les opérations lentes.

## 9. Logging vs Tracing vs Metrics

| Concept | Objectif |
|---|---|
| Logging | Comprendre ce qui s'est passé |
| Tracing | Suivre le parcours d'une requête |
| Metrics | Mesurer les performances et l'état du système |

Ces trois mécanismes sont complémentaires et constituent les piliers de l'observabilité.

## 10. Bonnes pratiques

- Utiliser un TraceId commun à tous les services d'une même requête.
- Privilégier le standard W3C Trace Context.
- Propager automatiquement le contexte via les clients HTTP instrumentés.
- Ajouter des spans personnalisés pour les opérations métier importantes.
- Corréler les logs avec TraceId et SpanId.
- Adapter le sampling en fonction de l'environnement.
- Éviter d'ajouter des données sensibles dans les tags et attributs des spans.
- Ne pas confondre TraceId et identifiant métier.
- Ne pas générer manuellement un nouveau TraceId à chaque appel interne.

## 11. Documentation officielle

- [Spring Boot - Tracing](https://docs.spring.io/spring-boot/reference/actuator/tracing.html)
- [Micrometer Tracing](https://micrometer.io/docs/tracing)
- [W3C Trace Context](https://www.w3.org/TR/trace-context/)
- [Zipkin](https://zipkin.io/)
- [OpenTelemetry](https://opentelemetry.io/docs/)