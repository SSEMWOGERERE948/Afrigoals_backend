package com.cosek.edms.soccer.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponseWrapper {
    private List<PlayerResponse> response;
}
