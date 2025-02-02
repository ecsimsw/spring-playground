package com.ecsimsw.auth.service;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.account.domain.UserRepository;
import com.ecsimsw.account.domain.UserRoleRepository;
import com.ecsimsw.auth.domain.*;
import com.ecsimsw.auth.dto.LogInResponse;
import com.ecsimsw.auth.dto.Tokens;
import com.ecsimsw.auth.exception.AuthException;
import com.ecsimsw.auth.utils.JwtUtils;
import com.ecsimsw.common.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BlockedTokenRepository blockedTokenRepository;
    private final BlockedUserRepository blockedUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public LogInResponse issue(String username) {
        var user = getUserByUsername(username);
        var tokens = createTokens(user);
        refreshTokenRepository.save(username, tokens.refreshToken());
        return new LogInResponse(tokens, user.isTempPassword());
    }

    public LogInResponse reissue(String refreshToken) {
        var username = JwtUtils.getClaimValue(secretKey, refreshToken, "username");
        var tokenOpt = refreshTokenRepository.findByUsername(username);
        if (tokenOpt.isEmpty()) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        return issue(username);
    }

    public Tokens createTokens(User user) {
        return new Tokens(
            AccessToken.of(user).asJwtToken(secretKey),
            RefreshToken.of(user).asJwtToken(secretKey)
        );
    }

    @Transactional
    public void blockToken(String token) {
        blockedTokenRepository.save(token);
    }

    @Transactional
    public void blockUser(Long userId) {
        var user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        blockedUserRepository.save(user.getUsername());
    }

    @Transactional(readOnly = true)
    public List<String> roleNames(String username) {
        var user = getUserByUsername(username);
        if (user.isAdmin()) {
            return List.of("ADMIN");
        }
        return userRoleRepository.findAllByUserId(user.getId()).stream()
            .flatMap(role -> role.roleNames().stream())
            .toList();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
    }
}
