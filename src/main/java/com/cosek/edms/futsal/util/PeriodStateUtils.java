package com.cosek.edms.futsal.util;

import com.cosek.edms.futsal.entity.PeriodState;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PeriodStateUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(PeriodState state) {
        try {
            return mapper.writeValueAsString(state);
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize PeriodState", e);
        }
    }

    public static PeriodState fromJson(String json) {
        try {
            return mapper.readValue(json, PeriodState.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize PeriodState", e);
        }
    }
}
