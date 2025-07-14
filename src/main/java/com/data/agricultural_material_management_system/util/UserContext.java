package com.data.agricultural_material_management_system.util;

import com.data.agricultural_material_management_system.entity.User;

/**
 * 用户上下文工具类
 * 用于存储当前登录用户信息
 */
public class UserContext {
    
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();
    
    /**
     * 设置当前用户
     */
    public static void setCurrentUser(User user) {
        userHolder.set(user);
    }
    
    /**
     * 获取当前用户
     */
    public static User getCurrentUser() {
        return userHolder.get();
    }
    
    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }
    
    /**
     * 清除当前用户
     */
    public static void clear() {
        userHolder.remove();
    }
} 