package com.ecsimsw.common.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    private final String hostName;
    private final int port;

    public RedisConfig(
        @Value("${spring.data.redis.host}")
        String hostName,
        @Value("${spring.data.redis.port}")
        int port
    ) {
        this.hostName = hostName;
        this.port = port;
    }

    public RedisTemplate<String, Object> redisTemplate(int commandTimeout) {
        var connFactory = redisConnectionFactory(commandTimeout);
        return redisTemplate(connFactory);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        var defaultTimeout = 3000;
        var connFactory = redisConnectionFactory(defaultTimeout);
        return redisTemplate(connFactory);
    }

    private LettuceConnectionFactory redisConnectionFactory(int commandTimeoutMs) {
        var redisConfig = new RedisStandaloneConfiguration(hostName, port);
        var clientConfig = LettucePoolingClientConfiguration
            .builder()
            .poolConfig(getPoolConfig())
            .commandTimeout(Duration.ofMillis(commandTimeoutMs))
            .build();
        var lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig, clientConfig);
        lettuceConnectionFactory.start();
        return lettuceConnectionFactory;
    }

    private RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    private GenericObjectPoolConfig<?> getPoolConfig() {
        var poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(3);
        poolConfig.setMinIdle(1);
        return poolConfig;
    }
}


