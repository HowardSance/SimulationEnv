# 无人机仿真系统前端目录包结构设计

## 1. 总体架构说明

前端采用**功能模块化架构**，参考后端DDD分层思想，按业务功能组织代码结构。每个功能模块内部按技术职责分层，确保代码清晰、易维护。

### 1.1 架构原则
- **功能导向**：按业务功能划分模块，而非技术类型
- **职责单一**：每个文件/组件只负责一个明确的功能
- **依赖简单**：避免复杂的模块间依赖关系
- **易于扩展**：新增功能时只需在对应模块内开发

### 1.2 技术分层
- **pages/**: 页面级组件，对应业务用例
- **components/**: 可复用组件，按功能分组
- **services/**: API服务层，对接后端接口
- **store/**: 状态管理，按业务领域分片
- **hooks/**: 自定义Hook，封装业务逻辑
- **types/**: TypeScript类型定义
- **utils/**: 工具函数库

## 2. 完整目录结构

```
frontend/
├── public/                          # 静态资源
│   ├── index.html                   // 主HTML模板
│   ├── favicon.ico                  // 网站图标
│   └── models/                      // 3D模型文件
│       ├── uav-model.glb           // 无人机3D模型
│       ├── radar-model.glb         // 雷达设备3D模型
│       └── camera-model.glb        // 相机设备3D模型
├── src/
│   ├── pages/                       # 页面组件 - 对应业务用例
│   │   ├── AirspaceView/           # 空域视图页面模块
│   │   │   ├── index.tsx           // 空域主页面组件 - 整合3D场景、控制面板和部署功能
│   │   │   ├── components/         // 空域页面专用组件
│   │   │   │   ├── ThreeScene.tsx  // 3D场景组件 - 渲染空域、设备、无人机的3D视图
│   │   │   │   ├── ControlPanel.tsx// 控制面板 - 环境参数调整、仿真控制按钮
│   │   │   │   ├── DeploymentForm.tsx// 部署表单 - 设备/无人机部署参数配置
│   │   │   │   ├── EntityList.tsx  // 实体列表 - 显示已部署的设备和无人机列表
│   │   │   │   ├── PositionSelector.tsx// 位置选择器 - 3D场景中点击选择部署位置
│   │   │   │   └── EnvironmentPanel.tsx// 环境面板 - 天气、时间等环境参数显示和调整
│   │   │   ├── hooks/              // 空域页面专用Hook
│   │   │   │   ├── useThreeScene.ts// 3D场景管理Hook - 处理Three.js场景初始化和交互
│   │   │   │   ├── useEntitySelection.ts// 实体选择Hook - 处理3D场景中实体的选择逻辑
│   │   │   │   ├── useDeployment.ts// 部署逻辑Hook - 处理设备/无人机部署流程
│   │   │   │   └── useEnvironment.ts// 环境管理Hook - 处理环境参数的获取和更新
│   │   │   └── types/              // 空域页面类型定义
│   │   │       ├── airspace.types.ts// 空域相关类型 - Position、Environment等
│   │   │       └── deployment.types.ts// 部署相关类型 - DeploymentParams等
│   │   ├── DeviceMonitor/          # 设备监控页面模块
│   │   │   ├── index.tsx           // 设备监控主页面 - 设备状态总览和管理
│   │   │   ├── components/         // 设备监控专用组件
│   │   │   │   ├── DeviceGrid.tsx  // 设备网格 - 卡片式展示所有设备状态
│   │   │   │   ├── DeviceCard.tsx  // 设备卡片 - 单个设备的状态展示组件
│   │   │   │   ├── DeviceTable.tsx // 设备表格 - 表格形式展示设备详细信息
│   │   │   │   ├── StatusIndicator.tsx// 状态指示器 - 设备运行状态的可视化指示
│   │   │   │   ├── ParameterPanel.tsx// 参数面板 - 设备参数调整界面
│   │   │   │   └── FilterBar.tsx   // 过滤栏 - 设备类型、状态等过滤条件
│   │   │   ├── hooks/              // 设备监控专用Hook
│   │   │   │   ├── useDeviceStatus.ts// 设备状态Hook - 获取和监听设备状态变化
│   │   │   │   ├── useDeviceFilter.ts// 设备过滤Hook - 处理设备列表的过滤逻辑
│   │   │   │   └── useParameterAdjust.ts// 参数调整Hook - 处理设备参数的实时调整
│   │   │   └── types/              // 设备监控类型定义
│   │   │       └── device.types.ts// 设备相关类型 - DeviceStatus、FilterOptions等
│   │   ├── DetectionWindows/       # 探测窗口页面模块
│   │   │   ├── index.tsx           // 探测窗口管理页面 - 管理多个探测窗口的显示
│   │   │   ├── components/         // 探测窗口专用组件
│   │   │   │   ├── WindowManager.tsx// 窗口管理器 - 控制多个探测窗口的打开/关闭
│   │   │   │   ├── RadarWindow.tsx // 雷达窗口 - 雷达扫描界面和控制
│   │   │   │   ├── CameraWindow.tsx// 相机窗口 - 光电相机监控界面
│   │   │   │   ├── RadioWindow.tsx // 无线电窗口 - 无线电探测界面
│   │   │   │   ├── DetectionChart.tsx// 探测图表 - 探测数据的可视化图表
│   │   │   │   └── ControlPanel.tsx// 探测控制面板 - 探测模式、参数控制
│   │   │   ├── hooks/              // 探测窗口专用Hook
│   │   │   │   ├── useDetectionData.ts// 探测数据Hook - 获取和处理探测数据
│   │   │   │   ├── useWindowManager.ts// 窗口管理Hook - 管理探测窗口的状态
│   │   │   │   └── useDetectionMode.ts// 探测模式Hook - 处理自动/手动探测模式切换
│   │   │   └── types/              // 探测窗口类型定义
│   │   │       └── detection.types.ts// 探测相关类型 - DetectionData、ScanMode等
│   │   ├── SimulationControl/      # 仿真控制页面模块
│   │   │   ├── index.tsx           // 仿真控制主页面 - 仿真生命周期管理
│   │   │   ├── components/         // 仿真控制专用组件
│   │   │   │   ├── ControlBar.tsx  // 控制栏 - 启动/暂停/停止等控制按钮
│   │   │   │   ├── StatusPanel.tsx // 状态面板 - 仿真当前状态和统计信息
│   │   │   │   ├── TimeController.tsx// 时间控制器 - 仿真时间步长和速度控制
│   │   │   │   └── ConfigPanel.tsx // 配置面板 - 仿真参数配置界面
│   │   │   ├── hooks/              // 仿真控制专用Hook
│   │   │   │   ├── useSimulation.ts// 仿真Hook - 处理仿真的启动、停止等操作
│   │   │   │   └── useSimulationStatus.ts// 仿真状态Hook - 监听仿真状态变化
│   │   │   └── types/              // 仿真控制类型定义
│   │   │       └── simulation.types.ts// 仿真相关类型 - SimulationStatus等
│   │   └── DataQuery/              # 数据查询页面模块
│   │       ├── index.tsx           // 数据查询主页面 - 历史数据查询和展示
│   │       ├── components/         // 数据查询专用组件
│   │       │   ├── QueryForm.tsx   // 查询表单 - 查询条件设置（时间范围、设备等）
│   │       │   ├── LogTable.tsx    // 日志表格 - 探测日志的表格展示
│   │       │   ├── DataChart.tsx   // 数据图表 - 历史数据的图表可视化
│   │       │   ├── ExportPanel.tsx // 导出面板 - 数据导出功能界面
│   │       │   └── FilterPanel.tsx // 过滤面板 - 高级过滤条件设置
│   │       ├── hooks/              // 数据查询专用Hook
│   │       │   ├── useLogQuery.ts  // 日志查询Hook - 处理探测日志的查询逻辑
│   │       │   ├── useDataExport.ts// 数据导出Hook - 处理数据导出功能
│   │       │   └── useDataFilter.ts// 数据过滤Hook - 处理复杂的数据过滤逻辑
│   │       └── types/              // 数据查询类型定义
│   │           └── query.types.ts  // 查询相关类型 - QueryParams、LogEntry等
│   ├── components/                  # 共享组件库
│   │   ├── Layout/                 # 布局相关组件
│   │   │   ├── AppLayout.tsx       // 应用主布局 - 顶部导航、侧边栏、内容区域
│   │   │   ├── Header.tsx          // 头部组件 - 导航菜单、用户信息、全局操作
│   │   │   ├── Sidebar.tsx         // 侧边栏 - 主导航菜单和功能入口
│   │   │   └── Footer.tsx          // 底部组件 - 版权信息、状态栏等
│   │   ├── Common/                 # 通用基础组件
│   │   │   ├── Loading.tsx         // 加载组件 - 统一的加载状态展示
│   │   │   ├── ErrorBoundary.tsx   // 错误边界 - 全局错误捕获和展示
│   │   │   ├── ConfirmDialog.tsx   // 确认对话框 - 统一的确认操作弹窗
│   │   │   ├── Empty.tsx           // 空状态组件 - 无数据时的友好提示
│   │   │   └── NotFound.tsx        // 404组件 - 页面不存在时的提示
│   │   ├── Charts/                 # 图表组件库
│   │   │   ├── LineChart.tsx       // 折线图 - 时间序列数据展示
│   │   │   ├── BarChart.tsx        // 柱状图 - 分类数据对比展示
│   │   │   ├── PieChart.tsx        // 饼图 - 比例数据展示
│   │   │   ├── ScatterPlot.tsx     // 散点图 - 位置数据展示
│   │   │   └── StatusChart.tsx     // 状态图表 - 设备状态统计图表
│   │   ├── Forms/                  # 表单组件库
│   │   │   ├── FormWrapper.tsx     // 表单包装器 - 统一的表单样式和验证
│   │   │   ├── CoordinateInput.tsx // 坐标输入 - 3D坐标的专用输入组件
│   │   │   ├── AngleInput.tsx      // 角度输入 - 角度参数的专用输入组件
│   │   │   └── DeviceSelector.tsx  // 设备选择器 - 设备类型选择的专用组件
│   │   └── UI/                     # UI基础组件
│   │       ├── Modal.tsx           // 模态框 - 统一的弹窗组件
│   │       ├── Tooltip.tsx         // 提示框 - 鼠标悬停提示组件
│   │       ├── IconButton.tsx      // 图标按钮 - 带图标的按钮组件
│   │       └── StatusBadge.tsx     // 状态徽章 - 状态标识的视觉组件
│   ├── services/                    # API服务层 - 对接后端接口
│   │   ├── api.ts                  // API基础配置 - axios实例配置、拦截器等
│   │   ├── airspace.service.ts     // 空域服务 - 空域管理相关API调用
│   │   ├── device.service.ts       // 设备服务 - 设备管理相关API调用
│   │   ├── uav.service.ts          // 无人机服务 - 无人机管理相关API调用
│   │   ├── simulation.service.ts   // 仿真服务 - 仿真控制相关API调用
│   │   ├── query.service.ts        // 查询服务 - 数据查询相关API调用
│   │   └── websocket.service.ts    // WebSocket服务 - 实时数据通信服务
│   ├── store/                       # 状态管理 - Redux Toolkit
│   │   ├── index.ts                // Store配置 - Redux store的创建和配置
│   │   ├── slices/                 # 状态切片 - 按业务领域分片
│   │   │   ├── airspaceSlice.ts    // 空域状态 - 空域信息、环境参数、实体列表
│   │   │   ├── deviceSlice.ts      // 设备状态 - 设备列表、选中设备、设备窗口
│   │   │   ├── uavSlice.ts         // 无人机状态 - 无人机列表、飞行状态
│   │   │   ├── simulationSlice.ts  // 仿真状态 - 仿真运行状态、时间步长
│   │   │   ├── uiSlice.ts          // UI状态 - 界面状态、弹窗显示、加载状态
│   │   │   └── querySlice.ts       // 查询状态 - 查询条件、查询结果、分页信息
│   │   └── middleware/             # Redux中间件
│   │       ├── websocketMiddleware.ts// WebSocket中间件 - 处理实时数据更新
│   │       └── apiMiddleware.ts    // API中间件 - 统一的API调用处理
│   ├── hooks/                       # 自定义Hooks - 封装业务逻辑
│   │   ├── useWebSocket.ts         // WebSocket Hook - 管理WebSocket连接和消息处理
│   │   ├── useApi.ts               // API Hook - 统一的API调用Hook
│   │   ├── useRealTimeData.ts      // 实时数据Hook - 处理实时数据的订阅和更新
│   │   ├── useLocalStorage.ts      // 本地存储Hook - 浏览器本地存储操作
│   │   ├── useDebounce.ts          // 防抖Hook - 防抖处理Hook
│   │   └── usePolling.ts           // 轮询Hook - 定时数据轮询Hook
│   ├── utils/                       # 工具函数库
│   │   ├── constants.ts            // 常量定义 - 应用中使用的常量
│   │   ├── formatters.ts           // 数据格式化 - 日期、数字、坐标等格式化函数
│   │   ├── validators.ts           // 数据验证 - 表单验证、数据校验函数
│   │   ├── helpers.ts              // 辅助函数 - 通用的辅助工具函数
│   │   ├── coordinate.ts           // 坐标工具 - 坐标系转换、距离计算等
│   │   └── three.utils.ts          // Three.js工具 - 3D相关的工具函数
│   ├── types/                       # TypeScript类型定义
│   │   ├── api.types.ts            // API类型 - 请求响应的数据类型定义
│   │   ├── common.types.ts         // 通用类型 - 全局通用的类型定义
│   │   ├── entity.types.ts         // 实体类型 - 业务实体的类型定义
│   │   ├── ui.types.ts             // UI类型 - 界面相关的类型定义
│   │   └── three.types.ts          // 3D类型 - Three.js相关的类型定义
│   ├── styles/                      # 样式文件
│   │   ├── globals.css             // 全局样式 - 全局CSS变量、重置样式等
│   │   ├── variables.css           // CSS变量 - 颜色、间距、字体等设计变量
│   │   ├── themes.css              // 主题样式 - 明暗主题等样式定义
│   │   └── components.css          // 组件样式 - 自定义组件的样式
│   ├── assets/                      # 静态资源
│   │   ├── images/                 // 图片资源
│   │   │   ├── icons/              // 图标文件
│   │   │   └── backgrounds/        // 背景图片
│   │   └── fonts/                  // 字体文件
│   ├── App.tsx                      # 根组件 - 应用的根组件，配置路由和全局状态
│   ├── main.tsx                     # 入口文件 - 应用启动入口
│   └── vite-env.d.ts               // Vite环境类型 - Vite相关的类型定义
├── package.json                     # 项目配置 - 依赖管理、脚本配置
├── tsconfig.json                    # TypeScript配置 - TS编译配置
├── vite.config.ts                   # Vite配置 - 构建工具配置
├── .eslintrc.json                   # ESLint配置 - 代码规范检查配置
├── .prettierrc                      # Prettier配置 - 代码格式化配置
└── README.md                        # 项目文档 - 项目说明和使用指南
```

## 3. 核心模块详细说明

### 3.1 页面模块 (pages/)

#### 3.1.1 AirspaceView - 空域视图模块
**功能职责**：3D空域环境的展示和实体部署管理
**核心文件**：
- `ThreeScene.tsx`: 使用Three.js渲染3D空域场景
- `DeploymentForm.tsx`: 设备和无人机的部署参数配置
- `useThreeScene.ts`: 管理3D场景的初始化、更新和交互

#### 3.1.2 DeviceMonitor - 设备监控模块
**功能职责**：已部署设备的状态监控和参数管理
**核心文件**：
- `DeviceGrid.tsx`: 卡片网格方式展示设备状态
- `ParameterPanel.tsx`: 设备参数的实时调整界面
- `useDeviceStatus.ts`: 设备状态的获取和实时更新

#### 3.1.3 DetectionWindows - 探测窗口模块
**功能职责**：探测设备的专用操作界面和数据展示
**核心文件**：
- `WindowManager.tsx`: 管理多个探测窗口的显示状态
- `RadarWindow.tsx`: 雷达专用的扫描界面和控制
- `useDetectionData.ts`: 探测数据的获取和处理

### 3.2 服务层 (services/)

#### 3.2.1 API服务
每个服务文件对应后端的一个应用服务：
- `airspace.service.ts` ↔ `AirspaceManagementAppService.java`
- `device.service.ts` ↔ `DeviceManagementAppService.java`
- `simulation.service.ts` ↔ `SimulationAppService.java`
- `query.service.ts` ↔ `QueryAppService.java`

#### 3.2.2 实时通信
- `websocket.service.ts`: 对接后端的`SimulationWebSocketHandler.java`

### 3.3 状态管理 (store/)

#### 3.3.1 状态切片设计
按照后端领域模型划分状态：
- `airspaceSlice.ts`: 对应`Airspace`聚合根
- `deviceSlice.ts`: 对应`AbstractProbeDevice`聚合
- `uavSlice.ts`: 对应`UAV`聚合根
- `simulationSlice.ts`: 对应仿真状态管理

### 3.4 组件库 (components/)

#### 3.4.1 分层原则
- **Layout**: 应用级布局组件
- **Common**: 通用基础组件
- **Charts**: 数据可视化组件
- **Forms**: 表单相关组件
- **UI**: 原子级UI组件

## 4. 开发规范

### 4.1 文件命名规范
- **组件文件**: PascalCase + `.tsx` (如 `DeviceCard.tsx`)
- **Hook文件**: camelCase + `.ts` (如 `useDeviceStatus.ts`)
- **类型文件**: camelCase + `.types.ts` (如 `device.types.ts`)
- **服务文件**: camelCase + `.service.ts` (如 `device.service.ts`)
- **样式文件**: kebab-case + `.css` (如 `device-card.css`)

### 4.2 组件设计规范

#### 4.2.1 页面组件
```typescript
// pages/AirspaceView/index.tsx
/**
 * 空域视图页面
 * 功能：3D空域环境展示、设备部署、实时监控
 */
