package com.ecsimsw.auth.service;

import com.ecsimsw.auth.config.TokenConfig;
import com.ecsimsw.auth.domain.AccessToken;
import com.ecsimsw.auth.domain.BlockedTokenRepository;
import com.ecsimsw.auth.domain.BlockedUserRepository;
import com.ecsimsw.auth.domain.CustomUserDetail;
import com.ecsimsw.auth.dto.RequestWrapper;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.TokenUtils;
import com.ecsimsw.error.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TokenFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final TokenConfig tokenConfig;
    private final BlockedTokenRepository blockedTokenRepository;
    private final BlockedUserRepository blockedUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            var token = TokenUtils.getToken(request)
//                .orElseThrow(() -> new AuthException(ErrorType.TOKEN_NOT_FOUND));
//            var loginUser = userDetailFromToken(token);
//            checkBlocked(token, loginUser.getUsername());
//
            var authToken = new UsernamePasswordAuthenticationToken(
                "loginUser", null, null
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            var requestWrapper = new RequestWrapper(request);
            requestWrapper.addHeader("X-User-Id", "ecsimsw");
            requestWrapper.addHeader("X-User-Roles", "roles");

            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private void checkBlocked(String token, String username) {
        if (blockedTokenRepository.exists(token) || blockedUserRepository.exists(username)) {
            throw new AuthException(ErrorType.USER_NOT_APPROVED_YET);
        }
    }

    private CustomUserDetail userDetailFromToken(String token) {
        var accessToken = AccessToken.fromToken(tokenConfig.secretKey, token);
        var roles = authService.roleNames(accessToken.username());
        return CustomUserDetail.builder()
            .username(accessToken.username())
            .userStatus(accessToken.userStatus())
            .isAdmin(accessToken.isAdmin())
            .roleNames(roles)
            .build();
    }
}
