package com.ecsimsw.supportratelimiter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class FixedWindowNonAtomic {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean limitPerSecond(int limit) {
        var key = "RATE_LIMIT";
        var windowSecond = 1;

        var count = redisTemplate.opsForValue().increment(key);
        if(count == 1) {
            redisTemplate.expire(key, windowSecond, TimeUnit.SECONDS);
        }
        return count < limit;
    }
}
