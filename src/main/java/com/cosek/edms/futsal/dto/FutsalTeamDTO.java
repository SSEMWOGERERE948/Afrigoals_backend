package com.cosek.edms.futsal.dto;

import com.cosek.edms.futsal.entity.TeamFutsalStats;
import lombok.Data;

import java.util.List;

@Data
public class FutsalTeamDTO {
    private Long id;
    private String teamName;
    private String institution;
    private String category;
    private int preferredTeamSize;
    private String homeVenue;
    private int founded;
    private String manager;
    private String logo;
    private String description;
    private String contactEmail;
    private String contactPhone;
    private TeamFutsalStats stats;
    private List<FutsalPlayerDTO> players;
}


