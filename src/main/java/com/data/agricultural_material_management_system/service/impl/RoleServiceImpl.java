package com.data.agricultural_material_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.agricultural_material_management_system.dto.RoleDTO;
import com.data.agricultural_material_management_system.entity.Permission;
import com.data.agricultural_material_management_system.entity.Role;
import com.data.agricultural_material_management_system.entity.RolePermissionRelation;
import com.data.agricultural_material_management_system.mapper.PermissionMapper;
import com.data.agricultural_material_management_system.mapper.RoleMapper;
import com.data.agricultural_material_management_system.mapper.RolePermissionRelationMapper;
import com.data.agricultural_material_management_system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RolePermissionRelationMapper rolePermissionRelationMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<RoleDTO> listWithPermissions() {
        // 1. 查询所有角色
        List<Role> roles = this.list();

        // 2. 转换为DTO并填充权限信息
        return roles.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);

            // 3. 根据角色ID查询权限ID
            QueryWrapper<RolePermissionRelation> relationQueryWrapper = new QueryWrapper<>();
            relationQueryWrapper.eq("role_id", role.getRoleId());
            List<RolePermissionRelation> relations = rolePermissionRelationMapper.selectList(relationQueryWrapper);
            List<Long> permissionIds = relations.stream().map(RolePermissionRelation::getPermissionId).collect(Collectors.toList());

            if (!permissionIds.isEmpty()) {
                // 4. 根据权限ID查询权限信息
                List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
                roleDTO.setPermissions(permissions);
            }

            return roleDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleWithPermissions(Long id) {
        // 1. 查询角色
        Role role = this.getById(id);
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);

        // 2. 根据角色ID查询权限ID
        QueryWrapper<RolePermissionRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("role_id", role.getRoleId());
        List<RolePermissionRelation> relations = rolePermissionRelationMapper.selectList(relationQueryWrapper);
        List<Long> permissionIds = relations.stream().map(RolePermissionRelation::getPermissionId).collect(Collectors.toList());

        if (!permissionIds.isEmpty()) {
            // 3. 根据权限ID查询权限信息
            List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
            roleDTO.setPermissions(permissions);
        }

        return roleDTO;
    }

    @Override
    @Transactional
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        // 1. 删除角色旧的权限关系
        QueryWrapper<RolePermissionRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        rolePermissionRelationMapper.delete(queryWrapper);

        // 2. 添加角色新的权限关系
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                RolePermissionRelation relation = new RolePermissionRelation();
                relation.setRoleId(roleId);
                relation.setPermissionId(permissionId);
                rolePermissionRelationMapper.insert(relation);
            }
        }
    }
} 