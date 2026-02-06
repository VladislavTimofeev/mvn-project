package com.vlad.security.service;

import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.exception.ex.RateLimitExceededException;
import com.vlad.repository.UserRepository;
import com.vlad.security.auth.*;
import com.vlad.security.jwt.JwtTokenProvider;
import com.vlad.service.RateLimitService;
import com.vlad.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RateLimitService rateLimitService;

    private static final String BEARER = "Bearer";

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Registration attempt for email: {}", request.getUsername());

        validateUserNotExists(request.getUsername());

        User user = createUser(request);
        userRepository.save(user);

        log.info("User registered successfully: {}", user.getUsername());

        return generateAuthResponse(user.getUsername(), request.getPassword());
    }

    public AuthResponseDto login(AuthRequestDto request) {
        String username = request.getUsername();

        log.info("Login attempt for user: {}", username);

        checkRateLimitBlock(username);

        try {
            Authentication authentication = authenticateUser(username, request.getPassword());
            rateLimitService.resetAttempts(username);

            log.info("User '{}' logged in successfully", username);

            return generateAuthResponse(authentication);
        } catch (BadCredentialsException e) {
            handleFailedLoginAttempt(username);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public void logout(LogoutRequestDto request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();
        validateAndBlacklistTokens(accessToken, refreshToken);
    }

    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        log.info("Refresh token request received");

        validateRefreshToken(refreshToken);

        String username = jwtTokenProvider.getUsername(refreshToken);
        String newAccessToken = jwtTokenProvider.generateTokenFromUsername(username);

        log.info("Access token refreshed for user: {}", username);

        return buildAuthResponse(newAccessToken, refreshToken);
    }

    private void validateUserNotExists(String username) {
        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed: email {} already exists", username);
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private User createUser(RegisterRequestDto request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .contactInfo(request.getContactInfo())
                .address(request.getAddress())
                .role(Role.GUEST)
                .build();
    }

    private void checkRateLimitBlock(String username) {
        if (rateLimitService.isBlocked(username)) {
            long blockTimeRemaining = rateLimitService.getBlockTimeRemaining(username);
            log.warn("Login blocked for user: {}. Time remaining: {} seconds", username, blockTimeRemaining);
            throw createRateLimitException(blockTimeRemaining, "Please try again in");
        }
    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private void handleFailedLoginAttempt(String username) {
        int remainingAttempts = rateLimitService.recordFailedAttempt(username);

        if (remainingAttempts == -1) {
            long blockTimeRemaining = rateLimitService.getBlockTimeRemaining(username);
            log.warn("Rate limit exceeded for user: {}. Blocked for {} seconds", username, blockTimeRemaining);
            throw createRateLimitException(blockTimeRemaining, "Account locked for");
        }

        log.warn("Failed login attempt for user: {}. Remaining attempts: {}", username, remainingAttempts);

        throw new ApiException(
                ErrorCode.INVALID_CREDENTIALS,
                String.format("Invalid username or password. %d attempt(s) remaining before account lock.",
                        remainingAttempts)
        );
    }

    private RateLimitExceededException createRateLimitException(long blockTimeSeconds, String messagePrefix) {
        long minutesRemaining = Math.max(1, blockTimeSeconds / 60);
        String message = String.format("Too many failed login attempts. %s %d minute(s).",
                messagePrefix, minutesRemaining);
        return new RateLimitExceededException(message, blockTimeSeconds);
    }

    private void validateAndBlacklistTokens(String accessToken, String refreshToken) {
        try {
            if (!jwtTokenProvider.validateToken(accessToken)) {
                throw new ApiException(ErrorCode.INVALID_TOKEN);
            }

            String username = jwtTokenProvider.getUsername(accessToken);

            tokenBlacklistService.blacklistToken(accessToken, username);
            tokenBlacklistService.blacklistToken(refreshToken, username);

            log.info("User '{}' logged out successfully", username);
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (tokenBlacklistService.isBlacklisted(refreshToken)) {
            log.warn("Attempted to refresh using blacklisted token");
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("Invalid refresh token");
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }
    }

    private AuthResponseDto generateAuthResponse(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return generateAuthResponse(authentication);
    }

    private AuthResponseDto generateAuthResponse(Authentication authentication) {
        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        return buildAuthResponse(accessToken, refreshToken);
    }

    private AuthResponseDto buildAuthResponse(String accessToken, String refreshToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(BEARER)
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }
}
