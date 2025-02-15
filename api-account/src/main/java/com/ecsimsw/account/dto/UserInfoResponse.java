package com.ecsimsw.account.dto;

import com.ecsimsw.common.dto.ApiResult;
import com.ecsimsw.domain.User;

public record UserInfoResponse(
    String username,
    String email
) implements ApiResult {
    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
            user.getUsername(),
            user.getEmail()
        );
    }
}
