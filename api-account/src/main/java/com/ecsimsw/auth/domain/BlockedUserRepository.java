package com.ecsimsw.auth.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static com.ecsimsw.auth.config.SpringSecurityConfig.REFRESH_TOKEN_EXPIRED_TIME;

@Repository
public class BlockedUserRepository {

    private final Cache<String, Object> usernames = Caffeine.newBuilder()
        .expireAfterWrite(REFRESH_TOKEN_EXPIRED_TIME, TimeUnit.MICROSECONDS)
        .build();

    public void save(String username) {
        usernames.put(username, null);
    }

    public boolean exists(String username) {
        return usernames.getIfPresent(username) != null;
    }
}
