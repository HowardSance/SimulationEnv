package com.JP.dronesim.infrastructure.adapter.airsim;

import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.domain.uav.model.UAV;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.infrastructure.adapter.airsim.AirSimStateAdapter.UAVStateInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 空域数据同步服务
 * 负责空域领域模型与AirSim仿真环境之间的数据同步
 * 包括UAV状态同步、环境参数同步等
 * 
 * @author JP Team
 * @version 1.0
 */
@Service
public class AirspaceSyncService {
    
    @Autowired
    private AirSimStateAdapter airSimStateAdapter;
    
    /**
     * 同步任务执行器
     */
    private final ScheduledExecutorService syncExecutor = Executors.newScheduledThreadPool(2);
    
    /**
     * 同步状态映射 (空域ID -> 是否正在同步)
     */
    private final Map<String, Boolean> syncStatusMap = new ConcurrentHashMap<>();
    
    /**
     * 上次同步时间映射 (空域ID -> 时间戳)
     */
    private final Map<String, Long> lastSyncTimeMap = new ConcurrentHashMap<>();
    
    /**
     * 默认同步间隔（毫秒）
     */
    private static final long DEFAULT_SYNC_INTERVAL = 100; // 10Hz同步频率
    
    /**
     * 启动空域与AirSim的数据同步
     * 
     * @param airspace 空域聚合根
     * @return 是否启动成功
     */
    public boolean startSync(Airspace airspace) {
        if (airspace == null) {
            System.err.println("空域不能为空");
            return false;
        }
        
        String airspaceId = airspace.getId();
        
        // 检查是否已在同步
        if (syncStatusMap.getOrDefault(airspaceId, false)) {
            System.out.println("空域 " + airspaceId + " 已在同步中");
            return true;
        }
        
        // 初始化AirSim连接
        if (!airSimStateAdapter.initializeConnection()) {
            System.err.println("无法连接到AirSim，同步启动失败");
            return false;
        }
        
        // 初始化环境同步
        if (!initializeEnvironmentSync(airspace)) {
            System.err.println("环境同步初始化失败");
            return false;
        }
        
        // 初始化UAV同步
        if (!initializeUAVSync(airspace)) {
            System.err.println("UAV同步初始化失败");
            return false;
        }
        
        // 启动定时同步任务
        startPeriodicSync(airspace);
        
        syncStatusMap.put(airspaceId, true);
        lastSyncTimeMap.put(airspaceId, System.currentTimeMillis());
        
        System.out.println("空域 " + airspaceId + " 同步已启动");
        return true;
    }
    
    /**
     * 停止空域同步
     * 
     * @param airspaceId 空域ID
     */
    public void stopSync(String airspaceId) {
        if (airspaceId == null) {
            return;
        }
        
        syncStatusMap.put(airspaceId, false);
        lastSyncTimeMap.remove(airspaceId);
        
        // 关闭AirSim连接
        airSimStateAdapter.shutdown();
        
        System.out.println("空域 " + airspaceId + " 同步已停止");
    }
    
