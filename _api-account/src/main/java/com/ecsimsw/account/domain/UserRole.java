package com.ecsimsw.account.domain;

import com.ecsimsw.common.domain.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRole {

    @Id
    private Long userId;

    private Boolean isAdmin;

    @Convert(converter = UserRoleConverter.class)
    private Set<RoleType> roles = new HashSet<>();

    public List<String> roleNames() {
        return roles.stream()
            .map(Enum::name)
            .toList();
    }
}
