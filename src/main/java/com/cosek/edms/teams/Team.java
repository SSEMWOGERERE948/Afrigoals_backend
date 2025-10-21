package com.cosek.edms.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class  Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String logo;
    private String league;
    private int founded;
    private String stadium;
    private String manager;

    private String teamType; // ✅ New field: "club" or "national"

    @Embedded
    private TeamStats stats;

    @Transient // Placeholder - still not persisted
    private List<String> players;
}
