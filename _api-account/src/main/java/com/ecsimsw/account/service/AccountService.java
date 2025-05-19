package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.*;
import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import com.ecsimsw.common.service.client.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashSet;

import static com.ecsimsw.common.config.LogConfig.MDC_TRACE_ID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPasswordRepository accountPasswordRepository;
    private final UserRoleRepository userRoleRepository;
    private final NotificationClient notificationClient;

    public Long create(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(ErrorType.USER_ALREADY_EXISTS);
        }
        var user = userRepository.save(request.toUser(passwordEncoder));

        var userPassword = new AccountPassword(passwordEncoder, user.getId(), request.username(), request.password());
        accountPasswordRepository.save(userPassword);

        var userRole = new UserRole(user.getId(), false, new HashSet<>());
        userRoleRepository.save(userRole);

        notificationClient.createNotificationAsync("user created : " + user.getId())
            .doOnError(WebClientResponseException.class, ex -> {
                log.info(
                    "traceId : {}, status : {}, body : {}",
                    ex.getHeaders().getFirst(MDC_TRACE_ID),
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString()
                );
            }).subscribe(success -> {
            }, error -> {
            });
        return user.getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse userInfo(String userName) {
        var user = getByUsername(userName);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void delete(String username) {
        var user = getByUsername(username);
        user.deleted();
    }

    private User getByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
            .orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }
}
