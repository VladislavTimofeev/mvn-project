package com.vlad.service;

import com.vlad.entity.User;

public interface EmailVerificationService {
    void createAndSendVerificationToken(User user);
    void verifyEmail(String token);
    void resendVerificationEmail(String email);
}
