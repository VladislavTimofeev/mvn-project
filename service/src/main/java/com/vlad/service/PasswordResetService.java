package com.vlad.service;

import com.vlad.security.auth.ValidateResetTokenResponseDto;

public interface PasswordResetService {

    ValidateResetTokenResponseDto validateResetTokenWithDetails(String token);

    void createAndSendResetToken(String email);

    void resetPassword(String token, String newPassword);

    boolean validateResetToken(String token);

    String getEmailByToken(String token);
}
