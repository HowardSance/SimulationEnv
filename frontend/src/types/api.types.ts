/**
 * API数据类型定义
 * 对应后端DTO结构
 */

// 三维位置
export interface Position {
  x: number;
  y: number;
  z: number;
}

// 三维速度
export interface Velocity {
  vx: number;
  vy: number;
  vz: number;
}

// 三维姿态（欧拉角）
export interface Orientation {
  roll: number;  // 横滚角
  pitch: number; // 俯仰角
  yaw: number;   // 偏航角
}

// 环境参数
export interface EnvironmentParameters {
  temperature: number;      // 温度（摄氏度）
  humidity: number;        // 湿度（百分比）
  windSpeed: number;       // 风速（m/s）
  windDirection: number;   // 风向（度）
  visibility: number;      // 能见度（米）
  pressure: number;        // 大气压（hPa）
  weatherCondition: string; // 天气状况
}

// 实体状态DTO
export interface EntityStateDTO {
  id: string;
  type: string;              // 'RADAR' | 'OPTICAL_CAMERA' | 'RADIO_DETECTOR' | 'UAV'
  name: string;
  position: Position;
  velocity?: Velocity;
  orientation?: Orientation;
  status: string;            // 'ACTIVE' | 'INACTIVE' | 'ERROR' | 'HOVERING' | 'MOVING'
  properties?: Record<string, any>;
  lastUpdateTime: string;
  active: boolean;
  healthStatus?: string;
}

// 空域详情DTO
export interface AirspaceDetailsDTO {
  id: string;
  name: string;
  description?: string;
  boundaryMin: Position;
  boundaryMax: Position;
  environmentParameters: EnvironmentParameters;
  timeStep: number;
  currentTime: number;
  entityCount: number;
  devices: EntityStateDTO[];
  uavs: EntityStateDTO[];
  createdAt: string;
  updatedAt: string;
}

// 设备初始化参数DTO
export interface DeviceInitParamsDTO {
  deviceId: string;
  deviceName: string;
  deviceType: 'RADAR' | 'OPTICAL_CAMERA' | 'RADIO_DETECTOR' | 'GPS_JAMMER';
  position: Position;
  orientation: number;      // 朝向角度
  elevation: number;        // 仰角
  detectionRange: number;   // 探测距离
  fieldOfView?: number;     // 视场角
  sensorParameters?: any;   // 设备特定参数
}

// 雷达参数
export interface RadarParameters {
  frequency: number;        // 工作频率（Hz）
  power: number;           // 发射功率（W）
  gain: number;            // 天线增益（dB）
  noiseFigure: number;     // 噪声系数（dB）
  minSNRDetect: number;    // 最小信噪比（dB）
  scanPattern: 'FIXED' | 'SECTOR' | 'DEGREE_360';
  pulseRepetitionFrequency: number;
  pulseWidth: number;
  beamWidth: number;
  maxUnambiguousRange: number;
  rangeResolution: number;
  velocityResolution: number;
}

// 雷达部署请求
export interface RadarDeploymentRequest {
  deviceId: string;
  deviceName: string;
  position: Position;
  orientation: number;
  elevation: number;
  detectionRange: number;
  customParameters?: RadarParameters;
}

// 环境更新参数DTO
export interface EnvironmentUpdateParamsDTO {
  temperature?: number;
  humidity?: number;
  windSpeed?: number;
  windDirection?: number;
  visibility?: number;
  pressure?: number;
  weatherCondition?: string;
}

// 探测事件DTO（WebSocket推送）
export interface DetectionEventDTO {
  eventId: string;
  timestamp: string;
  detectorId: string;
  detectorName: string;
  detectorType: string;
  detectedUavId: string;
  detectedUavName: string;
  detectedPosition: Position;
  confidence: number;
  detectionDistance: number;
  description: string;
}

// 位置更新事件DTO（WebSocket推送）
export interface PositionUpdateEventDTO {
  entityId: string;
  entityType: string;
  position: Position;
  velocity?: Velocity;
  orientation?: Orientation;
  timestamp: string;
}

// API响应格式
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
} 