package com.cosek.edms.futsal.dto;

import lombok.Data;

@Data
public class MatchPeriodDTO {
    private Long id;
    private String name;
    private int duration;
    private int orderIndex;
    private boolean breakPeriod;

}

