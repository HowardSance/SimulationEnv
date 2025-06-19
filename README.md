# 无人机探测仿真系统架构设计

## 项目概述

本项目是一个基于Spring Boot的无人机探测仿真系统，集成Unreal Engine + AirSim仿真引擎，实现对空域内无人机的多类型探测设备仿真。系统采用事件驱动架构，通过Kafka消息总线实现组件解耦，支持实时态势展示和动态配置管理。

## 1. 业务逻辑架构

### 1.1 核心业务流程

```
配置中心 → 环境容器初始化 → 设备实体创建 → 仿真时钟驱动
    ↓
时间步长推进 → 无人机运动学更新 → 探测设备扫描判定
    ↓
探测事件生成 → Kafka消息发布 → 前端实时展示
    ↓
用户交互 → 参数热更新 → 配置中心同步
```

### 1.2 关键业务实体

- **环境容器(EnvironmentContainer)**: 维护3D空域状态的核心容器
- **探测设备(DetectionDevice)**: 雷达、光电、无线电等多类型探测器
- **无人机实体(DroneEntity)**: 可机动的目标对象
- **探测事件(DetectionEvent)**: 包含距离、方位、俯仰等探测结果
- **仿真时钟(SimulationClock)**: 驱动整个仿真系统的离散时间引擎

### 1.3 探测算法模型

- **电磁波雷达**: 基于雷达方程，考虑发射功率、目标RCS、大气衰减
- **光电摄像头**: 基于视线遮挡、光照条件、目标可见性判定
- **无线电侦测**: 基于信号强度、频谱特征、调制识别

## 2. 项目代码架构

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        Web层                                │
├─────────────────────────────────────────────────────────────┤
│  WebSocket Controller  │  REST API Controller  │  Web UI     │
├─────────────────────────────────────────────────────────────┤
│                       服务层                                │
├─────────────────────────────────────────────────────────────┤
│ SimulationService │ DeviceService │ ConfigService │ EventService │
├─────────────────────────────────────────────────────────────┤
│                       核心引擎层                             │
├─────────────────────────────────────────────────────────────┤
│ EnvironmentEngine │ DetectionEngine │ MovementEngine │ ClockEngine │
├─────────────────────────────────────────────────────────────┤
│                      集成适配层                              │
├─────────────────────────────────────────────────────────────┤
│  AirSim Adapter  │  Kafka Adapter  │  Config Adapter        │
├─────────────────────────────────────────────────────────────┤
│                      基础设施层                              │
└─────────────────────────────────────────────────────────────┘
│     Redis缓存    │    PostgreSQL   │     Kafka MQ           │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 包结构设计

