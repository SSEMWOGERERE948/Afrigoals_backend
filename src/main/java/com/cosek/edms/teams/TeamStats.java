package com.cosek.edms.teams;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TeamStats {
    private int position;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
}
