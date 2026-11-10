# Reverse Proxy

## Définition

Un **Reverse Proxy** (proxy inverse) est un serveur intermédiaire placé devant un ou plusieurs serveurs backend. Il reçoit les requêtes des clients et les transmet aux serveurs appropriés.

Contrairement à un proxy classique (*Forward Proxy*), qui agit au nom du client pour accéder à des serveurs externes, le Reverse Proxy agit au nom des serveurs backend pour recevoir les requêtes des clients.

## Fonctionnement

```text
              Client
                 |
                 v
          Reverse Proxy
                 |
          +------+------+
          |      |      |
          v      v      v
        API 1   API 2   API 3
```

1. Le client envoie une requête au Reverse Proxy.
2. Le Reverse Proxy reçoit la requête.
3. Il sélectionne le serveur backend approprié.
4. Il transmet la requête au serveur sélectionné.
5. Il retourne la réponse au client.

Le client communique uniquement avec le Reverse Proxy et n'a pas besoin de connaître l'adresse des serveurs backend.

## Fonctionnalités principales

- **Routage** : rediriger les requêtes vers les serveurs backend appropriés.
- **Load Balancing** : répartir les requêtes entre plusieurs instances d'un même service.
- **TLS Termination** : gérer le chiffrement HTTPS et transmettre les requêtes au backend.
- **Caching** : mettre en cache certaines réponses pour réduire la charge des serveurs.
- **Compression** : compresser les réponses HTTP pour réduire le volume des données échangées.
- **Health Checks** : vérifier la disponibilité des serveurs backend.
- **Sécurité** : masquer l'architecture interne et limiter l'exposition directe des serveurs backend.

## Exemple avec Nginx

Configuration d'un Reverse Proxy qui redirige les requêtes vers une application Spring Boot exécutée sur le port `8080`.

```nginx
server {
    listen 80;
    server_name api.example.com;

    location /api/ {
        proxy_pass http://localhost:8080/;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Avec cette configuration, une requête :

```http
GET http://api.example.com/api/accounts
```

est transmise à l'application backend.

## Reverse Proxy vs Forward Proxy

| Forward Proxy | Reverse Proxy |
|---|---|
| Placé devant les clients | Placé devant les serveurs |
| Agit au nom du client | Agit au nom des serveurs |
| Contrôle l'accès aux ressources externes | Contrôle l'accès aux serveurs backend |
| Exemple : Squid | Exemple : Nginx |

## Reverse Proxy vs API Gateway

Un API Gateway est une forme spécialisée de Reverse Proxy destinée aux API.

Il ajoute généralement des fonctionnalités spécifiques :

- Authentification et autorisation.
- Rate Limiting et quotas.
- Transformation des requêtes et réponses.
- Agrégation de plusieurs API.
- Gestion des politiques d'accès aux API.

Un Reverse Proxy classique se concentre davantage sur le transfert des requêtes, la disponibilité et les performances.

## Technologies

- **Nginx** : serveur web et Reverse Proxy.
- **HAProxy** : Reverse Proxy et Load Balancer.
- **Apache HTTP Server** : Reverse Proxy avec `mod_proxy`.
- **Envoy Proxy** : proxy conçu pour les architectures distribuées et microservices.
- **Traefik** : Reverse Proxy dynamique adapté aux environnements conteneurisés.

## Concepts associés

- Forward Proxy
- Load Balancing
- API Gateway
- Service Discovery
- TLS Termination
- High Availability