const AirspaceView: React.FC = () => {
  // 页面级状态管理
  // 子组件组合
  // 业务逻辑处理
};
```

#### 4.2.2 业务组件
```typescript
// pages/AirspaceView/components/DeploymentForm.tsx
/**
 * 设备部署表单组件
 * 功能：设备部署参数配置和提交
 */
interface DeploymentFormProps {
  onDeploy: (params: DeviceDeploymentParams) => void;
  selectedPosition?: Position3D;
}

const DeploymentForm: React.FC<DeploymentFormProps> = ({
  onDeploy,
  selectedPosition
}) => {
  // 表单逻辑
  // 参数验证
  // 提交处理
};
```

#### 4.2.3 自定义Hook
```typescript
// hooks/useDeviceStatus.ts
/**
 * 设备状态管理Hook
 * 功能：获取设备状态、监听状态变化、状态更新
 */
export const useDeviceStatus = (deviceId?: string) => {
  // 状态获取
  // 实时更新
  // 错误处理
  return {
    devices,
    selectedDevice,
    loading,
    error,
    updateDevice,
    refreshDevices
  };
};
```

### 4.3 API服务规范

#### 4.3.1 服务文件结构
```typescript
// services/device.service.ts
/**
 * 设备管理API服务
 * 对接后端DeviceManagementAppService
 */