```
com.simulation.drone
├── config/                     # 配置类
│   ├── KafkaConfig.java       # Kafka消息中间件配置
│   ├── RedisConfig.java       # Redis缓存配置
│   ├── WebSocketConfig.java   # WebSocket实时通信配置
│   ├── SwaggerConfig.java     # API文档配置
│   ├── AirSimConfig.java      # AirSim连接配置
│   └── EnvironmentConfig.java # 环境参数配置
├── controller/                 # 控制器层
│   ├── SimulationController.java  # 仿真控制接口
│   ├── DeviceController.java      # 设备管理接口
│   ├── WebSocketController.java   # WebSocket消息处理
│   └── ConfigController.java      # 配置管理接口
├── service/                    # 服务层
│   ├── SimulationService.java     # 仿真逻辑服务
│   ├── DeviceManagementService.java # 设备管理服务
│   ├── ConfigurationService.java    # 配置管理服务
│   ├── EventPublishService.java     # 事件发布服务
│   └── impl/                        # 服务实现类
│       ├── SimulationServiceImpl.java
│       ├── DeviceManagementServiceImpl.java
│       ├── ConfigurationServiceImpl.java
│       └── EventPublishServiceImpl.java
├── engine/                     # 核心引擎
│   ├── EnvironmentEngine.java     # 环境状态管理引擎
│   ├── DetectionEngine.java       # 探测逻辑处理引擎
│   ├── MovementEngine.java        # 运动学计算引擎
│   ├── SimulationEngine.java      # 仿真主引擎
│   └── impl/                      # 引擎实现类
│       ├── DetectionEngineImpl.java
│       └── SimulationEngineImpl.java
├── entity/                     # 实体类
│   ├── simulation/                # 仿真相关实体
│   │   ├── DroneEntity.java         # 无人机实体
│   │   ├── DetectionDevice.java     # 探测设备实体
│   │   └── DetectionEvent.java      # 探测事件实体
│   ├── config/                    # 配置相关实体
│   │   ├── DeviceTemplate.java      # 设备模板配置
│   │   ├── EnvironmentConfig.java   # 环境配置
│   │   └── TargetCharacteristic.java # 目标特征配置
│   └── dto/                       # 数据传输对象
│       ├── core/
│       │   └── SimulationCoreDTO.java  # 仿真核心数据
│       ├── detection/
│       │   └── DetectionResultDTO.java # 探测结果数据
│       ├── device/
│       │   └── DeviceStateDTO.java     # 设备状态数据
│       ├── event/
│       │   └── DetectionEventDTO.java  # 探测事件数据
│       └── metrics/
│           └── PerformanceMetricsDTO.java # 性能指标数据
├── adapter/                    # 外部系统适配
│   ├── airsim/                    # AirSim适配器
│   │   ├── api/                      # API接口定义
│   │   │   ├── RpcLibClientBase.java      # RPC基础客户端接口
│   │   │   ├── CarClientInterface.java    # 车辆控制接口
│   │   │   └── AirSimRpcMessageTrait.java # RPC消息序列化特征
│   │   ├── messages/                 # 消息定义(完整的AirSim消息类型)
│   │   │   ├── Vector3r.java           # 3D向量数据结构
│   │   │   ├── Quaternionr.java        # 四元数数据结构
│   │   │   ├── Pose.java              # 位姿数据结构
│   │   │   ├── KinematicsState.java   # 运动学状态
│   │   │   ├── LidarData.java         # 激光雷达数据
│   │   │   ├── ImuData.java           # IMU传感器数据
│   │   │   ├── DistanceSensorData.java # 距离传感器数据
│   │   │   ├── CarState.java          # 车辆状态数据
│   │   │   ├── CarControls.java       # 车辆控制指令
│   │   │   ├── MultirotorState.java   # 多旋翼无人机状态
│   │   │   ├── CollisionInfo.java     # 碰撞信息
│   │   │   ├── GeoPoint.java          # 地理位置点
│   │   │   ├── RCData.java            # 遥控器数据
│   │   │   ├── GpsData.java           # GPS传感器数据
│   │   │   ├── BarometerData.java     # 气压计数据
│   │   │   ├── MagnetometerData.java  # 磁力计数据
│   │   │   ├── ImageRequest.java      # 图像请求
│   │   │   ├── ImageResponse.java     # 图像响应
│   │   │   └── YawMode.java           # 偏航控制模式
│   │   ├── DroneClientInterface.java   # 无人机客户端接口
│   │   ├── AirSimAdapter.java         # 适配器接口定义
│   │   ├── impl/                     # 适配器实现
│   │   │   └── AirSimAdapterImpl.java  # AirSim适配器实现
│   │   └── example/                  # 使用示例
│   │       └── DroneSimulationExample.java # 无人机仿真示例
│   └── kafka/                     # Kafka适配器
│       ├── EventProducer.java       # 事件生产者
│       └── ConfigConsumer.java      # 配置消费者
├── algorithm/                  # 算法实现
│   ├── detection/                # 探测算法
│   │   ├── RadarAlgorithm.java     # 雷达探测算法
│   │   ├── OpticalAlgorithm.java   # 光电探测算法
│   │   └── RadioAlgorithm.java     # 无线电探测算法
│   └── movement/                 # 运动算法
│       └── KinematicsCalculator.java # 运动学计算器
├── repository/                 # 数据访问层
│   ├── DeviceRepository.java      # 设备数据访问
│   ├── DroneRepository.java       # 无人机数据访问
│   ├── EventRepository.java       # 事件数据访问
│   ├── DeviceTemplateRepository.java # 设备模板数据访问
│   ├── EnvironmentConfigRepository.java # 环境配置数据访问
│   └── TargetCharacteristicRepository.java # 目标特征数据访问
├── util/                      # 工具类
│   ├── GeometryUtils.java       # 几何计算工具
│   ├── MathUtils.java          # 数学计算工具
│   └── ValidationUtils.java    # 数据验证工具
└── DroneSimulationApplication.java # 应用程序入口
```

