package com.vlad.exception.ex;

import lombok.Getter;

@Getter
public class RateLimitExceededException extends RuntimeException {

    private final long blockTimeRemaining;

    public RateLimitExceededException(String message, long blockTimeRemaining) {
        super(message);
        this.blockTimeRemaining = blockTimeRemaining;
    }
}
