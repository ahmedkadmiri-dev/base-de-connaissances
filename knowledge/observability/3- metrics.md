# Metrics

## 1. Présentation

Les **métriques (Metrics)** sont des mesures numériques permettant de surveiller l'état, les performances et le comportement d'une application ou d'une infrastructure au cours du temps.

Elles permettent notamment de répondre aux questions suivantes :

- Combien de requêtes sont traitées par seconde ?
- Quel est le temps moyen de réponse d'une API ?
- Quel est le taux d'erreur HTTP ?
- Quelle quantité de mémoire JVM est utilisée ?
- Quel est le taux d'utilisation du CPU ?
- Combien de connexions à la base de données sont actives ?

Les métriques sont généralement représentées sous forme de séries temporelles (*Time Series*).

### 1.1. Metrics, Logging et Tracing

Ces trois mécanismes sont complémentaires :

| Mécanisme | Objectif | Exemple |
|---|---|---|
| Logging | Comprendre un événement précis | Erreur lors de la sauvegarde |
| Tracing | Suivre le parcours d'une requête | Gateway → Service A → Service B |
| Metrics | Mesurer le comportement global | 500 requêtes/minute, 2 % d'erreurs |

### 1.2. Architecture générale

Une architecture de monitoring repose généralement sur quatre éléments :

1. **Instrumentation** : mesure les indicateurs de l'application.
2. **Collecte** : récupère périodiquement les métriques.
3. **Stockage** : conserve les données historiques.
4. **Visualisation et alerting** : affiche les indicateurs et déclenche des alertes.

---

## 2. Types de métriques

Micrometer propose plusieurs types de mesures.

### 2.1. Counter

Un compteur représente une valeur qui augmente uniquement.

Exemples :
- Nombre de simulations créées.
- Nombre de requêtes reçues.
- Nombre d'erreurs rencontrées.

```java
Counter counter = Counter.builder("simulation.created")
        .description("Nombre de simulations créées")
        .register(meterRegistry);

counter.increment();
```

### 2.2. Gauge

Une jauge représente une valeur qui peut augmenter ou diminuer.

Exemples :
- Nombre de traitements en cours.
- Nombre de connexions actives.
- Taille d'une file d'attente.

```java
AtomicInteger activeRequests = new AtomicInteger();

Gauge.builder("application.active.requests", activeRequests, AtomicInteger::get)
        .description("Nombre de requêtes actives")
        .register(meterRegistry);
```

### 2.3. Timer

Un Timer mesure la durée et le nombre d'exécutions d'une opération.

Exemples :
- Durée d'un appel API.
- Temps d'exécution d'un traitement métier.
- Durée d'une opération en base de données.

```java
Timer timer = Timer.builder("simulation.processing")
        .description("Durée de traitement des simulations")
        .register(meterRegistry);

timer.record(() -> simulationService.process());
```

### 2.4. DistributionSummary

Permet de mesurer la distribution de valeurs numériques.

Exemples :
- Montant des simulations.
- Taille des requêtes.
- Taille des fichiers traités.

```java
DistributionSummary summary = DistributionSummary.builder("simulation.amount")
        .description("Distribution des montants des simulations")
        .register(meterRegistry);

summary.record(250000);
```

---

## 3. Indicateurs importants

Pour surveiller une application Spring Boot, plusieurs indicateurs sont particulièrement utiles.

| Catégorie | Indicateur | Description |
|---|---|---|
| HTTP | Request Count | Nombre de requêtes |
| HTTP | Request Rate | Nombre de requêtes par seconde |
| HTTP | Error Rate | Pourcentage de requêtes en erreur |
| HTTP | Response Time | Temps de réponse |
| JVM | Heap Usage | Mémoire Heap utilisée |
| JVM | GC Count | Nombre de Garbage Collections |
| JVM | Thread Count | Nombre de threads actifs |
| CPU | CPU Usage | Utilisation du processeur |
| Database | Active Connections | Connexions actives |
| Database | Connection Pool Usage | Utilisation du pool de connexions |

### 3.1. Les quatre Golden Signals

Les quatre Golden Signals sont des indicateurs fondamentaux pour surveiller un service :

- **Latency** : temps nécessaire pour répondre à une requête.
- **Traffic** : volume de requêtes reçu.
- **Errors** : fréquence des requêtes échouées.
- **Saturation** : niveau de sollicitation des ressources disponibles.

Ils permettent d'avoir une première vision de la santé d'un service.

---

## 4. Micrometer et Spring Boot Actuator

### 4.1. Présentation

