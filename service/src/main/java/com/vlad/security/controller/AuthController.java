package com.vlad.security.controller;

import com.vlad.security.auth.*;
import com.vlad.security.service.AuthService;
import com.vlad.service.EmailVerificationService;
import com.vlad.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponseDto> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        passwordResetService.createAndSendResetToken(request.getEmail());
        return ResponseEntity.ok(
                PasswordResetResponseDto.builder()
                        .message("If an account exists with this email, you will receive password reset instructions.")
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponseDto> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(
                PasswordResetResponseDto.builder()
                        .message("Password has been reset successfully. You can now login with your new password.")
                        .build()
        );
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<ValidateResetTokenResponseDto> validateResetToken(@RequestParam String token) {
        ValidateResetTokenResponseDto response = passwordResetService.validateResetTokenWithDetails(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public VerifyEmailResponseDto verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        String email = emailVerificationService.getEmailByToken(token);
        return VerifyEmailResponseDto.builder()
                .message("Email verified successfully! You can now login.")
                .verified(true)
                .email(email)
                .build();
    }

    @PostMapping("/resend-verification")
    public VerifyEmailResponseDto resendVerification(@Valid @RequestBody ResendVerificationRequestDto request) {
        emailVerificationService.resendVerificationEmail(request.getEmail());
        return VerifyEmailResponseDto.builder()
                .message("Verification email sent. Please check your inbox.")
                .email(request.getEmail())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequestDto request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
