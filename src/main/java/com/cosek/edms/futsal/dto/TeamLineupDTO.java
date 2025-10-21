package com.cosek.edms.futsal.dto;

import com.cosek.edms.futsal.entity.TeamLineup;

import java.util.List;

/**
 * DTO used only on WRITE operations (PUT) — just IDs, no player objects.
 */
public record TeamLineupDTO(
        TeamLineup.Side side,          // HOME or AWAY
        String formation,
        String coach,
        List<Long> startingXIIds,
        List<Long> substituteIds
) {
    /** Convenience helper so the controller can inject the side from the path */
    public TeamLineupDTO withSide(TeamLineup.Side newSide) {
        return new TeamLineupDTO(
                newSide,
                this.formation,
                this.coach,
                this.startingXIIds,
                this.substituteIds
        );
    }
}

