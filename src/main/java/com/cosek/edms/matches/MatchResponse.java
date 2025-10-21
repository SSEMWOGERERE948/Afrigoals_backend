package com.cosek.edms.matches;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class MatchResponse {
    private Long id;
    private String league;
    private String homeTeam;
    private String awayTeam;
    private String status;
    private String stadium;
    private String referee;
    private LocalDate date;
    private LocalTime time;
    private Integer homeScore;
    private Integer awayScore;

    // ✅ Add these:
    private String period;
    private Integer currentMinute;
    private Boolean running;
    private Date startTime;

    private TeamLineupResponse homeLineup;
    private TeamLineupResponse awayLineup;
}
