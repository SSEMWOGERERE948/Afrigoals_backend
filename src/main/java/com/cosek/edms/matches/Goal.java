package com.cosek.edms.matches;

import com.cosek.edms.players.Player;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String team; // "home" or "away"

    private int minute;

    private String type; // "goal", "penalty", "own_goal"

    private Long playerId;

    private String playerName;

    private Long assistPlayerId;

    private String assistPlayerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
}
