package com.cosek.edms.futsal.dto;

import lombok.Data;

@Data
public class FutsalPlayerDTO {
    private Long id;
    private String name;
    private int number;
    private String nationality;
    private int age;
    private String position;
}
