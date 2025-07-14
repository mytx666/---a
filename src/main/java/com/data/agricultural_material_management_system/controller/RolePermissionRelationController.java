package com.data.agricultural_material_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.data.agricultural_material_management_system.annotation.RequiresPermission;
import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.RolePermissionRelation;
import com.data.agricultural_material_management_system.service.RolePermissionRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/role-permission-relations")
public class RolePermissionRelationController {
    @Autowired
    private RolePermissionRelationService rolePermissionRelationService;

    @GetMapping
    @RequiresPermission("role_permission:list")
    public Result<List<RolePermissionRelation>> list(@RequestParam(required = false) Long roleId,
                                                    @RequestParam(required = false) Long permissionId) {
        QueryWrapper<RolePermissionRelation> wrapper = new QueryWrapper<>();
        if (roleId != null) wrapper.eq("role_id", roleId);
        if (permissionId != null) wrapper.eq("permission_id", permissionId);
        return Result.success(rolePermissionRelationService.list(wrapper));
    }

    @PostMapping
    @RequiresPermission("role_permission:add")
    public Result<Boolean> add(@RequestBody RolePermissionRelation relation) {
        return Result.success(rolePermissionRelationService.save(relation));
    }

    @DeleteMapping("/{id}")
    @RequiresPermission("role_permission:delete")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(rolePermissionRelationService.removeById(id));
    }

    @PostMapping("/assign")
    @RequiresPermission("role_permission:assign")
    public Result<Boolean> assignPermissionsToRole(
            @RequestParam Long roleId,
            @RequestBody List<Long> permissionIds) {
        // 先删除该角色原有权限
        rolePermissionRelationService.remove(new QueryWrapper<RolePermissionRelation>().eq("role_id", roleId));
        // 批量插入新权限
        List<RolePermissionRelation> list = permissionIds.stream().map(pid -> {
            RolePermissionRelation rel = new RolePermissionRelation();
            rel.setRoleId(roleId);
            rel.setPermissionId(pid);
            return rel;
        }).toList();
        boolean success = rolePermissionRelationService.saveBatch(list);
        return Result.success(success);
    }
} 