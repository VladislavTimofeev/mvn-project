package com.vlad.service.impl;

import com.vlad.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_ATTEMPTS = 5;
    private static final int ATTEMPT_WINDOW_MINUTES = 1;
    private static final int BLOCK_DURATION_MINUTES = 5;
    private static final String BLOCK = "block:";
    private static final String ATTEMPTS = "attempts:";


    @Override
    public boolean isBlocked(String key) {
        String blockKey = BLOCK + key;
        Boolean isBlocked = redisTemplate.hasKey(blockKey);

        if (Boolean.TRUE.equals(isBlocked)) {
            Long ttl = redisTemplate.getExpire(blockKey, TimeUnit.SECONDS);
            log.warn("Access blocked for key: {}. Time remaining: {} seconds", key, ttl);
            return true;
        }
        return false;
    }

    @Override
    public int recordFailedAttempt(String key) {
        String attemptKey = ATTEMPTS + key;

        Long attempts = redisTemplate.opsForValue().increment(attemptKey);

        if (attempts == null) {
            attempts = 1L;
        }

        if (attempts == 1) {
            redisTemplate.expire(attemptKey, Duration.ofMinutes(ATTEMPT_WINDOW_MINUTES));
        }

        log.debug("Failed login attempt {} for key: {}", attempts, key);

        if (attempts >= MAX_ATTEMPTS) {
            blockKey(key);
            log.warn("Rate limit exceeded for key: {}. Blocked for {} minutes", key, BLOCK_DURATION_MINUTES);
            return -1;
        }
        return MAX_ATTEMPTS - attempts.intValue();
    }

    @Override
    public void resetAttempts(String key) {
        String attemptKey = ATTEMPTS + key;
        redisTemplate.delete(attemptKey);
        log.debug("Reset attempts for key: {}", key);
    }

    @Override
    public long getBlockTimeRemaining(String key) {
        String blockKey = BLOCK + key;
        Long ttl = redisTemplate.getExpire(blockKey, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }

    @Override
    public int getRemainingAttempts(String key) {
        String attemptKey = ATTEMPTS + key;
        String attempts = redisTemplate.opsForValue().get(attemptKey);

        if (attempts == null) {
            return MAX_ATTEMPTS;
        }

        int currentAttempts = Integer.parseInt(attempts);
        return Math.max(0, MAX_ATTEMPTS - currentAttempts);
    }

    private void blockKey(String key) {
        String blockKey = BLOCK + key;
        String attemptKey = ATTEMPTS + key;

        redisTemplate.delete(attemptKey);

        redisTemplate.opsForValue().set(blockKey, "blocked", Duration.ofMinutes(BLOCK_DURATION_MINUTES));
    }
}