export const deviceService = {
  // 设备部署
  deployDevice: (airspaceId: string, params: DeviceInitParamsDTO) =>
    api.post<DeviceDetailsDTO>(`/api/airspace/${airspaceId}/devices`, params),
  
  // 获取设备列表
  getDevices: (airspaceId: string) =>
    api.get<DeviceDetailsDTO[]>(`/api/airspace/${airspaceId}/devices`),
  
  // 更新设备参数
  updateDeviceParams: (airspaceId: string, deviceId: string, params: AdjustDeviceParamDTO) =>
    api.put(`/api/airspace/${airspaceId}/devices/${deviceId}/params`, params),
};
```

## 5. 实现优先级

### 5.1 第一阶段：基础框架
1. **项目初始化**
   - 基础目录结构创建
   - 依赖安装和配置
   - 开发环境设置

2. **核心基础组件**
   - Layout组件
   - 基础UI组件
   - API服务配置

3. **状态管理基础**
   - Redux store配置
   - 基础slice创建

### 5.2 第二阶段：核心功能
1. **AirspaceView页面**
   - 3D场景基础渲染
   - 设备部署功能
   - 基础交互

2. **DeviceMonitor页面**
   - 设备列表展示
   - 状态监控
   - 参数调整

3. **实时数据功能**
   - WebSocket连接
   - 状态实时更新

### 5.3 第三阶段：高级功能
1. **DetectionWindows页面**
   - 探测窗口管理
   - 专用探测界面

2. **DataQuery页面**
   - 数据查询功能
   - 历史数据展示

3. **优化和完善**
   - 性能优化
   - 错误处理
   - 用户体验优化

## 6. 技术实现要点

### 6.1 3D渲染架构
```
ThreeScene.tsx
├── SceneManager (场景管理)
├── EntityRenderer (实体渲染)
├── InteractionHandler (交互处理)
└── PerformanceOptimizer (性能优化)
```

### 6.2 状态管理架构
```
Redux Store
├── airspaceSlice (空域状态)
├── deviceSlice (设备状态)
├── uavSlice (无人机状态)
├── simulationSlice (仿真状态)
└── uiSlice (UI状态)
```

### 6.3 实时数据流
```
WebSocket → Middleware → Redux → React Components
```

## 7. 总结

本前端目录包结构设计遵循以下原则：

1. **功能导向**：按业务功能组织代码，便于维护和扩展
2. **职责清晰**：每个文件都有明确的功能职责
3. **依赖简单**：避免复杂的模块间依赖关系
4. **易于开发**：清晰的结构便于团队协作开发
5. **对接规范**：与后端DDD架构保持良好的对应关系

通过这种组织方式，可以确保前端代码的可维护性、可扩展性和开发效率，同时与后端架构保持良好的一致性。