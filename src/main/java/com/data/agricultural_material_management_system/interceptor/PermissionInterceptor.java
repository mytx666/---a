package com.data.agricultural_material_management_system.interceptor;

import com.data.agricultural_material_management_system.annotation.RequiresPermission;
import com.data.agricultural_material_management_system.annotation.RequiresRole;
import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.exception.PermissionDeniedException;
import com.data.agricultural_material_management_system.service.TokenService;
import com.data.agricultural_material_management_system.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 权限拦截器
 * 用于拦截请求并检查权限
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 获取当前用户信息
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)) {
            // 设置当前请求的Token
            setCurrentToken(token);
            
            User user = tokenService.validateToken(token);
            if (user != null) {
                UserContext.setCurrentUser(user);
            }
        }

        // 检查权限注解
        RequiresPermission permissionAnnotation = handlerMethod.getMethodAnnotation(RequiresPermission.class);
        if (permissionAnnotation != null) {
            checkPermission(permissionAnnotation.value());
        }

        // 检查角色注解
        RequiresRole roleAnnotation = handlerMethod.getMethodAnnotation(RequiresRole.class);
        if (roleAnnotation != null) {
            checkRole(roleAnnotation.value());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户上下文和Token
        UserContext.clear();
        clearToken();
    }

    /**
     * 检查权限
     */
    private void checkPermission(String permissionCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new PermissionDeniedException("用户未登录");
        }

        // 从Token中获取权限信息
        String token = getCurrentToken();
        if (token != null) {
            Set<String> permissions = tokenService.getPermissionsFromToken(token);
            if (permissions.contains(permissionCode)) {
                return;
            }
        }

        // 如果Token中没有权限信息，则从数据库查询
        if (!tokenService.hasPermission(token, permissionCode)) {
            throw new PermissionDeniedException("权限不足：" + permissionCode);
        }
    }

    /**
     * 检查角色
     */
    private void checkRole(String roleCode) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new PermissionDeniedException("用户未登录");
        }

        // 从Token中获取角色信息
        String token = getCurrentToken();
        if (token != null) {
            Set<String> roles = tokenService.getRolesFromToken(token);
            if (roles.contains(roleCode)) {
                return;
            }
        }

        // 如果Token中没有角色信息，则从数据库查询
        if (!tokenService.hasRole(token, roleCode)) {
            throw new PermissionDeniedException("角色不足：" + roleCode);
        }
    }
    
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    
    /**
     * 设置当前请求的Token
     */
    public static void setCurrentToken(String token) {
        tokenHolder.set(token);
    }
    
    /**
     * 获取当前请求的Token
     */
    public static String getCurrentToken() {
        return tokenHolder.get();
    }
    
    /**
     * 清理Token
     */
    public static void clearToken() {
        tokenHolder.remove();
    }
} 