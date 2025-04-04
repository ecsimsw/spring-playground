package com.ecsimsw.common.support;

import com.ecsimsw.common.support.annotation.InternalHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class InternalHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod method) {
            if (method.hasMethodAnnotation(InternalHandler.class) || method.getBeanType().isAnnotationPresent(InternalHandler.class)) {
                var clientKey = request.getHeader("X-Client-Key");
                if (clientKey == null || !ClientKeyUtils.isValid(clientKey, 30_000)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid authorization token\"}");
                    return false;
                }
            }
        }
        return true;
    }
}
