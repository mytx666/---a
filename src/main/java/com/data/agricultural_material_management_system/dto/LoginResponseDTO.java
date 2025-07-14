package com.data.agricultural_material_management_system.dto;

import com.data.agricultural_material_management_system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class LoginResponseDTO {
    private User user;
    private List<Long> roleIds;
    private String token;
} 