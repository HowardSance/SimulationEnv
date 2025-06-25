package com.JP.dronesim.domain.device.model.radar;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
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
 * 电磁波雷达探测设备实体类
 * 基于AirSim服务器的传感器数据和实现基类接口
 * 达到电磁波雷达的模拟效果
 * 
 * @author JP
 * @version 1.0
 */
public class ElectromagneticRadar extends AbstractProbeDevice {
    
    /**
     * 当前扫描角度（度）
     */
    private double currentScanAngle;
    
    /**
     * 扫描方向（1为正向，-1为反向）
     */
    private int scanDirection;
    
    /**
     * 最后一次扫描时间
     */
    private LocalDateTime lastScanTime;
    
    /**
     * 历史雷达接触记录
     * Key: 目标ID，Value: 最近的雷达接触
     */
    private final Map<String, RadarContact> historicalContacts;
    
    /**
     * 扫描计数器
     */
    private long scanCounter;
    
    /**
     * 构造函数
     */
    public ElectromagneticRadar() {
        super(DeviceType.ELECTROMAGNETIC_RADAR);
        this.currentScanAngle = 0.0;
        this.scanDirection = 1;
        this.historicalContacts = new HashMap<>();
        this.scanCounter = 0;
    }
    
    // ================ 电磁波雷达特有行为方法 ================
    
    /**
     * 按照其扫描模式在指定空域内执行雷达扫描
     * 根据雷达方程模拟探测无人机
     * 
     * @param airspace 当前空域环境的AirspaceEnvironment对象
     * @param uavs 当前空域中的无人机列表
     * @return List<RadarContact> 雷达探测到的目标列表，包含距离、方位、速度信息
     * @throws IllegalStateException 如果设备未初始化或非活跃状态
     */
    public List<RadarContact> scanArea(AirspaceEnvironment airspace, List<UAV> uavs) {
        if (!isInitialized()) {
            throw new IllegalStateException("雷达设备未初始化");
        }
        if (!isActive()) {
            throw new IllegalStateException("雷达设备未处于活跃状态");
        }
        
        RadarParameters radarParams = (RadarParameters) this.getDetectionParameters();
        if (radarParams == null) {
            throw new IllegalStateException("雷达参数未设置");
        }
        
        List<RadarContact> contacts = new ArrayList<>();
        this.lastScanTime = LocalDateTime.now();
        this.scanCounter++;
        
        // 根据扫描模式执行扫描
        switch (radarParams.getScanPattern()) {
            case FIXED:
                contacts.addAll(performFixedScan(airspace, uavs, radarParams));
                break;
            case SECTOR:
                contacts.addAll(performSectorScan(airspace, uavs, radarParams));
                break;
            case DEGREE_360:
                contacts.addAll(perform360DegreeScan(airspace, uavs, radarParams));
                break;
        }
        
        // 更新历史接触记录
        updateHistoricalContacts(contacts);
        
        return contacts;
    }
    
    /**
     * 获取当前扫描角度
     * 
     * @return 当前扫描角度（度）
     */
    public double getCurrentScanAngle() {
        return currentScanAngle;
    }
    
    /**
     * 获取扫描方向
     * 
     * @return 扫描方向（1为正向，-1为反向）
     */
    public int getScanDirection() {
        return scanDirection;
    }
    
    /**
     * 获取最后扫描时间
     * 
     * @return 最后一次扫描时间
     */
    public LocalDateTime getLastScanTime() {
        return lastScanTime;
    }
    
    /**
     * 获取扫描计数
     * 
     * @return 扫描次数计数器
     */
    public long getScanCounter() {
        return scanCounter;
    }
    
    /**
     * 获取历史接触记录
     * 
     * @return 历史雷达接触映射的副本
     */
    public Map<String, RadarContact> getHistoricalContacts() {
        return new HashMap<>(historicalContacts);
    }
    
    /**
     * 手动设置扫描角度（仅适用于固定扫描模式）
     * 
     * @param angle 目标扫描角度（0-360度）
     * @throws IllegalArgumentException 如果角度超出范围
     * @throws IllegalStateException 如果不是固定扫描模式
     */
    public void setScanAngle(double angle) {
        if (angle < 0.0 || angle >= 360.0) {
            throw new IllegalArgumentException("扫描角度必须在0到360度之间");
        }
        
        RadarParameters params = (RadarParameters) this.getDetectionParameters();
        if (params != null && params.getScanPattern() != RadarParameters.ScanPattern.FIXED) {
            throw new IllegalStateException("只有固定扫描模式才能手动设置扫描角度");
        }
        
        this.currentScanAngle = angle;
    }
    
