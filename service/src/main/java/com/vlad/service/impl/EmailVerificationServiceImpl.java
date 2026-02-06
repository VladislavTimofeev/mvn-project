package com.vlad.service.impl;

import com.vlad.entity.EmailVerificationToken;
import com.vlad.entity.User;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.repository.EmailVerificationTokenRepository;
import com.vlad.repository.UserRepository;
import com.vlad.service.EmailService;
import com.vlad.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.verification.token-expiration-hours}")
    private int tokenExpirationHours;

    @Override
    public void createAndSendVerificationToken(User user) {
        deleteExistingTokens(user);

        EmailVerificationToken verificationToken = createToken(user);
        tokenRepository.save(verificationToken);

        log.info("Created verification token for user: {}", user.getUsername());

        emailService.sendVerificationEmail(user.getUsername(), user.getName(), verificationToken.getToken());
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = findAndValidateToken(token);

        markTokenAsUsed(verificationToken);
        markUserAsVerified(verificationToken.getUser());

        log.info("Email verified successfully for user: {}", verificationToken.getUser().getUsername());
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        User user = findUserByEmail(email);
        validateUserNotVerified(user);

        createAndSendVerificationToken(user);

        log.info("Verification email resent to: {}", email);
    }

    private void deleteExistingTokens(User user) {
        tokenRepository.deleteByUser(user);
    }

    private EmailVerificationToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        return EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(tokenExpirationHours))
                .build();
    }

    private EmailVerificationToken findAndValidateToken(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Invalid verification token: {}", token);
                    return new ApiException(ErrorCode.INVALID_VERIFICATION_TOKEN);
                });

        if (verificationToken.isUsed()) {
            log.warn("Verification token already used: {}", token);
            throw new ApiException(ErrorCode.TOKEN_ALREADY_USED);
        }

        if (verificationToken.isExpired()) {
            log.warn("Verification token expired: {}", token);
            throw new ApiException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
        }
        return verificationToken;
    }

    private void markTokenAsUsed(EmailVerificationToken token) {
        token.setUsed(true);
        token.setVerifiedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    private void markUserAsVerified(User user) {
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> {
                    log.warn("User not found for resend verification: {}", email);
                    return new ApiException(ErrorCode.USER_NOT_FOUND);
                });
    }

    private void validateUserNotVerified(User user) {
        if (user.isEmailVerified()) {
            log.warn("Email already verified for user: {}", user.getUsername());
            throw new ApiException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }
    }
}
