package com.cosek.edms.soccer.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {
    private Player player;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Player {
        private Long id;
        private String name;
        private String firstname;
        private String lastname;
        private String nationality;
        private String position;
        private Integer age;
    }
}
