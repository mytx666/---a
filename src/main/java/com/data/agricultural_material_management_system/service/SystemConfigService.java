package com.data.agricultural_material_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.agricultural_material_management_system.entity.SystemConfig;

import java.util.List;

public interface SystemConfigService extends IService<SystemConfig> {
    SystemConfig getByKey(String configKey);
    boolean updateByKey(String configKey, SystemConfig config);
    boolean deleteByKey(String configKey);
    List<SystemConfig> listAll();
} 