# API 接口汇总表

## 📋 接口总览

| 模块 | 接口数量 | 主要功能 |
|------|----------|----------|
| 用户管理 | 9个 | 用户CRUD、登录注册、角色分配 |
| 角色管理 | 7个 | 角色CRUD、权限分配 |
| 权限管理 | 6个 | 权限CRUD |
| Token管理 | 8个 | Token验证、刷新、权限检查 |
| 文件上传 | 1个 | 图片上传 |
| 图片管理 | 5个 | 图片CRUD |

## 🔐 认证接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 用户登录 | POST | `/users/login` | 用户登录获取Token | 无 |
| 用户注册 | POST | `/users/register` | 用户注册 | 无 |
| Token验证 | GET | `/token/validate` | 验证Token有效性 | 无 |
| Token刷新 | POST | `/token/refresh` | 刷新Token | 无 |
| Token过期检查 | GET | `/token/expiring` | 检查Token是否即将过期 | 无 |

## 👥 用户管理接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 获取用户列表(分页) | GET | `/users/page` | 分页获取用户列表 | 需要 |
| 获取用户列表(全部) | GET | `/users` | 获取所有用户(含角色) | 需要 |
| 获取用户详情 | GET | `/users/{id}` | 获取单个用户信息 | 需要 |
| 添加用户 | POST | `/users` | 创建新用户 | 需要 |
| 更新用户 | PUT | `/users/{id}` | 更新用户信息 | 需要 |
| 删除用户 | DELETE | `/users/{id}` | 删除用户 | 需要 |
| 更新用户角色 | PUT | `/users/{userId}/roles` | 为用户分配角色 | 需要 |
| 修改密码 | PUT | `/users/{userId}/password` | 修改用户密码 | 需要 |
| 批量更新密码 | POST | `/users/update-passwords` | 批量加密密码 | 需要 |

## 🎭 角色管理接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 获取角色列表 | GET | `/roles` | 获取所有角色(含权限) | 需要 |
| 获取角色详情 | GET | `/roles/{roleId}` | 获取单个角色信息 | 需要 |
| 获取角色详情(含权限) | GET | `/roles/{id}` | 获取角色及权限信息 | 需要 |
| 添加角色 | POST | `/roles` | 创建新角色 | 需要 |
| 更新角色 | PUT | `/roles/{roleId}` | 更新角色信息 | 需要 |
| 删除角色 | DELETE | `/roles/{roleId}` | 删除角色 | 需要 |
| 更新角色权限 | PUT | `/roles/{roleId}/permissions` | 为角色分配权限 | 需要 |

## 🔑 权限管理接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 获取权限列表(分页) | GET | `/permissions/page` | 分页获取权限列表 | 需要 |
| 获取权限列表(全部) | GET | `/permissions` | 获取所有权限 | 需要 |
| 获取权限详情 | GET | `/permissions/{id}` | 获取单个权限信息 | 需要 |
| 添加权限 | POST | `/permissions` | 创建新权限 | 需要 |
| 更新权限 | PUT | `/permissions` | 更新权限信息 | 需要 |
| 删除权限 | DELETE | `/permissions/{id}` | 删除权限 | 需要 |

## 🔗 角色权限关系接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 获取关系列表 | GET | `/role-permission-relations` | 获取角色权限关系 | `role_permission:list` |
| 添加关系 | POST | `/role-permission-relations` | 添加角色权限关系 | `role_permission:add` |
| 删除关系 | DELETE | `/role-permission-relations/{id}` | 删除角色权限关系 | `role_permission:delete` |
| 批量分配权限 | POST | `/role-permission-relations/assign` | 为角色批量分配权限 | `role_permission:assign` |