## 3. 核心模块功能拆解

### 3.1 仿真引擎模块 (Simulation Engine)

**核心职责**: 驱动整个仿真系统运行的时钟引擎

**主要组件**:
- `SimulationClock`: 提供离散时间步长Δt的时钟驱动
- `EnvironmentEngine`: 管理3D空域环境状态
- `MovementEngine`: 处理无人机运动学更新
- `DetectionEngine`: 协调各类探测设备的探测逻辑

**关键功能**:
- 可配置的时间步长控制(支持1ms-1s范围)
- 多线程并行计算支持
- 仿真状态快照与回放
- 性能监控与瓶颈分析

### 3.2 空域环境模块 (Environment Module)

**核心职责**: 维护虚拟3D空间内所有实体的实时状态

**主要组件**:
- `EnvironmentContainer`: 空域状态容器，使用空间索引优化
- `SpatialIndex`: 基于八叉树的3D空间索引
- `WeatherModel`: 气象条件模拟（温度、湿度、大气衰减）
- `TerrainModel`: 地形遮挡计算

**关键功能**:
- 高效的3D空间查询(O(log n)复杂度)
- 动态环境参数更新
- 地理坐标系转换(WGS84 ↔ 笛卡尔坐标)
- 环境状态缓存与持久化

### 3.3 设备实体模块 (Device Entity Module)

**核心职责**: 实现多类型探测设备的建模与仿真

**设备类型**:

#### 3.3.1 电磁波雷达 (RadarDetector)
- **探测原理**: 雷达方程 Pr = (Pt×Gt×Gr×λ²×σ) / ((4π)³×R⁴×L)
- **配置参数**: 发射功率、天线增益、工作频率、波束宽度
- **探测输出**: 距离、方位、俯仰、径向速度、RCS

#### 3.3.2 光电摄像头 (OpticalDetector)
- **探测原理**: 视线遮挡判定 + 光照条件评估
- **配置参数**: 视场角、分辨率、灵敏度、焦距
- **探测输出**: 目标位置、尺寸、可见度评分

#### 3.3.3 无线电侦测器 (RadioDetector)
- **探测原理**: 信号功率检测 + 频谱分析
- **配置参数**: 频率范围、带宽、灵敏度阈值
- **探测输出**: 信号强度、频率、调制类型

**通用特性**:
- 支持设备姿态动态调整(俯仰角、方位角)
- 探测范围可视化计算
- 设备故障与噪声仿真
- 多设备协同探测融合

### 3.4 AirSim集成模块 (AirSim Integration)

**核心职责**: 与UE+AirSim仿真引擎的双向通信，基于msgpack-rpc协议实现

**主要组件**:
- `DroneClientInterface`: 无人机客户端接口，继承RpcLibClientBase
- `AirSimAdapter`: AirSim API封装适配器
- `AirSimAdapterImpl`: 适配器具体实现
- `MessagePackClient`: msgpack-rpc协议客户端
- 完整的消息类型系统(messages包)

