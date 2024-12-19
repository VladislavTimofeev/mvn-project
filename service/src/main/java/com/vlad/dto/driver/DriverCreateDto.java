package com.vlad.dto.driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class DriverCreateDto {
    @NotNull
    Long carrierId;
    @NotBlank
    String name;
    String licenseNumber;
    String phoneNumber;
}
