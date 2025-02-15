package com.ecsimsw.auth.service;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.domain.User;
import com.ecsimsw.domain.UserRepository;
import com.ecsimsw.domain.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public List<String> roleNames(String username) {
        var user = getUserByUsername(username);
        if (user.isAdmin()) {
            return List.of("ADMIN");
        }
        return userRoleRepository.findAllByUserId(user.getId()).stream()
            .flatMap(role -> role.roleNames().stream())
            .toList();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
    }
}
