# Logging

## 1. Présentation

Spring Boot utilise **SLF4J** comme API de logging et **Logback** comme implémentation par défaut.

L'utilisation de **Lombok `@Slf4j`** permet d'obtenir automatiquement un logger dans les classes applicatives.

Pour une application structurée en couches, le logging peut être organisé de deux manières :

- **Logging manuel** : utiliser `@Slf4j` pour enregistrer les événements métier importants.
- **Logging transversal avec AOP** : utiliser une annotation personnalisée et un Aspect pour journaliser automatiquement les appels de méthodes, leur durée d'exécution et leurs erreurs.

L'objectif est de centraliser les informations techniques sans multiplier les instructions `log.info()` dans toutes les classes.

---

## 2. Utilisation de SLF4J

Ajouter l'annotation `@Slf4j` sur les classes nécessitant des logs.

### Exemple

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimulationServiceImpl {

    public void save(Long simulationId) {
        log.info("Sauvegarde de la simulation id={}", simulationId);

        try {
            // Traitement métier
        } catch (Exception exception) {
            log.error(
                "Erreur lors de la sauvegarde de la simulation id={}",
                simulationId,
                exception
            );
            throw exception;
        }
    }
}
```

### Niveaux de logs

| Niveau | Utilisation |
|---|---|
| `ERROR` | Erreur nécessitant un diagnostic ou une intervention |
| `WARN` | Situation anormale mais non bloquante |
| `INFO` | Événement important du traitement applicatif |
| `DEBUG` | Informations techniques utiles au diagnostic |
| `TRACE` | Informations très détaillées |

Exemple :

```java
log.error("Erreur lors du traitement");
log.warn("Simulation non trouvée : id={}", simulationId);
log.info("Simulation créée : id={}", simulationId);
log.debug("Traitement de la simulation id={}", simulationId);
log.trace("Détail du traitement : {}", data);
```

---

## 3. Logging transverse avec AOP

### 3.1. Présentation

Le logging est une préoccupation transversale : plusieurs couches de l'application ont besoin des mêmes informations de diagnostic.

Avec **Spring AOP**, il est possible de centraliser cette responsabilité dans un Aspect.

Au lieu de répéter les mêmes instructions de logging dans chaque méthode, on utilise une annotation personnalisée, par exemple `@Loggable`.

L'Aspect intercepte les méthodes annotées et enregistre automatiquement :

- Le nom de la classe et de la méthode.
- Le début de l'exécution.
- La durée d'exécution.
- La fin de l'exécution.
- Les exceptions éventuelles.

### 3.2. Dépendance Maven

Ajouter le starter AOP :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 3.3. Créer l'annotation `@Loggable`

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}
```

Cette annotation peut être placée sur une classe ou directement sur une méthode.

### 3.4. Créer le LoggingAspect

```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@within(Loggable) || @annotation(Loggable)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String method = joinPoint.getSignature().toShortString();
        long start = System.nanoTime();

        log.info("Début exécution : {}", method);

        try {
            Object result = joinPoint.proceed();

            long duration = (System.nanoTime() - start) / 1_000_000;

            log.info(
                "Fin exécution : {} | durée={} ms",
                method,
                duration
            );

            return result;

        } catch (Throwable exception) {
            long duration = (System.nanoTime() - start) / 1_000_000;

            log.error(
                "Erreur exécution : {} | durée={} ms",
                method,
                duration,
                exception
            );

            throw exception;
        }
    }
}
```

**Fonctionnement :**

- `@Aspect` : déclare une classe comme Aspect.
- `@Around` : intercepte l'exécution d'une méthode.
- `ProceedingJoinPoint` : donne accès à la méthode interceptée.
- `proceed()` : exécute réellement la méthode.
- `System.nanoTime()` : mesure la durée d'exécution.

L'Aspect ne modifie pas le comportement métier : il observe l'exécution puis laisse remonter les exceptions.

### 3.5. Utilisation dans les couches applicatives

L'annotation peut être utilisée sur les Controllers, Services et Repositories.

**Controller**

```java
@RestController
@RequestMapping("/simulations")
@Loggable
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public SimulationResponse create(
            @RequestBody SimulationRequest request) {

        return simulationService.create(request);
    }
}
```

**Service**

