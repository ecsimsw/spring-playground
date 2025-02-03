package com.ecsimsw.auth.service;

import com.ecsimsw.account.domain.UserRepository;
import com.ecsimsw.account.domain.UserRoleRepository;
import com.ecsimsw.auth.domain.CustomUserDetail;
import com.ecsimsw.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
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
        if (!user.isApproved()) {
            throw new AuthException(ErrorType.USER_NOT_APPROVED_YET);
        }
        var authRoles = userRoleRepository.findAllByUserId(user.getId()).stream()
            .flatMap(role -> role.roleNames().stream())
            .toList();
        return CustomUserDetail.builder()
            .username(user.getUsername())
            .password(user.getPassword().getEncrypted())
            .userStatus(user.getStatus())
            .isAdmin(user.isAdmin())
            .roleNames(authRoles)
            .build();
    }
}