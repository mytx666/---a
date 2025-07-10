package com.data.agricultural_material_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.Permission;
import com.data.agricultural_material_management_system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/page")
    public Result<IPage<Permission>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Permission> page = permissionService.page(new Page<>(pageNum, pageSize));
        return Result.success(page);
    }

    @GetMapping
    public Result<List<Permission>> list() {
        return Result.success(permissionService.list());
    }

    @GetMapping("/{id}")
    public Result<Permission> getById(@PathVariable Long id) {
        return Result.success(permissionService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Permission permission) {
        return Result.success(permissionService.save(permission));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Permission permission) {
        return Result.success(permissionService.updateById(permission));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(permissionService.removeById(id));
    }
} 