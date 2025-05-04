package com.ecsimsw.gateway.service;

import com.ecsimsw.common.config.TokenConfig;
import com.ecsimsw.common.domain.BlockedUser;
import com.ecsimsw.common.domain.BlockedUserRepository;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.domain.AccessToken;
import com.ecsimsw.gateway.dto.RequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Value("${auth.token.secret}")
    private String secret;

    private final BlockedUserRepository blockedUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = getToken(request).orElseThrow(() -> new AuthException(ErrorType.TOKEN_NOT_FOUND));
            var accessToken = AccessToken.fromToken(secret, token);
            checkBlocked(token);

            var requestWrapper = new RequestWrapper(request);
            requestWrapper.addHeader("X-User-Id", accessToken.username());
            requestWrapper.addHeader("X-User-Roles", Arrays.toString(accessToken.roles()));

            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private void checkBlocked(String token) {
        if (blockedUserRepository.contains(new BlockedUser(token))) {
            throw new AuthException(ErrorType.USER_NOT_APPROVED_YET);
        }
    }

    public static Optional<String> getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring("Bearer ".length()));
    }
}
