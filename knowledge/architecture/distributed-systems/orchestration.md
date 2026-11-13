# Orchestration

## Definition

L'Orchestration est un pattern de coordination dans une architecture distribuée où un composant central, appelé **Orchestrator**, pilote et contrôle l'exécution d'un workflow métier impliquant plusieurs services.

L'Orchestrateur connaît les différentes étapes du processus, détermine leur ordre d'exécution et décide de la prochaine action en fonction des résultats obtenus.

## Core Concepts

- **Orchestrator** : composant central responsable de la coordination du workflow.
- **Workflow** : séquence d'étapes métier à exécuter.
- **Service** : microservice responsable d'une opération métier.
- **Command** : instruction envoyée par l'orchestrateur à un service pour déclencher une action.
- **Result** : résultat retourné par un service après traitement.
- **Compensation** : opération métier permettant d'annuler ou de contrebalancer une étape déjà exécutée en cas d'échec.

## How It Works

1. L'orchestrateur démarre le workflow.
2. Il envoie une commande au premier service.
3. Le service exécute son traitement et retourne un résultat.
4. L'orchestrateur analyse le résultat et déclenche l'étape suivante.
5. Le processus continue jusqu'à la réussite ou l'échec du workflow.
6. En cas d'échec, l'orchestrateur peut déclencher des opérations de compensation.

## Example: Order Processing

Un workflow de création de commande peut impliquer plusieurs microservices :

1. **Order Service** : création de la commande.
2. **Payment Service** : traitement du paiement.
3. **Inventory Service** : réservation du stock.
4. **Shipping Service** : préparation de l'expédition.

L'Orchestrator contrôle l'ensemble du processus et décide de l'étape suivante.

```text
                 Orchestrator
                      |
                      v
                 Order Service
                      |
                   Result
                      |
                      v
                 Orchestrator
                      |
                      v
                Payment Service
                      |
                   Result
                      |
                      v
                 Orchestrator
                      |
                      v
               Inventory Service
                      |
                   Result
                      |
                      v
                 Orchestrator
                      |
                      v
                Shipping Service
```

## Communication

L'orchestration peut utiliser deux modes de communication :

- **Synchronous** : l'orchestrateur appelle directement les services via REST ou gRPC et attend leurs réponses.
- **Asynchronous** : l'orchestrateur envoie des commandes via un message broker et reçoit les résultats sous forme de messages ou d'événements.

Le choix dépend des contraintes de disponibilité, de latence et de résilience du workflow.

## Advantages

- Centralisation de la logique de coordination.
- Visibilité complète sur l'état du workflow.
- Gestion explicite des erreurs et des compensations.
- Facilite le suivi et le monitoring des processus métier complexes.
- Contrôle précis de l'ordre d'exécution des opérations.

## Disadvantages

- Dépendance envers l'orchestrateur.
- Risque de point de défaillance centralisé si aucune stratégie de haute disponibilité n'est prévue.
- Complexité croissante de l'orchestrateur lorsque les workflows deviennent nombreux.
- Couplage entre l'orchestrateur et les contrats des services.

## Related Concepts

- **Choreography** : coordination décentralisée où chaque service réagit aux événements sans coordinateur central.
- **Saga Pattern** : gestion des transactions distribuées à travers plusieurs transactions locales et des opérations de compensation. Peut être implémenté par orchestration ou choreography.
- **Event-Driven Architecture** : style architectural basé sur la production et la consommation d'événements.
- **Workflow Engine** : moteur permettant de définir, exécuter et suivre des workflows complexes.

## Technologies

- Temporal
- Camunda
- Netflix Conductor
- Spring State Machine
- Spring Boot + Kafka / RabbitMQ
