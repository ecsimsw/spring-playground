package com.ecsimsw.supportratelimiter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class FixedWindow {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean limit(int windowSize, TimeUnit timeUnit, int limit) {
        var key = "RATE_LIMIT";
        var windowSecond = timeUnit.toSeconds(windowSize);

        var script = """
             local key = KEYS[1]
             local maxCount = tonumber(ARGV[1])
             local expireSeconds = tonumber(ARGV[2])
             local curr = tonumber(redis.call('get', key) or '0')
        
             if curr == 0 or redis.call('ttl', key) == -1 then
               redis.call('expire', key, expireSeconds)
             end
        
             if curr >= maxCount then
               return 0
             end
        
             redis.call('incr', key)
             return 1
        """;
        var result = redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            Collections.singletonList(key),
            String.valueOf(limit),
            String.valueOf(windowSecond)
        );
        return result != null && result == 1L;
    }
}
