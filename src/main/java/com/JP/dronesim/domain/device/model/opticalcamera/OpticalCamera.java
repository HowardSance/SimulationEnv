package com.JP.dronesim.domain.device.model.opticalcamera;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.device.model.common.SensorParameters;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 光电摄像头探测设备实体类
 * 通过实现基类设备接口和从AirSim服务器获取传感器数据
 * 实现场景内光电摄像头的探测效果
 * 
 * @author JP
 * @version 1.0
 */
public class OpticalCamera extends AbstractProbeDevice {
    
    /**
     * 当前正在跟踪的目标映射
     * Key: 目标ID，Value: 目标当前位置
     */
    private final Map<String, Position> trackingTargets;
    
    /**
     * 摄像头当前朝向（可以与设备基础朝向不同）
     */
    private double currentCameraOrientation;
    
    /**
     * 摄像头当前仰角（可以与设备基础仰角不同）
     */
    private double currentCameraElevation;
    
    /**
     * 最后一次获取的摄像头数据流
     */
    private CameraFeed lastCameraFeed;
    
    /**
     * 构造函数
     */
    public OpticalCamera() {
        super(DeviceType.OPTICAL_CAMERA);
        this.trackingTargets = new HashMap<>();
        this.currentCameraOrientation = 0.0;
        this.currentCameraElevation = 0.0;
    }
    
    // ================ 光电摄像头特有行为方法 ================
    
    /**
     * 转动摄像头
     * 调整摄像头的朝向和仰角
     * 
     * @param orientation 需要指向的朝向（0-360度）
     * @param elevation 调整仰角角度（0-90度）
     * @throws IllegalArgumentException 如果角度参数无效
     * @throws IllegalStateException 如果设备未初始化或非活跃状态
     */
    public void moveCameraFeed(double orientation, double elevation) {
        if (!isInitialized()) {
            throw new IllegalStateException("设备未初始化");
        }
        if (!isActive()) {
            throw new IllegalStateException("设备未处于活跃状态");
        }
        
        validateAngle(orientation, 0.0, 360.0, "朝向角度");
        validateAngle(elevation, 0.0, 90.0, "仰角");
        
        this.currentCameraOrientation = orientation;
        this.currentCameraElevation = elevation;
        
        // 记录摄像头移动事件
        String eventId = UUID.randomUUID().toString();
        DetectionEvent moveEvent = new DetectionEvent(
                eventId, LocalDateTime.now(), this.getId(), this.getName(),
                "CAMERA_MOVE", "摄像头移动",
                this.getPosition(), 1.0, 0.0,
                String.format("摄像头转动到朝向%.1f度，仰角%.1f度", orientation, elevation)
        );
        this.getDetectionLog().addEvent(moveEvent);
    }
    
    /**
     * 模拟获取摄像头当前视野内的图像数据
     * 直接调用AirSim API获取可见目标信息
     * 
     * @param airspace 当前空域环境的AirspaceEnvironment对象
     * @return CameraFeed 表示模拟的图像流或直接的检测信息
     * @throws IllegalStateException 如果设备未初始化或非活跃状态
     */
    public CameraFeed simulateCameraFeed(AirspaceEnvironment airspace) {
        if (!isInitialized()) {
            throw new IllegalStateException("设备未初始化");
        }
        if (!isActive()) {
            throw new IllegalStateException("设备未处于活跃状态");
        }
        
        OpticalParameters opticalParams = (OpticalParameters) this.getDetectionParameters();
        if (opticalParams == null) {
            throw new IllegalStateException("光电摄像头参数未设置");
        }
        
        // 获取环境参数
        EnvironmentParameters envParams = airspace.getEnvironmentParameters();
        
        // 模拟从AirSim API获取检测数据
        List<CameraFeed.RawDetectionData> rawDetections = simulateAirSimDetections(airspace, opticalParams);
        
        // 计算图像质量
        double imageQuality = calculateImageQuality(envParams, opticalParams);
        
        // 创建摄像头数据流
        String feedId = UUID.randomUUID().toString();
        this.lastCameraFeed = new CameraFeed(
                feedId, LocalDateTime.now(), this.getId(),
                opticalParams.getResolutionWidth(), opticalParams.getResolutionHeight(),
                this.getFieldOfView(), this.currentCameraOrientation, this.currentCameraElevation,
                envParams.getLightIntensity(), envParams.getVisibility(), imageQuality,
                rawDetections, null, true
        );
        
        return this.lastCameraFeed;
    }
    