**API接口体系**:
```java
// 基础RPC接口
RpcLibClientBase {
    confirmConnection(), reset(), ping()
    simIsPaused(), simPause(), simContinueForTime()
    simGetVehiclePose(), simSetVehiclePose()
    getLidarData(), getImuData(), getDistanceSensorData()
}

// 无人机专用接口
DroneClientInterface extends RpcLibClientBase {
    // 控制相关
    enableApiControl(), armDisarm(), takeoffAsync(), landAsync()
    moveToPositionAsync(), moveByVelocityAsync(), rotateToYawAsync()
    
    // 状态获取
    getMultirotorState(), getPosition(), getVelocity(), getOrientation()
    getGpsData(), getBarometerData(), getMagnetometerData()
    
    // 图像相关
    simGetImages(), getImage()
    
    // 碰撞检测
    simGetCollisionInfo()
}
```

**集成功能**:
- **无人机控制**: 启动/关闭、起飞/降落、精确位置控制、速度控制、姿态控制
- **状态监控**: 实时位置、速度、姿态、运动学状态、碰撞检测
- **传感器数据**: IMU、GPS、气压计、磁力计、距离传感器、激光雷达
- **图像获取**: 多相机、多类型图像(场景、深度、分割等)
- **环境交互**: 仿真控制、对象操作、天气设置

**消息类型系统**:
- **状态类**: `MultirotorState`, `KinematicsState`, `CollisionInfo`
- **传感器类**: `GpsData`, `ImuData`, `BarometerData`, `MagnetometerData`
- **图像类**: `ImageRequest`, `ImageResponse`
- **几何类**: `Vector3r`, `Quaternionr`, `Pose`, `GeoPoint`
- **控制类**: `YawMode`, `CarControls`, `RCData`

**使用示例**:
```java
// 连接初始化
EventLoop loop = EventLoop.defaultEventLoop();
Client client = new Client("127.0.0.1", 41451, loop);
DroneClientInterface droneClient = client.proxy(DroneClientInterface.class);

// 基本控制流程
droneClient.confirmConnection();
droneClient.enableApiControl(true, "");
droneClient.armDisarm(true, "");
droneClient.takeoffAsync(10.0f, "");

// 运动控制
YawMode yawMode = YawMode.createYawMode(0);
droneClient.moveToPositionAsync(10, 0, -10, 5.0f, 30.0f, 0, yawMode, "");

// 获取状态和传感器数据
MultirotorState state = droneClient.getMultirotorState("");
GpsData gps = droneClient.getGpsData("gps", "");
ImageResponse[] images = droneClient.simGetImages(requests, "");
```

### 3.5 消息总线模块 (Message Bus Module)

**核心职责**: 基于Kafka的事件驱动消息通信

**Topic设计**:
- `detection-events`: 探测事件实时发布
- `device-commands`: 设备控制指令
- `config-updates`: 配置更新通知
- `simulation-metrics`: 性能监控数据

**消息格式**:
```json
{
  "eventId": "uuid",
  "timestamp": 1645123456789,
  "deviceId": "radar-001",
  "eventType": "DETECTION",
  "data": {
    "targetId": "drone-123",
    "range": 1500.5,
    "azimuth": 45.2,
    "elevation": 15.8,
    "radialVelocity": 25.3,
    "snr": 12.5,
    "detected": true
  }
}
```

### 3.6 配置管理模块 (Configuration Module)

**核心职责**: 集中化配置管理与热更新

**配置类型**:
- **设备模板配置**: 设备类型、参数范围、默认值
- **环境参数配置**: 气象条件、地形数据、大气模型
- **目标特性配置**: 无人机RCS、运动模式、航迹预设

**YAML配置示例**:
```yaml
devices:
  radar:
    type: "ELECTROMAGNETIC_RADAR"
    parameters:
      transmitPower:
        min: 100
        max: 10000
        default: 1000
        unit: "W"
      frequency:
        min: 1.0e9
        max: 10.0e9
        default: 3.0e9
        unit: "Hz"
      beamWidth:
        horizontal: 2.0
        vertical: 5.0
        unit: "degree"
    
environment:
  weather:
    temperature: 15.0
    humidity: 60.0
    pressure: 1013.25
    visibility: 10000
  atmosphere:
    attenuationModel: "ITU-R P.676"
    scatteringModel: "Rayleigh"
```

