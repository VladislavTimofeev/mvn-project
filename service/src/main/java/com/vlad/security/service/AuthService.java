package com.vlad.security.service;

import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.repository.UserRepository;
import com.vlad.security.auth.AuthRequestDto;
import com.vlad.security.auth.AuthResponseDto;
import com.vlad.security.auth.RefreshTokenRequestDto;
import com.vlad.security.auth.RegisterRequestDto;
import com.vlad.security.jwt.JwtTokenProvider;
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
    private static final String BEARER = "Bearer";

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Registration attempt for email: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: email {} already exists", request.getUsername());
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .contactInfo(request.getContactInfo())
                .address(request.getAddress())
                .role(Role.GUEST)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.getPassword()
        );

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(BEARER)
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }

    public AuthResponseDto login(AuthRequestDto request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            log.info("User '{}' logged in successfully", request.getUsername());

            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType(BEARER)
                    .expiresIn(jwtTokenProvider.getExpirationTime())
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", request.getUsername());
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        log.info("Refresh token request received");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("Invalid refresh token");
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtTokenProvider.generateTokenFromUsername(userDetails.getUsername());

        log.info("Access token refreshed for user: {}", username);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType(BEARER)
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }
}
