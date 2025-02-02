package com.ecsimsw.account.dto;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.common.domain.UserStatus;
import com.ecsimsw.common.dto.ApiResult;

public record UserInfoAdminResponse(
    Long userId,
    String username,
    String email,
    String tempPassword,
    UserStatus status,
    boolean deleted
) implements ApiResult {
    public static UserInfoAdminResponse of(User user) {
        return new UserInfoAdminResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getTempPassword(),
            user.getStatus(),
            user.isDeleted()
        );
    }
}
