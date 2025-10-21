package com.cosek.edms.futsal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MatchPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration;
    private int orderIndex;
    private boolean breakPeriod;


    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonBackReference
    private FutsalMatch match;
}

