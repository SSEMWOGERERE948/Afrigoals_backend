package com.cosek.edms.futsal.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class TeamFutsalStats {
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
}

