## 4. Exemple d'utilisation

### 4.1. Configuration du microservice

Chaque microservice consommateur doit configurer la connexion à la base de données d'audit dans son fichier `application.yml`.

Les paramètres doivent pointer vers la base centralisée d'audit.

```yaml
audit:
  datasource:
    url: jdbc:oracle:thin:@//oracle-host:1521/AUDITDB
    username: ${AUDIT_DB_USERNAME}
    password: ${AUDIT_DB_PASSWORD}
```

Les identifiants de connexion sont injectés via des variables d'environnement.

Le module `spring-boot-audit` fournit la configuration technique du DataSource, de l'EntityManager et du Repository. Le microservice fournit uniquement les paramètres de connexion et le driver JDBC adapté à la base utilisée.

### 4.2. Appel depuis un microservice

Dans `moteur-financier`, le développeur utilise `AuditService` pour enregistrer une action métier.

```java
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final AuditService auditService;

    public void modifierSimulation(Long id, BigDecimal nouveauMontant) {

        BigDecimal ancienMontant = recupererMontant(id);

        // Traitement métier
        modifierMontant(id, nouveauMontant);

        // Préparation de l'audit
        AuditCommand command = new AuditCommand(
                "SIMULATION",
                id.toString(),
                AuditAction.UPDATED,
                Map.of("montant", ancienMontant),
                Map.of("montant", nouveauMontant)
        );

        auditService.record(command);
    }
}
```

Le microservice transmet uniquement les informations métier :

- Type de l'objet concerné.
- Identifiant de l'objet.
- Action effectuée.
- État avant modification.
- État après modification.

Il ne transmet ni l'utilisateur, ni le nom du microservice, ni le `traceId`.

### 4.3. Traitement dans le module Audit

La méthode `record()` récupère automatiquement le contexte technique avant de construire l'événement.

```java
@Override
public void record(AuditCommand command) {

    // 1. Récupération du contexte technique
    AuditContext context = contextProvider.getCurrentContext();

    // 2. Construction de l'événement complet
    AuditEvent event = new AuditEvent(
            UUID.randomUUID(),
            command.aggregateType(),
            command.aggregateId(),
            command.action(),
            context.actorId(),
            context.source(),
            context.traceId(),
            command.before(),
            command.after(),
            Instant.now()
    );

    // 3. Conversion en entité JPA
    AuditEventEntity entity = toEntity(event);

    // 4. Persistance
    repository.save(entity);
}
```

### 4.4. Récupération du contexte technique

Le `AuditContextProvider` centralise la récupération des métadonnées :

| Information | Source |
|---|---|
| `actorId` | Spring Security |
| `source` | Configuration du microservice |
| `traceId` | Micrometer Tracing |

```java
public AuditContext getCurrentContext() {

    return new AuditContext(
            getCurrentActor(),
            applicationName,
            getCurrentTraceId()
    );
}
```

### 4.5. Résultat en base de données

L'événement complet est enregistré dans la table `AUDIT_EVENT`.

| Colonne | Valeur |
|---|---|
| EVENT_ID | UUID généré |
| AGGREGATE_TYPE | SIMULATION |
| AGGREGATE_ID | 286 |
| ACTION | UPDATED |
| ACTOR_ID | AHMED |
| SOURCE | MOTEUR_FINANCIER |
| TRACE_ID | trace-id-123 |
| BEFORE_STATE | `{"montant":200000}` |
| AFTER_STATE | `{"montant":250000}` |
| OCCURRED_AT | Date de création de l'événement |

**Principe :** le microservice décrit l'action métier à auditer ; le module Audit enrichit cette action avec le contexte technique, construit l'événement complet et le persiste dans la base centralisée d'audit.