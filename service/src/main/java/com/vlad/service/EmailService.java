package com.vlad.service;

public interface EmailService {
    void sendVerificationEmail(String to, String userName, String verificationToken);
    void sendPasswordResetEmail(String to, String userName, String resetToken);
}
