package com.cosek.edms.futsal.dto;

public record MatchLineupResponse(
        Long matchId,
        TeamLineupFullDTO homeLineup,
        TeamLineupFullDTO awayLineup
) {}
