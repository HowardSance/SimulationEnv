package com.JP.dronesim.infrastructure.external.airsim;

import com.JP.dronesim.infrastructure.external.airsim.dto.*;
import com.JP.dronesim.infrastructure.adapter.airsim.client.DroneClientInterface;
import com.JP.dronesim.infrastructure.adapter.airsim.client.SensorClientInterface;
import com.JP.dronesim.infrastructure.adapter.airsim.config.AirSimConnectionConfig;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.Map;

/**
 * AirSim API客户端
 * 封装与AirSim仿真器API的通信逻辑
 */
@Component
public class AirSimApiClient {
    
    private final AirSimConnectionConfig connectionConfig;
    private final EventLoop eventLoop;
    private Client client;
    private DroneClientInterface droneClient;
    private SensorClientInterface sensorClient;
    
    /**
     * 连接状态
     */
    private volatile boolean connected = false;
    
    /**
     * 重连计数器
     */
    private volatile int reconnectAttempts = 0;
    
    /**
     * 活跃的载具映射
     */
    private final Map<String, String> activeVehicles = new ConcurrentHashMap<>();
    
    public AirSimApiClient(AirSimConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
        this.eventLoop = EventLoop.defaultEventLoop();
    }
    
    /**
     * 初始化连接
     * @throws Exception 连接异常
     */
    public void initialize() throws Exception {
        connect();
    }
    
