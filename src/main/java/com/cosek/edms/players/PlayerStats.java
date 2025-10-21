package com.cosek.edms.players;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PlayerStats {
    private int matches;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
}
