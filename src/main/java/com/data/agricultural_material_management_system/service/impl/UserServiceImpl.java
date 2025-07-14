package com.data.agricultural_material_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.agricultural_material_management_system.entity.Role;
import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.entity.UserRole;
import com.data.agricultural_material_management_system.entity.Permission;
import com.data.agricultural_material_management_system.entity.RolePermissionRelation;
import com.data.agricultural_material_management_system.mapper.RoleMapper;
import com.data.agricultural_material_management_system.mapper.UserMapper;
import com.data.agricultural_material_management_system.mapper.UserRoleMapper;
import com.data.agricultural_material_management_system.mapper.PermissionMapper;
import com.data.agricultural_material_management_system.mapper.RolePermissionRelationMapper;
import com.data.agricultural_material_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.data.agricultural_material_management_system.dto.UserDTO;
import com.data.agricultural_material_management_system.dto.LoginDTO;
import com.data.agricultural_material_management_system.dto.LoginResponseDTO;
import com.data.agricultural_material_management_system.utill.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RolePermissionRelationMapper rolePermissionRelationMapper;

    @Override
    @Transactional
    public boolean save(User user) {
        // 1. 哈希密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. 保存用户
        boolean saved = super.save(user);
        if (saved) {
            // 2. 查找 "普通用户" 角色
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name", "普通用户");
            Role role = roleMapper.selectOne(queryWrapper);

            if (role != null) {
                // 3. 分配角色
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(role.getRoleId());
                userRoleMapper.insert(userRole);
            } else {
                // 如果找不到角色，可以抛出异常或记录日志
                // 这里我们选择抛出异常，因为这是一个关键的业务逻辑
                throw new RuntimeException("默认角色 '普通用户' 未找到，无法为新用户分配角色。");
            }
        }
        return saved;
    }

    @Override
    public boolean updateById(User user) {
        // 如果密码字段不为空且不为空字符串，则进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null); // 防止将空密码或空字符串存入数据库
        }
        return super.updateById(user);
    }

    @Override
    public List<UserDTO> listWithRoles() {
        // 1. 查询所有用户
        List<User> users = list();

        // 2. 转换为DTO并填充角色信息
        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setUserId(user.getUserId());
            userDTO.setAvatar(user.getAvatar());

            // 3. 根据用户ID查询角色ID
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", user.getUserId());
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            userDTO.setRoleIds(roleIds);

            if (!roleIds.isEmpty()) {
                // 4. 根据角色ID查询角色信息
                QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
                roleQueryWrapper.in("role_id", roleIds);
                List<Role> roles = roleMapper.selectList(roleQueryWrapper);
                List<String> roleCodes = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
                userDTO.setRoleCodes(roleCodes);
            }

            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = this.lambdaQuery()
                .eq(User::getUsername, loginDTO.getUsername())
                .one();

        if (user == null) {
            throw new RuntimeException("用户不存在"); // 用户不存在
        }

        // 2. 验证密码
        boolean passwordValid = false;

        if (user.getPassword() != null && user.getPassword().startsWith("$2a$")) {
            passwordValid = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        } else {
            passwordValid = loginDTO.getPassword().equals(user.getPassword());
            if (passwordValid) {
                user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
                this.updateById(user);
            }
        }

        if (!passwordValid) {
            throw new RuntimeException("密码错误"); // 密码错误
        }

        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用"); // 用户被禁用
        }

        // 3. 查询用户的角色ID和权限
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getUserId());
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        // 4. 获取用户权限和角色编码
        Set<String> permissions = getUserPermissions(user.getUserId());
        Set<String> roles = getUserRoles(user.getUserId());
        
        // 5. 封装返回结果
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setUser(user);
        responseDTO.setRoleIds(roleIds);
        
        // 6. 生成包含角色和权限信息的Token
        String rolesStr = String.join(",", roles);
        String permissionsStr = String.join(",", permissions);
        String token = JwtUtil.createTokenWithRoles(user.getUserId().toString(), user.getUsername(), rolesStr, permissionsStr);
        responseDTO.setToken(token);

        return responseDTO;
    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        // 1. 删除用户旧的角色关系
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        userRoleMapper.delete(queryWrapper);

        // 2. 添加用户新的角色关系
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @Transactional
    public void updateAllPasswordsToEncrypted() {
        // 获取所有用户
        List<User> users = this.list();
        
        for (User user : users) {
            // 检查密码是否已经是加密格式（BCrypt格式通常以$2a$开头）
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                // 如果密码是明文，则加密它
                String encryptedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encryptedPassword);
                this.updateById(user);
            }
        }
    }
    
    @Override
    public Set<String> getUserPermissions(Long userId) {
        // 1. 查询用户的所有角色ID
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        
        if (roleIds.isEmpty()) {
            return Set.of();
        }
        
        // 2. 查询这些角色的所有权限ID
        QueryWrapper<RolePermissionRelation> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.in("role_id", roleIds);
        List<RolePermissionRelation> rolePermissions = rolePermissionRelationMapper.selectList(rolePermissionQueryWrapper);
        List<Long> permissionIds = rolePermissions.stream().map(RolePermissionRelation::getPermissionId).collect(Collectors.toList());
        
        if (permissionIds.isEmpty()) {
            return Set.of();
        }
        
        // 3. 查询权限编码
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.in("permission_id", permissionIds);
        List<Permission> permissions = permissionMapper.selectList(permissionQueryWrapper);
        
        return permissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toSet());
    }
    
    @Override
    public Set<String> getUserRoles(Long userId) {
        // 1. 查询用户的所有角色ID
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        
        if (roleIds.isEmpty()) {
            return Set.of();
        }
        
        // 2. 查询角色编码
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.in("role_id", roleIds);
        List<Role> roles = roleMapper.selectList(roleQueryWrapper);
        
        return roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        Set<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }
    
    @Override
    public boolean hasRole(Long userId, String roleCode) {
        Set<String> roles = getUserRoles(userId);
        return roles.contains(roleCode);
    }
} 
