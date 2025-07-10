# RBAC权限控制系统使用指南

## 概述

本系统实现了基于RBAC（Role-Based Access Control）的权限控制，支持用户-角色-权限的三层权限管理。

## 系统架构

### 实体关系
- **User（用户）** ↔ **Role（角色）**：多对多关系，通过UserRole表关联
- **Role（角色）** ↔ **Permission（权限）**：多对多关系，通过RolePermissionRelation表关联
- **User（用户）** → **Permission（权限）**：通过角色间接关联

### 核心组件

1. **注解系统**
   - `@RequiresPermission`：权限控制注解
   - `@RequiresRole`：角色控制注解

2. **拦截器**
   - `PermissionInterceptor`：权限拦截器，自动检查注解权限

3. **工具类**
   - `UserContext`：用户上下文，存储当前登录用户
   - `PermissionUtil`：权限工具类，提供静态方法检查权限

4. **服务层**
   - `UserService`：扩展了权限相关方法

## 使用方法

### 1. 在Controller方法上添加权限注解

```java
@RestController
@RequestMapping("/api")
public class UserController {

    // 需要特定权限
    @GetMapping("/users")
    @RequiresPermission("user:list")
    public Result<List<User>> listUsers() {
        return Result.success(userService.list());
    }

    // 需要特定角色
    @PostMapping("/users")
    @RequiresRole("admin")
    public Result<Boolean> createUser(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    // 同时需要权限和角色
    @DeleteMapping("/users/{id}")
    @RequiresPermission("user:delete")
    @RequiresRole("admin")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }
}
```

### 2. 在代码中动态检查权限

```java
@GetMapping("/dynamic")
public Result<String> dynamicCheck(@RequestParam String action) {
    switch (action) {
        case "read":
            // 检查权限，无权限会抛出异常
            PermissionUtil.checkPermission("data:read");
            return Result.success("有读取权限");
        case "write":
            PermissionUtil.checkPermission("data:write");
            return Result.success("有写入权限");
        default:
            return Result.error("未知操作");
    }
}
```

### 3. 条件性权限检查

```java
@GetMapping("/conditional")
public Result<String> conditional() {
    // 检查是否有管理员权限（不抛异常）
    if (PermissionUtil.hasPermission("admin:all")) {
        return Result.success("管理员可以执行所有操作");
    }
    
    // 检查是否有普通用户权限
    if (PermissionUtil.hasPermission("user:read")) {
        return Result.success("普通用户可以读取数据");
    }
    
    return Result.error("权限不足");
}
```

### 4. 获取当前用户信息

```java
@GetMapping("/current-user")
public Result<String> getCurrentUser() {
    User user = UserContext.getCurrentUser();
    if (user != null) {
        return Result.success("当前用户：" + user.getUsername());
    }
    return Result.error("用户未登录");
}
```

## 权限编码规范

建议使用以下格式的权限编码：

### 模块权限
- `user:list` - 用户列表
- `user:add` - 添加用户
- `user:edit` - 编辑用户
- `user:delete` - 删除用户
- `user:view` - 查看用户详情

### 角色权限
- `role:list` - 角色列表
- `role:add` - 添加角色
- `role:edit` - 编辑角色
- `role:delete` - 删除角色
- `role:assign` - 分配角色

### 权限管理
- `permission:list` - 权限列表
- `permission:assign` - 分配权限

### 系统权限
- `admin:all` - 管理员所有权限
- `system:config` - 系统配置
- `system:log` - 系统日志

## 角色编码规范

建议使用以下角色编码：

- `admin` - 系统管理员
- `manager` - 部门经理
- `user` - 普通用户
- `guest` - 访客

## 数据库初始化

### 1. 创建权限数据

```sql
INSERT INTO permissions (permission_name, permission_code, description, status) VALUES
('用户列表', 'user:list', '查看用户列表', 1),
('添加用户', 'user:add', '添加新用户', 1),
('编辑用户', 'user:edit', '编辑用户信息', 1),
('删除用户', 'user:delete', '删除用户', 1),
('角色列表', 'role:list', '查看角色列表', 1),
('添加角色', 'role:add', '添加新角色', 1),
('编辑角色', 'role:edit', '编辑角色信息', 1),
('删除角色', 'role:delete', '删除角色', 1),
('分配权限', 'role:assign', '为角色分配权限', 1);
```

### 2. 创建角色数据

```sql
INSERT INTO roles (role_name, role_code, description, status) VALUES
('系统管理员', 'admin', '系统管理员，拥有所有权限', 1),
('部门经理', 'manager', '部门经理，拥有部门管理权限', 1),
('普通用户', 'user', '普通用户，基础操作权限', 1);
```

### 3. 为角色分配权限

```sql
-- 为管理员分配所有权限
INSERT INTO role_permission_relation (role_id, permission_id) 
SELECT r.role_id, p.permission_id 
FROM roles r, permissions p 
WHERE r.role_code = 'admin';

-- 为经理分配部分权限
INSERT INTO role_permission_relation (role_id, permission_id) 
SELECT r.role_id, p.permission_id 
FROM roles r, permissions p 
WHERE r.role_code = 'manager' 
AND p.permission_code IN ('user:list', 'user:view', 'role:list');
```

## 配置说明

### 1. 拦截器配置

在 `WebConfig` 中配置拦截器，排除不需要权限检查的路径：

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(permissionInterceptor)
            .addPathPatterns("/**")  // 拦截所有请求
            .excludePathPatterns(    // 排除不需要权限检查的路径
                    "/user/login",   // 登录接口
                    "/user/register", // 注册接口
                    "/error",        // 错误页面
                    "/swagger-ui/**", // Swagger UI
                    "/v3/api-docs/**" // OpenAPI文档
            );
}
```

### 2. 异常处理

系统会自动处理权限异常，返回403状态码和错误信息。

## 注意事项

1. **Token验证**：确保请求头中包含有效的JWT Token
2. **权限缓存**：考虑对用户权限进行缓存，提高性能
3. **权限粒度**：根据业务需求设计合适的权限粒度
4. **安全考虑**：敏感操作建议同时使用权限和角色双重验证

## 扩展功能

### 1. 权限缓存

可以添加Redis缓存来提高权限检查性能：

```java
@Cacheable(value = "user_permissions", key = "#userId")
public Set<String> getUserPermissions(Long userId) {
    // 权限查询逻辑
}
```

### 2. 动态权限

可以实现动态权限配置，支持运行时修改权限：

```java
@EventListener
public void handlePermissionChange(PermissionChangeEvent event) {
    // 清除相关缓存
    cacheManager.getCache("user_permissions").evict(event.getUserId());
}
```

### 3. 权限审计

可以添加权限审计功能，记录权限使用情况：

```java
@Aspect
@Component
public class PermissionAuditAspect {
    @AfterReturning("@annotation(requiresPermission)")
    public void auditPermission(RequiresPermission requiresPermission) {
        // 记录权限使用日志
    }
}
``` 