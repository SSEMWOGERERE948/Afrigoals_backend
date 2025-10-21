package com.cosek.edms.managers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ManagerRole {
    HEAD_MANAGER,
    ASSISTANT_MANAGER;

    @JsonCreator
    public static ManagerRole fromString(String value) {
        return ManagerRole.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}


