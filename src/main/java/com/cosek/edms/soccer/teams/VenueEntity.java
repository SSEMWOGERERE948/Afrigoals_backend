package com.cosek.edms.soccer.teams;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private String surface;
    private String image;
}
