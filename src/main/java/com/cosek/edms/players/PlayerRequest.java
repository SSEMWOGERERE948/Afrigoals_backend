package com.cosek.edms.players;

import lombok.Data;

@Data
public class PlayerRequest {
    private Player player;
    private Long clubTeamId;
    private Long nationalTeamId;
    private Long positionId;
}
