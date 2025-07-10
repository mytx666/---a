package com.data.agricultural_material_management_system.dto;

import com.data.agricultural_material_management_system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends User {
    private List<String> roleCodes;
    private List<Long> roleIds;
} 