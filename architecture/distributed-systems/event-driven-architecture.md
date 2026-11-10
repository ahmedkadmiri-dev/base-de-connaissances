# Event-Driven Architecture (EDA)

## Définition

L'Event-Driven Architecture (EDA), ou architecture événementielle, est un style architectural dans lequel les composants communiquent et réagissent à des événements représentant des faits métier survenus dans le système.

Au lieu d'appeler directement un autre service pour lui demander d'exécuter une opération, un producteur publie un événement que les consommateurs intéressés peuvent recevoir et traiter indépendamment.

L'EDA favorise le faible couplage, la communication asynchrone, la scalabilité et l'évolution indépendante des composants.

## Concepts fondamentaux

- **Événement (Event)** : représentation immuable d'un fait qui s'est déjà produit dans le système.
- **Producteur d'événements (Event Producer)** : composant responsable de la publication des événements.
- **Consommateur d'événements (Event Consumer)** : composant qui s'abonne aux événements et les traite.
- **Courtier de messages (Message Broker)** : infrastructure chargée de recevoir, router et distribuer les événements entre les producteurs et les consommateurs.
- **Gestionnaire d'événements (Event Handler)** : composant responsable du traitement d'un événement reçu.
- **Canal d'événements (Event Channel)** : canal logique utilisé pour publier et consommer les événements.
- **Charge utile (Event Payload)** : données métier transportées par un événement, accompagnées de leurs métadonnées.

## Événements vs Commandes (Events vs Commands)

Un événement et une commande représentent deux notions différentes.

| Événement (Event) | Commande (Command) |
|---|---|
| Décrit un fait qui s'est produit | Demande l'exécution d'une action |
| Formulé au passé | Formulée comme une instruction |
| Représente un fait | Représente une intention |
| Peut avoir plusieurs consommateurs | Cible généralement un gestionnaire logique précis |
| `OrderCreated` | `CreateOrder` |

Un événement ne demande pas à un autre service d'exécuter une action. Il annonce un fait auquel les consommateurs peuvent réagir.

## Structure d'un événement

Un événement bien conçu contient généralement les informations suivantes :

- **Event ID** : identifiant unique de l'événement.
- **Event Type** : nom de l'événement métier.
- **Timestamp** : date et heure de production de l'événement.
- **Aggregate ID** : identifiant de l'entité métier concernée.
- **Correlation ID** : identifiant permettant de corréler plusieurs opérations liées.
- **Causation ID** : identifiant de l'événement ou de la commande à l'origine de cet événement.
- **Version** : version du schéma de l'événement.
- **Payload** : données métier associées à l'événement.

Exemple :

```json
{
  "eventId": "evt-12345",
  "eventType": "OrderCreated",
  "timestamp": "2026-11-09T14:30:00Z",
  "aggregateId": "order-456",
  "correlationId": "corr-789",
  "causationId": "cmd-123",
  "version": 1,
  "payload": {
    "orderId": "order-456",
    "customerId": "customer-789",
    "amount": 2500.00,
    "currency": "MAD"
  }
}
```

## Modèles de communication événementielle

### Publication / Abonnement (Publish-Subscribe)

Un producteur publie un événement sur un canal ou un topic sans connaître les consommateurs.

Plusieurs consommateurs peuvent s'abonner au même événement et le traiter indépendamment selon leurs responsabilités.

Exemple :

- Order Service publie `OrderCreated`.
- Notification Service consomme l'événement pour envoyer une notification.
- Analytics Service consomme le même événement pour mettre à jour les indicateurs de reporting.

### Communication point à point (Point-to-Point)

Un producteur envoie un message dans une file d'attente (Queue), dans laquelle un seul consommateur parmi plusieurs instances concurrentes traite chaque message.

Ce modèle est particulièrement adapté à la répartition de la charge entre plusieurs instances d'un même service.

### Flux d'événements (Event Streaming)

Les événements sont publiés dans un flux ordonné et persistant. Ils peuvent être consommés en continu ou rejoués à partir d'une position donnée.

