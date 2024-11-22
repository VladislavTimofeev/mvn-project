package com.vlad.dto.driver;

import lombok.Value;

@Value
public class DriverCreateDto {
    Long carrierId;
    String name;
    String licenseNumber;
    String phoneNumber;
}
