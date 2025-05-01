package com.ecsimsw.account.support;

import com.ecsimsw.common.domain.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class UserRoleConverter implements AttributeConverter<Set<RoleType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<RoleType> attribute) {
        try {
            return attribute.stream()
                .map(RoleType::name)
                .collect(Collectors.joining(DELIMITER));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert service role from db", e);
        }
    }

    @Override
    public Set<RoleType> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.stream(dbData.split(DELIMITER))
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert service role from entity", e);
        }
    }
}