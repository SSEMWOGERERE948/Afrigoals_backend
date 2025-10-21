package com.cosek.edms.players;

import com.cosek.edms.position.Position;
import com.cosek.edms.teams.Team;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int number;
    private String nationality;
    private int age;
    private String image;

    @ManyToOne
    @JoinColumn(name = "club_team_id")
    private Team clubTeam;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;


    @ManyToOne
    @JoinColumn(name = "national_team_id")
    private Team nationalTeam;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matches", column = @Column(name = "club_matches")),
            @AttributeOverride(name = "goals", column = @Column(name = "club_goals")),
            @AttributeOverride(name = "assists", column = @Column(name = "club_assists")),
            @AttributeOverride(name = "yellowCards", column = @Column(name = "club_yellow_cards")),
            @AttributeOverride(name = "redCards", column = @Column(name = "club_red_cards"))
    })
    private PlayerStats clubStats;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matches", column = @Column(name = "national_matches")),
            @AttributeOverride(name = "goals", column = @Column(name = "national_goals")),
            @AttributeOverride(name = "assists", column = @Column(name = "national_assists")),
            @AttributeOverride(name = "yellowCards", column = @Column(name = "national_yellow_cards")),
            @AttributeOverride(name = "redCards", column = @Column(name = "national_red_cards"))
    })
    private PlayerStats nationalStats;

}