**Micrometer** est une bibliothèque de métriques qui fournit une API commune pour instrumenter une application Java.

Elle permet de mesurer des indicateurs sans dépendre directement d'un outil de monitoring.

Les métriques peuvent ensuite être exportées vers différentes solutions, notamment Prometheus ou d'autres systèmes compatibles.

**Spring Boot Actuator** intègre Micrometer et fournit des métriques automatiques concernant :

- La JVM.
- La mémoire.
- Le CPU.
- Les threads.
- Les requêtes HTTP.
- Les connexions aux bases de données.
- Les pools de connexions.

### 4.2. Dépendances Maven

Pour une application Spring Boot 3 :

```xml
<!-- Monitoring et métriques -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Export des métriques vers Prometheus -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 4.3. Configuration

Dans `application.yml` :

```yaml
spring:
  application:
    name: moteur-financier

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

  endpoint:
    health:
      show-details: never

  metrics:
    tags:
      application: ${spring.application.name}
```

Les principaux endpoints :

| Endpoint | Utilité |
|---|---|
| `/actuator/health` | État de santé de l'application |
| `/actuator/info` | Informations sur l'application |
| `/actuator/metrics` | Liste des métriques disponibles |
| `/actuator/prometheus` | Exposition des métriques au format Prometheus |

L'endpoint `/actuator/metrics` permet par exemple de consulter une métrique :

```text
GET /actuator/metrics/jvm.memory.used
```

L'endpoint `/actuator/prometheus` expose les métriques dans un format exploitable par Prometheus.

**Attention :** en production, les endpoints de monitoring doivent être protégés et accessibles uniquement depuis les composants autorisés. Ne pas exposer publiquement les métriques ou les détails de santé sans contrôle d'accès.

### 4.4. Créer des métriques personnalisées

Les métriques automatiques ne couvrent pas nécessairement les besoins métier.

On peut utiliser `MeterRegistry` pour créer ses propres indicateurs.

```java
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl {

    private final MeterRegistry meterRegistry;

    public void createSimulation() {

        Counter.builder("simulation.created")
                .description("Nombre de simulations créées")
                .register(meterRegistry)
                .increment();

        // Traitement métier
    }
}
```

Pour mesurer une durée :

```java
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl {

    private final MeterRegistry meterRegistry;

    public void calculate() {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Traitement métier

        } finally {
            sample.stop(
                Timer.builder("simulation.calculation.duration")
                        .description("Durée de calcul d'une simulation")
                        .register(meterRegistry)
            );
        }
    }
}
```

**Bonne pratique :** utiliser des noms de métriques explicites et des tags pour distinguer les dimensions utiles, par exemple le service, le statut ou le type de traitement.

Éviter les tags à forte cardinalité comme `userId`, `simulationId`, `requestId` ou les URL contenant des identifiants variables. Ils peuvent provoquer une explosion du nombre de séries temporelles.

---

# 5. Implémentation avec Prometheus et Grafana

## 5.1. Présentation

**Prometheus** est un système de monitoring qui collecte et stocke des métriques sous forme de séries temporelles.

Il utilise principalement un modèle Pull : il interroge périodiquement les endpoints exposés par les applications.

**Grafana** est une plateforme de visualisation qui se connecte à Prometheus pour construire des tableaux de bord et des alertes.

### 5.2. Architecture

```text
+---------------------------+
|   Spring Boot Application |
|                           |
|  Actuator + Micrometer    |
|                           |
|  /actuator/prometheus     |
+-------------+-------------+
              |
              | Scrape
              |
              v
+---------------------------+
|        Prometheus         |
|                           |
| Collecte et stockage      |
| des métriques             |
+-------------+-------------+
              |
              | PromQL
              |
              v
+---------------------------+
|          Grafana          |
|                           |
| Dashboards et Alerting    |
+---------------------------+
```

### 5.3. Configuration Prometheus

Créer un fichier `prometheus.yml` :

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'moteur-financier'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets:
          - 'host.docker.internal:8887'
```

Dans cet exemple, Prometheus interroge l'application toutes les 15 secondes.

`host.docker.internal` permet à un conteneur Docker d'accéder à l'application exécutée sur la machine hôte dans les environnements qui prennent en charge ce nom.

Adapter la cible selon l'environnement de déploiement.

### 5.4. Lancer Prometheus et Grafana avec Docker Compose

Créer un fichier `docker-compose.yml` :

```yaml
services:

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus-data:/prometheus
    extra_hosts:
      - "host.docker.internal:host-gateway"

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - "3000:3000"
    volumes:
      - grafana-data:/var/lib/grafana
    depends_on:
      - prometheus

volumes:
  prometheus-data:
  grafana-data:
```