```java
@Service
@Loggable
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

    private final SimulationRepository simulationRepository;

    @Override
    public SimulationResponse create(SimulationRequest request) {
        // Traitement métier
        return null;
    }
}
```

**Repository**

```java
@Repository
@Loggable
public interface SimulationRepository
        extends JpaRepository<Simulation, Long> {
}
```

L'annotation au niveau de la classe permet d'intercepter ses méthodes éligibles. Elle peut aussi être placée uniquement sur certaines méthodes pour limiter le logging.

**Attention :** Spring AOP fonctionne avec des proxies. Les appels internes entre méthodes d'une même instance (self-invocation), ainsi que certaines méthodes privées ou non interceptables, ne passent pas par le proxy.

### 3.6. Bonnes pratiques avec AOP

- Ne pas logger systématiquement les paramètres et les objets retournés : ils peuvent contenir des données sensibles ou volumineuses.
- Éviter de journaliser plusieurs fois la même exception dans différentes couches.
- Utiliser `INFO` pour les événements importants et `DEBUG` pour les détails techniques.
- Éviter d'annoter systématiquement toutes les couches si cela génère des logs trop volumineux.
- Privilégier le logging métier explicite dans les Services et le logging technique transversal avec AOP.

---

## 4. Corrélation des requêtes : RequestId et TraceId

### 4.1. RequestId

Le `requestId` identifie une requête HTTP.

Il permet de retrouver tous les logs associés à une même requête dans un service donné.

Exemple :

```text
requestId=8f42a1
```

Il peut être généré par l'application à la réception d'une requête ou récupéré depuis un en-tête HTTP, par exemple `X-Request-Id`.

### 4.2. TraceId et SpanId

Dans une architecture distribuée, une requête peut traverser plusieurs microservices.

Le tracing distribué permet de suivre son parcours.

- `traceId` : identifie une trace complète, partagée entre les opérations liées à une même requête distribuée.
- `spanId` : identifie une opération particulière au sein de cette trace.
- `parentSpanId` : permet de représenter la relation entre un Span parent et un Span enfant.

Exemple :

```text
API Gateway
    |
    | traceId=ABC123, spanId=001
    |
    +----> Moteur Financier
    |          traceId=ABC123, spanId=002
    |
    +----> Service Client
               traceId=ABC123, spanId=003
```

Le `traceId` reste identique pendant toute la trace, tandis que chaque opération possède son propre `spanId`.

### 4.3. Différence entre RequestId et TraceId

| Identifiant | Objectif |
|---|---|
| `requestId` | Identifier une requête HTTP |
| `traceId` | Identifier une requête distribuée à travers plusieurs services |
| `spanId` | Identifier une opération dans une trace |

Ces identifiants sont complémentaires. Dans une architecture microservices, le `traceId` est particulièrement utile pour suivre une requête de bout en bout.

---

## 5. Implémentation du RequestId avec un Filter

Pour gérer un `requestId` personnalisé, on peut utiliser `OncePerRequestFilter` et le MDC de SLF4J.

Le **MDC (Mapped Diagnostic Context)** permet d'associer des informations contextuelles aux logs du thread courant.

### 5.1. RequestIdFilter

```java
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestId = request.getHeader(REQUEST_ID_HEADER);

        if (requestId == null || !requestId.matches("[a-zA-Z0-9-]{1,64}")) {
            requestId = UUID.randomUUID().toString();
        }

        try {
            MDC.put(REQUEST_ID, requestId);
            response.setHeader(REQUEST_ID_HEADER, requestId);

            filterChain.doFilter(request, response);

        } finally {
            MDC.remove(REQUEST_ID);
        }
    }
}
```

Le Filter :

1. Récupère le `requestId` transmis dans l'en-tête HTTP.
2. Vérifie son format.
3. Génère un identifiant si nécessaire.
4. Ajoute l'identifiant au MDC.
5. Retourne l'identifiant dans la réponse HTTP.
6. Nettoie le MDC à la fin de la requête.

La validation du format évite d'accepter des valeurs arbitraires dans cet en-tête.

**Important :** le MDC est lié au thread. Pour les traitements asynchrones et les changements de thread, il faut prévoir une propagation du contexte.

---

## 6. Configuration Logback avec les identifiants

