// FutsalPlayerResponse.java
package com.cosek.edms.futsal.dto;

import com.cosek.edms.futsal.entity.PlayerFutsalStats;
import lombok.Data;

@Data
public class FutsalPlayerResponse {
    private Long id;
    private String name;
    private int number;
    private String positionId;
    private String nationality;
    private int age;
    private int height;
    private int weight;
    private String preferredFoot;
    private String studentId;
    private String course;
    private String yearOfStudy;
    private String image;
    private PlayerFutsalStats stats;
    private String teamName;
}
