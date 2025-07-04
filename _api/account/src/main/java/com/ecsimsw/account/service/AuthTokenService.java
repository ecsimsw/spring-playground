package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.*;
import com.ecsimsw.account.dto.AuthTokenResponse;
import com.ecsimsw.account.error.AccountException;
import com.ecsimsw.common.domain.*;
import com.ecsimsw.common.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthTokenService {

    @Value("${auth.token.secret}")
    private String tokenSecret;

    private final UserPasswordRepository userPasswordRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthTokenResponse betaIssue(String username, String uid) {
        var at = new AccessToken(username, uid).asJwtToken(tokenSecret);
        var rt = new RefreshToken(username, uid).asJwtToken(tokenSecret);
        refreshTokenRepository.save(username, rt);
        return new AuthTokenResponse(at, rt);
    }

    @Transactional
    public AuthTokenResponse issue(String username) {
        userRepository.findByUsername(username).orElseThrow(() -> new AccountException(ErrorType.FAILED_TO_AUTHENTICATE));
        var at = new AccessToken(username, "").asJwtToken(tokenSecret);
        var rt = new RefreshToken(username, "").asJwtToken(tokenSecret);
        refreshTokenRepository.save(username, rt);
        return new AuthTokenResponse(at, rt);
    }

    @Transactional
    public AuthTokenResponse reissue(String rt) {
        var username = RefreshToken.fromToken(tokenSecret, rt).username();
        var uid = RefreshToken.fromToken(tokenSecret, rt).uid();
        refreshTokenRepository.findByUsername(username).orElseThrow(() -> new AccountException(ErrorType.INVALID_TOKEN));
        return betaIssue(username, uid);
    }
}
