# 无人机探测仿真系统 (Drone Detection Simulation System)

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://openjdk.java.net/projects/jdk/11/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.14-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 项目简介

无人机探测仿真系统是一个基于DDD（领域驱动设计）架构的高性能仿真平台，用于在虚拟空域环境中模拟各类探测设备对无人机的检测过程。系统支持多种探测设备类型，包括光电摄像头、电磁波雷达、无线电侦测器和GPS诱导器，提供完整的仿真环境管理和实时数据分析功能。

### 🎯 核心特性

- **多设备类型支持**：光电摄像头、电磁波雷达、无线电侦测器、GPS诱导器
- **真实物理仿真**：基于AirSim引擎的精确物理模型
- **实时数据同步**：WebSocket实时推送仿真状态和探测事件
- **高性能空间索引**：八叉树空间索引支持大规模实体管理
- **环境参数模拟**：天气、温度、风向等环境因素影响探测效果
- **事件驱动架构**：松耦合的事件系统支持灵活扩展

## 🏗️ 系统架构

### DDD分层架构

```
src/main/java/com/JP/dronesim/
├── application/                    # 应用层 - 业务流程编排
│   ├── services/                   # 应用服务
│   └── dtos/                       # 数据传输对象
├── domain/                         # 领域层 - 核心业务逻辑
│   ├── airspace/                   # 空域聚合
│   ├── device/                     # 探测设备聚合
│   ├── uav/                        # 无人机聚合
│   ├── common/                     # 通用对象
│   └── services/                   # 领域服务
├── infrastructure/                 # 基础设施层 - 技术实现
│   ├── persistence/                # 数据持久化
│   ├── external/                   # 外部服务集成
│   ├── realtime/                   # 实时通信
│   └── config/                     # 配置管理
└── interface/                      # 接口层 - 外部接口
    ├── rest/                       # REST API
    └── websocket/                  # WebSocket接口
```

### 核心聚合设计

#### 1. 空域聚合 (Airspace)
- **聚合根**：`Airspace` - 管理三维仿真空间和所有实体
- **值对象**：`EnvironmentParameters` - 环境参数
- **职责**：空间索引、实体管理、探测计算、事件分发

#### 2. 探测设备聚合 (ProbeDevice)
- **聚合根**：`ProbeDevice` - 探测设备基类
- **实体**：
  - `OpticalCamera` - 光电摄像头
  - `ElectromagneticRadar` - 电磁波雷达
  - `RadioDetector` - 无线电侦测器
  - `GPSJammer` - GPS诱导器
- **职责**：设备状态管理、探测算法执行、事件生成

#### 3. 无人机聚合 (UAV)
- **聚合根**：`UAV` - 无人机领域模型
- **实体**：`Waypoint` - 航点
- **值对象**：`PhysicalProperties` - 物理特性
- **职责**：飞行路径管理、状态更新、物理特征生成

## 🚀 快速开始

### 环境要求

- **Java**: 11 或更高版本
- **Maven**: 3.6 或更高版本
- **PostgreSQL**: 12 或更高版本
- **AirSim**: 1.7.0 或更高版本（可选，用于物理仿真）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone git@github.com:HowardSance/SimulationEnv.git
   cd SimulationEnv
   ```

2. **配置数据库**
   ```bash
   # 创建数据库
   createdb drone_simulation
   
   # 运行数据库迁移（如果使用Flyway）
   mvn flyway:migrate
   ```

3. **配置应用参数**
   ```bash
   # 复制配置文件模板
   cp src/main/resources/application.yml.example src/main/resources/application.yml
   
   # 编辑配置文件
   vim src/main/resources/application.yml
   ```

4. **编译项目**
   ```bash
   mvn clean compile
   ```

5. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

### 配置文件说明

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drone_simulation
    username: your_username
    password: your_password

# AirSim配置
airsim:
  host: localhost
  port: 41451
  connection:
    timeout: 5000
    retry:
      max-attempts: 3

# 仿真参数
simulation:
  default:
    time-step: 0.01
    max-entities: 100
    airspace:
      width: 1000.0
      height: 1000.0
      depth: 500.0
```

## 📖 开发指南

### 项目结构说明

```
SimulationEnv/
├── src/
│   ├── main/
│   │   ├── java/com/JP/dronesim/
│   │   │   ├── application/           # 应用层
│   │   │   │   ├── services/          # 应用服务
│   │   │   │   └── dtos/              # 数据传输对象
│   │   │   ├── domain/                # 领域层
│   │   │   │   ├── airspace/          # 空域聚合
│   │   │   │   ├── device/            # 探测设备聚合
│   │   │   │   ├── uav/               # 无人机聚合
│   │   │   │   ├── common/            # 通用对象
│   │   │   │   └── services/          # 领域服务
│   │   │   ├── infrastructure/        # 基础设施层
│   │   │   │   ├── persistence/       # 数据持久化
│   │   │   │   ├── external/          # 外部服务
│   │   │   │   ├── realtime/          # 实时通信
│   │   │   │   └── config/            # 配置管理
│   │   │   └── interface/             # 接口层
│   │   │       ├── rest/              # REST API
│   │   │       └── websocket/         # WebSocket接口
│   │   └── resources/
│   │       ├── application.yml        # 应用配置
│   │       └── static/                # 静态资源
│   └── test/                          # 测试代码
├── pom.xml                            # Maven配置
├── README.md                          # 项目说明
└── ARCHITECTURE.md                    # 架构文档
```

