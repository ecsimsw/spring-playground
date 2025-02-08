package com.ecsimsw.auth.domain;

import com.ecsimsw.auth.config.TokenConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private final static Cache<String, Object> tokens = Caffeine.newBuilder()
        .expireAfterWrite(TokenConfig.REFRESH_TOKEN_EXPIRED_TIME, TimeUnit.MICROSECONDS)
        .build();

    public void save(String username, String token) {
        tokens.put(username, token);
    }

    public Optional<String> findByUsername(String username) {
        Object value = tokens.getIfPresent(username);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of((String) value);
    }
}
