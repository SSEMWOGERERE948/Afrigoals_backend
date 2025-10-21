package com.cosek.edms.matches;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MatchStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;

    private int homeShots;
    private int awayShots;

    private int homeCorners;
    private int awayCorners;

    private int homeFouls;
    private int awayFouls;

    // Add more stats as needed
}
