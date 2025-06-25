package com.JP.dronesim.application.services;

import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.opticalcamera.CameraFeed;
import com.JP.dronesim.domain.device.model.opticalcamera.IdentifiedTarget;
import com.JP.dronesim.domain.device.model.opticalcamera.OpticalCamera;
import com.JP.dronesim.domain.device.model.opticalcamera.OpticalCameraFactory;
import com.JP.dronesim.domain.device.model.opticalcamera.OpticalParameters;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 光电摄像头应用服务类
 * 提供光电摄像头的创建、管理、控制和数据处理的业务逻辑
 * 协调多个摄像头的协同工作
 * 
 * @author JP
 * @version 1.0
 */
public class OpticalCameraAppService {
    
    /**
     * 管理的光电摄像头映射
     * Key: 设备ID，Value: 光电摄像头实例
     */
    private final Map<String, OpticalCamera> cameras;
    
    /**
     * 构造函数
     */
    public OpticalCameraAppService() {
        this.cameras = new ConcurrentHashMap<>();
    }
    
    // ================ 摄像头管理方法 ================
    
    /**
     * 创建并部署标准光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @return 创建的摄像头ID
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备ID已存在
     */
    public String deployStandardCamera(String deviceId, String deviceName, Position position,
                                     double orientation, double elevation, double detectionRange) {
        validateCameraId(deviceId);
        
        OpticalCamera camera = OpticalCameraFactory.createStandardCamera(
                deviceId, deviceName, position, orientation, elevation, detectionRange);
        
        cameras.put(deviceId, camera);
        return deviceId;
    }
    
    /**
     * 创建并部署高精度光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @return 创建的摄像头ID
     */
    public String deployHighPrecisionCamera(String deviceId, String deviceName, Position position,
                                          double orientation, double elevation, double detectionRange) {
        validateCameraId(deviceId);
        
        OpticalCamera camera = OpticalCameraFactory.createHighPrecisionCamera(
                deviceId, deviceName, position, orientation, elevation, detectionRange);
        
        cameras.put(deviceId, camera);
        return deviceId;
    }
    
    /**
     * 批量部署摄像头网络
     * 
     * @param namePrefix 名称前缀
     * @param centerPosition 中心位置
     * @param gridSize 网格大小
     * @param spacing 间距
     * @param height 高度
     * @param detectionRange 探测距离
     * @return 部署的摄像头ID列表
     */
    public List<String> deployCameraNetwork(String namePrefix, Position centerPosition,
                                          int gridSize, double spacing, double height,
                                          double detectionRange) {
        List<OpticalCamera> cameraList = OpticalCameraFactory.createCameraNetwork(
                namePrefix, centerPosition, gridSize, spacing, height, detectionRange);
        
        List<String> deployedIds = new ArrayList<>();
        for (OpticalCamera camera : cameraList) {
            cameras.put(camera.getId(), camera);
            deployedIds.add(camera.getId());
        }
        
        return deployedIds;
    }
    
    /**
     * 移除指定的光电摄像头
     * 
     * @param deviceId 设备ID
     * @return true表示成功移除，false表示设备不存在
     */
    public boolean removeCamera(String deviceId) {
        OpticalCamera camera = cameras.remove(deviceId);
        if (camera != null && camera.isActive()) {
            camera.disable();
        }
        return camera != null;
    }
    
    /**
     * 获取所有摄像头ID列表
     * 
     * @return 摄像头ID列表
     */
    public List<String> getAllCameraIds() {
        return new ArrayList<>(cameras.keySet());
    }
    
    /**
     * 获取活跃摄像头ID列表
     * 
     * @return 活跃摄像头ID列表
     */
    public List<String> getActiveCameraIds() {
        return cameras.values().stream()
                .filter(OpticalCamera::isActive)
                .map(OpticalCamera::getId)
                .collect(Collectors.toList());
    }
    
    // ================ 摄像头控制方法 ================
    
