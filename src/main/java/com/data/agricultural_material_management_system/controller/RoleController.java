package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.dto.RoleDTO;
import com.data.agricultural_material_management_system.entity.Role;
import com.data.agricultural_material_management_system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    @Autowired
    private RoleService roleService;

    // 添加角色分类
    @PostMapping
    public Result<Boolean> addRole(@RequestBody Role role) {
        return Result.success(roleService.save(role));
    }

    // 查询全部角色分类
    @GetMapping
    public Result<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roleDTOs = roleService.listWithPermissions();
        return Result.success(roleDTOs);
    }

    // 通过id查询
    @GetMapping("/{id}")
    public Result<RoleDTO> findById(@PathVariable Long id) {
        RoleDTO roleDTO = roleService.getRoleWithPermissions(id);
        if (roleDTO == null) {
            return Result.error("角色不存在");
        }
        return Result.success(roleDTO);
    }

    // 修改
    @PutMapping("/{roleId}")
    public Result<Boolean> updateRole(@PathVariable Long roleId, @RequestBody Role role) {
        role.setRoleId(roleId);
        return Result.success(roleService.updateById(role));
    }

    // 删除
    @DeleteMapping("/{roleId}")
    public Result<Boolean> deleteRole(@PathVariable Long roleId) {
        return Result.success(roleService.removeById(roleId));
    }

    @PutMapping("/{roleId}/permissions")
    public Result updateRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.updateRolePermissions(roleId, permissionIds);
        return Result.success();
    }
} 