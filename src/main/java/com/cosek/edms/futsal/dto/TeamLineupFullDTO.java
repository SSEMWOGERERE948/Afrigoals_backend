package com.cosek.edms.futsal.dto;

import java.util.List;

public record TeamLineupFullDTO(
        String formation,
        String coach,
        List<PlayerOnFieldDTO> startingXI,
        List<PlayerOnFieldDTO> substitutes
) {}
