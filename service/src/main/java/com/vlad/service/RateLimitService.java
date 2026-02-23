package com.vlad.service;

public interface RateLimitService {

    boolean isPasswordResetBlocked(String email);

    int recordPasswordResetAttempt(String email);

    long getPasswordResetBlockTimeRemaining(String email);

    boolean isBlocked(String key);

    int recordFailedAttempt(String key);

    void resetAttempts(String key);

    long getBlockTimeRemaining(String key);

    int getRemainingAttempts(String key);
}
