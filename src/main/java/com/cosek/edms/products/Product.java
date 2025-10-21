package com.cosek.edms.products;

import com.cosek.edms.soccer.teams.TeamEntity;
import com.cosek.edms.teams.Team;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private double price;
    private double originalPrice;
    private String sku;
    private int stock;
    private boolean featured;
    private boolean active;
    private String categoryId;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private List<String> tags;

    private double rating;
    private int reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Transient
    private String teamId;

}
