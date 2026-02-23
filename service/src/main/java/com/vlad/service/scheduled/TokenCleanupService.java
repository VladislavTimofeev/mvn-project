package com.vlad.service.scheduled;

import com.vlad.repository.EmailVerificationTokenRepository;
import com.vlad.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final String CRON_DAILY_3AM = "0 0 3 * * *";

    @Scheduled(cron = CRON_DAILY_3AM)
    public void cleanupExpiredTokens() {
        log.info("Starting scheduled token cleanup");

        LocalDateTime now = LocalDateTime.now();

        cleanupEmailVerificationTokens(now);
        cleanupPasswordResetTokens(now);

        log.info("Scheduled token cleanup completed");
    }

    @Transactional
    protected void cleanupEmailVerificationTokens(LocalDateTime expirationThreshold) {
        try {
            emailVerificationTokenRepository.deleteExpiredTokens(expirationThreshold);
            log.info("Email verification tokens cleanup completed successfully");
        } catch (Exception e) {
            log.error("Failed to cleanup email verification tokens", e);
        }
    }

    @Transactional
    protected void cleanupPasswordResetTokens(LocalDateTime expirationThreshold) {
        try {
            passwordResetTokenRepository.deleteExpiredTokens(expirationThreshold);
            log.info("Password reset tokens cleanup completed successfully");
        } catch (Exception e) {
            log.error("Failed to cleanup password reset tokens", e);
        }
    }
}
