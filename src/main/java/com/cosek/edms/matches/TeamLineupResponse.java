package com.cosek.edms.matches;

import com.cosek.edms.players.Player;
import lombok.Data;

import java.util.List;

@Data
public class TeamLineupResponse {
    private String formation;
    private String coach;
    private List<Player> startingXI;
    private List<Player> substitutes;
}
