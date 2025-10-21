package com.cosek.edms.futsal.dto;

import java.util.List;

public record TeamLineupRequest(
        String formation,
        String coach,
        List<Long> startingXIIds,
        List<Long> substituteIds
) {}