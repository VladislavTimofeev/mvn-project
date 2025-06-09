package com.vlad.dto.driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverCreateDto {
    @NotNull
    private Long carrierId;
    @NotBlank
    private String name;
    private String licenseNumber;
    private String phoneNumber;
}
