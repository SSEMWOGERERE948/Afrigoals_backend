package com.cosek.edms.futsal.entity;

import jakarta.persistence.*;
import lombok.Data;


/** Links one player to one team-lineup and stores his exact slot on the pitch. */
@Entity
@Data
public class LineupPlayer {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne @JoinColumn(nullable = false)
    private TeamLineup lineup;

    @ManyToOne @JoinColumn(nullable = false)
    private FutsalPlayer player;

    private boolean starting;   // true = starter, false = substitute

    /** 0-based row in the formation grid (0 = back line / goalkeeper row). */
    private int rowIndex;

    /** 0-based column inside that row (left-to-right). */
    private int colIndex;
}
