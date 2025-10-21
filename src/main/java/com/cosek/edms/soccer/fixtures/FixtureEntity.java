package com.cosek.edms.soccer.fixtures;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixtureEntity {
    @Id
    private Long fixtureId;

    private String date;
    private String status;

    private Integer leagueId;
    private Integer season;

    private Integer homeTeamId;
    private String homeTeamName;

    private Integer awayTeamId;
    private String awayTeamName;

    private Integer homeGoals;
    private Integer awayGoals;

    private String venueName;
    private String referee;
}
