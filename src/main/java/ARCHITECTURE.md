# 无人机探测仿真系统DDD架构设计

## 系统概述

本系统是一个基于DDD（领域驱动设计）的无人机探测仿真平台，在虚拟空域环境中模拟部署各类探测设备（光电摄像头、电磁波雷达、无线电侦测器、GPS诱导器）和无人机，实现探测设备对无人机的检测仿真。系统严格遵循DDD分层架构，实现业务逻辑与技术实现的清晰分离。

## 业务领域分析

### 核心业务概念
- **空域（Airspace）**：三维仿真空间，包含环境参数和所有实体
- **探测设备（ProbeDevice）**：部署在空域中的各类检测设备
- **无人机（UAV）**：被检测的目标，具有飞行路径和物理特性
- **检测事件（DetectionEvent）**：探测设备发现无人机时产生的事件
- **仿真会话（SimulationSession）**：一次完整的仿真执行过程

### 业务流程
1. **场景初始化**：创建空域，配置环境参数（天气、温度、风向等）
2. **设备部署**：在指定位置部署各类探测设备，配置检测参数
3. **无人机部署**：部署无人机并设置飞行路径
4. **仿真执行**：启动仿真，无人机按路径飞行，设备实时检测
5. **结果分析**：收集检测日志，分析检测效果

## DDD分层架构

### 目录结构
```
src/main/java/com/JP/dronesim/
├── application/                    # 应用层
│   ├── services/                   # 应用服务
│   │   ├── AirspaceManagementAppService.java    # 空域管理应用服务
│   │   ├── DeviceManagementAppService.java      # 设备管理应用服务  
│   │   ├── UAVManagementAppService.java         # 无人机管理应用服务
│   │   ├── SimulationAppService.java            # 仿真控制应用服务
│   │   └── QueryAppService.java                 # 查询应用服务
│   └── dtos/                       # 数据传输对象
│       ├── request/                # 请求DTO
│       ├── response/               # 响应DTO
│       └── event/                  # 事件DTO
├── domain/                         # 领域层
│   ├── airspace/                   # 空域聚合
│   │   ├── model/
│   │   │   ├── Airspace.java                   # 聚合根
│   │   │   ├── EnvironmentParameters.java      # 值对象
│   │   │   └── TimeStep.java                   # 值对象
│   │   └── repository/
│   │       └── IAirspaceRepository.java        # 仓储接口
│   ├── device/                     # 设备聚合
│   │   ├── model/
│   │   │   ├── ProbeDevice.java                # 聚合根
│   │   │   ├── common/
│   │   │   ├── opticalcamera/
│   │   │   ├── radar/
│   │   │   ├── radiodetector/
│   │   │   ├── gpsjammer/
│   │   │   └── events/
│   │   └── repository/
│   │       └── IProbeDeviceRepository.java     # 仓储接口
│   ├── uav/                        # 无人机聚合
│   │   ├── model/
│   │   │   ├── UAV.java                        # 聚合根
│   │   │   ├── Waypoint.java                   # 实体
│   │   │   └── PhysicalProperties.java         # 值对象
│   │   └── repository/
│   │       └── IUAVRepository.java             # 仓储接口
│   ├── common/                     # 通用对象
│   │   ├── valueobjects/           # 通用值对象
│   │   └── enums/                  # 枚举
│   └── services/                   # 领域服务
│       ├── DetectionService.java              # 检测算法服务
│       └── SimulationEngineService.java       # 仿真引擎服务
├── infrastructure/                 # 基础设施层
│   ├── persistence/                # 持久化
│   ├── external/                   # 外部服务
│   ├── realtime/                   # 实时通信
│   ├── config/                     # 配置
│   └── util/                       # 工具类
└── interface/                            # 接口层
    ├── rest/                      # REST接口
    └── websocket/                 # WebSocket接口
```

## 分层职责详述

### 1. 接口层（Interface Layer）
**职责**：处理外部请求，参数验证，响应格式化

**主要组件**：
- **REST控制器**：
  - `SimulationController`：仿真控制（启动/暂停/停止）
  - `AirspaceController`：场景管理（创建/加载/保存）
  - `DeviceController`：设备部署/删除/修改
  - `UavController`：无人机部署/删除/修改
  - `QueryController`：数据查询
- **WebSocket处理器**：
  - `SimulationWebSocketHandler`：实时指令处理