### 3.7 Web前端模块 (Web Frontend Module)

**核心职责**: 基于WebGL的实时态势展示界面

**主要功能**:
- **地图底图**: 集成OpenLayers显示地理底图
- **设备可视化**: 探测设备位置、方向、覆盖范围展示
- **无人机轨迹**: 实时轨迹绘制与历史回放
- **探测状态**: 探测事件实时标注与详情展示
- **参数调节**: 设备参数实时调整界面
- **性能监控**: 系统性能指标仪表盘

**技术实现**:
- WebSocket实时数据推送
- Canvas/WebGL高性能渲染
- 响应式布局设计
- 组件化开发架构

## 4. 技术栈选型

### 4.1 后端技术栈

| 分层 | 技术选型 | 版本 | 选型理由 |
|------|----------|------|----------|
| **应用框架** | Spring Boot | 2.7.x | 成熟的企业级框架，丰富的生态 |
| **Web框架** | Spring MVC + WebSocket | - | RESTful API + 实时通信支持 |
| **任务调度** | Spring Scheduler + Quartz | 2.3.x | 高精度定时任务，支持集群 |
| **消息中间件** | Apache Kafka | 3.2.x | 高吞吐量、低延迟的流处理平台 |
| **缓存** | Redis | 7.0.x | 高性能内存缓存，支持复杂数据类型 |
| **数据库** | PostgreSQL + PostGIS | 14.x | 空间数据库，支持地理信息处理 |
| **连接池** | HikariCP | 5.0.x | 高性能数据库连接池 |
| **序列化** | Jackson + MessagePack | - | JSON + 高效二进制序列化 |
| **数学计算** | Apache Commons Math | 3.6.x | 科学计算与统计分析 |
| **空间计算** | JTS Topology Suite | 1.19.x | 几何计算与空间分析 |
| **RPC通信** | msgpack-rpc | 0.7.x | 与AirSim的高效二进制通信 |
| **消息序列化** | MessagePack | 0.9.x | 高性能序列化协议 |
| **配置管理** | Spring Cloud Config | 3.1.x | 分布式配置管理 |
| **监控** | Micrometer + Prometheus | - | 应用性能监控 |

### 4.2 前端技术栈

| 分层 | 技术选型 | 版本 | 选型理由 |
|------|----------|------|----------|
| **基础框架** | Vue.js | 3.2.x | 轻量级、易学习的MVVM框架 |
| **UI组件库** | Element Plus | 2.2.x | 成熟的Vue3组件库 |
| **地图引擎** | OpenLayers | 7.1.x | 功能强大的WebGIS库 |
| **图表库** | ECharts | 5.4.x | 丰富的可视化图表组件 |
| **3D渲染** | Three.js | 0.144.x | WebGL 3D场景渲染 |
| **状态管理** | Pinia | 2.0.x | Vue3官方推荐状态管理 |
| **路由管理** | Vue Router | 4.1.x | 单页应用路由解决方案 |
| **HTTP客户端** | Axios | 0.27.x | Promise based HTTP客户端 |
| **实时通信** | Socket.IO Client | 4.5.x | WebSocket实时通信 |
| **构建工具** | Vite | 3.1.x | 快速的前端构建工具 |

### 4.3 外部系统集成

| 系统 | 协议/接口 | 用途 | 实现状态 |
|------|-----------|------|----------|
| **Unreal Engine + AirSim** | msgpack-rpc over TCP | 3D仿真引擎集成 | ✅ 已完成 |
| **配置中心** | REST API + WebHook | 配置管理与热更新 | 🚧 进行中 |
| **监控告警** | Prometheus + Grafana | 系统监控与告警 | 📋 计划中 |
| **日志收集** | ELK Stack | 日志聚合与分析 | 📋 计划中 |
| **服务发现** | Consul | 微服务注册与发现 | 📋 计划中 |

