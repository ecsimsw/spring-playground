package com.ecsimsw.account.dto;

import com.ecsimsw.common.domain.RoleType;

import java.util.Set;

public record UpdateUserRoleRequest(
    Set<RoleType> type
) {
}