- **异常处理**：
  - `GlobalExceptionHandler`：全局异常处理器

**数据流**：
- 输入：HTTP请求/WebSocket消息
- 输出：标准JSON响应/实时事件推送

### 2. 应用层（Application Layer）
**职责**：业务流程编排，用例协调，事务管理

**应用服务**：
- **AirspaceManagementAppService**：
  - 创建空域场景
  - 加载已保存的场景
  - 保存当前场景配置
  - 更新环境参数
- **DeviceManagementAppService**：
  - 部署探测设备到指定位置
  - 删除已部署的设备
  - 修改设备检测参数
  - 监控设备运行状态
- **UAVManagementAppService**：
  - 部署无人机到空域
  - 设置无人机飞行路径
  - 修改无人机物理特性
  - 控制无人机飞行状态
- **SimulationAppService**：
  - 启动仿真执行
  - 暂停/恢复仿真
  - 停止仿真
  - 控制仿真时间推进
- **QueryAppService**：
  - 查询所有实体状态
  - 获取特定设备详情
  - 检索探测日志
  - 提供实时状态数据

**DTO分类**：
- **请求DTO**：封装前端请求参数
- **响应DTO**：格式化返回数据
- **事件DTO**：实时事件推送数据

### 3. 领域层（Domain Layer）
**职责**：核心业务逻辑，领域模型，业务规则

**聚合设计**：

#### Airspace聚合
- **聚合根**：`Airspace`
  - 管理三维模拟空间
  - 维护环境参数
  - 协调所有实体
- **值对象**：
  - `EnvironmentParameters`：天气、温度、风向量
  - `TimeStep`：仿真时间步长

#### ProbeDevice聚合  
- **聚合根**：`ProbeDevice`
  - 所有探测设备的基类
  - 维护设备状态和检测日志
- **实体**：
  - `OpticalCamera`：光电摄像头
  - `ElectromagneticRadar`：电磁波雷达
  - `RadioDetector`：无线电侦测器
  - `GPSJammer`：GPS诱导器
- **值对象**：
  - 各设备特有参数对象
  - `DetectionEvent`：检测事件信息

#### UAV聚合
- **聚合根**：`UAV`
  - 无人机领域模型
  - 管理飞行状态和路径
- **实体**：
  - `Waypoint`：航点
- **值对象**：
  - `PhysicalProperties`：物理特性

**领域服务**：
- **DetectionService**：执行探测判定逻辑
  - 雷达方程计算
  - 光电识别算法
  - 无线电信号传播模型
- **SimulationEngineService**：仿真引擎
  - 时间推进控制
  - 实体状态同步
  - 检测任务调度

### 4. 基础设施层（Infrastructure Layer）
**职责**：技术实现，外部系统集成，数据持久化

**主要组件**：
- **持久化**：
  - 仓储实现（PostgreSQL）
  - ORM映射模型
- **外部服务**：
  - `AirSimApiClient`：AirSim仿真器集成
- **实时通信**：
  - `WebSocketService`：实时数据推送
- **配置管理**：
  - 数据库配置
  - AirSim连接配置
  - WebSocket服务器配置

## 关键调用链路

### 1. 创建仿真场景并部署设备
```
前端 POST /api/airspace/scenes
  └─> AirspaceController.createScene()
      └─> AirspaceManagementAppService.createAirspace()
          ├─> new Airspace() [领域层]
          ├─> Airspace.initializeEnvironment() [领域层]
          ├─> IAirspaceRepository.save() [基础设施层]
          └─> DeviceManagementAppService.deployDevices()
              ├─> new ProbeDevice() [领域层]
              ├─> AirSimApiClient.deployDevice() [基础设施层]
              └─> IProbeDeviceRepository.save() [基础设施层]
```

### 2. 启动仿真并执行检测
```
前端 POST /api/simulation/start
  └─> SimulationController.startSimulation()
      └─> SimulationAppService.startSimulation()
          ├─> SimulationEngineService.initialize() [领域层]
          ├─> UAVManagementAppService.startUAVMovement()
          │   └─> UAV.startFlight() [领域层]
          └─> DetectionService.startContinuousDetection() [领域层]
              ├─> 计算设备与无人机距离
              ├─> 判断是否在检测范围内
              ├─> 计算检测置信度
              └─> 发布DetectionEvent [领域事件]
```