### 4.4 部署架构

```yaml
# Docker Compose部署配置
services:
  simulation-app:
    image: drone-simulation:latest
    ports:
      - "8080:8080"
      - "8081:8081"  # WebSocket
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
      - REDIS_HOST=redis
      - DB_HOST=postgres
    depends_on:
      - postgres
      - redis
      - kafka
      
  postgres:
    image: postgis/postgis:14-3.2
    environment:
      - POSTGRES_DB=drone_simulation
      - POSTGRES_USER=sim_user
      - POSTGRES_PASSWORD=sim_pass
    volumes:
      - postgres_data:/var/lib/postgresql/data
      
  redis:
    image: redis:7.0-alpine
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data
      
  kafka:
    image: confluentinc/cp-kafka:7.2.0
    environment:
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
      - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
    depends_on:
      - zookeeper
      
  zookeeper:
    image: confluentinc/cp-zookeeper:7.2.0
    environment:
      - ZOOKEEPER_CLIENT_PORT=2181
```

## 5. 性能优化策略

### 5.1 计算性能优化

- **并行计算**: 使用线程池并行处理多设备探测计算
- **空间索引**: 八叉树空间索引提升3D查询效率
- **缓存策略**: Redis缓存热点数据，减少数据库访问
- **算法优化**: 使用快速傅里叶变换(FFT)优化信号处理

### 5.2 内存管理优化

- **对象池**: 重用频繁创建的临时对象
- **分代回收**: 合理配置JVM GC参数
- **数据压缩**: 历史数据压缩存储
- **内存映射**: 大文件使用内存映射技术

### 5.3 网络IO优化

- **连接复用**: HTTP/2 长连接减少握手开销
- **消息批处理**: Kafka批量发送消息提升吞吐量
- **数据压缩**: gzip压缩减少网络传输量
- **CDN加速**: 静态资源CDN分发

## 6. 质量保证

### 6.1 测试策略

- **单元测试**: JUnit5 + Mockito，覆盖率>80%
- **集成测试**: Spring Boot Test测试完整业务流程
- **性能测试**: JMeter压力测试，验证高并发场景
- **端到端测试**: Selenium自动化测试前端功能

### 6.2 监控体系

- **应用监控**: Spring Boot Actuator + Micrometer
- **基础监控**: Prometheus + Grafana监控系统资源
- **业务监控**: 自定义指标监控探测准确率、延迟等
- **日志监控**: ELK Stack实现日志聚合与告警

### 6.3 可用性保证

- **健康检查**: 应用、数据库、中间件健康状态检查
- **故障转移**: 关键组件故障自动切换
- **数据备份**: 定期数据备份与恢复测试
- **降级策略**: 高负载时自动降级非核心功能

## 7. 开发规范

### 7.1 代码规范

- **编码标准**: 遵循阿里巴巴Java开发规范
- **注释规范**: JavaDoc注释覆盖所有公开API
- **异常处理**: 统一异常处理机制
- **日志规范**: 分级日志记录，便于问题定位

### 7.2 API设计规范

```java
// RESTful API设计示例
@RestController
@RequestMapping("/api/v1/devices")
@Api(tags = "探测设备管理")
public class DeviceController {
    
    @GetMapping
    @ApiOperation("获取设备列表")
    public ResponseEntity<PageResult<DeviceDTO>> getDevices(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String type) {
        // 实现逻辑
    }
    
    @PostMapping
    @ApiOperation("创建探测设备")
    public ResponseEntity<DeviceDTO> createDevice(
        @Valid @RequestBody CreateDeviceRequest request) {
        // 实现逻辑
    }
    
    @PutMapping("/{deviceId}/parameters")
    @ApiOperation("热更新设备参数")
    public ResponseEntity<Void> updateDeviceParameters(
        @PathVariable String deviceId,
        @Valid @RequestBody DeviceParametersRequest request) {
        // 实现逻辑
    }
}
```

