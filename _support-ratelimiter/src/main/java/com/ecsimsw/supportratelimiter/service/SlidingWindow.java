package com.ecsimsw.supportratelimiter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class SlidingWindow {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean limit(int windowSize, TimeUnit timeUnit, int limit) {
        var key = "RATE_LIMIT";
        var windowSecond = timeUnit.toSeconds(windowSize);

        var script = """
            local key = KEYS[1]
            local now = tonumber(ARGV[1])
            local window = tonumber(ARGV[2])
            local maxReq = tonumber(ARGV[3])
            local member = ARGV[4]
        
            redis.call('ZREMRANGEBYSCORE', key, 0, now-window)
            local cnt = redis.call('ZCOUNT', key, now-window+1, now)
            if cnt >= maxReq then
              return 0
            else
              redis.call('ZADD', key, now, member)
              redis.call('EXPIRE', key, window+10)
              return 1
            end
        """;
        var result = redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            Collections.singletonList(key),
            String.valueOf(System.currentTimeMillis()),
            String.valueOf(windowSecond * 1000),
            String.valueOf(limit),
            UUID.randomUUID().toString()
        );
        return result != null && result == 1L;
    }
}
