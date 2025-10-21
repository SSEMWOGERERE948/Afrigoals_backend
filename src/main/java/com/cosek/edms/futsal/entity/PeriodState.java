package com.cosek.edms.futsal.entity;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PeriodState {
    private Long currentPeriodId;
    private int currentPeriodOrder;
    private long elapsedTimeInCurrentPeriodMs;
    private long totalPlayingTimeMs;
    private boolean breakPeriod;
    private boolean matchPaused;
    private long lastUpdatedTimestamp;
    private Date startTime;
    private String status;
    private int elapsedSeconds;
    private int elapsedMinutes;

}

