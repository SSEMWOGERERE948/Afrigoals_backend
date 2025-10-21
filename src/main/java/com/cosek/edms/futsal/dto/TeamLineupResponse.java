package com.cosek.edms.futsal.dto;

import java.util.List;

public record TeamLineupResponse(
        String formation,
        String coach,
        List<FutsalPlayerResponse> startingXI,
        List<FutsalPlayerResponse> substitutes
) {}
