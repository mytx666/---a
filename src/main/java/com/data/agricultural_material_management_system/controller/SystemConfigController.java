package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.SystemConfig;
import com.data.agricultural_material_management_system.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system-configs")
public class SystemConfigController {
    @Autowired
    private SystemConfigService systemConfigService;

    // 获取所有配置
    @GetMapping
    public Result<List<SystemConfig>> list() {
        return Result.success(systemConfigService.listAll());
    }

    // 根据key获取配置
    @GetMapping("/{configKey}")
    public Result<SystemConfig> getByKey(@PathVariable String configKey) {
        return Result.success(systemConfigService.getByKey(configKey));
    }

    // 新增配置
    @PostMapping
    public Result<Boolean> add(@RequestBody SystemConfig config) {
        return Result.success(systemConfigService.save(config));
    }

    // 修改配置
    @PutMapping("/{configKey}")
    public Result<Boolean> update(@PathVariable String configKey, @RequestBody SystemConfig config) {
        return Result.success(systemConfigService.updateByKey(configKey, config));
    }

    // 删除配置
    @DeleteMapping("/{configKey}")
    public Result<Boolean> delete(@PathVariable String configKey) {
        return Result.success(systemConfigService.deleteByKey(configKey));
    }
} 