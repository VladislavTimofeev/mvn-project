package com.vlad.service.impl;

import com.vlad.exception.ex.EmailSendException;
import com.vlad.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.verification.base-url}")
    private String baseUrl;

    @Value("${app.verification.from-email}")
    private String fromEmail;

    @Value("${app.verification.from-name}")
    private String fromName;

    private static final String VERIFICATION_SUBJECT = "Verify Your Email Address";
    private static final String VERIFICATION_TEMPLATE = "email/verification";
    private static final String PASSWORD_RESET_SUBJECT = "Reset Your Password";
    private static final String PASSWORD_RESET_TEMPLATE = "email/password-reset";

    @Override
    public void sendVerificationEmail(String to, String userName, String verificationToken) {
        try {
            String verificationUrl = baseUrl + "/api/v2/auth/verify-email?token=" + verificationToken;

            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("verificationUrl", verificationUrl);

            String htmlContent = templateEngine.process(VERIFICATION_TEMPLATE, context);

            sendHtmlEmail(to, VERIFICATION_SUBJECT, htmlContent);
            log.info("Verification email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", to, e);
            throw new EmailSendException("Failed to send verification email to: " + to, e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String userName, String resetToken) {
        try {
            String resetUrl = baseUrl + "/api/v2/auth/reset-password?token=" + resetToken;

            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("resetUrl", resetUrl);

            String htmlContent = templateEngine.process(PASSWORD_RESET_TEMPLATE, context);

            sendHtmlEmail(to, PASSWORD_RESET_SUBJECT, htmlContent);
            log.info("Password reset email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new EmailSendException("Failed to send password reset email to: " + to, e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
