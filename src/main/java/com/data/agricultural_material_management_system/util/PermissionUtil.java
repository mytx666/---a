package com.data.agricultural_material_management_system.util;

import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.exception.PermissionDeniedException;
import com.data.agricultural_material_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限工具类
 * 提供静态方法检查权限
 */
@Component
public class PermissionUtil {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        PermissionUtil.userService = userService;
    }

    /**
     * 检查当前用户是否有指定权限
     */
    public static void checkPermission(String permissionCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new PermissionDeniedException("用户未登录");
        }

        if (!userService.hasPermission(currentUser.getUserId(), permissionCode)) {
            throw new PermissionDeniedException("权限不足：" + permissionCode);
        }
    }

    /**
     * 检查当前用户是否有指定角色
     */
    public static void checkRole(String roleCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new PermissionDeniedException("用户未登录");
        }

        if (!userService.hasRole(currentUser.getUserId(), roleCode)) {
            throw new PermissionDeniedException("角色不足：" + roleCode);
        }
    }

    /**
     * 检查指定用户是否有指定权限
     */
    public static boolean hasPermission(Long userId, String permissionCode) {
        return userService.hasPermission(userId, permissionCode);
    }

    /**
     * 检查指定用户是否有指定角色
     */
    public static boolean hasRole(Long userId, String roleCode) {
        return userService.hasRole(userId, roleCode);
    }

    /**
     * 检查当前用户是否有指定权限（不抛异常）
     */
    public static boolean hasPermission(String permissionCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return userService.hasPermission(currentUser.getUserId(), permissionCode);
    }

    /**
     * 检查当前用户是否有指定角色（不抛异常）
     */
    public static boolean hasRole(String roleCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return userService.hasRole(currentUser.getUserId(), roleCode);
    }
} 