## 🛡️ Token管理接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 检查用户权限 | GET | `/token/check-permission` | 检查当前用户权限 | `token:check` |
| 检查用户角色 | GET | `/token/check-role` | 检查当前用户角色 | `token:check` |
| 获取用户权限列表 | GET | `/token/permissions` | 获取当前用户权限 | `token:check` |
| 获取用户角色列表 | GET | `/token/roles` | 获取当前用户角色 | `token:check` |
| 获取当前用户信息 | GET | `/token/current-user` | 获取当前登录用户信息 | 无 |

## 📁 文件管理接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 上传图片 | POST | `/upload/image` | 上传图片到七牛云 | 需要 |
| 获取图片列表 | GET | `/images` | 获取所有图片 | 需要 |
| 获取图片详情 | GET | `/images/{id}` | 获取单个图片信息 | 需要 |
| 添加图片 | POST | `/images` | 添加图片记录 | 需要 |
| 更新图片 | PUT | `/images/{id}` | 更新图片信息 | 需要 |
| 删除图片 | DELETE | `/images/{id}` | 删除图片 | 需要 |

## 🧪 示例接口

| 接口 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| 管理员专用 | GET | `/example/admin-only` | 需要admin:read权限 | `admin:read` |
| 经理专用 | GET | `/example/manager-only` | 需要manager角色 | `manager` |
| 动态权限检查 | GET | `/example/dynamic-check` | 动态检查权限 | 需要 |
| 条件性权限检查 | GET | `/example/conditional` | 条件性权限检查 | 需要 |
| 获取当前用户 | GET | `/example/current-user` | 获取当前用户信息 | 需要 |

## 📊 数据格式

### 用户数据
```json
{
  "userId": 1,
  "username": "admin",
  "realName": "管理员",
  "email": "admin@example.com",
  "phone": "13800138000",
  "department": "技术部",
  "status": 1,
  "avatar": "http://example.com/avatar.jpg"
}
```

### 角色数据
```json
{
  "roleId": 1,
  "roleName": "管理员",
  "roleCode": "admin",
  "description": "系统管理员",
  "status": 1
}
```

### 权限数据
```json
{
  "permissionId": 1,
  "permissionName": "用户列表",
  "permissionCode": "user:list",
  "description": "查看用户列表",
  "status": 1
}
```

### 登录响应
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {...},
    "roleIds": [1, 2]
  }
}
```

## 🔧 请求头格式

### 普通请求
```
Content-Type: application/json
```

### 需要认证的请求
```
Content-Type: application/json
Authorization: Bearer <token>
```

### 文件上传请求
```
Authorization: Bearer <token>
```

## ⚠️ 错误码

| 错误码 | 说明 | 处理方式 |
|--------|------|----------|
| 200 | 成功 | 正常处理 |
| 400 | 请求参数错误 | 检查参数格式 |
| 401 | 未授权 | 重新登录 |
| 403 | 权限不足 | 提示用户无权限 |
| 404 | 资源不存在 | 提示资源不存在 |
| 500 | 服务器错误 | 联系管理员 |

## 🚀 快速开始

### 1. 登录获取Token
```javascript
const login = async (username, password) => {
  const response = await fetch('/users/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  const result = await response.json();
  if (result.code === 200) {
    localStorage.setItem('token', result.data.token);
  }
  return result;
};
```

### 2. 使用Token请求数据
```javascript
const getUsers = async () => {
  const token = localStorage.getItem('token');
  const response = await fetch('/users', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return response.json();
};
```

### 3. 检查权限
```javascript
const checkPermission = async (permissionCode) => {
  const token = localStorage.getItem('token');
  const response = await fetch(`/token/check-permission?permissionCode=${permissionCode}`, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return response.json();
};
```

## 📝 注意事项

1. **Token管理**: 登录后保存Token，请求时自动添加
2. **权限控制**: 前端进行基础权限控制，后端验证是必须的
3. **错误处理**: 统一处理API错误，提供友好提示
4. **数据验证**: 前端进行基础数据验证
5. **Token刷新**: 实现自动Token刷新机制
6. **HTTPS**: 生产环境必须使用HTTPS 