package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.common.valueobjects.Position;

import java.time.LocalDateTime;

/**
 * 雷达状态响应DTO
 * 用于向前端返回雷达设备的当前状态信息
 * 
 * @author JP
 * @version 1.0
 */
public class RadarStatusDTO {
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 设备名称
     */
    private String deviceName;
    
    /**
     * 设备位置
     */
    private Position position;
    
    /**
     * 设备朝向（度）
     */
    private double orientation;
    
    /**
     * 设备仰角（度）
     */
    private double elevation;
    
    /**
     * 探测距离（米）
     */
    private double detectionRange;
    
    /**
     * 视场角（度）
     */
    private double fieldOfView;
    
    /**
     * 设备运行状态
     */
    private String status;
    
    /**
     * 是否已初始化
     */
    private boolean initialized;
    
    /**
     * 是否活跃
     */
    private boolean active;
    
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
     * 扫描计数器
     */
    private long scanCounter;
    
    /**
     * 雷达参数描述
     */
    private String parametersDescription;
    
    /**
     * 工作频率（Hz）
     */
    private double frequency;
    
    /**
     * 发射功率（Watts）
     */
    private double power;
    
    /**
     * 天线增益（dB）
     */
    private double gain;
    
    /**
     * 扫描模式
     */
    private String scanPattern;
    
    /**
     * 脉冲重复频率（Hz）
     */
    private double pulseRepetitionFrequency;
    
    /**
     * 波束宽度（度）
     */
    private double beamWidth;
    
    /**
     * 最大不模糊距离（米）
     */
    private double maxUnambiguousRange;
    
    /**
     * 距离分辨率（米）
     */
    private double rangeResolution;
    
    /**
     * 速度分辨率（m/s）
     */
    private double velocityResolution;
    
    /**
     * 历史接触记录数量
     */
    private int historicalContactsCount;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;
    
    /**
     * 默认构造函数
     */
    public RadarStatusDTO() {
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    /**
     * 构造函数
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @param fieldOfView 视场角
     * @param status 运行状态
     * @param initialized 是否已初始化
     * @param active 是否活跃
     */
    public RadarStatusDTO(String deviceId, String deviceName, Position position,
                         double orientation, double elevation, double detectionRange,
                         double fieldOfView, String status, boolean initialized, boolean active) {
        this();
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.position = position;
        this.orientation = orientation;
        this.elevation = elevation;
        this.detectionRange = detectionRange;
        this.fieldOfView = fieldOfView;
        this.status = status;
        this.initialized = initialized;
        this.active = active;
    }
    
    /**
     * 获取设备ID
     * 
     * @return 设备ID
     */
    public String getDeviceId() {
        return deviceId;
    }
    
    /**
     * 设置设备ID
     * 
     * @param deviceId 设备ID
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    /**
     * 获取设备名称
     * 
     * @return 设备名称
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    /**
     * 设置设备名称
     * 
     * @param deviceName 设备名称
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    /**
     * 获取设备位置
     * 
     * @return 设备位置
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 设置设备位置
     * 
     * @param position 设备位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    
    /**
     * 获取朝向
     * 
     * @return 朝向角度
     */
    public double getOrientation() {
        return orientation;
    }
    
    /**
     * 设置朝向
     * 
     * @param orientation 朝向角度
     */
    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }
    
    /**
     * 获取仰角
     * 
     * @return 仰角
     */
    public double getElevation() {
        return elevation;
    }
    
    /**
     * 设置仰角
     * 
     * @param elevation 仰角
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    
    /**
     * 获取探测距离
     * 
     * @return 探测距离
     */
    public double getDetectionRange() {
        return detectionRange;
    }
    
    /**
     * 设置探测距离
     * 
     * @param detectionRange 探测距离
     */
    public void setDetectionRange(double detectionRange) {
        this.detectionRange = detectionRange;
    }
    
