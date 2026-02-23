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

    private static final int MAX_PASSWORD_RESET_ATTEMPTS = 3;
    private static final int PASSWORD_RESET_WINDOW_HOURS = 1;
    private static final String PASSWORD_RESET_ATTEMPTS_PREFIX = "password_reset_attempts:";


    @Override
    public boolean isPasswordResetBlocked(String email) {
        String key = PASSWORD_RESET_ATTEMPTS_PREFIX + email;
        String attempts = redisTemplate.opsForValue().get(key);

        if (attempts == null) {
            return false;
        }

        int attemptCount = Integer.parseInt(attempts);
        boolean isBlocked = attemptCount >= MAX_PASSWORD_RESET_ATTEMPTS;

        if (isBlocked) {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            log.warn("Password reset blocked for email: {}. Time remaining: {} seconds", email, ttl);
        }
        return isBlocked;
    }

    @Override
    public int recordPasswordResetAttempt(String email) {
        String key = PASSWORD_RESET_ATTEMPTS_PREFIX + email;
        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts == null) {
            attempts = 1L;
        }

        if (attempts == 1) {
            redisTemplate.expire(key, Duration.ofHours(PASSWORD_RESET_WINDOW_HOURS));
        }

        log.debug("Password reset attempt {} for email: {}", attempts, email);

        if (attempts >= MAX_PASSWORD_RESET_ATTEMPTS) {
            log.warn("Max password reset attempts reached for email: {}", email);
            return -1;
        }
        return MAX_PASSWORD_RESET_ATTEMPTS - attempts.intValue();
    }

    @Override
    public long getPasswordResetBlockTimeRemaining(String email) {
        String key = PASSWORD_RESET_ATTEMPTS_PREFIX + email;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }

    @Override
    public boolean isBlocked(String key) {
        String blockKey = buildBlockKey(key);
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
        String attemptKey = buildAttemptKey(key);
        Long attempts = incrementAttempts(attemptKey);

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
        String attemptKey = buildAttemptKey(key);
        redisTemplate.delete(attemptKey);
        log.debug("Reset attempts for key: {}", key);
    }

    @Override
    public long getBlockTimeRemaining(String key) {
        String blockKey = buildBlockKey(key);
        Long ttl = redisTemplate.getExpire(blockKey, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }

    @Override
    public int getRemainingAttempts(String key) {
        String attemptKey = buildAttemptKey(key);
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

    private Long incrementAttempts(String attemptKey) {
        Long attempts = redisTemplate.opsForValue().increment(attemptKey);

        if (attempts == null) {
            attempts = 1L;
        }
        if (attempts == 1) {
            redisTemplate.expire(attemptKey, Duration.ofMinutes(ATTEMPT_WINDOW_MINUTES));
        }
        return attempts;
    }

    private String buildBlockKey(String key) {
        return BLOCK + key;
    }

    private String buildAttemptKey(String key) {
        return ATTEMPTS + key;
    }
}
