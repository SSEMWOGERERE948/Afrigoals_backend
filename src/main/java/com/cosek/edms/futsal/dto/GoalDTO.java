package com.cosek.edms.futsal.dto;

import lombok.Data;

@Data
public class GoalDTO {
    private Long id;
    private String playerId;
    private String playerName;
    private String assistPlayerId;
    private String assistPlayerName;
    private String team;
    private int minute;
    private String type;
}


