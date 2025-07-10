-- =====================================================
-- 农业资源管理系统数据库表结构
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4
-- 排序规则：utf8mb4_unicode_ci
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS agricultural_material_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE agricultural_material_management;

-- =====================================================
-- 1. 用户表 (users)
-- =====================================================
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `real_name` VARCHAR(100) NOT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '部门',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 角色表 (roles)
-- =====================================================
CREATE TABLE IF NOT EXISTS `roles` (
    `role_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =====================================================
-- 3. 权限表 (permissions)
-- =====================================================
CREATE TABLE IF NOT EXISTS `permissions` (
    `permission_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '权限描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`permission_id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- =====================================================
-- 4. 用户角色关系表 (user_role_relation)
-- =====================================================
CREATE TABLE IF NOT EXISTS `user_role_relation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关系ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    CONSTRAINT `fk_urr_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_urr_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

-- =====================================================
-- 5. 角色权限关系表 (role_permission_relation)
-- =====================================================
CREATE TABLE IF NOT EXISTS `role_permission_relation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关系ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`),
    CONSTRAINT `fk_rpr_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_rpr_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关系表';

-- =====================================================
-- 6. 图片表 (images)
-- =====================================================
CREATE TABLE IF NOT EXISTS `images` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '图片标题',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '图片描述',
    `file_name` VARCHAR(200) DEFAULT NULL COMMENT '文件名',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) DEFAULT NULL COMMENT '文件类型',
    `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `upload_user_id` BIGINT DEFAULT NULL COMMENT '上传用户ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-删除',
    PRIMARY KEY (`id`),
    KEY `idx_upload_time` (`upload_time`),
    KEY `idx_upload_user_id` (`upload_user_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_images_user_id` FOREIGN KEY (`upload_user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- =====================================================
-- 7. 系统日志表 (system_logs)
-- =====================================================
CREATE TABLE IF NOT EXISTS `system_logs` (
    `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作类型',
    `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
    `params` TEXT DEFAULT NULL COMMENT '请求参数',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '操作状态：1-成功，0-失败',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `execution_time` BIGINT DEFAULT NULL COMMENT '执行时间（毫秒）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`log_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation` (`operation`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_logs_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- =====================================================
-- 8. 操作日志表 (operation_logs)
-- =====================================================
CREATE TABLE IF NOT EXISTS `operation_logs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
    `action` VARCHAR(100) NOT NULL COMMENT '操作动作',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
    `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '操作状态：1-成功，0-失败',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `execution_time` BIGINT DEFAULT NULL COMMENT '执行时间（毫秒）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_module` (`module`),
    KEY `idx_action` (`action`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_op_logs_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =====================================================
-- 9. 系统配置表 (system_configs)
-- =====================================================
CREATE TABLE IF NOT EXISTS `system_configs` (
    `config_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `config_type` VARCHAR(50) DEFAULT 'string' COMMENT '配置类型：string, number, boolean, json',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '配置描述',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统配置：1-是，0-否',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_is_system` (`is_system`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- =====================================================
-- 10. 数据字典表 (data_dictionaries)
-- =====================================================
CREATE TABLE IF NOT EXISTS `data_dictionaries` (
    `dict_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典值',
    `dict_sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`dict_id`),
    UNIQUE KEY `uk_dict_type_value` (`dict_type`, `dict_value`),
    KEY `idx_dict_type` (`dict_type`),
    KEY `idx_dict_sort` (`dict_sort`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入默认用户（密码：123456，BCrypt加密）
INSERT INTO `users` (`username`, `password`, `real_name`, `email`, `department`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', 'admin@example.com', '技术部', 1),
('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '部门经理', 'manager@example.com', '管理部', 1),
('user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '普通用户', 'user@example.com', '业务部', 1);

-- 插入默认角色
INSERT INTO `roles` (`role_name`, `role_code`, `description`, `status`) VALUES
('系统管理员', 'admin', '系统管理员，拥有所有权限', 1),
('部门经理', 'manager', '部门经理，拥有部门管理权限', 1),
('普通用户', 'user', '普通用户，基础操作权限', 1),
('访客', 'guest', '访客，只读权限', 1);

-- 插入默认权限
INSERT INTO `permissions` (`permission_name`, `permission_code`, `description`, `status`) VALUES
-- 用户管理权限
('用户列表', 'user:list', '查看用户列表', 1),
('用户详情', 'user:view', '查看用户详情', 1),
('添加用户', 'user:add', '添加新用户', 1),
('编辑用户', 'user:edit', '编辑用户信息', 1),
('删除用户', 'user:delete', '删除用户', 1),
('分配用户角色', 'user:assign_role', '为用户分配角色', 1),

-- 角色管理权限
('角色列表', 'role:list', '查看角色列表', 1),
('角色详情', 'role:view', '查看角色详情', 1),
('添加角色', 'role:add', '添加新角色', 1),
('编辑角色', 'role:edit', '编辑角色信息', 1),
('删除角色', 'role:delete', '删除角色', 1),
('分配角色权限', 'role:assign_permission', '为角色分配权限', 1),

-- 权限管理权限
('权限列表', 'permission:list', '查看权限列表', 1),
('权限详情', 'permission:view', '查看权限详情', 1),
('添加权限', 'permission:add', '添加新权限', 1),
('编辑权限', 'permission:edit', '编辑权限信息', 1),
('删除权限', 'permission:delete', '删除权限', 1),

-- 系统管理权限
('系统配置', 'system:config', '系统配置管理', 1),
('系统日志', 'system:log', '查看系统日志', 1),
('数据字典', 'system:dict', '数据字典管理', 1),

-- 文件管理权限
('文件上传', 'file:upload', '上传文件', 1),
('文件下载', 'file:download', '下载文件', 1),
('文件删除', 'file:delete', '删除文件', 1),

-- Token管理权限
('Token验证', 'token:validate', '验证Token', 1),
('Token刷新', 'token:refresh', '刷新Token', 1),
('Token检查', 'token:check', '检查Token权限', 1),

-- 管理员所有权限
('管理员所有权限', 'admin:all', '管理员拥有所有权限', 1);

-- 插入用户角色关系
INSERT INTO `user_role_relation` (`user_id`, `role_id`) VALUES
(1, 1), -- admin -> 系统管理员
(2, 2), -- manager -> 部门经理
(3, 3); -- user -> 普通用户

-- 插入角色权限关系
-- 系统管理员拥有所有权限
INSERT INTO `role_permission_relation` (`role_id`, `permission_id`)
SELECT 1, `permission_id` FROM `permissions`;

-- 部门经理权限
INSERT INTO `role_permission_relation` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 2), (2, 7), (2, 8), (2, 13), (2, 14), (2, 19), (2, 20), (2, 21), (2, 22), (2, 23), (2, 24), (2, 25), (2, 26);

-- 普通用户权限
INSERT INTO `role_permission_relation` (`role_id`, `permission_id`) VALUES
(3, 1), (3, 2), (3, 7), (3, 8), (3, 13), (3, 14), (3, 19), (3, 20), (3, 22), (3, 23), (3, 24), (3, 25), (3, 26);

-- 访客权限
INSERT INTO `role_permission_relation` (`role_id`, `permission_id`) VALUES
(4, 1), (4, 7), (4, 13), (4, 22), (4, 23), (4, 24), (4, 25), (4, 26);

-- 插入系统配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_name`, `config_type`, `description`, `is_system`) VALUES
('system.name', '农业资源管理系统', '系统名称', 'string', '系统显示名称', 1),
('system.version', '1.0.0', '系统版本', 'string', '系统版本号', 1),
('system.description', '农业资源管理系统', '系统描述', 'string', '系统描述信息', 1),
('token.expire.time', '1800000', 'Token过期时间', 'number', 'Token过期时间（毫秒）', 1),
('token.refresh.threshold', '300000', 'Token刷新阈值', 'number', 'Token刷新阈值（毫秒）', 1),
('file.upload.max.size', '10485760', '文件上传最大大小', 'number', '文件上传最大大小（字节）', 1),
('file.upload.allowed.types', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx', '允许上传的文件类型', 'string', '允许上传的文件类型', 1),
('system.log.enabled', 'true', '系统日志启用', 'boolean', '是否启用系统日志', 1),
('operation.log.enabled', 'true', '操作日志启用', 'boolean', '是否启用操作日志', 1);

-- 插入数据字典
INSERT INTO `data_dictionaries` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `status`, `remark`) VALUES
-- 用户状态
('user_status', '启用', '1', 1, 1, '用户状态-启用'),
('user_status', '禁用', '0', 2, 1, '用户状态-禁用'),

-- 角色状态
('role_status', '启用', '1', 1, 1, '角色状态-启用'),
('role_status', '禁用', '0', 2, 1, '角色状态-禁用'),

-- 权限状态
('permission_status', '启用', '1', 1, 1, '权限状态-启用'),
('permission_status', '禁用', '0', 2, 1, '权限状态-禁用'),

-- 操作状态
('operation_status', '成功', '1', 1, 1, '操作状态-成功'),
('operation_status', '失败', '0', 2, 1, '操作状态-失败'),

-- 文件状态
('file_status', '正常', '1', 1, 1, '文件状态-正常'),
('file_status', '删除', '0', 2, 1, '文件状态-删除'),

-- 配置类型
('config_type', '字符串', 'string', 1, 1, '配置类型-字符串'),
('config_type', '数字', 'number', 2, 1, '配置类型-数字'),
('config_type', '布尔值', 'boolean', 3, 1, '配置类型-布尔值'),
('config_type', 'JSON', 'json', 4, 1, '配置类型-JSON');

-- =====================================================
-- 创建索引优化
-- =====================================================

-- 用户表索引
CREATE INDEX `idx_users_username` ON `users` (`username`);
CREATE INDEX `idx_users_email` ON `users` (`email`);
CREATE INDEX `idx_users_phone` ON `users` (`phone`);
CREATE INDEX `idx_users_department` ON `users` (`department`);

-- 角色表索引
CREATE INDEX `idx_roles_role_name` ON `roles` (`role_name`);

-- 权限表索引
CREATE INDEX `idx_permissions_permission_name` ON `permissions` (`permission_name`);

-- 图片表索引
CREATE INDEX `idx_images_title` ON `images` (`title`);
CREATE INDEX `idx_images_file_type` ON `images` (`file_type`);

-- 日志表索引
CREATE INDEX `idx_system_logs_operation` ON `system_logs` (`operation`);
CREATE INDEX `idx_system_logs_ip` ON `system_logs` (`ip`);
CREATE INDEX `idx_operation_logs_module` ON `operation_logs` (`module`);
CREATE INDEX `idx_operation_logs_action` ON `operation_logs` (`action`);
CREATE INDEX `idx_operation_logs_ip_address` ON `operation_logs` (`ip_address`);

-- =====================================================
-- 创建视图
-- =====================================================

-- 用户角色视图
CREATE VIEW `v_user_roles` AS
SELECT 
    u.user_id,
    u.username,
    u.real_name,
    u.email,
    u.department,
    u.status as user_status,
    GROUP_CONCAT(r.role_name) as role_names,
    GROUP_CONCAT(r.role_code) as role_codes
FROM users u
LEFT JOIN user_role_relation urr ON u.user_id = urr.user_id
LEFT JOIN roles r ON urr.role_id = r.role_id
GROUP BY u.user_id, u.username, u.real_name, u.email, u.department, u.status;

-- 角色权限视图
CREATE VIEW `v_role_permissions` AS
SELECT 
    r.role_id,
    r.role_name,
    r.role_code,
    r.status as role_status,
    GROUP_CONCAT(p.permission_name) as permission_names,
    GROUP_CONCAT(p.permission_code) as permission_codes
FROM roles r
LEFT JOIN role_permission_relation rpr ON r.role_id = rpr.role_id
LEFT JOIN permissions p ON rpr.permission_id = p.permission_id
GROUP BY r.role_id, r.role_name, r.role_code, r.status;

-- 用户权限视图
CREATE VIEW `v_user_permissions` AS
SELECT 
    u.user_id,
    u.username,
    u.real_name,
    GROUP_CONCAT(DISTINCT p.permission_code) as permissions
FROM users u
LEFT JOIN user_role_relation urr ON u.user_id = urr.user_id
LEFT JOIN role_permission_relation rpr ON urr.role_id = rpr.role_id
LEFT JOIN permissions p ON rpr.permission_id = p.permission_id
WHERE p.permission_code IS NOT NULL
GROUP BY u.user_id, u.username, u.real_name;

-- =====================================================
-- 创建存储过程
-- =====================================================

-- 获取用户权限存储过程
DELIMITER //
CREATE PROCEDURE `GetUserPermissions`(IN p_user_id BIGINT)
BEGIN
    SELECT DISTINCT p.permission_code
    FROM permissions p
    INNER JOIN role_permission_relation rpr ON p.permission_id = rpr.permission_id
    INNER JOIN user_role_relation urr ON rpr.role_id = urr.role_id
    WHERE urr.user_id = p_user_id AND p.status = 1;
END //
DELIMITER ;

-- 获取用户角色存储过程
DELIMITER //
CREATE PROCEDURE `GetUserRoles`(IN p_user_id BIGINT)
BEGIN
    SELECT r.role_code
    FROM roles r
    INNER JOIN user_role_relation urr ON r.role_id = urr.role_id
    WHERE urr.user_id = p_user_id AND r.status = 1;
END //
DELIMITER ;

-- 检查用户权限存储过程
DELIMITER //
CREATE PROCEDURE `CheckUserPermission`(IN p_user_id BIGINT, IN p_permission_code VARCHAR(100), OUT p_has_permission BOOLEAN)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    
    SELECT COUNT(1) INTO v_count
    FROM permissions p
    INNER JOIN role_permission_relation rpr ON p.permission_id = rpr.permission_id
    INNER JOIN user_role_relation urr ON rpr.role_id = urr.role_id
    WHERE urr.user_id = p_user_id 
      AND p.permission_code = p_permission_code 
      AND p.status = 1;
    
    SET p_has_permission = (v_count > 0);
END //
DELIMITER ;

-- =====================================================
-- 创建触发器
-- =====================================================

-- 用户表更新触发器
DELIMITER //
CREATE TRIGGER `tr_users_update` 
BEFORE UPDATE ON `users`
FOR EACH ROW
BEGIN
    SET NEW.update_time = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- 角色表更新触发器
DELIMITER //
CREATE TRIGGER `tr_roles_update` 
BEFORE UPDATE ON `roles`
FOR EACH ROW
BEGIN
    SET NEW.update_time = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- 权限表更新触发器
DELIMITER //
CREATE TRIGGER `tr_permissions_update` 
BEFORE UPDATE ON `permissions`
FOR EACH ROW
BEGIN
    SET NEW.update_time = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- =====================================================
-- 创建事件
-- =====================================================

-- 清理过期日志事件（每天凌晨2点执行）
DELIMITER //
CREATE EVENT `ev_clean_logs`
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    -- 删除30天前的系统日志
    DELETE FROM system_logs WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    -- 删除30天前的操作日志
    DELETE FROM operation_logs WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
END //
DELIMITER ;

-- =====================================================
-- 创建用户和授权
-- =====================================================

-- 创建应用用户
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'AppUser@123456';

-- 授予权限
GRANT SELECT, INSERT, UPDATE, DELETE ON agricultural_material_management.* TO 'app_user'@'%';
GRANT EXECUTE ON PROCEDURE agricultural_material_management.* TO 'app_user'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '数据库表结构创建完成！' as message;
SELECT '默认用户：admin/123456, manager/123456, user/123456' as default_users;
SELECT '请修改默认密码！' as security_notice; 