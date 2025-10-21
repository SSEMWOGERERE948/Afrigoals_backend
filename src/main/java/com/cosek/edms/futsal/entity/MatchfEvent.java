package com.cosek.edms.futsal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MatchfEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private int minute;
    private String team;
    private String playerId;
    private String playerName;
    private String description;

    private String playerInId;
    private String playerInName;
    private String playerOutId;
    private String playerOutName;
    private Integer timeoutDuration;
    private String timeoutReason;

    @ManyToOne
    private FutsalMatch match;
}
