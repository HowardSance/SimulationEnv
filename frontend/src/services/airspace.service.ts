/**
 * 空域管理API服务
 * 对接后端AirspaceManagementAppService
 */

import { http } from './api';
import { 
  AirspaceDetailsDTO, 
  EnvironmentUpdateParamsDTO,
  DeviceInitParamsDTO,
  EntityStateDTO 
} from '../types/api.types';

/**
 * 空域服务类
 */
export const airspaceService = {
  /**
   * 获取空域详情
   * GET /api/airspace
   */
  getAirspace: () => 
    http.get<AirspaceDetailsDTO>('/api/airspace'),

  /**
   * 更新环境参数
   * PUT /api/airspace/environment
   */
  updateEnvironment: (params: EnvironmentUpdateParamsDTO) =>
    http.put<string>('/api/airspace/environment', params),

  /**
   * 获取空域边界信息
   * GET /api/airspace/boundary
   */
  getAirspaceBoundary: () =>
    http.get<any>('/api/airspace/boundary'),

  /**
   * 重置空域
   * POST /api/airspace/reset
   */
  resetAirspace: () =>
    http.post<string>('/api/airspace/reset'),

  /**
   * 获取空域内所有实体
   * 包含设备和无人机
   */
  getAllEntities: async (): Promise<EntityStateDTO[]> => {
    const airspace = await airspaceService.getAirspace();
    return [...(airspace.devices || []), ...(airspace.uavs || [])];
  },

  /**
   * 获取空域尺寸
   * 返回空域的宽度、深度和高度
   */
  getAirspaceDimensions: async () => {
    const airspace = await airspaceService.getAirspace();
    const { boundaryMin, boundaryMax } = airspace;
    
    return {
      width: boundaryMax.x - boundaryMin.x,
      depth: boundaryMax.z - boundaryMin.z,
      height: boundaryMax.y - boundaryMin.y,
      center: {
        x: (boundaryMax.x + boundaryMin.x) / 2,
        y: (boundaryMax.y + boundaryMin.y) / 2,
        z: (boundaryMax.z + boundaryMin.z) / 2,
      }
    };
  },

  /**
   * 检查位置是否在空域边界内
   */
  isPositionInBounds: async (x: number, y: number, z: number) => {
    const airspace = await airspaceService.getAirspace();
    const { boundaryMin, boundaryMax } = airspace;
    
    return x >= boundaryMin.x && x <= boundaryMax.x &&
           y >= boundaryMin.y && y <= boundaryMax.y &&
           z >= boundaryMin.z && z <= boundaryMax.z;
  }
};

// 导出默认服务
export default airspaceService; 