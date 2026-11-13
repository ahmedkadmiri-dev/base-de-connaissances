# API Management

## Définition

L'**API Management** désigne l'ensemble des pratiques et des outils permettant de gérer le cycle de vie complet des API, depuis leur création jusqu'à leur retrait.

Une plateforme d'API Management permet aux organisations de publier, sécuriser, documenter, superviser et contrôler l'utilisation de leurs API par des consommateurs internes ou externes.

Elle facilite également la gouvernance des API et leur mise à disposition auprès des développeurs et des partenaires.

## Fonctionnement

```text
             API Providers
                   |
                   v
           API Management
                   |
       +-----------+-----------+
       |           |           |
       v           v           v
   API Gateway  Developer   Analytics
                Portal
       |
       v
   Backend APIs
```

1. Les équipes développent et publient leurs API.
2. La plateforme centralise leur documentation et leur configuration.
3. Les consommateurs découvrent les API via un Developer Portal.
4. Les consommateurs s'abonnent aux API et obtiennent leurs identifiants d'accès.
5. La plateforme applique les politiques de sécurité, quotas et limitations.
6. Les utilisations sont suivies à travers des métriques et des rapports.

## Fonctionnalités principales

- **API Catalog** : centraliser et référencer les API disponibles dans l'organisation.
- **Developer Portal** : fournir un portail aux développeurs pour découvrir les API, consulter la documentation et obtenir les accès nécessaires.
- **API Lifecycle Management** : gérer les différentes étapes du cycle de vie des API : conception, publication, versioning, évolution et dépréciation.
- **API Documentation** : générer et publier la documentation des API, notamment avec OpenAPI / Swagger.
- **API Versioning** : gérer plusieurs versions d'une API afin de garantir la compatibilité avec les consommateurs existants.
- **Access Management** : gérer les applications consommatrices, les abonnements et les clés API.
- **Policies Management** : définir les politiques de sécurité, quotas, rate limiting et restrictions d'accès.
- **Analytics and Monitoring** : suivre le trafic, les erreurs, les performances et l'utilisation des API.
- **Monetization** : définir des offres commerciales et facturer l'utilisation des API lorsque le modèle économique le nécessite.

## Cycle de vie d'une API

```text
Design
   |
   v
Development
   |
   v
Testing
   |
   v
Publication
   |
   v
Consumption
   |
   v
Monitoring
   |
   v
Versioning / Deprecation
   |
   v
Retirement
```

Chaque étape permet de maîtriser l'évolution des API et de garantir leur qualité et leur disponibilité.

## Exemple : gestion des abonnements

Une entreprise expose une API permettant de consulter les informations d'un client.

Avec une plateforme d'API Management :

1. L'équipe publie l'API dans le catalogue.
2. Un partenaire découvre l'API via le Developer Portal.
3. Le partenaire demande un abonnement.
4. La plateforme valide la demande et lui attribue une clé API ou des identifiants OAuth2.
5. Un quota de 10 000 requêtes par jour est associé à son abonnement.
6. La plateforme collecte les statistiques d'utilisation.

## API Gateway vs API Management

L'API Gateway et l'API Management sont complémentaires, mais leurs responsabilités sont différentes.

| API Gateway | API Management |
|---|---|
| Gère le trafic API en temps réel | Gère le cycle de vie des API |
| Route les requêtes vers les services | Centralise le catalogue des API |
| Applique les politiques d'accès | Gère les abonnements et les consommateurs |
| Gère le rate limiting | Définit les quotas et les plans d'utilisation |
| Transforme et agrège les requêtes | Fournit la documentation et le portail développeur |
| Se concentre sur l'exécution des requêtes | Se concentre sur la gouvernance et l'administration des API |

Une plateforme d'API Management intègre généralement un ou plusieurs API Gateways pour appliquer les politiques configurées.

## Avantages

- Gouvernance centralisée des API.
- Simplification de l'accès aux API pour les partenaires et développeurs.
- Meilleure maîtrise de la sécurité et des consommations.
- Suivi de l'utilisation et des performances.
- Gestion cohérente des versions et de la documentation.
- Possibilité de proposer des offres commerciales autour des API.

## Inconvénients

- Coût de licence et d'infrastructure selon la solution choisie.
- Complexité supplémentaire dans l'administration des API.
- Nécessité de définir une gouvernance claire.
- Dépendance potentielle à une plateforme ou à un fournisseur.

## Technologies

- **Apigee** : plateforme complète d'API Management proposée par Google Cloud.
- **Azure API Management** : solution Microsoft pour publier, sécuriser et gérer les API.
- **AWS API Gateway** : service AWS de gestion et d'exposition des API, avec des fonctionnalités de gestion associées.
- **Kong Konnect** : plateforme de gestion et de gouvernance des API basée sur l'écosystème Kong.
- **WSO2 API Manager** : plateforme open source d'API Management.
- **Gravitee** : plateforme de gestion et de sécurisation des API.

## Concepts associés

- API Gateway
- Reverse Proxy
- API Lifecycle Management
- API Governance
- OpenAPI / Swagger
- OAuth2 / OpenID Connect
- Rate Limiting
- API Versioning