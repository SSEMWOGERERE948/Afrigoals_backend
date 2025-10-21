package com.cosek.edms.matches;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PlayerInfo {
    private Long id;
    private String name;
    private int number;
    private String nationality;
    private int age;
    private String image;
    private String position;
}

