package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.annotation.RequiresPermission;
import com.data.agricultural_material_management_system.annotation.RequiresRole;
import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.util.PermissionUtil;
import com.data.agricultural_material_management_system.util.UserContext;
import org.springframework.web.bind.annotation.*;

/**
 * 示例Controller
 * 演示权限控制的使用方法
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    /**
     * 需要特定权限的接口
     */
    @GetMapping("/admin-only")
    @RequiresPermission("admin:read")
    public Result<String> adminOnly() {
        return Result.success("只有管理员才能访问此接口");
    }

    /**
     * 需要特定角色的接口
     */
    @GetMapping("/manager-only")
    @RequiresRole("manager")
    public Result<String> managerOnly() {
        return Result.success("只有经理角色才能访问此接口");
    }

    /**
     * 在代码中动态检查权限
     */
    @GetMapping("/dynamic-check")
    public Result<String> dynamicCheck(@RequestParam String action) {
        // 根据action参数动态检查权限
        switch (action) {
            case "read":
                PermissionUtil.checkPermission("data:read");
                return Result.success("有读取权限");
            case "write":
                PermissionUtil.checkPermission("data:write");
                return Result.success("有写入权限");
            case "delete":
                PermissionUtil.checkPermission("data:delete");
                return Result.success("有删除权限");
            default:
                return Result.error("未知操作");
        }
    }

    /**
     * 条件性权限检查
     */
    @GetMapping("/conditional")
    public Result<String> conditional() {
        // 检查是否有管理员权限
        if (PermissionUtil.hasPermission("admin:all")) {
            return Result.success("管理员可以执行所有操作");
        }
        
        // 检查是否有普通用户权限
        if (PermissionUtil.hasPermission("user:read")) {
            return Result.success("普通用户可以读取数据");
        }
        
        return Result.error("权限不足");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current-user")
    public Result<String> getCurrentUser() {
        var user = UserContext.getCurrentUser();
        if (user != null) {
            return Result.success("当前用户：" + user.getUsername());
        }
        return Result.error("用户未登录");
    }
} 