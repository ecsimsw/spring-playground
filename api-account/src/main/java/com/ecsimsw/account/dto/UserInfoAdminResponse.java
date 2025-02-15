package com.ecsimsw.account.dto;

import com.ecsimsw.common.dto.ApiResult;
import com.ecsimsw.domain.User;

public record UserInfoAdminResponse(
    Long userId,
    String username,
    String email,
    String tempPassword,
    boolean deleted
) implements ApiResult {
    public static UserInfoAdminResponse of(User user) {
        return new UserInfoAdminResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getTempPassword(),
            user.isDeleted()
        );
    }
}
