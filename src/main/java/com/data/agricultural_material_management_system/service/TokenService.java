package com.data.agricultural_material_management_system.service;

import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.utill.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Token服务类
 * 提供Token验证和管理功能
 */
@Service
public class TokenService {

    @Autowired
    private UserService userService;

    /**
     * 验证Token并返回用户信息
     *
     * @param token JWT Token
     * @return 用户信息，如果Token无效则返回null
     */
    public User validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证Token
        String userId = JwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return null;
        }

        // 检查Token是否过期
        if (JwtUtil.isTokenExpired(token)) {
            return null;
        }

        // 获取用户信息
        return userService.getById(Long.valueOf(userId));
    }

    /**
     * 验证Token并检查用户是否有指定权限
     *
     * @param token JWT Token
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    public boolean hasPermission(String token, String permissionCode) {
        User user = validateToken(token);
        if (user == null) {
            return false;
        }

        // 首先尝试从Token中获取权限信息
        String permissions = JwtUtil.getPermissionsFromToken(token);
        if (permissions != null && permissions.contains(permissionCode)) {
            return true;
        }

        // 如果Token中没有权限信息，则从数据库查询
        return userService.hasPermission(user.getUserId(), permissionCode);
    }

    /**
     * 验证Token并检查用户是否有指定角色
     *
     * @param token JWT Token
     * @param roleCode 角色编码
     * @return 是否有角色
     */
    public boolean hasRole(String token, String roleCode) {
        User user = validateToken(token);
        if (user == null) {
            return false;
        }

        // 首先尝试从Token中获取角色信息
        String roles = JwtUtil.getRolesFromToken(token);
        if (roles != null && roles.contains(roleCode)) {
            return true;
        }

        // 如果Token中没有角色信息，则从数据库查询
        return userService.hasRole(user.getUserId(), roleCode);
    }

    /**
     * 从Token中获取用户权限
     *
     * @param token JWT Token
     * @return 权限集合
     */
    public Set<String> getPermissionsFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Set.of();
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String permissions = JwtUtil.getPermissionsFromToken(token);
        if (permissions != null) {
            return Set.of(permissions.split(","));
        }

        return Set.of();
    }

    /**
     * 从Token中获取用户角色
     *
     * @param token JWT Token
     * @return 角色集合
     */
    public Set<String> getRolesFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Set.of();
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String roles = JwtUtil.getRolesFromToken(token);
        if (roles != null) {
            return Set.of(roles.split(","));
        }

        return Set.of();
    }

    /**
     * 刷新Token
     *
     * @param token 原始Token
     * @return 新的Token，如果原始Token无效则返回null
     */
    public String refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return JwtUtil.refreshToken(token);
    }

    /**
     * 检查Token是否即将过期（提前5分钟）
     *
     * @param token JWT Token
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        if (!StringUtils.hasText(token)) {
            return true;
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        java.util.Date expiration = JwtUtil.getTokenExpiration(token);
        if (expiration == null) {
            return true;
        }

        // 检查是否在5分钟内过期
        long fiveMinutes = 5 * 60 * 1000;
        return expiration.getTime() - System.currentTimeMillis() < fiveMinutes;
    }
} 