package com.cosek.edms.futsal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TeamLineup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formation;
    private String coach;

    @Enumerated(EnumType.STRING)
    private Side side; // HOME or AWAY

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private FutsalMatch match;


    @OneToMany(mappedBy = "lineup", cascade = CascadeType.ALL)
    private List<LineupPlayer> players;

    public enum Side {
        HOME,
        AWAY
    }
}
