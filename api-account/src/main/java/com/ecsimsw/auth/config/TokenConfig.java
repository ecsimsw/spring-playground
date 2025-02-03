package com.ecsimsw.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    public static final Integer ACCESS_TOKEN_EXPIRED_TIME = 30 * 60 * 1000;
    public static final Integer REFRESH_TOKEN_EXPIRED_TIME = 14 * 24 * 60 * 60 * 1000;

    public final String secretKey;

    public TokenConfig(
        @Value("${jwt.secret.key}")
        String secretKey
    ) {
        this.secretKey = secretKey;
    }
}
