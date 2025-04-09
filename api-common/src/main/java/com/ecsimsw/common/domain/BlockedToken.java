package com.ecsimsw.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class BlockedToken implements Serializable {

    private static final long REDIS_TTL_MS = 30 * 1000 * 60L;
    private static final String REDIS_KEY_FORMAT = "BlockedToken_%s";

    @Id
    private String token;

    public BlockedToken(String token) {
        this.token = token;
    }

    public String redisKey() {
        return String.format(REDIS_KEY_FORMAT, token);
    }

    public long timeoutMs() {
        return REDIS_TTL_MS;
    }
}