    /**
     * 手动执行一次完整同步
     * 
     * @param airspace 空域聚合根
     * @return 是否同步成功
     */
    public boolean performFullSync(Airspace airspace) {
        if (airspace == null) {
            return false;
        }
        
        try {
            // 同步环境参数
            syncEnvironmentParameters(airspace);
            
            // 同步所有UAV状态
            syncAllUAVStates(airspace);
            
            lastSyncTimeMap.put(airspace.getId(), System.currentTimeMillis());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("完整同步失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 同步特定UAV的状态
     * 
     * @param airspace 空域
     * @param uavId UAV ID
     * @return 是否同步成功
     */
    public boolean syncUAVState(Airspace airspace, String uavId) {
        if (airspace == null || uavId == null) {
            return false;
        }
        
        try {
            // 从AirSim获取UAV状态
            UAVStateInfo stateInfo = airSimStateAdapter.getUAVState(uavId);
            if (stateInfo == null) {
                return false;
            }
            
            // 更新空域中的UAV状态
            UAV uav = airspace.getUAV(uavId);
            if (uav != null) {
                // 更新位置
                uav.setPosition(stateInfo.getPosition());
                uav.setVelocity(stateInfo.getVelocity());
                uav.setOrientation(stateInfo.getOrientation());
                
                // 更新空域索引
                airspace.updateEntityPosition(uavId, stateInfo.getPosition());
                
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("同步UAV状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查同步状态
     * 
     * @param airspaceId 空域ID
     * @return 是否正在同步
     */
    public boolean isSyncing(String airspaceId) {
        return syncStatusMap.getOrDefault(airspaceId, false);
    }
    
    /**
     * 获取最后同步时间
     * 
     * @param airspaceId 空域ID
     * @return 最后同步时间戳，如果未同步过返回0
     */
    public long getLastSyncTime(String airspaceId) {
        return lastSyncTimeMap.getOrDefault(airspaceId, 0L);
    }
    
    /**
     * 获取已部署的UAV数量
     * 
     * @return UAV数量
     */
    public int getDeployedUAVCount() {
        return airSimStateAdapter.getDeployedUAVCount();
    }
    
    /**
     * 初始化环境同步
     * 
     * @param airspace 空域
     * @return 是否成功
     */
    private boolean initializeEnvironmentSync(Airspace airspace) {
        try {
            EnvironmentParameters envParams = airspace.getEnvironmentParameters();
            return airSimStateAdapter.setEnvironmentParameters(envParams);
        } catch (Exception e) {
            System.err.println("初始化环境同步失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 初始化UAV同步
     * 
     * @param airspace 空域
     * @return 是否成功
     */
    private boolean initializeUAVSync(Airspace airspace) {
        try {
            List<UAV> uavs = airspace.getAllUAVs();
            
            for (UAV uav : uavs) {
                // 部署UAV到AirSim
                boolean deployed = airSimStateAdapter.deployUAV(uav.getId(), uav.getPosition());
                if (!deployed) {
                    System.err.println("部署UAV失败: " + uav.getId());
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("初始化UAV同步失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 启动定时同步任务
     * 
     * @param airspace 空域
     */
    private void startPeriodicSync(Airspace airspace) {
        String airspaceId = airspace.getId();
        
        // UAV状态同步任务
        syncExecutor.scheduleAtFixedRate(() -> {
            if (syncStatusMap.getOrDefault(airspaceId, false)) {
                syncAllUAVStates(airspace);
            }
        }, 0, DEFAULT_SYNC_INTERVAL, TimeUnit.MILLISECONDS);
        
        // 环境参数同步任务（频率较低）
        syncExecutor.scheduleAtFixedRate(() -> {
            if (syncStatusMap.getOrDefault(airspaceId, false)) {
                syncEnvironmentParameters(airspace);
            }
        }, 0, DEFAULT_SYNC_INTERVAL * 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 同步所有UAV状态
     * 
     * @param airspace 空域
     */
    private void syncAllUAVStates(Airspace airspace) {
        try {
            List<UAV> uavs = airspace.getAllUAVs();
            
            for (UAV uav : uavs) {
                syncUAVState(airspace, uav.getId());
            }
            
        } catch (Exception e) {
            System.err.println("同步所有UAV状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 同步环境参数
     * 
     * @param airspace 空域
     */
    private void syncEnvironmentParameters(Airspace airspace) {
        try {
            EnvironmentParameters envParams = airspace.getEnvironmentParameters();
            airSimStateAdapter.setEnvironmentParameters(envParams);
        } catch (Exception e) {
            System.err.println("同步环境参数失败: " + e.getMessage());
        }
    }
    
    /**
     * 关闭同步服务
     */
    public void shutdown() {
        syncStatusMap.clear();
        lastSyncTimeMap.clear();
        syncExecutor.shutdown();
        airSimStateAdapter.shutdown();
        
        try {
            if (!syncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                syncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            syncExecutor.shutdownNow();
        }
    }
} 
