package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.common.dto.ApiResult;

public record UserInfoAdminResponse(
    Long userId,
    String username,
    String email,
    boolean deleted
) implements ApiResult {

    public static UserInfoAdminResponse of(User user) {
        return new UserInfoAdminResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isDeleted()
        );
    }
}
