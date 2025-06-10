package com.ecsimsw.springexternalplatform2.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Protocol {

    DATA(4),
    STATUS(20);

    public final int id;

    Protocol(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    @JsonCreator
    public static Protocol fromId(int id) {
        return Arrays.stream(values())
            .filter(v -> v.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid protocol id: " + id));
    }
}
