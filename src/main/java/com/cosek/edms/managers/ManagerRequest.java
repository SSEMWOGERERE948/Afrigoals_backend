package com.cosek.edms.managers;

import lombok.Data;

@Data
public class ManagerRequest {
    private Manager manager;
    private Long clubTeamId;
    private Long nationalTeamId;
}
