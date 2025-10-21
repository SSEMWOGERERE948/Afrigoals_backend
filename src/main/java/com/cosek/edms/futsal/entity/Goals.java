package com.cosek.edms.futsal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String playerId;
    private String playerName;
    private String assistPlayerId;
    private String assistPlayerName;
    private String team;
    private int minute;

    @ManyToOne
    @JsonIgnore
    private FutsalMatch match;

    private String type = "goal";
}

