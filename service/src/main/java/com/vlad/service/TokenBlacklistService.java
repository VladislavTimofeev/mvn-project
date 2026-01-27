package com.vlad.service;

public interface TokenBlacklistService {

    void blacklistToken(String token, String username);

    boolean isBlacklisted(String token);

    void removeFromBlacklist(String token);

    long getBlacklistSize();
}
