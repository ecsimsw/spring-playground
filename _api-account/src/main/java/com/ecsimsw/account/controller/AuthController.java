package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.LogInRequest;
import com.ecsimsw.account.dto.LogInResponse;
import com.ecsimsw.account.dto.ReissueRequest;
import com.ecsimsw.account.dto.Tokens;
import com.ecsimsw.account.service.AuthService;
import com.ecsimsw.account.service.CustomUserDetail;
import com.ecsimsw.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/api/account/login")
    public ApiResponse<LogInResponse> login(@RequestBody LogInRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var userDetail = (CustomUserDetail) authentication.getPrincipal();
        var result = authService.issue(userDetail.username());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/account/reissue")
    public ApiResponse<Tokens> reissue(@RequestBody ReissueRequest request) {
        var result = authService.reissue(request.refreshToken()).tokens();
        return ApiResponse.success(result);
    }

    @PostMapping("/api/account/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        getToken(request).ifPresent(authService::blockToken);
        return ApiResponse.success();
    }

    @GetMapping("/api/account/up")
    public ResponseEntity<Void> up() {
        return ResponseEntity.ok().build();
    }

    private Optional<String> getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring("Bearer ".length()));
    }
}
