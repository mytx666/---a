package com.data.agricultural_material_management_system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;

    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String department;
    private Integer status;      // 1-启用，0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String avatar;
    // private Integer role;

}
