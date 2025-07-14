package com.data.agricultural_material_management_system.service;

import com.data.agricultural_material_management_system.entity.Role;
import java.util.List;

public interface UserRoleService {
    boolean assignRolesToUser(Long userId, List<Long> roleIds);
    List<Role> getRolesByUserId(Long userId);
    List<Long> getUserIdsByRoleId(Long roleId);
} 