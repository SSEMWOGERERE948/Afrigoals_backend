package com.cosek.edms.futsal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class FutsalTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private String institution;
    private String category; // school, club, community
    private int preferredTeamSize;
    private String homeVenue;
    private int founded;
    private String manager;
    private String logo;
    private String description;
    private String contactEmail;
    private String contactPhone;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FutsalPlayer> players = new ArrayList<>();


    @Embedded
    private TeamFutsalStats stats = new TeamFutsalStats();

}

