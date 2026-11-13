# Spring Boot Parent

Parent Maven centralisé pour les projets Spring Boot basés sur **Java 21**.

## Technologies
- Java 21
- Spring Boot 4.1.2
- Maven

## Utilisation

Ajouter le parent dans le `pom.xml` du projet :

```xml
<parent>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-parent</artifactId>
    <version>1.0.0</version>
    <relativePath/>
</parent>
```

## Objectif

- Centraliser les versions des dépendances.
- Standardiser la configuration Maven.
- Harmoniser les versions Java et Spring Boot.
- Simplifier la maintenance des microservices et modules communs.
