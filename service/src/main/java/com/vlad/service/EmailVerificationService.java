package com.vlad.service;

import com.vlad.entity.User;
import com.vlad.security.auth.VerifyEmailResponseDto;

public interface EmailVerificationService {

    VerifyEmailResponseDto verifyEmailWithDetails(String token);

    void createAndSendVerificationToken(User user);

    void verifyEmail(String token);

    void resendVerificationEmail(String email);

    String getEmailByToken(String token);
}
