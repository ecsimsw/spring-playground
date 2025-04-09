package com.ecsimsw.common.domain;

import com.ecsimsw.common.config.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlockedTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public BlockedTokenRepository(RedisConfig redisConfig) {
        this.redisTemplate = redisConfig.redisTemplate(1000);
    }

    public boolean exists(BlockedToken token) {
        try {
            return redisTemplate.hasKey(token.redisKey());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(BlockedToken token) {
        try {
            redisTemplate.opsForValue().set(token.redisKey(), token, token.timeoutMs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}