Ce modèle est particulièrement adapté au traitement en temps réel, à l'analyse de données et à la synchronisation entre systèmes.

## Garanties de livraison des événements

Les brokers et les systèmes de messagerie proposent différents niveaux de garantie de livraison.

- **At-most-once** : un message est livré zéro ou une seule fois. Les doublons sont évités, mais des messages peuvent être perdus.
- **At-least-once** : un message est livré une ou plusieurs fois. Les pertes sont limitées, mais les consommateurs doivent gérer les doublons.
- **Exactly-once** : des mécanismes garantissent l'absence de doublons dans un périmètre transactionnel défini. Cela ne garantit pas automatiquement des effets métier exactement une seule fois entre plusieurs services et bases de données indépendants.

## Ordonnancement des événements (Event Ordering)

L'ordonnancement des événements désigne l'ordre dans lequel les événements sont publiés, distribués et traités.

Il est particulièrement important lorsque plusieurs événements représentent des modifications successives d'une même entité métier.

Approches courantes :

- **Partitioning** : les événements partageant une même clé sont routés vers la même partition.
- **Sequence Number** : un numéro de séquence permet d'identifier l'ordre des modifications.
- **Timestamp** : les horodatages permettent de reconstituer une chronologie, mais ne garantissent pas à eux seuls l'ordre causal.
- **Single Consumer** : traitement séquentiel des événements dans un périmètre défini.

Les garanties d'ordonnancement dépendent de la technologie de messagerie et de sa configuration.

## Fiabilité des consommateurs (Consumer Reliability)

### Consommateur idempotent (Idempotent Consumer)

Un consommateur idempotent peut traiter plusieurs fois le même événement sans provoquer de doublons dans les effets métier.

Cette propriété est essentielle lorsque le système utilise une livraison de type At-least-once.

Une implémentation courante consiste à utiliser l'identifiant unique de l'événement pour vérifier s'il a déjà été traité.

### Mécanisme de nouvelle tentative (Retry Mechanism)

Un mécanisme de nouvelle tentative permet de relancer le traitement après une erreur temporaire.

Stratégies courantes :

- Délai fixe (Fixed Delay).
- Délai exponentiel (Exponential Backoff).
- Nombre maximal de tentatives (Maximum Retry Attempts).

Il est important de distinguer les erreurs temporaires des erreurs métier ou de validation définitives.

### File des messages en échec (Dead Letter Queue - DLQ)

Une Dead Letter Queue est une file destinée à stocker les messages qui n'ont pas pu être traités correctement après épuisement des tentatives configurées.

Elle permet d'isoler les messages en échec, de les analyser et éventuellement de les retraiter sans bloquer le traitement normal des autres messages.

## Gestion des schémas et des versions (Event Schema & Versioning)

Les schémas d'événements définissent la structure et les types de données des événements publiés.

L'évolution des schémas permet de faire évoluer les contrats d'événements tout en préservant la compatibilité entre producteurs et consommateurs.

Stratégies courantes :

- **Backward Compatibility** : les nouveaux consommateurs peuvent traiter les événements produits avec d'anciens schémas.
- **Forward Compatibility** : les anciens consommateurs peuvent traiter les événements produits avec de nouveaux schémas, dans les limites des règles de compatibilité définies.
- **Schema Registry** : référentiel centralisé permettant de gérer et de valider les schémas d'événements.

Formats courants :

- JSON
- Avro
- Protocol Buffers

Bonnes pratiques :

- Privilégier les ajouts de champs.
- Éviter de modifier la signification des champs existants.
- Éviter de supprimer des champs sans stratégie de migration.
- Versionner les contrats lorsque cela est nécessaire.

## Pattern Transactional Outbox

Le Transactional Outbox Pattern garantit la cohérence entre une transaction en base de données et la publication d'un événement.

**Problématique :**

Un service met à jour sa base de données puis publie un événement. Si la transaction en base réussit mais que la publication échoue, le système devient incohérent.

**Solution :**

