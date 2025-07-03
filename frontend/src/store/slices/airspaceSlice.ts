/**
 * 空域状态管理
 * 管理空域信息、环境参数、实体列表
 */

import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { 
  AirspaceDetailsDTO, 
  EntityStateDTO, 
  EnvironmentParameters,
  Position 
} from '../../types/api.types';
import airspaceService from '../../services/airspace.service';

// 定义状态接口
interface AirspaceState {
  // 空域基本信息
  airspace: AirspaceDetailsDTO | null;
  
  // 加载状态
  loading: boolean;
  error: string | null;
  
  // 选中的实体
  selectedEntityId: string | null;
  
  // 部署模式
  deploymentMode: {
    enabled: boolean;
    deviceType: 'RADAR' | 'OPTICAL_CAMERA' | 'RADIO_DETECTOR' | 'GPS_JAMMER' | null;
    position: Position | null;
  };
  
  // 实时更新标记
  lastUpdateTime: number;
}

// 初始状态
const initialState: AirspaceState = {
  airspace: null,
  loading: false,
  error: null,
  selectedEntityId: null,
  deploymentMode: {
    enabled: false,
    deviceType: null,
    position: null
  },
  lastUpdateTime: Date.now()
};

// 异步操作：获取空域信息
export const fetchAirspace = createAsyncThunk(
  'airspace/fetch',
  async () => {
    const response = await airspaceService.getAirspace();
    return response;
  }
);

// 异步操作：更新环境参数
export const updateEnvironment = createAsyncThunk(
  'airspace/updateEnvironment',
  async (params: Partial<EnvironmentParameters>) => {
    await airspaceService.updateEnvironment(params);
    // 更新后重新获取空域信息
    const response = await airspaceService.getAirspace();
    return response;
  }
);

// 异步操作：重置空域
export const resetAirspace = createAsyncThunk(
  'airspace/reset',
  async () => {
    await airspaceService.resetAirspace();
    const response = await airspaceService.getAirspace();
    return response;
  }
);

// 创建slice
const airspaceSlice = createSlice({
  name: 'airspace',
  initialState,
  reducers: {
    // 选择实体
    selectEntity: (state, action: PayloadAction<string | null>) => {
      state.selectedEntityId = action.payload;
    },
    
    // 设置部署模式
    setDeploymentMode: (state, action: PayloadAction<{
      enabled: boolean;
      deviceType?: 'RADAR' | 'OPTICAL_CAMERA' | 'RADIO_DETECTOR' | 'GPS_JAMMER' | null;
    }>) => {
      state.deploymentMode.enabled = action.payload.enabled;
      if (action.payload.deviceType !== undefined) {
        state.deploymentMode.deviceType = action.payload.deviceType;
      }
      if (!action.payload.enabled) {
        state.deploymentMode.position = null;
      }
    },
    
    // 设置部署位置
    setDeploymentPosition: (state, action: PayloadAction<Position | null>) => {
      state.deploymentMode.position = action.payload;
    },
    
    // 更新实体状态（用于WebSocket实时更新）
    updateEntityState: (state, action: PayloadAction<EntityStateDTO>) => {
      if (!state.airspace) return;
      
      const entity = action.payload;
      
      // 更新设备列表
      const deviceIndex = state.airspace.devices.findIndex(d => d.id === entity.id);
      if (deviceIndex >= 0) {
        state.airspace.devices[deviceIndex] = entity;
      }
      
      // 更新无人机列表
      const uavIndex = state.airspace.uavs.findIndex(u => u.id === entity.id);
      if (uavIndex >= 0) {
        state.airspace.uavs[uavIndex] = entity;
      }
      
      state.lastUpdateTime = Date.now();
    },
    
    // 添加新实体
    addEntity: (state, action: PayloadAction<EntityStateDTO>) => {
      if (!state.airspace) return;
      
      const entity = action.payload;
      
      // 根据类型添加到对应列表
      if (['RADAR', 'OPTICAL_CAMERA', 'RADIO_DETECTOR', 'GPS_JAMMER'].includes(entity.type)) {
        state.airspace.devices.push(entity);
        state.airspace.entityCount++;
      } else if (entity.type === 'UAV') {
        state.airspace.uavs.push(entity);
        state.airspace.entityCount++;
      }
      
      state.lastUpdateTime = Date.now();
    },
    
    // 移除实体
    removeEntity: (state, action: PayloadAction<string>) => {
      if (!state.airspace) return;
      
      const entityId = action.payload;
      
      // 从设备列表移除
      state.airspace.devices = state.airspace.devices.filter(d => d.id !== entityId);
      
      // 从无人机列表移除
      state.airspace.uavs = state.airspace.uavs.filter(u => u.id !== entityId);
      
      // 更新实体数量
      state.airspace.entityCount = state.airspace.devices.length + state.airspace.uavs.length;
      
      // 如果移除的是选中的实体，清空选中状态
      if (state.selectedEntityId === entityId) {
        state.selectedEntityId = null;
      }
      
      state.lastUpdateTime = Date.now();
    },
    
    // 清空错误
    clearError: (state) => {
      state.error = null;
    }
  },
  
  extraReducers: (builder) => {
    // 处理获取空域信息
    builder
      .addCase(fetchAirspace.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAirspace.fulfilled, (state, action) => {
        state.loading = false;
        state.airspace = action.payload;
        state.lastUpdateTime = Date.now();
      })
      .addCase(fetchAirspace.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || '获取空域信息失败';
      });
    
    // 处理更新环境参数
    builder
      .addCase(updateEnvironment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateEnvironment.fulfilled, (state, action) => {
        state.loading = false;
        state.airspace = action.payload;
        state.lastUpdateTime = Date.now();
      })
      .addCase(updateEnvironment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || '更新环境参数失败';
      });
    
    // 处理重置空域
    builder
      .addCase(resetAirspace.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(resetAirspace.fulfilled, (state, action) => {
        state.loading = false;
        state.airspace = action.payload;
        state.selectedEntityId = null;
        state.deploymentMode = initialState.deploymentMode;
        state.lastUpdateTime = Date.now();
      })
      .addCase(resetAirspace.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || '重置空域失败';
      });
  }
});

// 导出actions
export const {
  selectEntity,
  setDeploymentMode,
  setDeploymentPosition,
  updateEntityState,
  addEntity,
  removeEntity,
  clearError
} = airspaceSlice.actions;

// 导出reducer
export default airspaceSlice.reducer;

// 选择器
export const selectAirspace = (state: { airspace: AirspaceState }) => state.airspace.airspace;
export const selectEntities = (state: { airspace: AirspaceState }) => {
  if (!state.airspace.airspace) return [];
  return [...state.airspace.airspace.devices, ...state.airspace.airspace.uavs];
};
export const selectSelectedEntity = (state: { airspace: AirspaceState }) => {
  const entities = selectEntities(state);
  return entities.find(e => e.id === state.airspace.selectedEntityId) || null;
};
export const selectDeploymentMode = (state: { airspace: AirspaceState }) => state.airspace.deploymentMode; 