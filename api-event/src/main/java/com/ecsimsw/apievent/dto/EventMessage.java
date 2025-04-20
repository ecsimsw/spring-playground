package com.ecsimsw.apievent.dto;

public record EventMessage(
    String protocol,
    Long t,
    String data
) {
}
