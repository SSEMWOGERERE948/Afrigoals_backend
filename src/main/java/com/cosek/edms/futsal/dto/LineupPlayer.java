package com.cosek.edms.futsal.dto;

import com.cosek.edms.futsal.entity.FutsalPlayer;
import com.cosek.edms.futsal.entity.TeamLineup;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class LineupPlayer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TeamLineup lineup;

    @ManyToOne @JoinColumn(nullable = false)
    private FutsalPlayer player;

    private boolean starting;      // true = Starting XI
    private int    rowIndex;       // 0 = rearmost row (GK), 1 = next line …
    private int    colIndex;       // 0 .. n-1 inside that row (left → right)
}