## 8. 部署运维

### 8.1 环境配置

```yaml
# application-production.yml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:5432/drone_simulation
    username: ${DB_USERNAME:sim_user}
    password: ${DB_PASSWORD:sim_pass}
    hikari:
      maximum-pool-size: 20
      connection-timeout: 30000
      
  redis:
    host: ${REDIS_HOST:localhost}
    port: 6379
    database: 0
    jedis:
      pool:
        max-active: 20
        max-idle: 10
        
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      batch-size: 16384
      linger-ms: 5
      compression-type: gzip
      
simulation:
  clock:
    time-step: 100  # 时间步长(ms)
    max-threads: 8  # 最大计算线程数
  environment:
    spatial-index-depth: 8  # 空间索引深度
    cache-ttl: 300  # 缓存TTL(秒)
    
airsim:
  host: ${AIRSIM_HOST:localhost}
  port: ${AIRSIM_PORT:41451}
  connection-timeout: 5000
```

### 8.2 启动脚本

```bash
#!/bin/bash
# start.sh - 系统启动脚本

# 环境检查
check_dependencies() {
    echo "检查依赖服务..."
    # 检查PostgreSQL
    if ! nc -z $DB_HOST 5432; then
        echo "PostgreSQL连接失败"
        exit 1
    fi
    
    # 检查Redis
    if ! nc -z $REDIS_HOST 6379; then
        echo "Redis连接失败"
        exit 1
    fi
    
    # 检查Kafka
    if ! nc -z $KAFKA_HOST 9092; then
        echo "Kafka连接失败"
        exit 1
    fi
    
    echo "依赖服务检查通过"
}

# 启动应用
start_application() {
    echo "启动无人机探测仿真系统..."
    
    # JVM参数优化
    JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=100"
    JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
    JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=/var/log/drone-simulation/"
    
    # 启动Spring Boot应用
    java $JAVA_OPTS -jar drone-simulation.jar \
        --spring.profiles.active=production \
        --server.port=8080 \
        --management.endpoints.web.exposure.include=health,metrics,prometheus
}

# 主流程
main() {
    check_dependencies
    start_application
}

main "$@"
```

## 9. AirSim适配器重构说明

### 9.1 重构背景

原有的AirSim适配器设计存在以下问题：
- 接口定义不完整，缺少关键的无人机控制功能
- 消息类型不规范，使用通用Map而非强类型
- 与AirSim官方API规范不一致
- 缺少完整的传感器数据支持

### 9.2 重构目标

基于AirSim官方提供的Java客户端示例，重构整个适配器模块：
1. **标准化API接口**：完全遵循AirSim官方API规范
2. **完整消息系统**：实现所有必要的消息类型
3. **类型安全**：使用强类型替代Map传参
4. **功能完整**：支持无人机的全部控制和传感器功能

### 9.3 核心改进

#### 接口设计
```java
// 新的接口继承体系
DroneClientInterface extends RpcLibClientBase {
    // 基础控制
    void enableApiControl(boolean isEnabled, String vehicleName);
    boolean armDisarm(boolean arm, String vehicleName);
    
    // 飞行控制
    boolean takeoffAsync(float timeout, String vehicleName);
    boolean landAsync(float timeout, String vehicleName);
    boolean moveToPositionAsync(float x, float y, float z, float velocity, 
                               float timeout, int drivetrain, YawMode yawMode, String vehicleName);
    
    // 状态获取
    MultirotorState getMultirotorState(String vehicleName);
    Vector3r getPosition(String vehicleName);
    GpsData getGpsData(String gpsName, String vehicleName);
}
```

