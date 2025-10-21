package com.cosek.edms.futsal.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PlayerFutsalStats {
    private int matchesPlayed;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private int cleanSheets;
}
