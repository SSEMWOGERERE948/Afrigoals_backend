package com.cosek.edms.matches;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;       // "yellow_card", "red_card", "substitution", etc.
    private String team;       // "home" or "away"
    private Long playerId;
    private String playerName;
    private int minute;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @JsonIgnore
    private Match match;

}
