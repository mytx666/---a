package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.annotation.RequiresPermission;
import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.service.TokenService;
import com.data.agricultural_material_management_system.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Token控制器
 * 提供Token验证和管理功能
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /**
     * 验证Token
     */
    @GetMapping("/validate")
    public Result<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
        User user = tokenService.validateToken(token);
        if (user == null) {
            return Result.error("Token无效或已过期");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("user", user);
        result.put("roles", tokenService.getRolesFromToken(token));
        result.put("permissions", tokenService.getPermissionsFromToken(token));
        
        return Result.success(result);
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = tokenService.refreshToken(token);
        if (newToken == null) {
            return Result.error("Token刷新失败");
        }
        return Result.success(newToken);
    }

    /**
     * 检查Token是否即将过期
     */
    @GetMapping("/expiring")
    public Result<Boolean> isTokenExpiringSoon(@RequestHeader("Authorization") String token) {
        boolean isExpiringSoon = tokenService.isTokenExpiringSoon(token);
        return Result.success(isExpiringSoon);
    }

    /**
     * 检查当前用户权限
     */
    @GetMapping("/check-permission")
    @RequiresPermission("token:check")
    public Result<Map<String, Object>> checkPermission(@RequestParam String permissionCode) {
        String token = getCurrentToken();
        boolean hasPermission = tokenService.hasPermission(token, permissionCode);
        
        Map<String, Object> result = new HashMap<>();
        result.put("permission", permissionCode);
        result.put("hasPermission", hasPermission);
        
        return Result.success(result);
    }

    /**
     * 检查当前用户角色
     */
    @GetMapping("/check-role")
    @RequiresPermission("token:check")
    public Result<Map<String, Object>> checkRole(@RequestParam String roleCode) {
        String token = getCurrentToken();
        boolean hasRole = tokenService.hasRole(token, roleCode);
        
        Map<String, Object> result = new HashMap<>();
        result.put("role", roleCode);
        result.put("hasRole", hasRole);
        
        return Result.success(result);
    }

    /**
     * 获取当前用户的所有权限
     */
    @GetMapping("/permissions")
    @RequiresPermission("token:check")
    public Result<Set<String>> getCurrentUserPermissions() {
        String token = getCurrentToken();
        Set<String> permissions = tokenService.getPermissionsFromToken(token);
        return Result.success(permissions);
    }

    /**
     * 获取当前用户的所有角色
     */
    @GetMapping("/roles")
    @RequiresPermission("token:check")
    public Result<Set<String>> getCurrentUserRoles() {
        String token = getCurrentToken();
        Set<String> roles = tokenService.getRolesFromToken(token);
        return Result.success(roles);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current-user")
    public Result<User> getCurrentUser() {
        User user = UserContext.getCurrentUser();
        if (user == null) {
            return Result.error("用户未登录");
        }
        return Result.success(user);
    }

    /**
     * 获取当前请求的Token
     */
    private String getCurrentToken() {
        // 这里应该从请求头中获取，为了演示，我们从拦截器中获取
        return com.data.agricultural_material_management_system.interceptor.PermissionInterceptor.getCurrentToken();
    }
} 