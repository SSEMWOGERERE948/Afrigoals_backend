package com.cosek.edms.matches;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class LineupMetadata {
    private String formation;
    private String coach;
}

