package com.vlad.dto.filter;

import com.vlad.entity.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserFilterDto {
    String username;
    String name;
    Role role;
}
