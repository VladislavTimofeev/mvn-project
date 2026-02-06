package com.vlad.service;

public interface RateLimitService {

    boolean isBlocked(String key);

    int recordFailedAttempt(String key);

    void resetAttempts(String key);

    long getBlockTimeRemaining(String key);

    int getRemainingAttempts(String key);
}
