package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.AuthTokenResponse;
import com.ecsimsw.account.dto.LogInRequest;
import com.ecsimsw.account.dto.ReissueRequest;
import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.error.AccountException;
import com.ecsimsw.account.service.AuthTokenService;
import com.ecsimsw.account.service.CustomUserDetail;
import com.ecsimsw.account.service.UserService;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.support.client.DeviceClient;
import com.ecsimsw.common.support.client.EventClient;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;
    private final ExternalPlatformService externalPlatformService;
    private final UserService userService;
    private final DeviceClient deviceClient;
    private final EventClient eventClient;

    @PostMapping("/api/account/beta/login")
    public ApiResponse<AuthTokenResponse> testLogin(@RequestBody LogInRequest request) {
        try {
            var uid = externalPlatformService.getUserIdByUsername(request.username());
            userService.betaCreate(new SignUpRequest(request.username(), "password"));
            var result = authTokenService.betaIssue(request.username(), uid);

            var eventRefreshFuture = CompletableFuture.supplyAsync(() ->
                eventClient.refresh(request.username())
            );
            var deviceRefreshFuture = CompletableFuture.supplyAsync(() ->
                deviceClient.refresh(request.username())
            );
            CompletableFuture.allOf(eventRefreshFuture, deviceRefreshFuture)
                .join();
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.fillInStackTrace();
            log.error(e.fillInStackTrace().getMessage());
            throw new AccountException(ErrorType.USER_NOT_FOUND);
        }
    }

    @PostMapping("/api/account/login")
    public ApiResponse<AuthTokenResponse> login(@RequestBody LogInRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var userDetail = (CustomUserDetail) authentication.getPrincipal();
        var result = authTokenService.issue(userDetail.username());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/account/reissue")
    public ApiResponse<AuthTokenResponse> reissue(@RequestBody ReissueRequest request) {
        var result = authTokenService.reissue(request.refreshToken());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/account/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        getToken(request).ifPresent(authTokenService::blockToken);
        return ApiResponse.success();
    }

    private Optional<String> getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring("Bearer ".length()));
    }
}
