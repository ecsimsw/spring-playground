package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.*;
import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPasswordRepository userPasswordRepository;
    private final UserRoleRepository userRoleRepository;
    private final ExternalPlatformService externalPlatformService;

    public Long create(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(ErrorType.USER_ALREADY_EXISTS);
        }
        var user = userRepository.save(request.toUser());
        var userPassword = new UserPassword(passwordEncoder, user.getId(), request.username(), request.password());
        userPasswordRepository.save(userPassword);
        var userRole = new UserRole(user.getId(), false, new HashSet<>());
        userRoleRepository.save(userRole);
        return user.getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse userInfo(String userName) {
        var user = getByUsername(userName);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void delete(String username) {
        userPasswordRepository.deleteByUsername(username);
    }

    public String getUid(String username) {
        externalPlatformService.getUidByUsername(username);
    }

    private User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }
}
