package com.vlad.dto.driver;

import com.vlad.dto.user.UserReadDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverReadDto {
    private Long id;
    private UserReadDto carrier;
    private String name;
    private String licenseNumber;
    private String phoneNumber;
}
