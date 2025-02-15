package com.ecsimsw.gateway.service;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.domain.UserRepository;
import com.ecsimsw.domain.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(ErrorType.FAILED_TO_AUTHENTICATE));
        if (user.isDeleted()) {
            throw new AuthException(ErrorType.FAILED_TO_AUTHENTICATE);
        }
        var authRoles = userRoleRepository.findAllByUserId(user.getId()).stream()
            .flatMap(role -> role.roleNames().stream())
            .toList();
        return CustomUserDetail.builder()
            .username(user.getUsername())
            .password(user.getPassword().getEncrypted())
            .isAdmin(user.isAdmin())
            .roleNames(authRoles)
            .build();
    }
}