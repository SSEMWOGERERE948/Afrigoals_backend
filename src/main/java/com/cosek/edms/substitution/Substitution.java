package com.cosek.edms.substitution;

import com.cosek.edms.players.Player;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Substitution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;

    private String team; // "home" or "away"

    @ManyToOne
    @JoinColumn(name = "player_out_id")
    private Player playerOut;

    @ManyToOne
    @JoinColumn(name = "player_in_id")
    private Player playerIn;

    private int minute;

    private String reason;

    private LocalDateTime timestamp;
}