#### 消息类型系统
- **状态消息**：`MultirotorState`, `CollisionInfo`, `KinematicsState`
- **传感器消息**：`GpsData`, `ImuData`, `BarometerData`, `MagnetometerData`
- **图像消息**：`ImageRequest`, `ImageResponse`
- **几何消息**：`Vector3r`, `Quaternionr`, `Pose`, `GeoPoint`
- **控制消息**：`YawMode`, `RCData`

#### 适配器实现
```java
@Component
public class AirSimAdapterImpl implements AirSimAdapter {
    // msgpack-rpc客户端
    private Client client;
    private DroneClientInterface droneClient;
    
    // 状态缓存
    private Map<String, MultirotorState> droneStateCache;
    private Map<String, Boolean> droneControlStatus;
    
    // 完整的数据转换方法
    private DroneState convertToInternalDroneState(...);
    private Vector3 convertVector3r(Vector3r vector3r);
}
```

### 9.4 使用示例

#### 基本控制流程
```java
// 连接初始化
EventLoop loop = EventLoop.defaultEventLoop();
Client client = new Client("127.0.0.1", 41451, loop);
DroneClientInterface droneClient = client.proxy(DroneClientInterface.class);

// 控制无人机
droneClient.confirmConnection();
droneClient.enableApiControl(true, "");
droneClient.armDisarm(true, "");
droneClient.takeoffAsync(10.0f, "");

// 移动控制
YawMode yawMode = YawMode.createYawMode(0);
droneClient.moveToPositionAsync(10, 0, -10, 5.0f, 30.0f, 0, yawMode, "");
```

#### 传感器数据获取
```java
// 获取完整状态
MultirotorState state = droneClient.getMultirotorState("");

// 获取各类传感器数据
GpsData gps = droneClient.getGpsData("gps", "");
ImuData imu = droneClient.getImuData("imu", "");
BarometerData barometer = droneClient.getBarometerData("barometer", "");

// 获取图像
ImageRequest[] requests = {
    new ImageRequest("front_center", ImageRequest.SCENE, false, true)
};
ImageResponse[] images = droneClient.simGetImages(requests, "");
```

### 9.5 技术优势

1. **API兼容性**：完全兼容AirSim官方Java API
2. **类型安全**：编译时类型检查，减少运行时错误
3. **功能完整**：支持无人机的所有控制和传感器功能
4. **性能优化**：高效的msgpack序列化和连接复用
5. **可维护性**：清晰的接口设计和完善的错误处理

### 9.6 文件结构

```
adapter/airsim/
├── api/                          # 基础API接口
├── messages/                     # 完整消息类型系统(20+类)
├── DroneClientInterface.java     # 核心客户端接口
├── AirSimAdapter.java           # 适配器接口
├── impl/AirSimAdapterImpl.java  # 适配器实现
└── example/                     # 使用示例和测试
```

## 10. 项目路线图

### Phase 1: 核心框架搭建 ✅
- [x] Spring Boot项目初始化
- [x] 数据库设计与建表
- [x] 基础实体类与Repository层
- [x] AirSim连接适配器开发(基于官方API规范)
- [x] 完整的AirSim消息类型系统
- [x] 消息总线Kafka集成

### Phase 2: 仿真引擎开发
- [ ] 环境容器与空间索引实现
- [ ] 仿真时钟与任务调度器
- [ ] 无人机运动学模型
- [ ] 探测设备基础框架
- [ ] 简单探测算法实现

### Phase 3: 探测算法实现
- [ ] 电磁波雷达探测算法
- [ ] 光电摄像头探测算法  
- [ ] 无线电侦测算法
- [ ] 多设备协同探测
- [ ] 探测精度验证与调优

### Phase 4: 前端界面开发
- [ ] Vue3前端项目搭建
- [ ] 地图底图与设备可视化
- [ ] 实时数据展示与交互
- [ ] 设备参数调节界面
- [ ] 性能监控仪表盘

### Phase 5: 系统集成测试
- [ ] 端到端功能测试
- [ ] 性能压力测试
- [ ] 配置管理验证
- [ ] 故障恢复测试
- [ ] 文档完善与部署