    /**
     * 重置扫描状态
     */
    public void resetScanState() {
        this.currentScanAngle = 0.0;
        this.scanDirection = 1;
        this.scanCounter = 0;
        this.historicalContacts.clear();
    }
    
    // ================ 抽象方法实现 ================
    
    @Override
    protected void doSpecificInitialization(DeviceInitParamsDTO params) {
        // 验证雷达特定参数
        if (!(params.getDetectionParameters() instanceof RadarParameters)) {
            throw new IllegalArgumentException("电磁波雷达需要RadarParameters类型的参数");
        }
        
        // 初始化扫描状态
        resetScanState();
        
        // 设置初始扫描角度为设备朝向
        this.currentScanAngle = params.getOrientation();
    }
    
    @Override
    protected List<DetectionEvent> doPerformDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
        List<DetectionEvent> detectionEvents = new ArrayList<>();
        
        try {
            // 执行雷达扫描
            List<RadarContact> contacts = scanArea(airspace, uavs);
            
            // 将雷达接触转换为探测事件
            for (RadarContact contact : contacts) {
                DetectionEvent event = new DetectionEvent(
                        UUID.randomUUID().toString(), contact.getDetectionTime(),
                        this.getId(), this.getName(),
                        contact.getTargetId() != null ? contact.getTargetId() : "UNKNOWN",
                        contact.getClassification().getDescription(),
                        contact.getWorldPosition(), contact.getConfidence(),
                        contact.getRange(),
                        String.format("雷达探测到目标，距离:%.1fm，方位:%.1f°，径向速度:%.1fm/s，信噪比:%.1fdB",
                                     contact.getRange(), contact.getAzimuth(), 
                                     contact.getRadialVelocity(), contact.getSignalToNoiseRatio())
                );
                detectionEvents.add(event);
            }
            
        } catch (Exception e) {
            // 记录错误事件
            DetectionEvent errorEvent = new DetectionEvent(
                    UUID.randomUUID().toString(), LocalDateTime.now(),
                    this.getId(), this.getName(),
                    "ERROR", "雷达扫描错误",
                    this.getPosition(), 0.0, 0.0,
                    "雷达扫描过程中发生错误: " + e.getMessage()
            );
            detectionEvents.add(errorEvent);
        }
        
