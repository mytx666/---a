package com.data.agricultural_material_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.agricultural_material_management_system.entity.Role;
import com.data.agricultural_material_management_system.dto.RoleDTO;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<RoleDTO> listWithPermissions();
    RoleDTO getRoleWithPermissions(Long id);
    void updateRolePermissions(Long roleId, List<Long> permissionIds);
} 