    /**
     * 对模拟图像数据进行处理，根据预设算法识别出其中的无人机目标
     * 
     * @param feed 模拟的摄像头数据CameraFeed
     * @param environment 当前环境光照、能见度等的EnvironmentParameters对象
     * @return List<IdentifiedTarget> 识别出的无人机目标列表，包含位置、大小、置信度
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备未初始化
     */
    public List<IdentifiedTarget> identifyTarget(CameraFeed feed, EnvironmentParameters environment) {
        if (!isInitialized()) {
            throw new IllegalStateException("设备未初始化");
        }
        if (feed == null) {
            throw new IllegalArgumentException("摄像头数据流不能为空");
        }
        if (environment == null) {
            throw new IllegalArgumentException("环境参数不能为空");
        }
        if (!feed.isValid()) {
            throw new IllegalArgumentException("摄像头数据流无效");
        }
        
        List<IdentifiedTarget> identifiedTargets = new ArrayList<>();
        OpticalParameters opticalParams = (OpticalParameters) this.getDetectionParameters();
        
        // 处理每个原始检测数据
        for (CameraFeed.RawDetectionData rawData : feed.getRawDetections()) {
            // 应用光电摄像头特定的识别算法
            IdentifiedTarget target = processRawDetection(rawData, feed, environment, opticalParams);
            if (target != null && target.getConfidence() >= opticalParams.getConfidenceThreshold()) {
                identifiedTargets.add(target);
            }
        }
        
        return identifiedTargets;
    }
    
    /**
     * 持续锁定并跟踪某个已识别的特定无人机目标
     * 
     * @param targetId 被追踪无人机的ID
     * @param currentPosition 无人机当前位置的Position对象
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备未初始化或非活跃状态
     */
    public void trackTarget(String targetId, Position currentPosition) {
        if (!isInitialized()) {
            throw new IllegalStateException("设备未初始化");
        }
        if (!isActive()) {
            throw new IllegalStateException("设备未处于活跃状态");
        }
        if (targetId == null || targetId.trim().isEmpty()) {
            throw new IllegalArgumentException("目标ID不能为空");
        }
        if (currentPosition == null) {
            throw new IllegalArgumentException("目标位置不能为空");
        }
        
        // 计算目标相对于摄像头的角度
        double[] angles = calculateTargetAngles(currentPosition);
        double targetOrientation = angles[0];
        double targetElevation = angles[1];
        
        // 自动转动摄像头跟踪目标
        moveCameraFeed(targetOrientation, targetElevation);
        
        // 将目标添加到跟踪列表
        trackingTargets.put(targetId, currentPosition);
        
        // 记录跟踪事件
        String eventId = UUID.randomUUID().toString();
        DetectionEvent trackEvent = new DetectionEvent(
                eventId, LocalDateTime.now(), this.getId(), this.getName(),
                targetId, "目标跟踪",
                currentPosition, 1.0, 
                calculateDistance(this.getPosition(), currentPosition),
                String.format("开始跟踪目标%s，位置%s", targetId, currentPosition)
        );
        this.getDetectionLog().addEvent(trackEvent);
    }
    
    /**
     * 停止跟踪指定目标
     * 
     * @param targetId 要停止跟踪的目标ID
     * @return true表示成功停止跟踪，false表示目标不在跟踪列表中
     */
    public boolean stopTracking(String targetId) {
        boolean removed = trackingTargets.remove(targetId) != null;
        if (removed) {
            // 记录停止跟踪事件
            String eventId = UUID.randomUUID().toString();
            DetectionEvent stopEvent = new DetectionEvent(
                    eventId, LocalDateTime.now(), this.getId(), this.getName(),
                    targetId, "停止跟踪",
                    this.getPosition(), 1.0, 0.0,
                    String.format("停止跟踪目标%s", targetId)
            );
            this.getDetectionLog().addEvent(stopEvent);
        }
        return removed;
    }
    
    /**
     * 获取当前正在跟踪的目标列表
     * 
     * @return 正在跟踪的目标ID列表
     */
    public List<String> getTrackingTargets() {
        return new ArrayList<>(trackingTargets.keySet());
    }
    
    /**
     * 检查是否正在跟踪指定目标
     * 
     * @param targetId 目标ID
     * @return true表示正在跟踪，false表示未跟踪
     */
    public boolean isTracking(String targetId) {
        return trackingTargets.containsKey(targetId);
    }
    
    /**
     * 获取摄像头当前朝向
     * 
     * @return 摄像头当前朝向（度）
     */
    public double getCurrentCameraOrientation() {
        return currentCameraOrientation;
    }
    
