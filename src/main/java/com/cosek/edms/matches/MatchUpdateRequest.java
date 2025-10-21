package com.cosek.edms.matches;

import lombok.Data;

@Data
public class MatchUpdateRequest {
    private String status;
    private TeamLineup homeLineup;
    private TeamLineup awayLineup;
}


