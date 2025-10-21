package com.cosek.edms.futsal.entity;

import com.cosek.edms.players.PlayerStats;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FutsalPlayer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int number;
    private String positionId;
    private String nationality;
    private int age;
    private int height;
    private int weight;
    private String preferredFoot;
    private String studentId;
    private String course;
    private String yearOfStudy;
    private String image;

    @ManyToOne
    @JsonBackReference
    private FutsalTeam team;


    @Embedded
    private PlayerFutsalStats stats;
}



