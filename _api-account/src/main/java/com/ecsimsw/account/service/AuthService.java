package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.AccessToken;
import com.ecsimsw.account.domain.RefreshToken;
import com.ecsimsw.account.domain.UserPasswordRepository;
import com.ecsimsw.account.domain.UserRoleRepository;
import com.ecsimsw.account.dto.LogInResponse;
import com.ecsimsw.account.dto.Tokens;
import com.ecsimsw.common.domain.BlockedUser;
import com.ecsimsw.common.domain.BlockedUserRepository;
import com.ecsimsw.common.domain.RefreshTokenRepository;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${auth.token.secret}")
    private String secret;

    private final UserPasswordRepository userPasswordRepository;
    private final UserRoleRepository userRoleRepository;
    private final BlockedUserRepository blockedUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public LogInResponse issue(String username) {
        var userPassword = userPasswordRepository.findByUsername(username).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        var userRole = userRoleRepository.findByUserId(userPassword.userId()).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        var tokens = createTokens(username, userRole.getIsAdmin());
        refreshTokenRepository.save(username, tokens.refreshToken());
        return new LogInResponse(tokens);
    }

    public LogInResponse reissue(String refreshToken) {
        var username = RefreshToken.fromToken(secret, refreshToken).username();
        var tokenOpt = refreshTokenRepository.findByUsername(username);
        if (tokenOpt.isEmpty()) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        return issue(username);
    }

    public Tokens createTokens(String username, boolean isAdmin) {
        return new Tokens(
            new AccessToken(username, isAdmin).asJwtToken(secret),
            new RefreshToken(username).asJwtToken(secret)
        );
    }

    public void blockToken(String token) {
        blockedUserRepository.save(new BlockedUser(token));
    }

    @Transactional(readOnly = true)
    public List<String> roleNames(String username) {
        var userPassword = userPasswordRepository.findByUsername(username).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        var userRole = userRoleRepository.findByUserId(userPassword.userId()).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        if (userRole.getIsAdmin()) {
            return List.of("ADMIN");
        }
        return userRole.roleNames();
    }
}
