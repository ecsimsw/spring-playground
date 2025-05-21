package com.ecsimsw.common.support.aop;

import com.ecsimsw.common.domain.AccessToken;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final String tokenSecret;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        org.springframework.web.bind.support.WebDataBinderFactory binderFactory
    ) {
        var bearerToken = webRequest.getHeader("Authorization");
        if(bearerToken == null) {
            throw new AuthException(ErrorType.FAILED_TO_AUTHENTICATE);
        }
        var token = bearerToken.split("Bearer ")[1];
        var accessToken = AccessToken.fromToken(tokenSecret, token);
        return new AuthUser(accessToken.username(), accessToken.uid());
    }
}
