package com.vlad.service.impl;

import com.vlad.security.jwt.JwtTokenProvider;
import com.vlad.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    /**
     * Add token to blacklist
     */
    @Override
    public void blacklistToken(String token, String username) {
        if (isBlacklisted(token)) {
            log.debug("Token already blacklisted");
            return;
        }

        String key = BLACKLIST_PREFIX + token;

        Date exprirationDate = jwtTokenProvider.getExpirationDate(token);
        long now = System.currentTimeMillis();
        long ttlMillis = exprirationDate.getTime() - now;

        if (ttlMillis > 0) {
            redisTemplate.opsForValue().set(
                    key,
                    username,
                    ttlMillis,
                    TimeUnit.MILLISECONDS
            );
            log.info("Token blacklisted for user '{}' with TTL: {} seconds",
                    username, ttlMillis / 1000);
        } else {
            log.warn("Token already expired, not adding to blacklist");
        }
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public void removeFromBlacklist(String token) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.delete(key);
        log.debug("Token removed from blacklist");
    }

    @Override
    public long getBlacklistSize() {
        var keys = redisTemplate.keys(BLACKLIST_PREFIX + "*");
        return keys != null ? keys.size() : 0;
    }
}