Pour un environnement réel, utiliser des versions d'images maîtrisées plutôt que `latest`, et configurer les accès et les secrets.

Démarrer les composants :

```bash
docker compose up -d
```

Accès :

| Composant | URL |
|---|---|
| Application | http://localhost:8887 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

### 5.5. Configurer Grafana

Dans Grafana :

1. Accéder à `http://localhost:3000`.
2. Ajouter une Data Source.
3. Sélectionner Prometheus.
4. Renseigner l'URL `http://prometheus:9090`.
5. Enregistrer et tester la connexion.
6. Créer un Dashboard et ajouter des panels.

Grafana peut interroger Prometheus avec le langage **PromQL**.

### 5.6. Exemples de requêtes PromQL

**Nombre de requêtes HTTP par seconde :**

```promql
sum(rate(http_server_requests_seconds_count[5m]))
```

**Taux d'erreurs HTTP 5xx :**

```promql
sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m]))
/
sum(rate(http_server_requests_seconds_count[5m]))
* 100
```

**Utilisation de la mémoire Heap :**

```promql
sum(jvm_memory_used_bytes{area="heap"})
```

**Pourcentage d'utilisation de la mémoire Heap :**

```promql
sum(jvm_memory_used_bytes{area="heap"})
/
sum(jvm_memory_max_bytes{area="heap"})
* 100
```

**Durée des requêtes HTTP au 95e percentile :**

```promql
histogram_quantile(
    0.95,
    sum by (le) (
        rate(http_server_requests_seconds_bucket[5m])
    )
)
```

Ces requêtes supposent que les métriques HTTP et JVM correspondantes sont exposées par l'application.

### 5.7. Alerting

Prometheus et Grafana permettent de définir des alertes lorsque certains seuils sont dépassés.

Exemples :

- Taux d'erreur HTTP supérieur à 5 %.
- Temps de réponse P95 supérieur à 2 secondes.
- Utilisation de la mémoire Heap supérieure à 85 %.
- Application indisponible.

Les alertes peuvent être associées à des notifications par email ou à des outils de communication.

---

# 6. Implémentation avec ELK / Elastic Stack

## 6.1. Présentation

La stack ELK désigne :

- **Elasticsearch** : stockage et recherche des données.
- **Logstash** : traitement et transformation des données.
- **Kibana** : visualisation et tableaux de bord.

Pour la collecte des métriques, l'écosystème Elastic propose notamment :

- **Metricbeat** : collecte et expédition des métriques.
- **Elastic Agent** : agent unifié pour collecter différents types de données.
- **Elastic APM** : collecte des informations de performance applicative, notamment les métriques JVM.

ELK n'est donc pas uniquement une solution de logs : Elasticsearch et Kibana peuvent aussi exploiter des données de monitoring.

## 6.2. Architecture avec Spring Boot et Metricbeat

Une approche consiste à réutiliser l'endpoint Prometheus exposé par Spring Boot.

```text
+---------------------------+
|   Spring Boot Application |
|                           |
|  Actuator + Micrometer    |
|                           |
|  /actuator/prometheus     |
+-------------+-------------+
              |
              | Scrape
              |
              v
+---------------------------+
|        Metricbeat         |
|                           |
| Collecte les métriques    |
+-------------+-------------+
              |
              v
+---------------------------+
|       Elasticsearch       |
|                           |
| Stockage des métriques    |
+-------------+-------------+
              |
              v
+---------------------------+
|          Kibana           |
|                           |
| Visualisation             |
+---------------------------+
```

## 6.3. Configuration Metricbeat

Metricbeat possède un module Prometheus permettant de récupérer périodiquement les métriques exposées par un endpoint Prometheus.

Exemple de configuration :

```yaml
metricbeat.modules:

  - module: prometheus
    period: 15s
    metricsets:
      - collector
    hosts:
      - "moteur-financier:8887"
    metrics_path: /actuator/prometheus
```

Dans cet exemple, `moteur-financier` représente le nom DNS ou le nom du service accessible depuis Metricbeat.

Configurer ensuite la sortie Elasticsearch dans `metricbeat.yml` :

```yaml
output.elasticsearch:
  hosts: ["http://elasticsearch:9200"]
```

Après démarrage de Metricbeat, les métriques collectées sont envoyées vers Elasticsearch et peuvent être exploitées dans Kibana.

## 6.4. Alternative : Elastic APM Java Agent

Elastic APM propose un agent Java capable d'instrumenter automatiquement une application.

