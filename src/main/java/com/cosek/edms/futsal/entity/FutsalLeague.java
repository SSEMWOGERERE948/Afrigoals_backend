package com.cosek.edms.futsal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FutsalLeague {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String season;
    private int teamSize;
    private int maxTeams;
    private int matchDuration;
    private int maxSubstitutions;
    private String description;
    private String status; // upcoming, active, completed
    private String logo;
    private int currentMatchday;
    private int teams;
}

