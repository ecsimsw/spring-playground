package com.ecsimsw.common.support.aop;

import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

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
        var username = webRequest.getHeader("X-User-Id");
        var roles = webRequest.getHeader("X-User-Roles");
        if(username == null || roles == null) {
            throw new AuthException(ErrorType.FAILED_TO_AUTHENTICATE);
        }
        return new AuthUser(username, roles.split(","));
    }
}
