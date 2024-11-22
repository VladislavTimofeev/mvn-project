package com.vlad.dto.driver;

import com.vlad.dto.user.UserReadDto;
import lombok.Value;

@Value
public class DriverReadDto {
    Long id;
    UserReadDto carrier;
    String name;
    String licenseNumber;
    String phoneNumber;
}
