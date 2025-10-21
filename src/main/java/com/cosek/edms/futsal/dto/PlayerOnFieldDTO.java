package com.cosek.edms.futsal.dto;


public record PlayerOnFieldDTO(
        Long playerId,
        String name,
        int number,
        String position,
        String teamName,
        boolean isStarting,
        int rowIndex,
        int colIndex
) {
}