### 6.1. Configuration `logback-spring.xml`

Le fichier `logback-spring.xml` permet de personnaliser le format des logs et les appenders.

Exemple avec `requestId`, `traceId` et `spanId` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <springProperty
        scope="context"
        name="applicationName"
        source="spring.application.name"
        defaultValue="application"/>

    <appender name="CONSOLE"
              class="ch.qos.logback.core.ConsoleAppender">

        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} %-5level
                [${applicationName}]
                [requestId=%X{requestId:-}]
                [traceId=%X{traceId:-}]
                [spanId=%X{spanId:-}]
                [%thread] %logger{36} - %msg%n
            </pattern>
        </encoder>

    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>
```

Les expressions `%X{...}` récupèrent les valeurs présentes dans le MDC.

- `requestId` est alimenté par notre `RequestIdFilter`.
- `traceId` et `spanId` peuvent être alimentés automatiquement par Micrometer Tracing lorsqu'un bridge de tracing est configuré.

### 6.2. Exemple de résultat

```text
2026-11-09 10:30:15.120 INFO
[moteur-financier]
[requestId=8f42a1]
[traceId=abc123]
[spanId=001]
[http-nio-8080-exec-1]
SimulationController - Début exécution

2026-11-09 10:30:15.250 INFO
[moteur-financier]
[requestId=8f42a1]
[traceId=abc123]
[spanId=002]
[http-nio-8080-exec-1]
SimulationServiceImpl - Fin exécution : SimulationServiceImpl.create() | durée=130 ms
```

Les identifiants permettent de retrouver les logs d'une requête et de les corréler avec une trace distribuée.

---

## 7. Configuration des niveaux de logs

Dans `application.properties` :

```properties
# Niveau global
logging.level.root=INFO

# Niveau de l'application
logging.level.ma.wafaimmobilier=DEBUG
```

Il est possible de configurer des niveaux différents selon les packages :

```properties
logging.level.ma.wafaimmobilier.controller=INFO
logging.level.ma.wafaimmobilier.service=DEBUG
logging.level.ma.wafaimmobilier.repository=WARN
```

---

## 8. Bonnes pratiques

### Utiliser les paramètres SLF4J

Privilégier :

```java
log.info("Simulation créée avec id={}", simulationId);
```

Éviter :

```java
log.info("Simulation créée avec id=" + simulationId);
```

### Logger les exceptions avec leur stack trace

Privilégier :

```java
log.error("Erreur lors de la sauvegarde de la simulation", exception);
```

Éviter :

```java
log.error("Erreur : {}", exception.getMessage());
```

La première version conserve la stack trace et facilite le diagnostic.

### Ne pas logger de données sensibles

Ne jamais logger :

- Mots de passe.
- Tokens d'authentification.
- Informations bancaires.
- Données personnelles sensibles.
- Corps complets des requêtes sans justification.

### Éviter les logs redondants

Ne pas logger systématiquement la même exception dans le Repository, le Service, le Controller et le GlobalExceptionHandler.

Il est préférable de choisir un niveau de journalisation adapté à chaque couche et de conserver une seule journalisation complète de l'exception.

### Utiliser AOP avec discernement

L'AOP est particulièrement adapté aux préoccupations techniques transversales : durée d'exécution, entrée/sortie des méthodes et suivi des erreurs.

Les événements métier doivent rester journalisés explicitement dans les Services.

---

## 9. Architecture recommandée

| Composant | Responsabilité |
|---|---|
| `@Slf4j` | Logger les événements métier et techniques |
| `@Loggable` | Marquer les classes ou méthodes à intercepter |
| `LoggingAspect` | Centraliser le logging des exécutions |
| `RequestIdFilter` | Générer ou récupérer le RequestId |
| `MDC` | Transporter les identifiants dans les logs du thread |
| `logback-spring.xml` | Définir le format et les appenders |
| `Micrometer Tracing` | Générer et propager les TraceId et SpanId |

**À retenir :**

- SLF4J est l'API de logging.
- Logback est l'implémentation.
- AOP permet de centraliser le logging technique.
- MDC permet d'ajouter un contexte aux logs.
- RequestId identifie une requête HTTP.
- Micrometer Tracing permet de suivre une requête distribuée entre plusieurs microservices.