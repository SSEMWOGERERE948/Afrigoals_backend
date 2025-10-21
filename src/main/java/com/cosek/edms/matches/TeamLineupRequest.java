package com.cosek.edms.matches;

import java.util.List;
import lombok.Data;

@Data
public class TeamLineupRequest {
    private String formation;
    private List<Long> startingXIIds;
    private List<Long> substituteIds;
    private String coach;
}
