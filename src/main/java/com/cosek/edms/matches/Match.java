package com.cosek.edms.matches;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String league;
    private String homeTeam;
    private String awayTeam;
    private LocalDate date;
    private LocalTime time;
    private String stadium;
    private String referee;

    private Integer homeScore;
    private Integer awayScore;
    private String status;       // Scheduled, Live, Finished
    private Integer extraTime;
    private Integer currentMinute;

    @Transient
    private Integer elapsedSeconds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Date startTime;

    // ✅ NEW: Track half-time periods for accurate timing
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Date halfTimeStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Date secondHalfStart;

    private String period;       // "First Half", "Half Time", "Second Half", "Full Time"
    private Boolean running;

    // ✅ NEW: Track pause/resume times for accurate calculations
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Date lastPauseTime;

    private Long totalPausedMillis; // Total time spent paused

    @Embedded
    private AddedTime addedTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "formation", column = @Column(name = "home_formation")),
            @AttributeOverride(name = "coach", column = @Column(name = "home_coach"))
    })
    private TeamLineup homeLineup;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "formation", column = @Column(name = "away_formation")),
            @AttributeOverride(name = "coach", column = @Column(name = "away_coach"))
    })
    private TeamLineup awayLineup;
}