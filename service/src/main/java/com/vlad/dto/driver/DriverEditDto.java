package com.vlad.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverEditDto {
    private Long carrierId;
    private String name;
    private String licenseNumber;
    private String phoneNumber;
}
