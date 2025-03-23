package com.ecsimsw.auth.service;

import com.ecsimsw.auth.domain.*;
import com.ecsimsw.auth.domain.AccessToken;
import com.ecsimsw.auth.dto.LogInResponse;
import com.ecsimsw.auth.dto.Tokens;
import com.ecsimsw.common.config.TokenConfig;
import com.ecsimsw.common.domain.BlockedTokenRepository;
import com.ecsimsw.common.domain.BlockedUserRepository;
import com.ecsimsw.common.domain.RefreshTokenRepository;
import com.ecsimsw.common.service.dto.AuthCreationRequest;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.service.dto.AuthUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserPasswordRepository userPasswordRepository;
    private final UserRoleRepository userRoleRepository;
    private final BlockedTokenRepository blockedTokenRepository;
    private final BlockedUserRepository blockedUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public LogInResponse issue(String username) {
        var userPassword = userPasswordRepository.findByUsername(username).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        var userRole = userRoleRepository.findByUserId(userPassword.userId()).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        var tokens = createTokens(username, userRole.getIsAdmin());
        refreshTokenRepository.save(username, tokens.refreshToken());
        return new LogInResponse(tokens);
    }

    public LogInResponse reissue(String refreshToken) {
        var username = RefreshToken.fromToken(TokenConfig.secretKey, refreshToken).username();
        var tokenOpt = refreshTokenRepository.findByUsername(username);
        if (tokenOpt.isEmpty()) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        return issue(username);
    }

    public Tokens createTokens(String username, boolean isAdmin) {
        return new Tokens(
            new AccessToken(username, isAdmin).asJwtToken(TokenConfig.secretKey),
            new RefreshToken(username).asJwtToken(TokenConfig.secretKey)
        );
    }

    public void blockToken(String token) {
        blockedTokenRepository.save(token);
    }

    public void blockUser(String username) {
        blockedUserRepository.save(username);
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

    public void createUserAuth(AuthCreationRequest request) {
        var userPassword = new UserPassword(passwordEncoder, request.userId(), request.purePassword(), request.purePassword());
        userPasswordRepository.save(userPassword);
    }

    public void updateUserAuth(AuthUpdateRequest request) {
        var userPassword = userPasswordRepository.findByUsername(request.username()).orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        userPassword.updatePassword(passwordEncoder, request.newPassword());
    }
}
