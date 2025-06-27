package com.ecsimsw.common.config;

import com.ecsimsw.common.support.aop.AuthUserArgumentResolver;
import com.ecsimsw.common.support.aop.InternalHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InternalHandlerInterceptor internalHandlerInterceptor;

    @Value("${auth.token.secret}")
    private String tokenSecret;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver(tokenSecret));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalHandlerInterceptor)
            .addPathPatterns("/**")
            .order(1);
    }
}