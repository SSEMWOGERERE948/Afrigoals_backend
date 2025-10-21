package com.cosek.edms.matches;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PlayerEmbedded {

    private Long id;
    private String name;
    private int number;
    private String nationality;
    private int age;
    private String image;

    @JsonIgnore
    private String clubTeam;
}
