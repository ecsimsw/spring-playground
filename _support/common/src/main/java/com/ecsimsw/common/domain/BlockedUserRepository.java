package com.ecsimsw.common.domain;

import com.ecsimsw.common.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BlockedUserRepository {

    private static final String REDIS_KEY_FORMAT = "BLOCKED_USER_CACHE_%s";

    private final RedisTemplate<String, Object> redisTemplate;
    private final BlockedUserDataRepository dataRepository;

    public BlockedUserRepository(RedisConfig redisConfig, BlockedUserDataRepository dataRepository) {
        this.redisTemplate = redisConfig.redisTemplate(1000);
        this.dataRepository = dataRepository;
    }

    public boolean contains(BlockedUser blockedUser) {
        try {
            var redisKey = redisKey(blockedUser);
            return redisTemplate.hasKey(redisKey);
        } catch (Exception e) {
            log.info("Failed to add redis");
        }
        return dataRepository.existsById(blockedUser.getUsername());
    }

    public void save(BlockedUser blockedUser) {
        try {
            var redisKey = redisKey(blockedUser);
            redisTemplate.opsForValue().set(redisKey, blockedUser.getUsername());
            dataRepository.save(blockedUser);
        } catch (Exception e) {
            log.info("Failed to add redis");
        }
    }

    private String redisKey(BlockedUser blockedUser) {
        return String.format(REDIS_KEY_FORMAT, blockedUser.getUsername());
    }
}