1. Exécuter l'opération métier.
2. Enregistrer les données métier et l'événement dans une table Outbox au sein de la même transaction.
3. Un processus indépendant récupère les événements non publiés.
4. Publier les événements dans le broker.
5. Marquer les événements publiés comme traités.

Le processus de publication peut générer des doublons. Les consommateurs doivent donc rester idempotents.

## Event Sourcing

L'Event Sourcing est un pattern architectural dans lequel la séquence des événements constitue la source de vérité principale de l'état d'une entité métier.

Au lieu de stocker uniquement le dernier état d'une entité, le système conserve les événements représentant ses différentes évolutions.

Exemple :

```text
AccountOpened
      |
MoneyDeposited
      |
MoneyDeposited
      |
MoneyWithdrawn
      |
Current Account Balance
```

L'état actuel est reconstruit en rejouant les événements enregistrés.

Concepts clés :

- **Event Store** : stockage persistant des événements métier.
- **Event Replay** : rejeu des événements pour reconstruire l'état.
- **Snapshot** : représentation périodique de l'état d'une entité permettant d'optimiser sa reconstruction.
- **Projection** : représentation dérivée des événements destinée aux requêtes et au reporting.

L'Event Sourcing n'est pas obligatoire dans une architecture événementielle. Un système EDA peut publier des événements tout en conservant son état courant dans une base relationnelle classique.

## Cohérence éventuelle (Eventual Consistency)

Dans un système distribué, les modifications des données ne sont pas nécessairement visibles simultanément par tous les services.

Lorsqu'un événement est publié, les consommateurs le traitent indépendamment. Leurs données locales peuvent donc présenter temporairement des différences.

L'Eventual Consistency désigne un modèle dans lequel les données distribuées convergent vers un état cohérent, à condition qu'aucune nouvelle modification ne survienne et que tous les événements soient finalement traités correctement.

Les applications doivent prendre en compte les incohérences temporaires, les retards de traitement et les éventuels échecs de livraison.

## Avantages

- Faible couplage entre producteurs et consommateurs.
- Communication asynchrone.
- Scalabilité indépendante des consommateurs.
- Intégration facilitée entre des systèmes hétérogènes.
- Prise en charge du traitement des données en temps réel.
- Possibilité de rejouer les événements conservés.
- Ajout de nouveaux consommateurs sans modification des producteurs.

## Inconvénients

- Complexité opérationnelle accrue.
- Débogage plus difficile entre composants distribués.
- Cohérence éventuelle au lieu d'une cohérence immédiate.
- Gestion des doublons et de l'ordonnancement.
- Gestion de l'évolution et de la compatibilité des schémas.
- Besoin d'infrastructures et de mécanismes de supervision supplémentaires.

## Technologies

### Brokers de messages et Event Streaming

- Apache Kafka
- RabbitMQ
- Apache Pulsar
- Redis Streams

### Écosystème Spring

- Spring Kafka
- Spring AMQP
- Spring Cloud Stream

### Gestion des schémas

- Confluent Schema Registry
- Apache Avro
- Protocol Buffers

### Observabilité

- OpenTelemetry
- Zipkin
- Jaeger

## Concepts associés

- **Choreography** : pattern de coordination décentralisée dans lequel les services réagissent aux événements pour participer à un workflow distribué.
- **Orchestration** : pattern de coordination centralisée dans lequel un orchestrateur pilote l'exécution d'un workflow.
- **Saga Pattern** : pattern permettant de gérer les transactions distribuées à travers des transactions locales et des opérations de compensation.
- **Event Sourcing** : pattern dans lequel les événements constituent la source de vérité de l'état métier.
- **CQRS (Command Query Responsibility Segregation)** : pattern architectural séparant les modèles de lecture et d'écriture.
- **Transactional Outbox** : pattern garantissant la publication fiable des événements en parallèle des transactions en base de données.
- **Change Data Capture (CDC)** : technique permettant de capturer les modifications d'une base de données et de les rendre disponibles à d'autres systèmes.