    /**
     * 获取视场角
     * 
     * @return 视场角
     */
    public double getFieldOfView() {
        return fieldOfView;
    }
    
    /**
     * 设置视场角
     * 
     * @param fieldOfView 视场角
     */
    public void setFieldOfView(double fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
    
    /**
     * 获取运行状态
     * 
     * @return 运行状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 设置运行状态
     * 
     * @param status 运行状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * 是否已初始化
     * 
     * @return true表示已初始化
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 设置是否已初始化
     * 
     * @param initialized true表示已初始化
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    
    /**
     * 是否活跃
     * 
     * @return true表示活跃
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * 设置是否活跃
     * 
     * @param active true表示活跃
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * 获取当前扫描角度
     * 
     * @return 当前扫描角度
     */
    public double getCurrentScanAngle() {
        return currentScanAngle;
    }
    
    /**
     * 设置当前扫描角度
     * 
     * @param currentScanAngle 当前扫描角度
     */
    public void setCurrentScanAngle(double currentScanAngle) {
        this.currentScanAngle = currentScanAngle;
    }
    
    /**
     * 获取扫描方向
     * 
     * @return 扫描方向
     */
    public int getScanDirection() {
        return scanDirection;
    }
    
    /**
     * 设置扫描方向
     * 
     * @param scanDirection 扫描方向
     */
    public void setScanDirection(int scanDirection) {
        this.scanDirection = scanDirection;
    }
    
    /**
     * 获取最后扫描时间
     * 
     * @return 最后扫描时间
     */
    public LocalDateTime getLastScanTime() {
        return lastScanTime;
    }
    
    /**
     * 设置最后扫描时间
     * 
     * @param lastScanTime 最后扫描时间
     */
    public void setLastScanTime(LocalDateTime lastScanTime) {
        this.lastScanTime = lastScanTime;
    }
    
    /**
     * 获取扫描计数
     * 
     * @return 扫描计数
     */
    public long getScanCounter() {
        return scanCounter;
    }
    
    /**
     * 设置扫描计数
     * 
     * @param scanCounter 扫描计数
     */
    public void setScanCounter(long scanCounter) {
        this.scanCounter = scanCounter;
    }
    
    /**
     * 获取参数描述
     * 
     * @return 参数描述
     */
    public String getParametersDescription() {
        return parametersDescription;
    }
    
    /**
     * 设置参数描述
     * 
     * @param parametersDescription 参数描述
     */
    public void setParametersDescription(String parametersDescription) {
        this.parametersDescription = parametersDescription;
    }
    
    /**
     * 获取工作频率
     * 
     * @return 工作频率
     */
    public double getFrequency() {
        return frequency;
    }
    
    /**
     * 设置工作频率
     * 
     * @param frequency 工作频率
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    
    /**
     * 获取发射功率
     * 
     * @return 发射功率
     */
    public double getPower() {
        return power;
    }
    
    /**
     * 设置发射功率
     * 
     * @param power 发射功率
     */
    public void setPower(double power) {
        this.power = power;
    }
    
    /**
     * 获取天线增益
     * 
     * @return 天线增益
     */
    public double getGain() {
        return gain;
    }
    
    /**
     * 设置天线增益
     * 
     * @param gain 天线增益
     */
    public void setGain(double gain) {
        this.gain = gain;
    }
    
    /**
     * 获取扫描模式
     * 
     * @return 扫描模式
     */
    public String getScanPattern() {
        return scanPattern;
    }
    
    /**
     * 设置扫描模式
     * 
     * @param scanPattern 扫描模式
     */
    public void setScanPattern(String scanPattern) {
        this.scanPattern = scanPattern;
    }
    
    /**
     * 获取脉冲重复频率
     * 
     * @return 脉冲重复频率
     */
    public double getPulseRepetitionFrequency() {
        return pulseRepetitionFrequency;
    }
    
    /**
     * 设置脉冲重复频率
     * 
     * @param pulseRepetitionFrequency 脉冲重复频率
     */
    public void setPulseRepetitionFrequency(double pulseRepetitionFrequency) {
        this.pulseRepetitionFrequency = pulseRepetitionFrequency;
    }
    
    /**
     * 获取波束宽度
     * 
     * @return 波束宽度
     */
    public double getBeamWidth() {
        return beamWidth;
    }
    
    /**
     * 设置波束宽度
     * 
     * @param beamWidth 波束宽度
     */
    public void setBeamWidth(double beamWidth) {
        this.beamWidth = beamWidth;
    }
    
    /**
     * 获取最大不模糊距离
     * 
     * @return 最大不模糊距离
     */
    public double getMaxUnambiguousRange() {
        return maxUnambiguousRange;
    }
    
    /**
     * 设置最大不模糊距离
     * 
     * @param maxUnambiguousRange 最大不模糊距离
     */
    public void setMaxUnambiguousRange(double maxUnambiguousRange) {
        this.maxUnambiguousRange = maxUnambiguousRange;
    }
    
    /**
     * 获取距离分辨率
     * 
     * @return 距离分辨率
     */
    public double getRangeResolution() {
        return rangeResolution;
    }
    
    /**
     * 设置距离分辨率
     * 
     * @param rangeResolution 距离分辨率
     */
    public void setRangeResolution(double rangeResolution) {
        this.rangeResolution = rangeResolution;
    }
    
    /**
     * 获取速度分辨率
     * 
     * @return 速度分辨率
     */
    public double getVelocityResolution() {
        return velocityResolution;
    }
    
    /**
     * 设置速度分辨率
     * 
     * @param velocityResolution 速度分辨率
     */
    public void setVelocityResolution(double velocityResolution) {
        this.velocityResolution = velocityResolution;
    }
    
    /**
     * 获取历史接触记录数量
     * 
     * @return 历史接触记录数量
     */
    public int getHistoricalContactsCount() {
        return historicalContactsCount;
    }
    
    /**
     * 设置历史接触记录数量
     * 
     * @param historicalContactsCount 历史接触记录数量
     */
    public void setHistoricalContactsCount(int historicalContactsCount) {
        this.historicalContactsCount = historicalContactsCount;
    }
    
    /**
     * 获取最后更新时间
     * 
     * @return 最后更新时间
     */
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * 设置最后更新时间
     * 
     * @param lastUpdateTime 最后更新时间
     */
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    /**
     * 检查雷达是否正常工作
     * 
     * @return true表示正常工作
     */
    public boolean isOperational() {
        return initialized && active && "active".equalsIgnoreCase(status);
    }
    
    /**
     * 计算扫描频率（次/分钟）
     * 
     * @return 扫描频率，如果没有扫描记录则返回0
     */
    public double getScanFrequency() {
        if (lastScanTime == null || scanCounter == 0) {
            return 0.0;
        }
        
        // 简化计算：假设扫描是连续的
        return scanCounter > 1 ? 60.0 : 0.0; // 返回每分钟扫描次数的估算值
    }
    
    /**
     * 获取雷达性能等级
     * 
     * @return 性能等级（HIGH/MEDIUM/LOW）
     */
    public String getPerformanceLevel() {
        if (frequency > 20e9 && power > 1500 && gain > 35) {
            return "HIGH";
        } else if (frequency > 5e9 && power > 500 && gain > 25) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    @Override
    public String toString() {
        return String.format("RadarStatusDTO[id=%s, name=%s, pos=%s, orient=%.1f°, " +
                           "elev=%.1f°, range=%.0fm, status=%s, active=%s, " +
                           "scanAngle=%.1f°, scanCount=%d, freq=%.0fMHz]",
                           deviceId, deviceName, position, orientation, elevation,
                           detectionRange, status, active, currentScanAngle, scanCounter,
                           frequency / 1e6);
    }
} 