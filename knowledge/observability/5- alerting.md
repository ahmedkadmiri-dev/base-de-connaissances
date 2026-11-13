# Alerting

## 1. Définition

L'**Alerting** consiste à déclencher automatiquement une notification lorsqu'une condition prédéfinie indique une anomalie, une dégradation de performance ou un risque sur un système.

Il s'appuie généralement sur les métriques, les logs et les traces collectés par la plateforme d'observabilité.

L'objectif est d'informer rapidement les équipes concernées afin qu'elles puissent intervenir avant ou pendant un incident.

## 2. Objectifs

- Détecter rapidement les incidents.
- Informer les équipes techniques en temps réel.
- Réduire le temps de détection (MTTD).
- Réduire le temps de résolution (MTTR).
- Éviter les interruptions de service prolongées.
- Déclencher des actions correctives lorsque cela est possible.

## 3. Fonctionnement

Un système d'alerting suit généralement ce processus :

1. Collecte des métriques, logs et traces.
2. Évaluation périodique des règles d'alerte.
3. Détection d'une condition anormale.
4. Déclenchement d'une alerte.
5. Envoi d'une notification aux équipes concernées.
6. Analyse et résolution de l'incident.

Exemple :

```text
Application Spring Boot
        |
        v
   Prometheus
        |
        v
  Alert Rules
        |
        v
  Alertmanager
        |
        v
  Notification
  (Email / Teams / Slack)
```

## 4. Types d'alertes

### Alertes de disponibilité

- Application inaccessible.
- Endpoint Health en état DOWN.
- Échec des Health Checks.

### Alertes de performance

- Temps de réponse P95 trop élevé.
- Augmentation du nombre de requêtes lentes.
- Dégradation du débit de traitement.

### Alertes d'erreurs

- Augmentation du taux d'erreurs HTTP 5xx.
- Échecs répétés d'appels à une API externe.
- Erreurs de connexion à la base de données.

### Alertes d'infrastructure

- CPU trop élevé.
- Mémoire presque saturée.
- Espace disque insuffisant.
- Pool de connexions JDBC saturé.

## 5. Niveaux de sévérité

Il est recommandé de définir des niveaux de sévérité adaptés à l'organisation.

| Niveau | Signification | Exemple |
|---|---|---|
| Critical | Incident majeur nécessitant une intervention immédiate | Application indisponible |
| Warning | Dégradation nécessitant une investigation | P95 supérieur au seuil |
| Info | Événement à surveiller, sans intervention urgente | Augmentation inhabituelle du trafic |

Les niveaux et les délais de réaction doivent être définis selon les engagements de service.

## 6. Implémentation avec Prometheus et Alertmanager

Prometheus permet de définir des règles d'alerte à partir des métriques collectées.

### Exemple : taux d'erreurs HTTP 5xx

Fichier `alert-rules.yml` :

```yaml
groups:
  - name: application-alerts
    rules:
      - alert: HighHttpErrorRate
        expr: |
          sum(rate(http_server_requests_seconds_count{
            status=~"5.."
          }[5m]))
          /
          sum(rate(http_server_requests_seconds_count[5m]))
          > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High HTTP error rate"
          description: "Le taux d'erreurs HTTP 5xx dépasse 5 % depuis 5 minutes."
```

Explication :

- `expr` : condition à surveiller.
- `for` : durée pendant laquelle la condition doit rester vraie avant de déclencher l'alerte.
- `labels` : métadonnées de l'alerte, notamment sa sévérité.
- `annotations` : description et informations destinées aux intervenants.

### Exemple : utilisation CPU élevée

```yaml
- alert: HighCpuUsage
  expr: process_cpu_usage > 0.85
  for: 5m
  labels:
    severity: warning
  annotations:
    summary: "High CPU usage"
    description: "L'utilisation CPU du processus dépasse 85 %."
```

La disponibilité de `process_cpu_usage` dépend de l'instrumentation et de l'export des métriques.

## 7. Alertmanager

**Alertmanager** reçoit les alertes émises par Prometheus et gère leur distribution.

Ses principales fonctionnalités :

- **Grouping** : regrouper plusieurs alertes similaires.
- **Deduplication** : éviter les notifications en double.
- **Routing** : envoyer les alertes aux équipes concernées.
- **Silencing** : désactiver temporairement certaines alertes.
- **Inhibition** : empêcher certaines alertes secondaires de générer des notifications lorsqu'une alerte principale est déjà active.

Exemple de configuration :

```yaml
route:
  receiver: default-team
  group_by: ['alertname', 'application']
  group_wait: 30s
  group_interval: 5m
  repeat_interval: 4h

receivers:
  - name: default-team
    email_configs:
      - to: 'devops@example.com'
        from: 'monitoring@example.com'
        smarthost: 'smtp.example.com:587'
```

Les adresses et paramètres SMTP sont des exemples à adapter à l'environnement.

## 8. Intégration avec ELK

Avec la stack Elastic, l'alerting peut être mis en place à partir des données collectées et indexées dans Elasticsearch.

**Kibana Alerting** permet notamment de :

- Définir des règles basées sur des requêtes Elasticsearch.
- Détecter des erreurs répétées dans les logs.
- Surveiller des seuils de métriques.
- Envoyer des notifications via des connecteurs configurés : email, Slack, webhook, etc.

Exemple de règle :

```text
Condition :
  Nombre de logs contenant "ERROR"
  supérieur à 100
  sur les 5 dernières minutes

Action :
  Envoyer une notification à l'équipe support.
```

Les connecteurs disponibles dépendent de la version et de la licence Elastic.

## 9. Bonnes pratiques

- Définir des alertes basées sur des symptômes réels, pas uniquement sur des seuils techniques.
- Privilégier les alertes exploitables nécessitant une action.
- Éviter les alertes trop sensibles qui génèrent du bruit (alert fatigue).
- Utiliser une durée `for` pour éviter les alertes déclenchées par des pics temporaires.
- Définir un responsable et une procédure de traitement pour chaque alerte critique.
- Ajouter un contexte utile : service, environnement, sévérité et description.
- Tester régulièrement les règles et les notifications.
- Prévoir des mécanismes de silence pendant les opérations de maintenance.
- Ne jamais exposer de données sensibles dans les notifications.

## 10. Alerting et SLI/SLO

Les alertes peuvent être définies à partir des objectifs de fiabilité du service.

Exemple :

- SLI : taux de disponibilité de l'API.
- SLO : disponibilité cible de 99,9 % sur 30 jours.
- Alerte : consommation rapide de l'Error Budget.

L'approche basée sur le **Burn Rate** permet de détecter une consommation anormalement rapide du budget d'erreur et d'alerter avant que le SLO ne soit dépassé.

## À retenir

Le monitoring permet de surveiller l'état du système.

L'alerting permet de signaler automatiquement les anomalies détectées par cette surveillance.

Une bonne stratégie d'alerting doit fournir des alertes pertinentes, compréhensibles et exploitables par les équipes techniques.