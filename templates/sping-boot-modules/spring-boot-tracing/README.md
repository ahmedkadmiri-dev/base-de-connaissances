# Spring Boot Tracing

Module de traçage distribué basé sur OpenTelemetry et Micrometer Tracing pour les applications Spring Boot.

## Fonctionnalités

- Configuration du traçage distribué.
- Intégration OpenTelemetry.
- Export des traces via OTLP.
- Auto-configuration Spring Boot.
- Injection automatique du `Tracer` dans les composants applicatifs.

## Installation

Ajouter la dépendance dans le `pom.xml` :

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-tracing</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Auto-configuration

Le module déclare son auto-configuration dans :

```text
src/main/resources/META-INF/spring/
└── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Contenu :

```text
com.example.tracing.config.TracingAutoConfiguration
```

Spring Boot détecte et charge automatiquement cette configuration lorsque le module est présent dans le classpath.

## Utilisation

Le bean `Tracer` peut être injecté dans les composants Spring :

```java
@Service
@RequiredArgsConstructor
public class MyService {

    private final Tracer tracer;

}
```

## Configuration

La configuration des traces et de leur export dépend des propriétés OpenTelemetry et OTLP définies par l'application consommatrice.