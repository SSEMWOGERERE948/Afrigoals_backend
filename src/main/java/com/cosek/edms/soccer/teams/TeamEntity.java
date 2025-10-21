package com.cosek.edms.soccer.teams;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    private int id;

    private String name;
    private String code;
    private String country;
    private Integer founded;
    private String logo;

    private int leagueId;
    private int season;

    @Column(name = "is_national")
    private boolean isNational;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;
}
