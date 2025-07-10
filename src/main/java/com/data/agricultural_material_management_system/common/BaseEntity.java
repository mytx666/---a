package com.data.agricultural_material_management_system.common;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体类
 */
@Data
public class BaseEntity {
    
//    @TableId(type = IdType.AUTO)
//    private Long id;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private Integer deleted;
} 