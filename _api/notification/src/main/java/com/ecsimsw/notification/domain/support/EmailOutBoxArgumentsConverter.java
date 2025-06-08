package com.ecsimsw.notification.domain.support;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EmailOutBoxArgumentsConverter implements AttributeConverter<String[], String> {

    private static final String DELIMITER = "&&&";

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        try {
            return String.join(DELIMITER, attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert service role from db", e);
        }
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        try {
            return dbData.split(DELIMITER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert service role from entity", e);
        }
    }
}