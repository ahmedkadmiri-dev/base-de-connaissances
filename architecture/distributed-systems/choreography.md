# Choreography

## Definition

La Choreography est un pattern de coordination dans une architecture distribuée où chaque service participe à un workflow métier de manière autonome, sans coordinateur central.

Chaque microservice exécute sa propre logique métier, publie des événements après son traitement et réagit aux événements publiés par les autres services.

La coordination globale du workflow émerge des interactions entre les différents services.

## Core Concepts

- **Event Producer** : service qui publie un événement après une opération métier.
- **Event Consumer** : service qui écoute et consomme les événements qui l'intéressent.
- **Event Broker** : infrastructure permettant de transporter les événements entre les services (Kafka, RabbitMQ).
- **Event** : fait métier déjà survenu, communiqué aux autres services.
- **Event Handler** : composant chargé de traiter un événement reçu.
- **Compensation** : opération métier permettant de contrebalancer une opération déjà réalisée en cas d'échec ultérieur.

## How It Works

1. Un service exécute une opération métier.
2. Il publie un événement décrivant le résultat de cette opération.
3. Les services intéressés consomment cet événement.
4. Chaque service déclenche son propre traitement métier.
5. Après son traitement, il peut publier un nouvel événement.
6. Le processus continue jusqu'à la réalisation du workflow ou à la gestion d'un échec.

Aucun service ne contrôle directement l'ensemble du workflow.

## Example: Order Processing

Un workflow de création de commande peut impliquer plusieurs microservices :

1. **Order Service** : crée une commande et publie `OrderCreated`.
2. **Payment Service** : consomme `OrderCreated`, traite le paiement et publie `PaymentCompleted`.
3. **Inventory Service** : consomme `PaymentCompleted`, réserve le stock et publie `InventoryReserved`.
4. **Shipping Service** : consomme `InventoryReserved` et prépare l'expédition.

```text
Order Service
     |
     | OrderCreated
     v
  Event Broker
     |
     v
Payment Service
     |
     | PaymentCompleted
     v
  Event Broker
     |
     v
Inventory Service
     |
     | InventoryReserved
     v
  Event Broker
     |
     v
Shipping Service
```

## Communication

La Choreography est généralement associée à une communication asynchrone et événementielle.

- **Asynchronous** : les services publient et consomment des événements via un message broker sans attendre directement la réponse d'un autre service.
- **Synchronous** : une coordination décentralisée peut également utiliser des appels directs entre services, mais cette approche n'est pas la forme classique de l'Event-Driven Choreography.

## Advantages

- Absence de coordinateur central.
- Faible couplage temporel entre les services.
- Bonne évolutivité et autonomie des microservices.
- Facilite l'ajout de nouveaux consommateurs d'événements.
- Favorise la résilience grâce à la communication asynchrone.

## Disadvantages

- Difficulté à comprendre et visualiser le workflow global.
- Traçabilité et debugging plus complexes.
- Gestion des événements dupliqués et de leur ordre de livraison.
- Gestion des erreurs et des compensations plus complexe.
- Risque de dépendances implicites entre les services à travers les événements.

## Best Practices

- **Idempotent Consumers** : garantir qu'un événement traité plusieurs fois ne provoque pas d'effets métier indésirables.
- **Transactional Outbox** : garantir la cohérence entre la transaction en base de données et la publication d'un événement.
- **Dead Letter Queue (DLQ)** : isoler les messages qui ne peuvent pas être traités après les tentatives de livraison.
- **Event Versioning** : gérer l'évolution des contrats d'événements.
- **Distributed Tracing** : suivre un workflow distribué à travers plusieurs services.
- **Correlation ID** : identifier et corréler les événements appartenant au même workflow.

## Related Concepts

- **Orchestration** : coordination centralisée où un orchestrateur pilote les interactions entre les services.
- **Saga Pattern** : gestion des transactions distribuées à travers plusieurs transactions locales et des opérations de compensation. Peut être implémenté par orchestration ou choreography.
- **Event-Driven Architecture** : style architectural basé sur la production et la consommation d'événements.
- **Publish-Subscribe** : mécanisme de communication dans lequel plusieurs consommateurs peuvent recevoir les événements publiés.
- **Eventual Consistency** : modèle de cohérence dans lequel les données convergent progressivement entre les différents services.

## Technologies

- Apache Kafka
- RabbitMQ
- Spring Cloud Stream
- Spring Kafka
