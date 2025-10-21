package com.cosek.edms.substitution;

import lombok.Data;
import java.util.List;

@Data
public class SubstitutionRequest {
    private List<Substitution> substitutions;
}
