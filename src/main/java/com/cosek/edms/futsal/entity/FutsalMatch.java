package com.cosek.edms.futsal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class FutsalMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String homeTeam;
    private String awayTeam;
    private String venue;
    private String referee;
    private String league;
    private String date;
    private String time;
    private int teamSize;
    private int matchDuration;
    private int maxSubstitutions;
    private String status;

    private Integer homeScore;
    private Integer awayScore;
    private Integer firstHalfDuration;
    private Integer secondHalfDuration;

    private Date startTime;

    @Column(name = "current_minute")
    private Integer currentMinute;

    // 🔥 FIX: Initialize lineups to avoid NullPointerException
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeamLineup> lineups = new ArrayList<>();

    // 🔥 FIX: Initialize events to avoid potential issues
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MatchfEvent> events = new ArrayList<>();

    // 🔥 FIX: Initialize goals to avoid potential issues
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Goals> goals = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String halvesJson;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MatchPeriod> periods = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String periodStateJson;

    @Transient
    private PeriodState periodState;

}