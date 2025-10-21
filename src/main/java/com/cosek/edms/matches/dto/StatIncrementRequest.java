package com.cosek.edms.matches.dto;

import lombok.Data;

@Data
public class StatIncrementRequest {
    private String statType; // e.g. "homeShots"
    private String team;     // "home" or "away" (optional)
    private int value = 1;   // default increment
}
