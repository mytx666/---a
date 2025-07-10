package com.data.agricultural_material_management_system.dto;

import com.data.agricultural_material_management_system.entity.Permission;
import com.data.agricultural_material_management_system.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends Role {
    private List<Permission> permissions;
} 