Il permet notamment de collecter :

- Métriques JVM.
- Utilisation CPU et mémoire.
- Nombre de threads.
- Informations de performance des transactions.
- Informations de performance des appels SQL et HTTP pris en charge.

L'agent peut être attaché au démarrage de la JVM :

```bash
java \
  -javaagent:/opt/elastic/elastic-apm-agent.jar \
  -Delastic.apm.service_name=moteur-financier \
  -Delastic.apm.server_urls=http://apm-server:8200 \
  -Delastic.apm.environment=production \
  -jar application.jar
```

Les données sont envoyées vers APM Server, puis stockées dans Elasticsearch et consultables dans Kibana.

L'agent Elastic APM peut également détecter les registres Micrometer, mais la prise en charge des métriques Micrometer personnalisées par cet agent est indiquée comme Beta dans sa documentation. Il faut tenir compte de cette limite avant de choisir cette approche.

---

# 7. Comparaison Prometheus/Grafana et ELK

| Critère | Prometheus + Grafana | ELK / Elastic Stack |
|---|---|---|
| Orientation principale | Metrics | Logs, recherche et observabilité |
| Collecte | Scraping | Agents et intégrations |
| Stockage | Prometheus | Elasticsearch |
| Visualisation | Grafana | Kibana |
| Requêtes | PromQL | Elasticsearch Query DSL / KQL selon l'usage |
| Alerting | Prometheus Alertmanager ou Grafana | Kibana / Elastic Alerting |
| Spring Boot | Actuator + Micrometer | Metricbeat, Elastic APM ou intégrations |
| Cas d'utilisation | Monitoring des performances et ressources | Centralisation et analyse de plusieurs types de données |

### Quel choix faire ?

**Prometheus + Grafana**

À privilégier lorsque le besoin principal est :
- Surveiller les performances des microservices.
- Suivre les temps de réponse et les taux d'erreur.
- Surveiller la JVM et les ressources.
- Construire des tableaux de bord techniques.

**ELK / Elastic Stack**

À privilégier lorsque l'organisation utilise déjà Elasticsearch et Kibana pour :
- Centraliser les logs.
- Rechercher et analyser les événements.
- Collecter des métriques système et applicatives.
- Regrouper logs, métriques et données APM dans une plateforme commune.

Les deux approches ne sont pas mutuellement exclusives.

---

## 8. Bonnes pratiques

### Définir des indicateurs utiles

Ne pas collecter des centaines de métriques sans objectif.

Commencer par :
- Taux de disponibilité.
- Nombre de requêtes.
- Taux d'erreur.
- Temps de réponse P95 et P99.
- Utilisation CPU et mémoire.
- Nombre de connexions actives à la base de données.

### Éviter la forte cardinalité

Éviter les tags contenant des valeurs uniques ou très variables :

```java
// À éviter
.tags("simulationId", simulationId.toString())
.tags("requestId", requestId)
```

Privilégier des dimensions maîtrisées :

```java
.tags("operation", "create")
.tags("status", "success")
```

### Définir des seuils d'alerte pertinents

Les alertes doivent détecter les anomalies qui nécessitent réellement une intervention.

### Sécuriser les endpoints Actuator

Ne pas exposer les endpoints de métriques publiquement.

### Surveiller la plateforme de monitoring

Prometheus, Elasticsearch, Grafana et les agents de collecte sont eux-mêmes des composants à surveiller.

---

## 9. Architecture recommandée

Pour une architecture Spring Boot composée de plusieurs microservices :

```text
                 +----------------------+
                 |      API Gateway     |
                 +----------------------+
                           |
             +-------------+-------------+
             |             |             |
             v             v             v
        +---------+   +---------+   +---------+
        | Service |   | Service |   | Service |
        |    A    |   |    B    |   |    C    |
        +---------+   +---------+   +---------+
             |             |             |
             +-------------+-------------+
                           |
                    Actuator + Micrometer
                           |
                           v
                     Prometheus
                           |
                           v
                       Grafana
                           |
                     Dashboards
                      et alertes
```

Chaque microservice expose ses métriques via Actuator. Prometheus les collecte et Grafana les visualise.

**À retenir :**

- Micrometer instrumente les applications Java.
- Actuator expose les métriques Spring Boot.
- Prometheus collecte et stocke les séries temporelles.
- Grafana visualise les métriques et permet de configurer des alertes.
- Metricbeat et Elastic APM permettent d'intégrer les métriques dans l'écosystème Elastic.
- Les métriques complètent le logging et le tracing dans une stratégie d'observabilité.