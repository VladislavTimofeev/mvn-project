package com.vlad.security.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret is required")
    private String secret;

    @NotNull(message = "JWT expiration is required")
    @Min(value = 60000, message = "Expiration must be at least 1 minute")
    private Long expiration;

    @NotNull
    @Min(value = 86400000, message = "Refresh expiration must be at least 1 day")
    private Long refreshExpiration;

    @NotBlank
    private String issuer;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 32 characters long. " +
                            "Current length: " + (secret != null ? secret.length() : 0)
            );
        }

        if (expiration == null || expiration <= 0) {
            throw new IllegalStateException("JWT expiration must be positive");
        }

        if (refreshExpiration == null || refreshExpiration <= expiration) {
            throw new IllegalStateException(
                    "Refresh token expiration must be greater than access token expiration"
            );
        }
    }
}
