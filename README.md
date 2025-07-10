# 农业物资管理系统后端

## 项目简介

农业物资管理系统是一个基于Spring Boot + MyBatis-Plus的现代化物资管理平台，主要用于管理农业相关的物资、供应商、仓库、库存等信息。

## 技术栈

- **框架**: Spring Boot 2.7.14
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus 3.4.3.1
- **工具类**: Hutool 5.8.26, FastJSON 1.2.83
- **验证**: Spring Validation
- **构建工具**: Maven

## 项目结构

```
src/main/java/com/data/agricultural_material_management_system/
├── AgriculturalMaterialManagementSystemApplication.java  # 启动类
├── common/                    # 通用类
│   ├── BaseEntity.java        # 基础实体类
│   ├── PageResult.java        # 分页结果类
│   └── Result.java           # 统一响应结果类
├── config/                    # 配置类
│   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   └── WebConfig.java         # Web配置
├── controller/                # 控制器层
│   ├── MaterialController.java      # 物资管理
│   ├── SupplierController.java      # 供应商管理
│   ├── WarehouseController.java     # 仓库管理
│   ├── InboundOrderController.java  # 入库单管理
│   └── TestController.java          # 测试接口
├── dto/                       # 数据传输对象
│   ├── InboundOrderDTO.java   # 入库单DTO
│   └── OutboundOrderDTO.java  # 出库单DTO
├── entity/                    # 实体类
│   ├── Material.java          # 物资实体
│   ├── Supplier.java          # 供应商实体
│   ├── Warehouse.java         # 仓库实体
│   ├── InboundOrder.java      # 入库单实体
│   ├── InboundOrderDetail.java # 入库单明细实体
│   ├── OutboundOrder.java     # 出库单实体
│   └── OutboundOrderDetail.java # 出库单明细实体
├── exception/                 # 异常处理
│   ├── BusinessException.java # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理器
├── mapper/                    # 数据访问层
│   ├── MaterialMapper.java    # 物资Mapper
│   ├── SupplierMapper.java    # 供应商Mapper
│   ├── WarehouseMapper.java   # 仓库Mapper
│   ├── InboundOrderMapper.java # 入库单Mapper
│   └── InboundOrderDetailMapper.java # 入库单明细Mapper
└── service/                   # 业务逻辑层
    ├── MaterialService.java   # 物资服务接口
    ├── SupplierService.java   # 供应商服务接口
    ├── WarehouseService.java  # 仓库服务接口
    ├── InboundOrderService.java # 入库单服务接口
    └── impl/                  # 服务实现类
        ├── MaterialServiceImpl.java
        ├── SupplierServiceImpl.java
        ├── WarehouseServiceImpl.java
        └── InboundOrderServiceImpl.java
```

## 功能模块

### 1. 基础信息管理
- **物资管理**: 物资信息的增删改查、图片上传、库存预警
- **供应商管理**: 供应商信息管理、状态控制、评估管理
- **仓库管理**: 仓库信息管理、容量监控、使用统计

### 2. 库存管理
- **入库管理**: 采购入库、退货入库、调拨入库
- **出库管理**: 生产领用、销售出库、调拨出库
- **库存查询**: 多条件查询、库存报表、库存预警
- **库存统计**: 库存周转率、库存余额分析

### 3. 财务管理
- **账户管理**: 财务账户信息管理
- **收支记录**: 业务收支记录、关联单据
- **财务统计**: 收支明细、利润分析、成本统计
- **报表导出**: Excel、PDF格式报表导出

### 4. 系统管理
- **用户管理**: 用户信息管理、权限控制
- **角色权限**: 角色定义、权限分配
- **系统配置**: 系统参数配置
- **操作日志**: 操作记录、审计追踪

## 快速开始

### 1. 环境要求
- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE agricultural_material_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行建表SQL（参考项目文档中的数据库设计）

3. 修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/agricultural_material_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 启动项目
```bash
# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run
```

### 4. 测试接口
启动成功后，访问以下接口测试：
- 健康检查: `GET http://localhost:8080/api/test/health`
- 测试接口: `GET http://localhost:8080/api/test/hello`

## API接口文档

### 物资管理接口
- `GET /api/materials/page` - 分页查询物资列表
- `GET /api/materials/{id}` - 根据ID查询物资
- `POST /api/materials` - 新增物资
- `PUT /api/materials/{id}` - 更新物资
- `DELETE /api/materials/{id}` - 删除物资
- `GET /api/materials/stock-warning` - 查询库存预警物资

### 供应商管理接口
- `GET /api/suppliers/page` - 分页查询供应商列表
- `GET /api/suppliers/{id}` - 根据ID查询供应商
- `POST /api/suppliers` - 新增供应商
- `PUT /api/suppliers/{id}` - 更新供应商
- `DELETE /api/suppliers/{id}` - 删除供应商
- `PUT /api/suppliers/{id}/status` - 更新供应商状态

### 仓库管理接口
- `GET /api/warehouses/page` - 分页查询仓库列表
- `GET /api/warehouses/{id}` - 根据ID查询仓库
- `POST /api/warehouses` - 新增仓库
- `PUT /api/warehouses/{id}` - 更新仓库
- `DELETE /api/warehouses/{id}` - 删除仓库
- `GET /api/warehouses/usage` - 获取仓库使用情况

### 入库单管理接口
- `GET /api/inbound-orders/page` - 分页查询入库单列表
- `GET /api/inbound-orders/{id}` - 根据ID查询入库单
- `POST /api/inbound-orders` - 新增入库单
- `PUT /api/inbound-orders/{id}` - 更新入库单
- `DELETE /api/inbound-orders/{id}` - 删除入库单
- `PUT /api/inbound-orders/{id}/approve` - 审核入库单
- `PUT /api/inbound-orders/{id}/confirm` - 确认入库

## 开发计划

### 已完成
- ✅ 项目基础架构搭建
- ✅ 基础信息管理模块（物资、供应商、仓库）
- ✅ 入库单管理模块
- ✅ 统一响应格式和异常处理
- ✅ 跨域配置和MyBatis-Plus配置

### 待开发
- 🔄 出库单管理模块
- 🔄 财务管理模块
- 🔄 用户权限管理模块
- 🔄 系统配置管理
- 🔄 操作日志记录
- 🔄 报表导出功能
- 🔄 文件上传功能完善
- 🔄 单元测试编写

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱: your-email@example.com
- 项目地址: https://github.com/your-username/agricultural-material-management-system 