    /**
     * 建立连接
     * @throws Exception 连接异常
     */
    private void connect() throws Exception {
        try {
            // 创建RPC客户端
            this.client = new Client(
                connectionConfig.getHost(), 
                connectionConfig.getPort(), 
                eventLoop
            );
            
            // 创建代理接口
            this.droneClient = client.proxy(DroneClientInterface.class);
            this.sensorClient = client.proxy(SensorClientInterface.class);
            
            // 确认连接
            droneClient.confirmConnection();
            
            // 检查版本兼容性
            checkVersionCompatibility();
            
            this.connected = true;
            this.reconnectAttempts = 0;
            
            // 启用API控制
            droneClient.enableApiControl(true, "");
            
        } catch (Exception e) {
            this.connected = false;
            throw new RuntimeException("连接AirSim失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查版本兼容性
     */
    private void checkVersionCompatibility() {
        try {
            int clientVersion = droneClient.getClientVersion();
            int serverVersion = droneClient.getServerVersion();
            int minRequiredServer = droneClient.getMinRequiredServerVersion();
            int minRequiredClient = droneClient.getMinRequiredClientVersion();
            
            if (serverVersion < minRequiredServer) {
                throw new RuntimeException(String.format(
                    "AirSim服务器版本过低: 当前版本=%d, 最低要求=%d", 
                    serverVersion, minRequiredServer));
            }
            
            if (clientVersion < minRequiredClient) {
                throw new RuntimeException(String.format(
                    "客户端版本过低: 当前版本=%d, 最低要求=%d", 
                    clientVersion, minRequiredClient));
            }
            
        } catch (Exception e) {
            // 版本检查失败不影响连接，只记录警告
            System.err.println("版本兼容性检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            if (connected && droneClient != null) {
                // 禁用API控制
                droneClient.enableApiControl(false, "");
                
                // 重置仿真环境
                droneClient.reset();
            }
        } catch (Exception e) {
            System.err.println("断开连接时出错: " + e.getMessage());
        } finally {
            this.connected = false;
            this.activeVehicles.clear();
            
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    System.err.println("关闭客户端时出错: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 检查连接状态
     * @return 是否已连接
     */
    public boolean isConnected() {
        if (!connected) {
            return false;
        }
        
        try {
            // 发送ping检查连接
            return droneClient.ping();
        } catch (Exception e) {
            this.connected = false;
            return false;
        }
    }
    
    /**
     * 自动重连
     * @return 重连是否成功
     */
    public boolean reconnect() {
        if (reconnectAttempts >= connectionConfig.getConnection().getRetry().getMaxAttempts()) {
            return false;
        }
        
        try {
            reconnectAttempts++;
            
            // 计算重试间隔
            long delay = calculateRetryDelay(reconnectAttempts);
            Thread.sleep(delay);
            
            connect();
            return true;
            
        } catch (Exception e) {
            System.err.println("重连失败 (尝试 " + reconnectAttempts + "): " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 计算重试延迟
     * @param attempt 尝试次数
     * @return 延迟时间（毫秒）
     */
    private long calculateRetryDelay(int attempt) {
        AirSimConnectionConfig.Connection.Retry retry = connectionConfig.getConnection().getRetry();
        long delay = (long) (retry.getInitialInterval() * Math.pow(retry.getMultiplier(), attempt - 1));
        return Math.min(delay, retry.getMaxInterval());
    }
    
    // ==================== 仿真控制接口 ====================
    
    /**
     * 暂停仿真
     * @param isPaused 是否暂停
     */
    public void pauseSimulation(boolean isPaused) {
        checkConnection();
        droneClient.simPause(isPaused);
    }
    
    /**
     * 继续仿真指定时间
     * @param seconds 继续时间（秒）
     */
    public void continueForTime(double seconds) {
        checkConnection();
        droneClient.simContinueForTime(seconds);
    }
    
    /**
     * 重置仿真
     */
    public void resetSimulation() {
        checkConnection();
        droneClient.reset();
        activeVehicles.clear();
    }
    
    /**
     * 设置天气条件
     * @param enable 是否启用天气
     */
    public void enableWeather(boolean enable) {
        checkConnection();
        droneClient.simEnableWeather(enable);
    }
    
    // ==================== 载具控制接口 ====================
    
    /**
     * 注册载具
     * @param vehicleId 载具ID
     * @param vehicleName AirSim中的载具名称
     */
    public void registerVehicle(String vehicleId, String vehicleName) {
        activeVehicles.put(vehicleId, vehicleName);
    }
    
    /**
     * 获取载具名称
     * @param vehicleId 载具ID
     * @return AirSim中的载具名称
     */
    private String getVehicleName(String vehicleId) {
        return activeVehicles.getOrDefault(vehicleId, vehicleId);
    }
    
    /**
     * 启用API控制
     * @param vehicleId 载具ID
     * @param enabled 是否启用
     */
    public void enableApiControl(String vehicleId, boolean enabled) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.enableApiControl(enabled, vehicleName);
    }
    
    /**
     * 检查API控制是否启用
     * @param vehicleId 载具ID
     * @return 是否启用API控制
     */
    public boolean isApiControlEnabled(String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return droneClient.isApiControlEnabled(vehicleName);
    }
    
    /**
     * 解锁/锁定载具
     * @param vehicleId 载具ID
     * @param arm 是否解锁
     */
    public void armDisarm(String vehicleId, boolean arm) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.armDisarm(arm, vehicleName);
    }
    
    /**
     * 起飞到指定高度
     * @param vehicleId 载具ID
     * @param altitude 目标高度
     */
    public void takeoff(String vehicleId, float altitude) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.takeoffAsync(altitude, vehicleName);
    }
    
    /**
     * 降落
     * @param vehicleId 载具ID
     */
    public void land(String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.landAsync(vehicleName);
    }
    
    /**
     * 悬停
     * @param vehicleId 载具ID
     */
    public void hover(String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.hoverAsync(vehicleName);
    }
    
    /**
     * 设置载具位姿
     * @param vehicleId 载具ID
     * @param pose 目标位姿
     * @param ignoreCollision 是否忽略碰撞
     */
    public void setVehiclePose(String vehicleId, Pose pose, boolean ignoreCollision) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        droneClient.simSetVehiclePose(pose, ignoreCollision, vehicleName);
    }
    
    /**
     * 获取载具位姿
     * @param vehicleId 载具ID
     * @return 当前位姿
     */
    public Pose getVehiclePose(String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return droneClient.simGetVehiclePose(vehicleName);
    }
    
    /**
     * 获取载具运动学状态
     * @param vehicleId 载具ID
     * @return 运动学状态
     */
    public KinematicsState getKinematicsState(String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return droneClient.simGetGroundTruthKinematics(vehicleName);
    }
    
    // ==================== 传感器数据接口 ====================
    
    /**
     * 获取激光雷达数据
     * @param sensorName 传感器名称
     * @param vehicleId 载具ID
     * @return 激光雷达数据
     */
    public LidarData getLidarData(String sensorName, String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return sensorClient.getLidarData(sensorName, vehicleName);
    }
    
    /**
     * 获取距离传感器数据
     * @param sensorName 传感器名称
     * @param vehicleId 载具ID
     * @return 距离传感器数据
     */
    public DistanceSensorData getDistanceSensorData(String sensorName, String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return sensorClient.getDistanceSensorData(sensorName, vehicleName);
    }
    
    /**
     * 获取IMU数据
     * @param sensorName 传感器名称
     * @param vehicleId 载具ID
     * @return IMU数据
     */
    public ImuData getImuData(String sensorName, String vehicleId) {
        checkConnection();
        String vehicleName = getVehicleName(vehicleId);
        return sensorClient.getImuData(sensorName, vehicleName);
    }
    
    // ==================== 异步操作接口 ====================
    
    /**
     * 异步起飞
     * @param vehicleId 载具ID
     * @param altitude 目标高度
     * @return 异步操作结果
     */
    public CompletableFuture<Void> takeoffAsync(String vehicleId, float altitude) {
        return CompletableFuture.runAsync(() -> takeoff(vehicleId, altitude));
    }
    
    /**
     * 异步降落
     * @param vehicleId 载具ID
     * @return 异步操作结果
     */
    public CompletableFuture<Void> landAsync(String vehicleId) {
        return CompletableFuture.runAsync(() -> land(vehicleId));
    }
    
    /**
     * 异步悬停
     * @param vehicleId 载具ID
     * @return 异步操作结果
     */
    public CompletableFuture<Void> hoverAsync(String vehicleId) {
        return CompletableFuture.runAsync(() -> hover(vehicleId));
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 检查连接状态，如果未连接则尝试重连
     */
    private void checkConnection() {
        if (!isConnected()) {
            if (!reconnect()) {
                throw new RuntimeException("AirSim连接已断开且重连失败");
            }
        }
    }
    
    /**
     * 获取连接配置
     * @return 连接配置
     */
    public AirSimConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }
    
    /**
     * 获取活跃载具列表
     * @return 载具映射
     */
    public Map<String, String> getActiveVehicles() {
        return new ConcurrentHashMap<>(activeVehicles);
    }
    
    /**
     * 获取重连尝试次数
     * @return 重连尝试次数
     */
    public int getReconnectAttempts() {
        return reconnectAttempts;
    }
} 