//package com.ecsimsw.auth.domain;
//
//import com.ecsimsw.auth.config.TokenConfig;
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import org.springframework.stereotype.Repository;
//
//import java.util.concurrent.TimeUnit;
//
//@Repository
//public class BlockedTokenRepository {
//
//    private final Cache<String, Object> tokens = Caffeine.newBuilder()
//        .expireAfterWrite(TokenConfig.REFRESH_TOKEN_EXPIRED_TIME, TimeUnit.MICROSECONDS)
//        .build();
//
//    public void save(String token) {
//        tokens.put(token, null);
//    }
//
//    public boolean exists(String token) {
//        return tokens.getIfPresent(token) != null;
//    }
//}
