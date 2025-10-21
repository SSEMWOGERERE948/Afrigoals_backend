package com.cosek.edms.futsal.dto;

public record MatchLineupRequest(
        TeamLineupRequest homeLineup,
        TeamLineupRequest awayLineup
) {}
