package com.data.agricultural_material_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.agricultural_material_management_system.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}