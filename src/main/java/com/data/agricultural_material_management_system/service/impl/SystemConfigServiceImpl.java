package com.data.agricultural_material_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.agricultural_material_management_system.entity.SystemConfig;
import com.data.agricultural_material_management_system.mapper.SystemConfigMapper;
import com.data.agricultural_material_management_system.service.SystemConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Override
    public SystemConfig getByKey(String configKey) {
        return this.getOne(new QueryWrapper<SystemConfig>().eq("config_key", configKey));
    }

    @Override
    public boolean updateByKey(String configKey, SystemConfig config) {
        return this.update(config, new QueryWrapper<SystemConfig>().eq("config_key", configKey));
    }

    @Override
    public boolean deleteByKey(String configKey) {
        return this.remove(new QueryWrapper<SystemConfig>().eq("config_key", configKey));
    }

    @Override
    public List<SystemConfig> listAll() {
        return this.list();
    }
} 