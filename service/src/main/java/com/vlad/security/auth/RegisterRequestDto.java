package com.vlad.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Email is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @JsonProperty("name")
    private String name;

    @Size(min = 2, max = 50, message = "Contact info must be between 2 and 50 characters")
    @JsonProperty("contact_info")
    private String contactInfo;

    @Size(min = 2, max = 50, message = "Address must be between 2 and 50 characters")
    @JsonProperty("address")
    private String address;
}
