package com.data.agricultural_material_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.dto.UserDTO;
import com.data.agricultural_material_management_system.dto.LoginDTO;
import com.data.agricultural_material_management_system.dto.LoginResponseDTO;

import java.util.List;
import java.util.Set;

public interface UserService extends IService<User> {
    List<UserDTO> listWithRoles();

    LoginResponseDTO login(LoginDTO loginDTO);

    void updateUserRoles(Long userId, List<Long> roleIds);
    
    void updateAllPasswordsToEncrypted();
    
    /**
     * 获取用户的所有权限编码
     */
    Set<String> getUserPermissions(Long userId);
    
    /**
     * 获取用户的所有角色编码
     */
    Set<String> getUserRoles(Long userId);
    
    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);
    
    /**
     * 检查用户是否有指定角色
     */
    boolean hasRole(Long userId, String roleCode);
}