        return detectionEvents;
    }
    
    @Override
    protected void doAdjustParameters(SensorParameters newParams) {
        if (!(newParams instanceof RadarParameters)) {
            throw new IllegalArgumentException("电磁波雷达只接受RadarParameters类型的参数");
        }
        
        RadarParameters newRadarParams = (RadarParameters) newParams;
        
        // 验证新参数的兼容性
        if (!newRadarParams.isValid()) {
            throw new IllegalArgumentException("新的雷达参数无效");
        }
        
        // 如果扫描模式发生变化，重置扫描状态
        RadarParameters oldParams = (RadarParameters) this.getDetectionParameters();
        if (oldParams != null && oldParams.getScanPattern() != newRadarParams.getScanPattern()) {
            resetScanState();
        }
    }
    
    @Override
    protected void doReset() {
        // 重置扫描状态
        resetScanState();
    }
    
    // ================ 私有扫描实现方法 ================
    
    /**
     * 执行固定角度扫描
     */
    private List<RadarContact> performFixedScan(AirspaceEnvironment airspace, List<UAV> uavs, 
                                              RadarParameters params) {
        List<RadarContact> contacts = new ArrayList<>();
        
        // 固定角度扫描，扫描当前角度方向
        for (UAV uav : uavs) {
            RadarContact contact = calculateRadarContact(uav, airspace, params, currentScanAngle);
            if (contact != null && isTargetInBeam(contact, params)) {
                contacts.add(contact);
            }
        }
        
        return contacts;
    }
    
    /**
     * 执行扇形扫描
     */
    private List<RadarContact> performSectorScan(AirspaceEnvironment airspace, List<UAV> uavs, 
                                               RadarParameters params) {
        List<RadarContact> contacts = new ArrayList<>();
        
        // 扇形扫描，通常在±60度范围内扫描
        double sectorRange = 120.0; // 总扫描角度
        double scanStep = params.getBeamWidth(); // 扫描步长
        
        // 更新扫描角度
        currentScanAngle += scanDirection * scanStep;
        
        // 检查是否需要反向扫描
        double baseAngle = this.getOrientation();
        if (currentScanAngle > baseAngle + sectorRange / 2) {
            scanDirection = -1;
            currentScanAngle = baseAngle + sectorRange / 2;
        } else if (currentScanAngle < baseAngle - sectorRange / 2) {
            scanDirection = 1;
            currentScanAngle = baseAngle - sectorRange / 2;
        }
        
        // 扫描当前角度
        for (UAV uav : uavs) {
            RadarContact contact = calculateRadarContact(uav, airspace, params, currentScanAngle);
            if (contact != null && isTargetInBeam(contact, params)) {
                contacts.add(contact);
            }
        }
        
        return contacts;
    }
    
    /**
     * 执行360度扫描
     */
    private List<RadarContact> perform360DegreeScan(AirspaceEnvironment airspace, List<UAV> uavs, 
                                                  RadarParameters params) {
        List<RadarContact> contacts = new ArrayList<>();
        
        // 360度扫描，每次扫描增加一个波束宽度
        double scanStep = params.getBeamWidth();
        currentScanAngle += scanStep;
        
        // 角度归一化
        if (currentScanAngle >= 360.0) {
            currentScanAngle -= 360.0;
        }
        
        // 扫描当前角度
        for (UAV uav : uavs) {
            RadarContact contact = calculateRadarContact(uav, airspace, params, currentScanAngle);
            if (contact != null && isTargetInBeam(contact, params)) {
                contacts.add(contact);
            }
        }
        
        return contacts;
    }
    
    /**
     * 计算雷达接触
     */
    private RadarContact calculateRadarContact(UAV uav, AirspaceEnvironment airspace, 
                                             RadarParameters params, double scanAngle) {
        Position radarPos = this.getPosition();
        Position uavPos = uav.getCurrentPosition();
        
        // 计算距离
        double range = calculateDistance(radarPos, uavPos);
        
        // 检查是否在探测范围内
        if (range > this.getDetectionRange()) {
            return null;
        }
        
        // 计算角度
        double[] angles = calculateTargetAngles(radarPos, uavPos);
        double azimuth = angles[0];
        double elevation = angles[1];
        
        // 计算径向速度
        Velocity uavVel = uav.getCurrentVelocity();
        double radialVelocity = calculateRadialVelocity(radarPos, uavPos, uavVel);
        
        // 计算雷达截面积（简化模型）
        double rcs = estimateRadarCrossSection(uav);
        
        // 应用雷达方程计算信噪比
        double snr = calculateSignalToNoiseRatio(range, rcs, params, airspace);
        
        // 检查是否可探测
        if (snr < params.getMinSNRDetect()) {
            return null;
        }
        
        // 计算置信度
        double confidence = calculateDetectionConfidence(snr, params);
        
        // 分类目标
        RadarContact.TargetClassification classification = classifyTarget(uav, rcs, radialVelocity);
        
        // 计算多普勒频移
        double dopplerShift = calculateDopplerShift(radialVelocity, params.getFrequency());
        
        // 计算测量精度
        double rangeAccuracy = params.getRangeResolution();
        double angleAccuracy = params.getBeamWidth() / 2.0;
        double velocityAccuracy = params.getVelocityResolution();
        
        // 检查是否为新探测
        boolean isNewDetection = !historicalContacts.containsKey(uav.getId());
        
        return new RadarContact(
                UUID.randomUUID().toString(), LocalDateTime.now(), this.getId(), uav.getId(),
                range, azimuth, elevation, radialVelocity, uavPos, uavVel,
                snr, rcs, confidence, classification, dopplerShift,
                rangeAccuracy, angleAccuracy, velocityAccuracy, isNewDetection,
                String.format("扫描角度:%.1f°", scanAngle)
        );
    }
    
    /**
     * 检查目标是否在雷达波束内
     */
    private boolean isTargetInBeam(RadarContact contact, RadarParameters params) {
        double angleDiff = Math.abs(contact.getAzimuth() - currentScanAngle);
        if (angleDiff > 180.0) {
            angleDiff = 360.0 - angleDiff;
        }
        return angleDiff <= params.getBeamWidth() / 2.0;
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
     * 计算目标角度
     */
    private double[] calculateTargetAngles(Position radarPos, Position targetPos) {
        double deltaX = targetPos.getX() - radarPos.getX();
        double deltaY = targetPos.getY() - radarPos.getY();
        double deltaZ = targetPos.getZ() - radarPos.getZ();
        
        // 计算方位角
        double azimuth = Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (azimuth < 0) azimuth += 360;
        
        // 计算仰角
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double elevation = Math.toDegrees(Math.atan2(-deltaZ, horizontalDistance));
        
        return new double[]{azimuth, elevation};
    }
    
    /**
     * 计算径向速度
     */
    private double calculateRadialVelocity(Position radarPos, Position targetPos, Velocity targetVel) {
        if (targetVel == null) {
            return 0.0;
        }
        
        // 计算径向单位向量
        double dx = targetPos.getX() - radarPos.getX();
        double dy = targetPos.getY() - radarPos.getY();
        double dz = targetPos.getZ() - radarPos.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        if (distance == 0.0) {
            return 0.0;
        }
        
        double ux = dx / distance;
        double uy = dy / distance;
        double uz = dz / distance;
        
        // 计算径向速度（点积）
        return targetVel.getVx() * ux + targetVel.getVy() * uy + targetVel.getVz() * uz;
    }
    
    /**
     * 估计雷达截面积
     */
    private double estimateRadarCrossSection(UAV uav) {
        // 简化的RCS模型，基于无人机类型和尺寸
        // 实际应用中应该从UAV的物理属性中获取
        return 0.1; // 默认0.1平方米
    }
    
    /**
     * 计算信噪比
     */
    private double calculateSignalToNoiseRatio(double range, double rcs, RadarParameters params, 
                                             AirspaceEnvironment airspace) {
        // 雷达方程: SNR = (Pt * Gt * Gr * λ² * σ) / ((4π)³ * R⁴ * Pn)
        double wavelength = 3e8 / params.getFrequency();
        double pt = params.getPower();
        double gt = Math.pow(10, params.getGain() / 10.0);
        double gr = gt; // 假设发射和接收增益相同
        double sigma = rcs;
        double r4 = Math.pow(range, 4);
        
        // 计算噪声功率
        double k = 1.38e-23; // 玻尔兹曼常数
        double t = 290.0;    // 标准温度
        double b = 1.0 / params.getPulseWidth(); // 带宽
        double f = Math.pow(10, params.getNoiseFigure() / 10.0);
        double pn = k * t * b * f;
        
        // 环境因素影响
        double atmosphericLoss = calculateAtmosphericLoss(range, params.getFrequency(), airspace);
        
        double snrLinear = (pt * gt * gr * wavelength * wavelength * sigma) / 
                          (Math.pow(4 * Math.PI, 3) * r4 * pn * atmosphericLoss);
        
        return 10 * Math.log10(snrLinear);
    }
    
    /**
     * 计算大气损耗
     */
    private double calculateAtmosphericLoss(double range, double frequency, AirspaceEnvironment airspace) {
        // 简化的大气损耗模型
        double rangeKm = range / 1000.0;
        double freqGHz = frequency / 1e9;
        
        // 基本大气损耗
        double basicLoss = 0.1 * rangeKm * Math.sqrt(freqGHz);
        
        // 天气影响
        if (airspace.getEnvironmentParameters().isRaining()) {
            basicLoss *= 2.0; // 降雨增加损耗
        }
        
        if (airspace.getEnvironmentParameters().getVisibility() < 1000.0) {
            basicLoss *= 1.5; // 低能见度增加损耗
        }
        
        return Math.pow(10, basicLoss / 10.0);
    }
    
    /**
     * 计算探测置信度
     */
    private double calculateDetectionConfidence(double snr, RadarParameters params) {
        double snrRatio = (snr - params.getMinSNRDetect()) / params.getMinSNRDetect();
        double confidence = Math.min(1.0, Math.max(0.0, snrRatio));
        return confidence;
    }
    
    /**
     * 分类目标
     */
    private RadarContact.TargetClassification classifyTarget(UAV uav, double rcs, double radialVelocity) {
        // 简化的目标分类逻辑
        if (rcs < 0.01) {
            return RadarContact.TargetClassification.SMALL_UAV;
        } else if (rcs < 0.1) {
            return RadarContact.TargetClassification.MEDIUM_UAV;
        } else {
            return RadarContact.TargetClassification.LARGE_UAV;
        }
    }
    
    /**
     * 计算多普勒频移
     */
    private double calculateDopplerShift(double radialVelocity, double frequency) {
        double c = 3e8; // 光速
        return 2.0 * frequency * radialVelocity / c;
    }
    
    /**
     * 更新历史接触记录
     */
    private void updateHistoricalContacts(List<RadarContact> contacts) {
        for (RadarContact contact : contacts) {
            if (contact.getTargetId() != null) {
                historicalContacts.put(contact.getTargetId(), contact);
            }
        }
    }
} 