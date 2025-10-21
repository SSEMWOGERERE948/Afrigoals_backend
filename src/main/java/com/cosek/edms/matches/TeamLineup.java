package com.cosek.edms.matches;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Embeddable
@Data
public class TeamLineup {

    private String formation;
    private String coach;

    @ElementCollection
    @CollectionTable(name = "lineup_starting_xi_ids", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "player_id")
    private List<Long> startingXIIds;

    @ElementCollection
    @CollectionTable(name = "lineup_substitute_ids", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "player_id")
    private List<Long> substituteIds;
}

