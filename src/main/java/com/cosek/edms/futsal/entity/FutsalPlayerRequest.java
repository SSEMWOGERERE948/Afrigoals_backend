package com.cosek.edms.futsal.entity;

import lombok.Data;

@Data
public class FutsalPlayerRequest {
    private FutsalPlayer player;
    private Long teamId;
    private String positionId;
}
