package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.UserPasswordRepository;
import com.ecsimsw.account.domain.UserRoleRepository;
import com.ecsimsw.account.error.AccountException;
import com.ecsimsw.common.error.ErrorType;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserPasswordRepository userPasswordRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userPasswordRepository.findByUsername(username)
            .orElseThrow(() -> new AccountException(ErrorType.FAILED_TO_AUTHENTICATE));
        var role = userRoleRepository.findByUserId(user.userId())
            .orElseThrow(() -> new AccountException(ErrorType.FAILED_TO_AUTHENTICATE));
        return CustomUserDetail.builder()
            .username(user.username())
            .password(user.password())
            .isAdmin(role.getIsAdmin())
            .roleNames(role.roleNames())
            .build();
    }
}