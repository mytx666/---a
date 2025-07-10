package com.data.agricultural_material_management_system.service.impl;

import com.data.agricultural_material_management_system.entity.Role;
import com.data.agricultural_material_management_system.service.UserRoleService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Override
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        // TODO: 实现用户分配角色逻辑
        return true;
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        // TODO: 查询用户所有角色
        return null;
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        // TODO: 查询角色下所有用户
        return null;
    }
} 