    /**
     * 获取摄像头当前仰角
     * 
     * @return 摄像头当前仰角（度）
     */
    public double getCurrentCameraElevation() {
        return currentCameraElevation;
    }
    
    /**
     * 获取最后一次的摄像头数据流
     * 
     * @return 最后一次获取的CameraFeed，可能为null
     */
    public CameraFeed getLastCameraFeed() {
        return lastCameraFeed;
    }
    
    // ================ 抽象方法实现 ================
    
    @Override
    protected void doSpecificInitialization(DeviceInitParamsDTO params) {
        // 验证光电摄像头特定参数
        if (!(params.getDetectionParameters() instanceof OpticalParameters)) {
            throw new IllegalArgumentException("光电摄像头需要OpticalParameters类型的参数");
        }
        
        // 初始化摄像头朝向和仰角
        this.currentCameraOrientation = params.getOrientation();
        this.currentCameraElevation = params.getElevation();
        
        // 清空跟踪目标
        this.trackingTargets.clear();
    }
    
    @Override
    protected List<DetectionEvent> doPerformDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
        List<DetectionEvent> detectionEvents = new ArrayList<>();
        
        try {
            // 获取摄像头数据流
            CameraFeed feed = simulateCameraFeed(airspace);
            
            // 识别目标
            List<IdentifiedTarget> targets = identifyTarget(feed, airspace.getEnvironmentParameters());
            
            // 将识别结果转换为探测事件
            for (IdentifiedTarget target : targets) {
                DetectionEvent event = new DetectionEvent(
                        UUID.randomUUID().toString(), target.getIdentifiedTime(),
                        this.getId(), this.getName(),
                        target.getTargetId(), target.getTargetType(),
                        target.getWorldPosition(), target.getConfidence(),
                        target.getDistance(),
                        String.format("光电摄像头探测到目标，类型:%s，置信度:%.2f", 
                                     target.getTargetType(), target.getConfidence())
                );
                detectionEvents.add(event);
            }
            
        } catch (Exception e) {
            // 记录错误事件
            DetectionEvent errorEvent = new DetectionEvent(
                    UUID.randomUUID().toString(), LocalDateTime.now(),
                    this.getId(), this.getName(),
                    "ERROR", "探测错误",
                    this.getPosition(), 0.0, 0.0,
                    "光电摄像头探测过程中发生错误: " + e.getMessage()
            );
            detectionEvents.add(errorEvent);
        }
        
