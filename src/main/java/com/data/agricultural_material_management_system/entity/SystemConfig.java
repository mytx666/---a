package com.data.agricultural_material_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("system_configs")
public class SystemConfig {
    @TableId(type = IdType.AUTO)
    private Long configId;
    private String configKey;
    private String configValue;
    private String configName;
    private String configType;
    private String description;
    private Integer isSystem;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 