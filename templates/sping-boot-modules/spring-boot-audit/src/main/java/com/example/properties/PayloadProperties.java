package com.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "audit.payload")
public class PayloadProperties {

    /**
     * Taille maximale autorisée pour chaque payload JSON.
     * Valeur par défaut : 64 Ko.
     */
    private int maxSize = 65536;
}
