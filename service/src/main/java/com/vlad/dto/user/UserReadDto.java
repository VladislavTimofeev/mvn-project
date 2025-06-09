package com.vlad.dto.user;

import com.vlad.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto {
    private Long id;
    private String username;
    private String name;
    private String contactInfo;
    private String address;
    private Role role;
}