        return detectionEvents;
    }
    
    @Override
    protected void doAdjustParameters(SensorParameters newParams) {
        if (!(newParams instanceof OpticalParameters)) {
            throw new IllegalArgumentException("光电摄像头只接受OpticalParameters类型的参数");
        }
        
        OpticalParameters newOpticalParams = (OpticalParameters) newParams;
        
        // 验证新参数的兼容性
        if (!newOpticalParams.isValid()) {
            throw new IllegalArgumentException("新的光电摄像头参数无效");
        }
        
        // 应用新参数可能需要重新初始化某些组件
        // 这里可以添加具体的参数调整逻辑
    }
    
    @Override
    protected void doReset() {
        // 清空跟踪目标
        this.trackingTargets.clear();
        
        // 重置摄像头朝向
        this.currentCameraOrientation = this.getOrientation();
        this.currentCameraElevation = this.getElevation();
        
        // 清空最后的数据流
        this.lastCameraFeed = null;
    }
    
    // ================ 私有辅助方法 ================
    
    /**
     * 验证角度参数
     */
    private void validateAngle(double angle, double min, double max, String name) {
        if (angle < min || angle >= max) {
            throw new IllegalArgumentException(String.format("%s必须在%.1f到%.1f度之间", name, min, max));
        }
    }
    
    /**
     * 模拟从AirSim API获取检测数据
     */
    private List<CameraFeed.RawDetectionData> simulateAirSimDetections(AirspaceEnvironment airspace, 
                                                                       OpticalParameters params) {
        List<CameraFeed.RawDetectionData> detections = new ArrayList<>();
        
        // 这里应该调用实际的AirSim API
        // 目前使用模拟数据
        
        return detections;
    }
    
    /**
     * 计算图像质量
     */
    private double calculateImageQuality(EnvironmentParameters env, OpticalParameters params) {
        double quality = 1.0;
        
        // 根据环境参数调整质量
        quality *= env.getLightIntensity() * 0.7 + 0.3; // 光照影响
        quality *= Math.min(1.0, env.getVisibility() / 1000.0); // 能见度影响
        quality *= env.getAtmosphericClarity(); // 大气透明度影响
        
        if (env.isRaining()) {
            quality *= 0.6; // 降水影响
        }
        
        return Math.max(0.0, Math.min(1.0, quality));
    }
    
    /**
     * 处理原始检测数据
     */
    private IdentifiedTarget processRawDetection(CameraFeed.RawDetectionData rawData, 
                                                CameraFeed feed, EnvironmentParameters env,
                                                OpticalParameters params) {
        // 应用光电识别算法
        double confidence = calculateDetectionConfidence(rawData, env, params);
        
        if (confidence < params.getConfidenceThreshold()) {
            return null;
        }
        
        // 计算世界坐标位置（需要实现从像素坐标到世界坐标的转换）
        Position worldPos = convertPixelToWorldPosition(rawData, feed);
        
        // 计算目标角度
        double[] angles = calculateTargetAngles(worldPos);
        
        return new IdentifiedTarget(
                UUID.randomUUID().toString(), LocalDateTime.now(), this.getId(),
                worldPos, rawData.getBoundingBoxX(), rawData.getBoundingBoxY(),
                rawData.getBoundingBoxWidth(), rawData.getBoundingBoxHeight(),
                rawData.getDistance(), angles[0], angles[1], confidence,
                "UAV", estimateTargetSize(rawData), 
                IdentifiedTarget.TargetMotionState.UNKNOWN,
                calculateVisibilityScore(env), "光电摄像头识别"
        );
    }
    
    /**
     * 计算探测置信度
     */
    private double calculateDetectionConfidence(CameraFeed.RawDetectionData rawData, 
                                              EnvironmentParameters env, OpticalParameters params) {
        double confidence = rawData.getConfidence();
        
        // 根据环境条件调整置信度
        confidence *= env.getOpticalDetectionSuitability();
        
        // 根据目标大小调整置信度
        int targetSize = rawData.getBoundingBoxWidth() * rawData.getBoundingBoxHeight();
        if (targetSize < params.getMinTargetSize()) {
            confidence *= 0.5; // 目标太小
        } else if (targetSize > params.getMaxTargetSize()) {
            confidence *= 0.8; // 目标可能太大
        }
        
        return Math.max(0.0, Math.min(1.0, confidence));
    }
    
    /**
     * 计算目标相对于摄像头的角度
     */
    private double[] calculateTargetAngles(Position targetPosition) {
        Position cameraPos = this.getPosition();
        
        double deltaX = targetPosition.getX() - cameraPos.getX();
        double deltaY = targetPosition.getY() - cameraPos.getY();
        double deltaZ = targetPosition.getZ() - cameraPos.getZ();
        
        // 计算方位角
        double azimuth = Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (azimuth < 0) azimuth += 360;
        
        // 计算仰角
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double elevation = Math.toDegrees(Math.atan2(-deltaZ, horizontalDistance));
        
        return new double[]{azimuth, Math.max(0, elevation)};
    }
    
    /**
     * 计算两点间距离
     */
    private double calculateDistance(Position pos1, Position pos2) {
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();
        double dz = pos1.getZ() - pos2.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 将像素坐标转换为世界坐标（简化实现）
     */
    private Position convertPixelToWorldPosition(CameraFeed.RawDetectionData rawData, CameraFeed feed) {
        // 这里需要实现具体的坐标转换算法
        // 简化实现，返回摄像头位置加上距离向量
        Position cameraPos = this.getPosition();
        double distance = rawData.getDistance();
        
        double x = cameraPos.getX() + distance * Math.cos(Math.toRadians(currentCameraOrientation));
        double y = cameraPos.getY() + distance * Math.sin(Math.toRadians(currentCameraOrientation));
        double z = cameraPos.getZ() - distance * Math.sin(Math.toRadians(currentCameraElevation));
        
        return new Position(x, y, z);
    }
    
    /**
     * 估计目标尺寸
     */
    private double estimateTargetSize(CameraFeed.RawDetectionData rawData) {
        // 基于检测框大小和距离估计实际尺寸
        // 简化实现
        double pixelSize = Math.sqrt(rawData.getBoundingBoxWidth() * rawData.getBoundingBoxHeight());
        return pixelSize * rawData.getDistance() / 1000.0; // 简化的尺寸估计
    }
    
    /**
     * 计算可见度评分
     */
    private double calculateVisibilityScore(EnvironmentParameters env) {
        double score = env.getLightIntensity() * 0.4 + 
                      Math.min(1.0, env.getVisibility() / 2000.0) * 0.4 +
                      env.getAtmosphericClarity() * 0.2;
        return Math.max(0.0, Math.min(1.0, score));
    }
} 