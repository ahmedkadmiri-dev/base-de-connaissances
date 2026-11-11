# Base de connaissances techniques

> Un référentiel technique structuré couvrant le génie logiciel, le développement Backend et Frontend, l'architecture logicielle, les infrastructures, le DevOps, la sécurité et les implémentations logicielles réutilisables.

[![Auteur](https://img.shields.io/badge/Auteur-Ahmed%20Kadmiri-blue)](https://github.com/ahmedkadmiri-dev)
![Domaine](https://img.shields.io/badge/Domaine-Génie%20Logiciel-green)
![Knowledge](https://img.shields.io/badge/Documentation-Concepts%20%26%20Bonnes%20Pratiques-orange)
![Templates](https://img.shields.io/badge/Templates-Implémentations%20Réutilisables-purple)

---

## 🎯 Présentation

Ce dépôt constitue une base de connaissances techniques destinée à centraliser les concepts du génie logiciel, les principes d'architecture, les documentations techniques, les bonnes pratiques ainsi que les implémentations logicielles réutilisables.

Il repose sur deux axes complémentaires :

- **Knowledge (Connaissances) :** définitions, concepts, principes, protocoles, normes, design patterns, architectures et bonnes pratiques d'ingénierie logicielle.
- **Templates (Modèles et implémentations) :** modules techniques réutilisables, implémentations de référence, configurations et composants prêts à être intégrés dans des projets.

L'objectif est de construire un référentiel technique structuré et évolutif, permettant de comprendre les technologies, de faciliter les choix techniques, de concevoir des architectures robustes et d'accélérer les développements.

---

# 📚 Organisation du dépôt

```text
base-de-connaissances/
│
├── README.md
│
├── knowledge/
│   │
│   ├── 01-software-engineering/
│   ├── 02-backend-engineering/
│   ├── 03-frontend-engineering/
│   ├── 04-software-architecture/
│   ├── 05-api-integration/
│   ├── 06-database-engineering/
│   ├── 07-security-engineering/
│   ├── 08-testing-engineering/
│   ├── 09-devops-ci-cd/
│   ├── 10-infrastructure-middleware/
│   ├── 11-observability/
│   ├── 12-performance-engineering/
│   ├── 13-technical-leadership/
│   └── tools/
│
└── templates/
    │
    ├── spring-boot/
    │   ├── core/
    │   ├── security/
    │   ├── integration/
    │   ├── testing/
    │   └── starters/
    │
    ├── devops/
    │   ├── docker/
    │   ├── jenkins/
    │   ├── sonarqube/
    │   ├── nexus/
    │   └── ansible/
    │
    └── infrastructure/
        ├── nginx/
        └── kubernetes/
```

---

# 📖 01 — Knowledge : Connaissances techniques

Cette section regroupe la documentation conceptuelle et théorique.

Elle couvre les définitions, les principes fondamentaux, les mécanismes de fonctionnement, les approches architecturales, les technologies, les protocoles et les bonnes pratiques.

Chaque documentation doit permettre de comprendre un concept, son utilité, son fonctionnement, ses avantages, ses limites et ses cas d'utilisation.

## 01. Génie logiciel (Software Engineering)

Fondamentaux et principes du développement logiciel professionnel.

- Paradigmes de programmation
  - Programmation orientée objet (OOP)
  - Programmation fonctionnelle
  - Programmation procédurale
  - Programmation orientée aspect (AOP)
- Principes de conception logicielle
  - SOLID
  - DRY
  - KISS
  - YAGNI
  - Séparation des responsabilités (Separation of Concerns)
  - Composition plutôt qu'héritage
  - Loi de Demeter
- Clean Code
- Clean Architecture
- Refactoring
- Dette technique
- Design Patterns
  - Création
  - Structure
  - Comportement
- Cycle de vie du développement logiciel (SDLC)
- Méthodes Agile et Scrum
- TDD, BDD et DDD
- Revue de code (Code Review)
- Standards de codage
- Versionnement sémantique (Semantic Versioning)
- Workflows Git

## 02. Développement Backend (Backend Engineering)

Concepts, technologies et frameworks dédiés au développement côté serveur.

- Java
  - Collections
  - Streams
  - Optional
  - Generics
  - Records
  - Enums
  - API Date & Time
  - Gestion des exceptions
  - Concurrence et multithreading
  - CompletableFuture
  - JVM et Garbage Collection
  - Performance Java
- Écosystème Spring
  - Spring Core
  - Injection de dépendances (DI)
  - Spring Boot
  - Spring MVC
  - Spring Data JPA
  - Spring Security
  - Spring Cloud
  - Spring AOP
  - Transactions Spring
  - Spring Actuator
- Persistance des données
  - JDBC
  - JPA
  - Hibernate
  - Mapping des entités
  - Relations entre entités
  - Stratégies de chargement
  - Lazy vs Eager
  - Problème N+1
  - Entity Graphs
  - JPQL et requêtes natives
  - Specifications
  - Persistence Context
  - Transactions
  - Multi-DataSources
- Gestion des dépendances et du build
  - Maven
  - Gradle
  - POM
  - Dependency Management
  - Maven Profiles
  - Maven Plugins
  - Projets multi-modules
- Bonnes pratiques Backend
  - DTO
  - Validation
  - Gestion centralisée des exceptions
  - Logging
  - Caching
  - Scheduling
  - Traitements asynchrones

## 03. Développement Frontend (Frontend Engineering)

Technologies et concepts de développement des applications côté client.

- HTML et CSS
- JavaScript
- TypeScript
- Angular
  - Architecture
  - Components
  - Services
  - Injection de dépendances
  - Routing
  - Guards
  - Interceptors
  - Reactive Forms
  - Template-Driven Forms
  - RxJS
  - Observables
  - Subjects
  - Signals
  - HTTP Client
  - Gestion d'état (State Management)
  - Lazy Loading
  - Authentification et autorisation
  - Tests Angular
- Architecture Frontend
- Architecture orientée composants
- SPA et SSR
- Performance Web
- Sécurité Frontend

## 04. Architecture logicielle (Software Architecture)

Concepts architecturaux, styles d'architecture et conception des systèmes.

- Fondamentaux de l'architecture logicielle
- Architecture en couches (Layered Architecture)
- Architecture N-Tiers
- Clean Architecture
- Architecture Hexagonale
- Architecture Onion
- Monolithe modulaire (Modular Monolith)
- Architecture Microservices
- Monolithe vs Microservices
- Domain-Driven Design (DDD)
- Bounded Context
- CQRS
- Event Sourcing
- Architecture événementielle (Event-Driven Architecture)
- Systèmes distribués
- Transactions distribuées
- Saga Pattern
- Strangler Pattern
- Backend for Frontend (BFF)
- API Gateway
- Service Discovery
- Configuration Server
- Reverse Proxy
- Load Balancing
- Patterns de résilience
  - Circuit Breaker
  - Retry
  - Timeout
  - Bulkhead
  - Rate Limiting
- Messaging et Streaming
  - Message Broker
  - Event Streaming
  - RabbitMQ
  - Apache Kafka

## 05. API et intégration (API & Integration Engineering)

Conception des API, protocoles de communication et intégration des systèmes.

- Principes REST
- Protocole HTTP
- Méthodes HTTP
- Codes de statut HTTP
- Versionnement des API
- Contrats d'API
- OpenAPI / Swagger
- Conception des requêtes et réponses
- Format standardisé des erreurs
- Validation des API
- Pagination
- Filtrage et tri
- Idempotence
- Correlation ID
- Rate Limiting
- Retry et Timeout
- REST Client
- WebClient
- OpenFeign
- SOAP
- Webhooks
- Communication synchrone et asynchrone
- Intégration d'API externes

## 06. Ingénierie des bases de données (Database Engineering)

Technologies de bases de données, modélisation et optimisation des performances.

- Fondamentaux des bases de données
- Bases relationnelles
- Bases NoSQL
- SQL et PL/SQL
- Modélisation des données
- Normalisation
- Tables, vues et séquences
- Index et contraintes
- Procédures stockées et fonctions
- Triggers
- Transactions
- Propriétés ACID
- Niveaux d'isolation
- Verrouillage
- Plans d'exécution
- Optimisation des requêtes
- Connection Pooling
- Migration de bases de données
- Oracle
- PostgreSQL
- Redis
- Flyway
- Liquibase
- Migration des données

## 07. Ingénierie de la sécurité (Security Engineering)

Concepts de sécurité des applications et des infrastructures.

- Fondamentaux de la sécurité
- Authentification et autorisation
- IAM
- RBAC et ABAC
- OAuth 2.0
- OpenID Connect
- JWT
- SSO
- Keycloak
- TLS / SSL
- Certificats
- KeyStore et TrustStore
- CORS
- CSRF
- OWASP Top 10
- Gestion des secrets (Secrets Management)
- Chiffrement et hachage
- Sécurité des API
- Secure Coding
- LDAP et Active Directory

## 08. Ingénierie des tests (Testing Engineering)

Méthodologies, stratégies et outils de test logiciel.

- Fondamentaux des tests logiciels
- Pyramide des tests (Testing Pyramid)
- Tests unitaires
- Tests d'intégration
- Tests de bout en bout (End-to-End)
- Tests de contrats (Contract Testing)
- Tests d'acceptation
- Tests de régression
- Tests de performance
- TDD
- BDD
- JUnit
- Mockito
- MockMvc
- Testcontainers
- Cucumber
- Test Fixtures
- Couverture de tests
- Stratégie de tests

## 09. DevOps et CI/CD

Automatisation, intégration continue, livraison continue et industrialisation.

- Fondamentaux DevOps
- CI / CD
- Git
- GitHub et GitLab
- Jenkins
- Pipeline as Code
- SonarQube
- Nexus
- Dépôt d'artefacts (Artifact Repository)
- Versionnement des artefacts
- Docker
- Docker Compose
- Kubernetes
- Ansible
- Infrastructure as Code
- Linux
- Bash
- Nginx
- Stratégies de déploiement
  - Blue-Green Deployment
  - Canary Deployment
  - Rollback

## 10. Infrastructure et Middleware

Composants d'infrastructure et services intermédiaires utilisés dans les systèmes d'information.

- Fondamentaux de l'infrastructure
- Réseaux
- DNS
- TCP/IP
- HTTP / HTTPS
- Reverse Proxy
- Load Balancer
- API Gateway
- API Manager
- Message Broker
- Event Streaming
- Serveur d'applications
- Serveur Web
- Service Discovery
- Configuration Server
- Cache
- Distributed Cache
- Object Storage
- SMTP
- LDAP / Active Directory

## 11. Observabilité et supervision (Observability & Monitoring)

Surveillance, diagnostic et compréhension du comportement des applications.

- Fondamentaux de l'observabilité
- Logging
- Structured Logging
- Correlation ID
- MDC
- Métriques
- Distributed Tracing
- Health Checks
- Monitoring
- Alerting
- OpenTelemetry
- Prometheus
- Grafana
- ELK / EFK
- Spring Boot Actuator

## 12. Ingénierie de la performance (Performance Engineering)

Optimisation des applications et des systèmes.

- Fondamentaux de la performance
- Performance JVM
- Gestion de la mémoire
- Garbage Collection
- Connection Pooling
- Performance des bases de données
- Optimisation des requêtes
- Stratégies de cache
- Tests de charge (Load Testing)
- Tests de stress
- Profiling
- Scalabilité
- Haute disponibilité

## 13. Leadership technique et gouvernance (Technical Leadership)

Organisation et gouvernance des activités d'ingénierie logicielle.

- Revue d'architecture
- Architecture Decision Records (ADR)
- Standards de revue de code
- Coding Standards
- Definition of Done
- Gestion de la dette technique
- Gestion des versions et releases
- Gestion des incidents
- Production Readiness
- Capacity Planning
- Roadmaps techniques
- Gouvernance de la qualité logicielle

---

# 🧩 02 — Templates : Modules et implémentations réutilisables

Cette section regroupe les modules techniques, les configurations et les projets de référence destinés à être réutilisés dans différents contextes de développement.

Contrairement à la section Knowledge, qui explique les concepts, cette section présente leur mise en œuvre concrète.

Chaque module doit être documenté, configurable, testable et conçu pour faciliter son intégration dans des projets réels.

## Structure des templates

Un module peut contenir les éléments suivants selon sa nature :

```text
module-name/
│
├── README.md
├── pom.xml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       └── java/
│
└── examples/
```

## 01. Modules Spring Boot — Core

Modules transverses et composants communs aux applications Spring Boot.

| Module | Description |
|---|---|
| Logging | Configuration centralisée du logging et logs structurés |
| Exception Handling | Gestion centralisée et standardisée des exceptions |
| API Documentation | Configuration OpenAPI / Swagger |
| Validation | Configuration et composants de validation |
| Audit | Traçabilité des opérations métier |
| Correlation ID | Corrélation des requêtes et propagation MDC |
| Configuration | Gestion des configurations et des profils |
| Health Checks | Vérification de la santé des applications |
| Monitoring | Intégration des métriques et Actuator |
| Common Library | Bibliothèque de composants communs |

## 02. Modules Spring Boot — Sécurité

Modules destinés à la sécurisation des applications.

| Module | Description |
|---|---|
| OAuth2 Resource Server | Validation des tokens JWT |
| Keycloak Integration | Intégration de l'authentification Keycloak |
| OAuth2 Client | Configuration d'un client OAuth2 |
| Role-Based Access | Implémentation RBAC |
| Method Security | Autorisation au niveau des méthodes |
| Secrets Management | Gestion externalisée des secrets |
| TLS Configuration | Configuration SSL/TLS |

## 03. Modules Spring Boot — Intégration

Composants dédiés à l'intégration des systèmes et services externes.

| Module | Description |
|---|---|
| REST Client | Client HTTP standardisé |
| WebClient | Communication HTTP réactive |
| OpenFeign | Client HTTP déclaratif |
| Resilience | Retry, Timeout et Circuit Breaker |
| Cache | Configuration et abstraction du cache |
| Messaging | Intégration des systèmes de messagerie |
| Database Migration | Intégration Flyway / Liquibase |
| File Management | Gestion des fichiers et des documents |
| Email | Envoi d'e-mails et templates |
| Scheduler | Configuration des tâches planifiées |

## 04. Modules Spring Boot — Testing

Composants et configurations destinés à faciliter les tests.

| Module | Description |
|---|---|
| Testing Parent | Gestion centralisée des dépendances de test |
| Unit Testing | Configuration JUnit et Mockito |
| Integration Testing | Tests avec les dépendances réelles |
| REST API Testing | Tests HTTP avec MockMvc |
| Testcontainers | Tests avec bases de données et infrastructures |
| Test Fixtures | Données de test réutilisables |
| Contract Testing | Validation des contrats d'API |

## 05. Templates DevOps

Configurations et outils destinés à l'industrialisation des applications.

| Template | Description |
|---|---|
| Dockerfile | Image Docker standard pour Spring Boot |
| Docker Compose | Environnement local de développement |
| Jenkins Pipeline | Pipeline CI/CD réutilisable |
| SonarQube | Configuration de l'analyse de qualité |
| Nexus | Publication et distribution des artefacts |
| Ansible | Automatisation des déploiements |

## 06. Templates Infrastructure

Configurations d'infrastructure réutilisables.

| Template | Description |
|---|---|
| Nginx | Configuration Reverse Proxy |
| Kubernetes Deployment | Déploiement des applications |
| Kubernetes Service | Exposition des services |
| ConfigMaps & Secrets | Gestion des configurations et secrets |
| Ingress | Gestion des accès entrants |

## 07. Applications de référence (Reference Applications)

Projets complets illustrant l'utilisation de plusieurs modules et concepts architecturaux.

- Spring Boot REST API Starter
- Spring Boot Microservice Starter
- Spring Cloud Microservices
- Spring Boot Modular Monolith
- Spring Boot Clean Architecture
- Spring Boot Hexagonal Architecture
- Spring Boot Event-Driven Application

---

# 📐 Conventions de documentation

## Documentation Knowledge

Chaque fichier Markdown doit suivre une structure cohérente, adaptée au sujet documenté :

1. Définition
2. Objectif et utilité
3. Concepts fondamentaux
4. Fonctionnement
5. Architecture ou diagramme (si pertinent)
6. Avantages et limitations
7. Cas d'utilisation
8. Exemples
9. Concepts associés
10. Références

Les documentations doivent rester concises, structurées et techniquement précises.

## Documentation Templates

Chaque module réutilisable doit proposer :

1. Présentation
2. Objectifs
3. Stack technique
4. Dépendances
5. Architecture
6. Implémentation
7. Configuration
8. Guide d'intégration
9. Exemples d'utilisation
10. Tests
11. Bonnes pratiques

Les implémentations doivent privilégier :

- La réutilisabilité
- La modularité
- Le faible couplage
- La configuration externalisée
- La testabilité
- La maintenabilité
- La compatibilité des versions
- La préparation à un usage en production

---

# 🚀 Évolution du référentiel

Cette base de connaissances est conçue pour évoluer progressivement.

Les principales orientations sont :

1. Consolider les fondamentaux du génie logiciel.
2. Structurer les connaissances Backend et Java / Spring.
3. Compléter la documentation sur l'architecture, les API et les infrastructures.
4. Enrichir les domaines Testing, Security et DevOps.
5. Développer des modules Spring Boot réutilisables.
6. Construire des applications de référence complètes.
7. Améliorer continuellement les documentations, exemples et tests automatisés.

---

# 👨‍💻 Auteur et responsable technique

**Ahmed Kadmiri**

**Rôle :** Author & Technical Lead

Domaines d'intérêt et d'expertise :

- Software Engineering
- Backend Development
- Java & Spring Ecosystem
- Software Architecture
- API Engineering
- DevOps & CI/CD
- Security Engineering
- Technical Leadership

GitHub : [ahmedkadmiri-dev](https://github.com/ahmedkadmiri-dev)

---

## ⭐ Contribution

Ce dépôt est une base de connaissances technique personnelle, évolutive et enrichie progressivement.

Les suggestions, améliorations, corrections et nouvelles implémentations sont les bienvenues.

**Construire, comprendre, partager et réutiliser les connaissances techniques.**