package com.cosek.edms.matches;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddedTime {
    private int firstHalf;
    private int secondHalf;
    private int extraTimeFirst;
    private int extraTimeSecond;
}