    /**
     * 启用指定摄像头
     * 
     * @param deviceId 设备ID
     * @return true表示成功启用，false表示设备不存在
     * @throws IllegalStateException 如果设备状态不允许启用
     */
    public boolean enableCamera(String deviceId) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null) {
            return false;
        }
        
        camera.enable();
        return true;
    }
    
    /**
     * 禁用指定摄像头
     * 
     * @param deviceId 设备ID
     * @return true表示成功禁用，false表示设备不存在
     */
    public boolean disableCamera(String deviceId) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null) {
            return false;
        }
        
        camera.disable();
        return true;
    }
    
    /**
     * 控制摄像头转动
     * 
     * @param deviceId 设备ID
     * @param orientation 目标朝向
     * @param elevation 目标仰角
     * @return true表示成功转动，false表示设备不存在或操作失败
     */
    public boolean moveCameraFeed(String deviceId, double orientation, double elevation) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null || !camera.isActive()) {
            return false;
        }
        
        try {
            camera.moveCameraFeed(orientation, elevation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 开始跟踪目标
     * 
     * @param deviceId 设备ID
     * @param targetId 目标ID
     * @param targetPosition 目标位置
     * @return true表示成功开始跟踪，false表示操作失败
     */
    public boolean startTracking(String deviceId, String targetId, Position targetPosition) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null || !camera.isActive()) {
            return false;
        }
        
        try {
            camera.trackTarget(targetId, targetPosition);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 停止跟踪目标
     * 
     * @param deviceId 设备ID
     * @param targetId 目标ID
     * @return true表示成功停止跟踪，false表示操作失败
     */
    public boolean stopTracking(String deviceId, String targetId) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null) {
            return false;
        }
        
        return camera.stopTracking(targetId);
    }
    
    // ================ 数据采集和处理方法 ================
    
    /**
     * 获取指定摄像头的实时图像数据
     * 
     * @param deviceId 设备ID
     * @param airspace 空域环境
     * @return 摄像头数据流，如果设备不存在或不活跃则返回null
     */
    public CameraFeed getCameraFeed(String deviceId, AirspaceEnvironment airspace) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null || !camera.isActive()) {
            return null;
        }
        
        try {
            return camera.simulateCameraFeed(airspace);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 执行目标识别
     * 
     * @param deviceId 设备ID
     * @param feed 摄像头数据流
     * @param environment 环境参数
     * @return 识别出的目标列表，如果操作失败则返回空列表
     */
    public List<IdentifiedTarget> identifyTargets(String deviceId, CameraFeed feed, 
                                                EnvironmentParameters environment) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null || !camera.isInitialized()) {
            return new ArrayList<>();
        }
        
        try {
            return camera.identifyTarget(feed, environment);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 执行全网摄像头协同探测
     * 
     * @param airspace 空域环境
     * @return 所有摄像头的识别结果汇总
     */
    public List<IdentifiedTarget> performNetworkDetection(AirspaceEnvironment airspace) {
        List<IdentifiedTarget> allTargets = new ArrayList<>();
        
        for (OpticalCamera camera : cameras.values()) {
            if (camera.isActive()) {
                try {
                    // 获取摄像头数据
                    CameraFeed feed = camera.simulateCameraFeed(airspace);
                    
                    // 识别目标
                    List<IdentifiedTarget> targets = camera.identifyTarget(feed, 
                            airspace.getEnvironmentParameters());
                    
                    allTargets.addAll(targets);
                } catch (Exception e) {
                    // 记录错误但继续处理其他摄像头
                    System.err.println("摄像头 " + camera.getId() + " 探测失败: " + e.getMessage());
                }
            }
        }
        
        return allTargets;
    }
    
    /**
     * 获取摄像头网络的整体覆盖统计
     * 
     * @return 覆盖统计信息
     */
    public CoverageStatistics getNetworkCoverageStatistics() {
        int totalCameras = cameras.size();
        int activeCameras = (int) cameras.values().stream().filter(OpticalCamera::isActive).count();
        int trackingCameras = (int) cameras.values().stream()
                .filter(camera -> !camera.getTrackingTargets().isEmpty()).count();
        
        return new CoverageStatistics(totalCameras, activeCameras, trackingCameras);
    }
    
    // ================ 参数管理方法 ================
    
    /**
     * 调整摄像头参数
     * 
     * @param deviceId 设备ID
     * @param newParameters 新参数
     * @return true表示成功调整，false表示操作失败
     */
    public boolean adjustCameraParameters(String deviceId, OpticalParameters newParameters) {
        OpticalCamera camera = cameras.get(deviceId);
        if (camera == null || !camera.isInitialized()) {
            return false;
        }
        
        try {
            camera.adjustParameters(newParameters);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 批量调整摄像头参数
     * 
     * @param deviceIds 设备ID列表
     * @param newParameters 新参数
     * @return 成功调整的设备数量
     */
    public int batchAdjustParameters(List<String> deviceIds, OpticalParameters newParameters) {
        int successCount = 0;
        for (String deviceId : deviceIds) {
            if (adjustCameraParameters(deviceId, newParameters)) {
                successCount++;
            }
        }
        return successCount;
    }
    
    // ================ 私有辅助方法 ================
    
    /**
     * 验证摄像头ID
     */
    private void validateCameraId(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        if (cameras.containsKey(deviceId)) {
            throw new IllegalStateException("设备ID已存在: " + deviceId);
        }
    }
    
    // ================ 内部类 ================
    
    /**
     * 覆盖统计信息
     */
    public static class CoverageStatistics {
        private final int totalCameras;
        private final int activeCameras;
        private final int trackingCameras;
        
        public CoverageStatistics(int totalCameras, int activeCameras, int trackingCameras) {
            this.totalCameras = totalCameras;
            this.activeCameras = activeCameras;
            this.trackingCameras = trackingCameras;
        }
        
        public int getTotalCameras() { return totalCameras; }
        public int getActiveCameras() { return activeCameras; }
        public int getTrackingCameras() { return trackingCameras; }
        
        public double getActiveRate() {
            return totalCameras > 0 ? (double) activeCameras / totalCameras : 0.0;
        }
        
        public double getTrackingRate() {
            return activeCameras > 0 ? (double) trackingCameras / activeCameras : 0.0;
        }
        
        @Override
        public String toString() {
            return String.format("CoverageStatistics[total=%d, active=%d(%.1f%%), tracking=%d(%.1f%%)]",
                    totalCameras, activeCameras, getActiveRate() * 100,
                    trackingCameras, getTrackingRate() * 100);
        }
    }
} 