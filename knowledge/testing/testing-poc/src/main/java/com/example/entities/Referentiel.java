package com.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_referentiel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Referentiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    private String description;
}