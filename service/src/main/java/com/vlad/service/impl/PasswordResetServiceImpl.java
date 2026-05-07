package com.vlad.service.impl;

import com.vlad.entity.PasswordResetToken;
import com.vlad.entity.User;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.repository.PasswordResetTokenRepository;
import com.vlad.repository.UserRepository;
import com.vlad.security.auth.ValidateResetTokenResponseDto;
import com.vlad.service.EmailService;
import com.vlad.service.PasswordResetService;
import com.vlad.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RateLimitService rateLimitService;

    @Value("${app.password-reset.token-expiration-hours:1}")
    private int tokenExpirationHours;

    @Override
    public ValidateResetTokenResponseDto validateResetTokenWithDetails(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByTokenWithUser(token)
                    .orElse(null);

            if (resetToken == null || !resetToken.isValid()) {
                return ValidateResetTokenResponseDto.builder()
                        .valid(false)
                        .message("Invalid or expired token")
                        .build();
            }
            return ValidateResetTokenResponseDto.builder()
                    .valid(true)
                    .email(resetToken.getUser().getUsername())
                    .build();
        } catch (Exception e) {
            log.error("Error validating reset token", e);
            return ValidateResetTokenResponseDto.builder()
                    .valid(false)
                    .message("Invalid or expired token")
                    .build();
        }
    }

    @Override
    @Transactional
    public void createAndSendResetToken(String email) {
        if (rateLimitService.isPasswordResetBlocked(email)) {
            long remainingTime = rateLimitService.getPasswordResetBlockTimeRemaining(email);
            log.warn("Password reset rate limit exceeded for email: {}. Blocked for {} seconds", email, remainingTime);
            return;
        }

        User user = userRepository.findByUsername(email).orElse(null);

        if (user == null) {
            log.warn("Password reset requested for non-existent email: {}", email);
            rateLimitService.recordPasswordResetAttempt(email);
            return;
        }

        rateLimitService.recordPasswordResetAttempt(email);

        deleteExistingTokens(user);

        PasswordResetToken resetToken = createToken(user);
        tokenRepository.save(resetToken);

        log.info("Created password reset token for user: {}", user.getUsername());

        emailService.sendPasswordResetEmail(user.getUsername(), user.getName(), resetToken.getToken());
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = findAndValidateToken(token);

        User user = resetToken.getUser();
        updateUserPassword(user, newPassword);
        markTokenAsUsed(resetToken);

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    @Override
    public boolean validateResetToken(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElse(null);

            if (resetToken == null) {
                return false;
            }
            return resetToken.isValid();
        } catch (Exception e) {
            log.error("Error validating reset token", e);
            return false;
        }
    }

    @Override
    public String getEmailByToken(String token) {
        return tokenRepository.findByTokenWithUser(token)
                .map(resetToken -> resetToken.getUser().getUsername())
                .orElse(null);
    }

    private void deleteExistingTokens(User user) {
        tokenRepository.deleteByUser(user);
    }

    private PasswordResetToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        return PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(tokenExpirationHours))
                .build();
    }

    private PasswordResetToken findAndValidateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Invalid password reset token: {}", token);
                    return new ApiException(ErrorCode.INVALID_RESET_TOKEN);
                });

        if (resetToken.isUsed()) {
            log.warn("Password reset token already used: {}", token);
            throw new ApiException(ErrorCode.RESET_TOKEN_ALREADY_USED);
        }

        if (resetToken.isExpired()) {
            log.warn("Password reset token expired: {}", token);
            throw new ApiException(ErrorCode.RESET_TOKEN_EXPIRED);
        }
        return resetToken;
    }

    private void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void markTokenAsUsed(PasswordResetToken token) {
        token.setUsed(true);
        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