### 开发规范

#### 1. 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化配置
- 所有类和方法必须有完整的JavaDoc注释
- 遵循DDD分层架构，严格禁止跨层调用

#### 2. 命名规范
- **类名**：使用PascalCase，如`AirspaceManagementAppService`
- **方法名**：使用camelCase，如`performDetection`
- **常量**：使用UPPER_SNAKE_CASE，如`MAX_DETECTION_RANGE`
- **包名**：使用小写字母，如`com.JP.dronesim.domain`

#### 3. 异常处理
- 使用自定义异常体系
- 在适当的层次处理异常
- 记录关键异常信息
- 不要吞噬异常

#### 4. 测试规范
- 单元测试覆盖率不低于80%
- 使用Spring Boot Test进行集成测试
- 测试方法命名：`test{被测方法}_{测试场景}`

### API文档

#### REST API

##### 空域管理
```http
POST /api/airspace/create          # 创建空域
GET  /api/airspace/{id}            # 获取空域信息
PUT  /api/airspace/{id}/environment # 更新环境参数
```

##### 设备管理
```http
POST /api/devices/deploy            # 部署探测设备
GET  /api/devices/{id}              # 获取设备信息
PUT  /api/devices/{id}/parameters   # 更新设备参数
DELETE /api/devices/{id}            # 移除设备
```

##### 无人机管理
```http
POST /api/uavs/deploy               # 部署无人机
GET  /api/uavs/{id}                 # 获取无人机信息
PUT  /api/uavs/{id}/waypoints       # 设置飞行路径
DELETE /api/uavs/{id}               # 移除无人机
```

##### 仿真控制
```http
POST /api/simulation/start          # 启动仿真
POST /api/simulation/pause          # 暂停仿真
POST /api/simulation/stop           # 停止仿真
POST /api/simulation/step           # 时间步进
```

#### WebSocket API

```javascript
// 连接WebSocket
const ws = new WebSocket('ws://localhost:8080/simulation-websocket');

// 订阅事件
ws.send(JSON.stringify({
    type: 'SUBSCRIBE',
    events: ['SIMULATION_STARTED', 'DETECTION_EVENT', 'UAV_POSITION_UPDATE']
}));

// 接收事件
ws.onmessage = function(event) {
    const data = JSON.parse(event.data);
    console.log('Received event:', data);
};
```

### 扩展开发

#### 添加新的探测设备类型

1. **创建设备实体类**
   ```java
   public class NewDevice extends AbstractProbeDevice {
       // 实现特定探测逻辑
       @Override
       protected List<DetectionEvent> doPerformDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
           // 实现探测算法
       }
   }
   ```

2. **添加设备类型枚举**
   ```java
   public enum DeviceType {
       // 添加新类型
       NEW_DEVICE("NewDevice", "新设备类型");
   }
   ```

3. **创建设备工厂**
   ```java
   public class NewDeviceFactory {
       public static NewDevice createDevice(DeviceInitParamsDTO params) {
           // 创建设备实例
       }
   }
   ```

#### 添加新的环境参数

1. **扩展EnvironmentParameters**
   ```java
   public class EnvironmentParameters {
       // 添加新的环境参数
       private final double newParameter;
   }
   ```

2. **更新AirspaceEnvironment接口**
   ```java
   public interface AirspaceEnvironment {
       // 添加新的环境方法
       double getNewParameter();
   }
   ```

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=AirspaceTest

# 生成测试报告
mvn surefire-report:report
```

### 测试覆盖

```bash
# 生成覆盖率报告
mvn jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html
```

## 📊 监控与运维

### 健康检查

```http
GET /actuator/health
```

### 性能监控

```http
GET /actuator/metrics
GET /actuator/prometheus
```

### 日志配置

```yaml
logging:
  level:
    com.JP.dronesim: INFO
    com.JP.dronesim.domain: DEBUG
  file:
    name: logs/drone-simulation.log
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- **项目维护者**: HowardSance
- **邮箱**: [your-email@example.com]
- **项目地址**: https://github.com/HowardSance/SimulationEnv

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和研究人员。

---

**注意**: 这是一个持续开发的项目，文档会随着功能更新而更新。如有问题，请提交Issue或联系项目维护者。 