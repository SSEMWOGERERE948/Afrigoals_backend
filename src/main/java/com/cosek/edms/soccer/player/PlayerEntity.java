package com.cosek.edms.soccer.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    @Id
    private Long id;

    private String name;
    private String firstname;
    private String lastname;
    private String nationality;
    private String position;
    private int age;

    private int teamId;
    private int season;
}