### 3. 实时状态查询和推送
```
前端 WebSocket连接
  └─> SimulationWebSocketHandler.handleConnection()
      └─> QueryAppService.subscribeRealTimeUpdates()
          ├─> 监听领域事件
          ├─> 查询实体状态
          └─> WebSocketService.broadcast() [基础设施层]
              └─> 推送EntityStateDTO给前端
```

## 数据模型设计

### 数据库表结构
```sql
-- 空域表
CREATE TABLE airspaces (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    environment_config JSONB,
    boundary_config JSONB,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 通用实体表（设备和无人机）
CREATE TABLE entities (
    id UUID PRIMARY KEY,
    airspace_id UUID REFERENCES airspaces(id),
    entity_type VARCHAR(50) NOT NULL, -- 'DEVICE' or 'UAV'
    device_type VARCHAR(50), -- 设备类型
    name VARCHAR(100),
    position JSONB,
    orientation JSONB,
    config JSONB, -- 设备特有配置
    status VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 检测事件表
CREATE TABLE detection_events (
    id UUID PRIMARY KEY,
    detector_id UUID REFERENCES entities(id),
    target_id UUID REFERENCES entities(id),
    detection_time TIMESTAMP,
    confidence DECIMAL(3,2),
    signal_strength DECIMAL(10,4),
    event_data JSONB
);
```

### 领域事件设计
```java
// 位置更新事件
public class UAVPositionUpdateEvent extends DomainEvent {
    private String uavId;
    private Position newPosition;
    private Velocity velocity;
}

// 检测事件
public class DetectionEvent extends DomainEvent {
    private String detectorId;
    private String targetId;
    private double confidence;
    private double signalStrength;
}

// 设备状态变更事件
public class DeviceStatusUpdateEvent extends DomainEvent {
    private String deviceId;
    private DeviceStatus oldStatus;
    private DeviceStatus newStatus;
}
```

## 技术栈

- **框架**：Spring Boot 3.x
- **架构模式**：DDD + 六边形架构
- **数据库**：PostgreSQL 14+
- **ORM**：MyBatis-Plus
- **仿真引擎**：AirSim
- **通信协议**：MessagePack-RPC（AirSim）+ WebSocket（前端）
- **消息传递**：Spring Events（领域事件）
- **API文档**：SpringDoc OpenAPI
- **日志**：SLF4J + Logback
- **测试**：JUnit 5 + Testcontainers

## 扩展性设计

### 1. 新增设备类型
```java
// 在device聚合中添加新的设备实体
public class InfraredCamera extends ProbeDevice {
    private InfraredParameters parameters;
    
    @Override
    public DetectionResult detect(UAV target) {
        // 红外检测算法实现
    }
}
```

### 2. 新增检测算法
```java
// 在领域服务中扩展检测算法
@Component
public class AdvancedDetectionService implements DetectionService {
    public DetectionResult detectWithAI(ProbeDevice device, UAV target) {
        // AI增强的检测算法
    }
}
```

### 3. 支持分布式部署
- 引入消息队列（RabbitMQ/Kafka）
- 实现事件驱动架构
- 支持多实例部署

### 4. 性能优化策略
- Redis缓存热点数据
- 数据库读写分离
- 异步处理检测任务
- 空间索引优化位置查询

## 部署架构

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│   前端应用  │────>│  Spring Boot应用  │────>│   AirSim    │
│  (Vue.js)   │     │   (本系统)       │     │  仿真环境   │
└─────────────┘     └──────────────────┘     └─────────────┘
                            │
                            ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │     数据库      │
                    └─────────────────┘
```

## 质量保证

### 1. 单元测试策略
- 领域模型测试：验证业务规则
- 应用服务测试：验证用例流程
- 仓储测试：使用Testcontainers

### 2. 集成测试
- AirSim集成测试
- 数据库集成测试
- WebSocket通信测试

### 3. 性能测试
- 大量设备部署测试
- 高频检测事件处理
- 并发用户访问测试

## 监控和运维

### 1. 应用监控
- Spring Boot Actuator
- Micrometer指标收集
- 自定义业务指标

### 2. 日志管理
- 结构化日志输出
- 分级日志记录
- 日志聚合分析

### 3. 错误处理
- 全局异常处理
- 业务异常分类
- 错误恢复机制

本架构设计确保了系统的可维护性、可扩展性和业务逻辑的清晰表达，为无人机探测仿真系统提供了坚实的技术基础。 