/**
 * 设备管理API服务
 * 对接后端DeviceManagementAppService
 */

import { http } from './api';
import { 
  DeviceInitParamsDTO,
  DeviceDetailsDTO,
  AdjustDeviceParamDTO,
  RadarDeploymentRequest,
  EntityStateDTO 
} from '../types/api.types';

// 定义设备参数调整DTO（如果后端没有定义）
export interface AdjustDeviceParamDTO {
  parameterName: string;
  parameterValue: any;
}

// 定义设备详情DTO（如果后端没有定义）
export interface DeviceDetailsDTO extends EntityStateDTO {
  deviceType: string;
  detectionRange: number;
  fieldOfView?: number;
  sensorParameters?: any;
  detectionLogs?: any[];
}

// 目标点
export interface RadarContactDTO {
  contactId: string;
  detectionTime: string;
  radarId: string;
  targetId: string;
  range: number; // 距离
  azimuth: number; // 方位角
  elevation: number;
  radialVelocity: number;
  confidence: number;
  classification: string;
}

/**
 * 设备服务类
 */
export const deviceService = {
  /**
   * 部署探测设备
   * POST /api/airspace/{airspaceId}/devices
   */
  deployDevice: (airspaceId: string, params: DeviceInitParamsDTO) =>
    http.post<DeviceDetailsDTO>(`/api/airspace/${airspaceId}/devices`, params),

  /**
   * 批量部署设备
   * POST /api/airspace/{airspaceId}/devices/batch
   */
  batchDeployDevices: (airspaceId: string, deviceList: DeviceInitParamsDTO[]) =>
    http.post<DeviceDetailsDTO[]>(`/api/airspace/${airspaceId}/devices/batch`, deviceList),

  /**
   * 获取设备详情
   * GET /api/airspace/{airspaceId}/devices/{deviceId}
   */
  getDevice: (airspaceId: string, deviceId: string) =>
    http.get<DeviceDetailsDTO>(`/api/airspace/${airspaceId}/devices/${deviceId}`),

  /**
   * 获取空域内所有设备
   * GET /api/airspace/{airspaceId}/devices
   */
  getAllDevices: (airspaceId: string) =>
    http.get<DeviceDetailsDTO[]>(`/api/airspace/${airspaceId}/devices`),

  /**
   * 根据设备类型获取设备列表
   * GET /api/airspace/{airspaceId}/devices/type/{deviceType}
   */
  getDevicesByType: (airspaceId: string, deviceType: string) =>
    http.get<DeviceDetailsDTO[]>(`/api/airspace/${airspaceId}/devices/type/${deviceType}`),

  /**
   * 更新设备参数
   * PUT /api/airspace/{airspaceId}/devices/{deviceId}/params
   */
  updateDeviceParams: (airspaceId: string, deviceId: string, params: AdjustDeviceParamDTO) =>
    http.put<string>(`/api/airspace/${airspaceId}/devices/${deviceId}/params`, params),

  /**
   * 激活设备
   * POST /api/airspace/{airspaceId}/devices/{deviceId}/activate
   */
  activateDevice: (airspaceId: string, deviceId: string) =>
    http.post<string>(`/api/airspace/${airspaceId}/devices/${deviceId}/activate`),

  /**
   * 停用设备
   * POST /api/airspace/{airspaceId}/devices/{deviceId}/deactivate
   */
  deactivateDevice: (airspaceId: string, deviceId: string) =>
    http.post<string>(`/api/airspace/${airspaceId}/devices/${deviceId}/deactivate`),

  /**
   * 删除设备
   * DELETE /api/airspace/{airspaceId}/devices/{deviceId}
   */
  removeDevice: (airspaceId: string, deviceId: string) =>
    http.delete<string>(`/api/airspace/${airspaceId}/devices/${deviceId}`),

  /**
   * 获取设备探测日志
   * GET /api/airspace/{airspaceId}/devices/{deviceId}/logs
   */
  getDeviceLogs: (airspaceId: string, deviceId: string, limit: number = 100) =>
    http.get<any>(`/api/airspace/${airspaceId}/devices/${deviceId}/logs`, { limit }),

  /**
   * 部署雷达设备（专用方法）
   */
  deployRadar: (airspaceId: string, radarParams: RadarDeploymentRequest) => {
    const deviceParams: DeviceInitParamsDTO = {
      deviceId: radarParams.deviceId,
      deviceName: radarParams.deviceName,
      deviceType: 'RADAR',
      position: radarParams.position,
      orientation: radarParams.orientation,
      elevation: radarParams.elevation,
      detectionRange: radarParams.detectionRange,
      fieldOfView: 120, // 默认120度扇形扫描
      sensorParameters: radarParams.customParameters
    };
    
    return deviceService.deployDevice(airspaceId, deviceParams);
  },

  /**
   * 创建设备部署参数
   */
  createDeploymentParams: (
    type: 'RADAR' | 'OPTICAL_CAMERA' | 'RADIO_DETECTOR' | 'GPS_JAMMER',
    position: { x: number; y: number; z: number },
    orientation: number = 0,
    elevation: number = 0,
    detectionRange: number = 1000
  ): DeviceInitParamsDTO => {
    const deviceId = `${type}_${Date.now()}`;
    const deviceName = `${type.replace('_', ' ')} ${new Date().toLocaleTimeString()}`;
    
    return {
      deviceId,
      deviceName,
      deviceType: type,
      position,
      orientation,
      elevation,
      detectionRange,
      fieldOfView: type === 'RADAR' ? 120 : type === 'OPTICAL_CAMERA' ? 60 : 360
    };
  }
};

// 导出默认服务
export default deviceService; 