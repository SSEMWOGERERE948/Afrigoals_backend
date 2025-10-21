package com.cosek.edms.futsal.dto;

import com.cosek.edms.futsal.entity.MatchPeriod;
import com.cosek.edms.futsal.entity.PeriodState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class FutsalMatchDTO {
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
    private Date startTime;
    private List<GoalDTO> goals;
    private Integer firstHalfDuration;
    private Integer secondHalfDuration;
    private PeriodState periodState;
    private List<MatchPeriod> periods;
}

