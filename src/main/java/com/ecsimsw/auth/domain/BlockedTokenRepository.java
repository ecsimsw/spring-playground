package com.ecsimsw.auth.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static com.ecsimsw.auth.config.SecurityConfig.ACCESS_TOKEN_EXPIRED_TIME;

@Repository
public class BlockedTokenRepository {

    private final Cache<String, Object> tokens = Caffeine.newBuilder()
        .expireAfterWrite(ACCESS_TOKEN_EXPIRED_TIME, TimeUnit.MICROSECONDS)
        .build();

    public void save(String token) {
        tokens.put(token, null);
    }

    public boolean exists(String token) {
        return tokens.getIfPresent(token) != null;
    }
}
