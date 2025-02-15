package com.ecsimsw.domain;

import com.ecsimsw.common.domain.RoleType;
import com.ecsimsw.domain.support.UserRoleConverter;
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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Convert(converter = UserRoleConverter.class)
    private Set<RoleType> roles = new HashSet<>();

    public UserRole(Long userId, Set<RoleType> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    public List<String> roleNames() {
        return roles.stream()
            .map(Enum::name)
            .toList();
    }
}
