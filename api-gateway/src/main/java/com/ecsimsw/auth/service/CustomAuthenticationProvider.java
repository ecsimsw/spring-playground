package com.ecsimsw.auth.service;

import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        var requestPassword = String.valueOf(authentication.getCredentials());
        if (passwordEncoder.matches(requestPassword, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        throw new AuthException(ErrorType.FAILED_TO_AUTHENTICATE);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
