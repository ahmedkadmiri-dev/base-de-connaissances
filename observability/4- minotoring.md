# Monitoring

## 1. Définition

Le **Monitoring** (supervision) consiste à surveiller en continu l'état, la disponibilité et les performances d'un système afin de détecter rapidement les anomalies et d'évaluer son bon fonctionnement.

Il s'appuie sur les données d'observabilité :

- **Logs** : événements et erreurs produits par l'application.
- **Metrics** : mesures numériques sur l'application et l'infrastructure.
- **Traces** : parcours d'une requête à travers les différents services.

Le monitoring exploite ces données pour fournir une vision globale de la santé du système.

## 2. Objectifs

- Surveiller la disponibilité des applications.
- Détecter les dégradations de performances.
- Identifier les erreurs et les anomalies.
- Suivre la consommation des ressources.
- Anticiper les problèmes de capacité.
- Faciliter le diagnostic des incidents en production.

## 3. Indicateurs à surveiller

### Application

| Indicateur | Description |
|---|---|
| Response Time | Temps de réponse des requêtes HTTP |
| Throughput | Nombre de requêtes traitées par seconde |
| Error Rate | Pourcentage de requêtes en erreur |
| Availability | Disponibilité de l'application |
| P95 / P99 | Temps de réponse sous lequel se situent 95 % / 99 % des requêtes |

### Infrastructure

| Indicateur | Description |
|---|---|
| CPU Usage | Utilisation du processeur |
| Memory Usage | Consommation mémoire |
| Disk Usage | Espace disque utilisé |
| Network I/O | Trafic réseau entrant et sortant |
| JVM Heap | Mémoire Heap utilisée par la JVM |
| Garbage Collection | Fréquence et durée des opérations GC |

### Base de données

- Nombre de connexions actives.
- Nombre de connexions disponibles dans le pool.
- Temps d'exécution des requêtes SQL.
- Nombre de requêtes lentes.
- Nombre de connexions en attente.
- Taux d'erreurs de connexion.

## 4. Monitoring dans Spring Boot

Spring Boot fournit **Spring Boot Actuator** pour exposer des informations de supervision sur l'application.

### Dépendances Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### Configuration

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

  endpoint:
    health:
      show-details: when_authorized

  metrics:
    tags:
      application: moteur-financier
```

### Principaux endpoints

| Endpoint | Description |
|---|---|
| `/actuator/health` | État de santé de l'application |
| `/actuator/metrics` | Liste des métriques disponibles |
| `/actuator/prometheus` | Métriques au format Prometheus |
| `/actuator/info` | Informations sur l'application |

Attention : ces endpoints doivent être protégés et leur exposition limitée selon l'environnement.

## 5. Health Checks

Un Health Check permet de vérifier si une application ou l'une de ses dépendances est opérationnelle.

Spring Boot Actuator expose notamment :

- **Liveness** : indique si l'application est capable de continuer à fonctionner.
- **Readiness** : indique si l'application est prête à recevoir du trafic.

Configuration :

```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
```

Endpoints :

```http
GET /actuator/health/liveness
GET /actuator/health/readiness
```

Ces contrôles sont particulièrement utiles dans un environnement Kubernetes pour gérer les redémarrages et le routage du trafic.

## 6. Outils de Monitoring

| Outil | Rôle |
|---|---|
| Prometheus | Collecte et stockage des métriques |
| Grafana | Visualisation des métriques et dashboards |
| Kibana | Visualisation et analyse des données Elasticsearch |
| Elastic APM | Monitoring des performances applicatives |
| OpenTelemetry | Instrumentation et collecte de données d'observabilité |

## 7. Bonnes pratiques

- Surveiller les indicateurs métier et techniques.
- Privilégier les percentiles P95 et P99 plutôt que la moyenne seule.
- Centraliser les métriques dans une plateforme commune.
- Construire des dashboards orientés service et métier.
- Surveiller les dépendances externes : API, bases de données et systèmes tiers.
- Définir des seuils cohérents avec les objectifs de disponibilité et de performance.
- Éviter les métriques avec une cardinalité excessive.
- Protéger les endpoints Actuator.
- Associer le monitoring à une stratégie d'alerting.

## 8. Exemple : Monitoring d'une API Spring Boot

Pour une API de simulation financière, on peut suivre :

- Le nombre de simulations exécutées.
- Le temps de réponse P95 de l'API.
- Le taux d'erreurs HTTP 5xx.
- Le nombre d'appels vers Oracle.
- Le temps d'exécution des requêtes SQL.
- La consommation mémoire de la JVM.

Le monitoring permet ainsi de détecter une dégradation du service avant qu'elle ne devienne un incident majeur.

## À retenir

Le monitoring permet de répondre à trois questions :

- Le système fonctionne-t-il correctement ?
- Ses performances sont-elles conformes aux attentes ?
- Une anomalie est-elle en train de se produire ?

Il s'appuie sur les logs, les métriques et les traces, et constitue la base de la détection des incidents.