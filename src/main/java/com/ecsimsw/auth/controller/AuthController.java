package com.ecsimsw.auth.controller;

import com.ecsimsw.auth.domain.CustomUserDetail;
import com.ecsimsw.auth.dto.LogInRequest;
import com.ecsimsw.auth.dto.LogInResponse;
import com.ecsimsw.auth.dto.ReissueRequest;
import com.ecsimsw.auth.dto.Tokens;
import com.ecsimsw.auth.service.AuthService;
import com.ecsimsw.auth.utils.TokenUtils;
import com.ecsimsw.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ApiResponse<LogInResponse> login(@RequestBody LogInRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var userDetail = (CustomUserDetail) authentication.getPrincipal();
        var result = authService.issue(userDetail.username());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/auth/reissue")
    public ApiResponse<Tokens> reissue(@RequestBody ReissueRequest request) {
        var result = authService.reissue(request.refreshToken()).tokens();
        return ApiResponse.success(result);
    }

    @PostMapping("/api/auth/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal UserDetails user, HttpServletRequest request) {
        TokenUtils.getToken(request)
            .ifPresent(authService::blockToken);
        return ApiResponse.success();
    }
}
