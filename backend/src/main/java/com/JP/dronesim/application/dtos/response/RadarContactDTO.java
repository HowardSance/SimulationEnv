package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;

import java.time.LocalDateTime;

/**
 * 雷达接触响应DTO
 * 用于向前端返回雷达探测到的目标信息
 * 
 * @author JP
 * @version 1.0
 */
public class RadarContactDTO {
    
    /**
     * 接触唯一标识符
     */
    private String contactId;
    
    /**
     * 探测时间戳
     */
    private LocalDateTime detectionTime;
    
    /**
     * 执行探测的雷达ID
     */
    private String radarId;
    
    /**
     * 目标ID（如果已知）
     */
    private String targetId;
    
    /**
     * 目标与雷达的距离（米）
     */
    private double range;
    
    /**
     * 目标相对于雷达的方位角（度，0-360）
     */
    private double azimuth;
    
    /**
     * 目标相对于雷达的仰角（度，-90到90）
     */
    private double elevation;
    
    /**
     * 径向速度（m/s，正值表示远离雷达）
     */
    private double radialVelocity;
    
    /**
     * 目标在世界坐标系中的位置
     */
    private Position worldPosition;
    
    /**
     * 目标的速度向量
     */
    private Velocity velocity;
    
    /**
     * 信噪比（dB）
     */
    private double signalToNoiseRatio;
    
    /**
     * 雷达截面积（m²）
     */
    private double radarCrossSection;
    
    /**
     * 探测置信度（0.0-1.0）
     */
    private double confidence;
    
    /**
     * 目标类型分类
     */
    private String classification;
    
    /**
     * 多普勒频移（Hz）
     */
    private double dopplerShift;
    
    /**
     * 距离测量精度（米）
     */
    private double rangeAccuracy;
    
    /**
     * 角度测量精度（度）
     */
    private double angleAccuracy;
    
    /**
     * 速度测量精度（m/s）
     */
    private double velocityAccuracy;
    
    /**
     * 是否为新探测目标
     */
    private boolean isNewDetection;
    
    /**
     * 额外的探测信息
     */
    private String additionalInfo;
    
    /**
     * 默认构造函数
     */
    public RadarContactDTO() {
    }
    
    /**
     * 构造函数
     * 
     * @param contactId 接触ID
     * @param detectionTime 探测时间
     * @param radarId 雷达ID
     * @param targetId 目标ID
     * @param range 距离
     * @param azimuth 方位角
     * @param elevation 仰角
     * @param radialVelocity 径向速度
     * @param worldPosition 世界位置
     * @param velocity 速度向量
     * @param signalToNoiseRatio 信噪比
     * @param radarCrossSection 雷达截面积
     * @param confidence 置信度
     * @param classification 目标分类
     * @param dopplerShift 多普勒频移
     * @param rangeAccuracy 距离精度
     * @param angleAccuracy 角度精度
     * @param velocityAccuracy 速度精度
     * @param isNewDetection 是否新探测
     * @param additionalInfo 额外信息
     */
    public RadarContactDTO(String contactId, LocalDateTime detectionTime, String radarId, String targetId,
                          double range, double azimuth, double elevation, double radialVelocity,
                          Position worldPosition, Velocity velocity, double signalToNoiseRatio,
                          double radarCrossSection, double confidence, String classification,
                          double dopplerShift, double rangeAccuracy, double angleAccuracy,
                          double velocityAccuracy, boolean isNewDetection, String additionalInfo) {
        this.contactId = contactId;
        this.detectionTime = detectionTime;
        this.radarId = radarId;
        this.targetId = targetId;
        this.range = range;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.radialVelocity = radialVelocity;
        this.worldPosition = worldPosition;
        this.velocity = velocity;
        this.signalToNoiseRatio = signalToNoiseRatio;
        this.radarCrossSection = radarCrossSection;
        this.confidence = confidence;
        this.classification = classification;
        this.dopplerShift = dopplerShift;
        this.rangeAccuracy = rangeAccuracy;
        this.angleAccuracy = angleAccuracy;
        this.velocityAccuracy = velocityAccuracy;
        this.isNewDetection = isNewDetection;
        this.additionalInfo = additionalInfo;
    }
    
    /**
     * 获取接触ID
     * 
     * @return 接触唯一标识符
     */
    public String getContactId() {
        return contactId;
    }
    
    /**
     * 设置接触ID
     * 
     * @param contactId 接触唯一标识符
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
    /**
     * 获取探测时间
     * 
     * @return 探测时间戳
     */
    public LocalDateTime getDetectionTime() {
        return detectionTime;
    }
    
    /**
     * 设置探测时间
     * 
     * @param detectionTime 探测时间戳
     */
    public void setDetectionTime(LocalDateTime detectionTime) {
        this.detectionTime = detectionTime;
    }
    
    /**
     * 获取雷达ID
     * 
     * @return 执行探测的雷达ID
     */
    public String getRadarId() {
        return radarId;
    }
    
    /**
     * 设置雷达ID
     * 
     * @param radarId 执行探测的雷达ID
     */
    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }
    
    /**
     * 获取目标ID
     * 
     * @return 目标ID（可能为null）
     */
    public String getTargetId() {
        return targetId;
    }
    
    /**
     * 设置目标ID
     * 
     * @param targetId 目标ID
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    
    /**
     * 获取距离
     * 
     * @return 目标与雷达的距离（米）
     */
    public double getRange() {
        return range;
    }
    
    /**
     * 设置距离
     * 
     * @param range 目标与雷达的距离（米）
     */
    public void setRange(double range) {
        this.range = range;
    }
    
    /**
     * 获取方位角
     * 
     * @return 目标相对于雷达的方位角（度）
     */
    public double getAzimuth() {
        return azimuth;
    }
    
    /**
     * 设置方位角
     * 
     * @param azimuth 目标相对于雷达的方位角（度）
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }
    
    /**
     * 获取仰角
     * 
     * @return 目标相对于雷达的仰角（度）
     */
    public double getElevation() {
        return elevation;
    }
    
    /**
     * 设置仰角
     * 
     * @param elevation 目标相对于雷达的仰角（度）
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    
    /**
     * 获取径向速度
     * 
     * @return 径向速度（m/s）
     */
    public double getRadialVelocity() {
        return radialVelocity;
    }
    
    /**
     * 设置径向速度
     * 
     * @param radialVelocity 径向速度（m/s）
     */
    public void setRadialVelocity(double radialVelocity) {
        this.radialVelocity = radialVelocity;
    }
    
    /**
     * 获取世界坐标位置
     * 
     * @return 目标在世界坐标系中的位置
     */
    public Position getWorldPosition() {
        return worldPosition;
    }
    
    /**
     * 设置世界坐标位置
     * 
     * @param worldPosition 目标在世界坐标系中的位置
     */
    public void setWorldPosition(Position worldPosition) {
        this.worldPosition = worldPosition;
    }
    
    /**
     * 获取速度向量
     * 
     * @return 目标的速度向量
     */
    public Velocity getVelocity() {
        return velocity;
    }
    
    /**
     * 设置速度向量
     * 
     * @param velocity 目标的速度向量
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }
    
    /**
     * 获取信噪比
     * 
     * @return 信噪比（dB）
     */
    public double getSignalToNoiseRatio() {
        return signalToNoiseRatio;
    }
    
    /**
     * 设置信噪比
     * 
     * @param signalToNoiseRatio 信噪比（dB）
     */
    public void setSignalToNoiseRatio(double signalToNoiseRatio) {
        this.signalToNoiseRatio = signalToNoiseRatio;
    }
    
    /**
     * 获取雷达截面积
     * 
     * @return 雷达截面积（m²）
     */
    public double getRadarCrossSection() {
        return radarCrossSection;
    }
    
    /**
     * 设置雷达截面积
     * 
     * @param radarCrossSection 雷达截面积（m²）
     */
    public void setRadarCrossSection(double radarCrossSection) {
        this.radarCrossSection = radarCrossSection;
    }
    
    /**
     * 获取探测置信度
     * 
     * @return 探测置信度（0.0-1.0）
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * 设置探测置信度
     * 
     * @param confidence 探测置信度（0.0-1.0）
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    /**
     * 获取目标分类
     * 
     * @return 目标类型分类
     */
    public String getClassification() {
        return classification;
    }
    
    /**
     * 设置目标分类
     * 
     * @param classification 目标类型分类
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }
    
    /**
     * 获取多普勒频移
     * 
     * @return 多普勒频移（Hz）
     */
    public double getDopplerShift() {
        return dopplerShift;
    }
    
    /**
     * 设置多普勒频移
     * 
     * @param dopplerShift 多普勒频移（Hz）
     */
    public void setDopplerShift(double dopplerShift) {
        this.dopplerShift = dopplerShift;
    }
    
    /**
     * 获取距离测量精度
     * 
     * @return 距离测量精度（米）
     */
    public double getRangeAccuracy() {
        return rangeAccuracy;
    }
    
    /**
     * 设置距离测量精度
     * 
     * @param rangeAccuracy 距离测量精度（米）
     */
    public void setRangeAccuracy(double rangeAccuracy) {
        this.rangeAccuracy = rangeAccuracy;
    }
    
    /**
     * 获取角度测量精度
     * 
     * @return 角度测量精度（度）
     */
    public double getAngleAccuracy() {
        return angleAccuracy;
    }
    
    /**
     * 设置角度测量精度
     * 
     * @param angleAccuracy 角度测量精度（度）
     */
    public void setAngleAccuracy(double angleAccuracy) {
        this.angleAccuracy = angleAccuracy;
    }
    
    /**
     * 获取速度测量精度
     * 
     * @return 速度测量精度（m/s）
     */
    public double getVelocityAccuracy() {
        return velocityAccuracy;
    }
    
    /**
     * 设置速度测量精度
     * 
     * @param velocityAccuracy 速度测量精度（m/s）
     */
    public void setVelocityAccuracy(double velocityAccuracy) {
        this.velocityAccuracy = velocityAccuracy;
    }
    
    /**
     * 是否为新探测目标
     * 
     * @return true表示新探测，false表示已知目标
     */
    public boolean isNewDetection() {
        return isNewDetection;
    }
    
    /**
     * 设置是否为新探测目标
     * 
     * @param newDetection true表示新探测，false表示已知目标
     */
    public void setNewDetection(boolean newDetection) {
        isNewDetection = newDetection;
    }
    
    /**
     * 获取额外信息
     * 
     * @return 额外的探测信息
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    /**
     * 设置额外信息
     * 
     * @param additionalInfo 额外的探测信息
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    /**
     * 检查是否为高质量探测
     * 
     * @return true表示高质量（高信噪比和高置信度）
     */
    public boolean isHighQualityDetection() {
        return signalToNoiseRatio >= 15.0 && confidence >= 0.8;
    }
    
    /**
     * 检查目标是否在近距离
     * 
     * @return true表示近距离（<=10km）
     */
    public boolean isCloseRange() {
        return range <= 10000.0;
    }
    
    /**
     * 检查目标是否在移动
     * 
     * @return true表示目标在移动（径向速度绝对值>1m/s）
     */
    public boolean isMoving() {
        return Math.abs(radialVelocity) > 1.0;
    }
    
    /**
     * 检查目标是否正在接近
     * 
     * @return true表示目标正在接近雷达
     */
    public boolean isApproaching() {
        return radialVelocity < -0.5; // 负值表示接近
    }
    
    /**
     * 检查目标是否正在远离
     * 
     * @return true表示目标正在远离雷达
     */
    public boolean isReceding() {
        return radialVelocity > 0.5; // 正值表示远离
    }
    
    /**
     * 计算目标的绝对速度
     * 
     * @return 目标的绝对速度（m/s）
     */
    public double getAbsoluteSpeed() {
        if (velocity != null) {
            return Math.sqrt(velocity.getVx() * velocity.getVx() + 
                           velocity.getVy() * velocity.getVy() + 
                           velocity.getVz() * velocity.getVz());
        }
        return Math.abs(radialVelocity); // 如果没有完整速度向量，使用径向速度
    }
    
    @Override
    public String toString() {
        return String.format("RadarContactDTO[id=%s, radar=%s, target=%s, time=%s, " +
                           "range=%.1fm, azimuth=%.1f°, elevation=%.1f°, radVel=%.1fm/s, " +
                           "worldPos=%s, SNR=%.1fdB, RCS=%.3fm², confidence=%.2f, " +
                           "class=%s, newDetect=%s]",
                           contactId, radarId, targetId, detectionTime, range, azimuth, elevation,
                           radialVelocity, worldPosition, signalToNoiseRatio, radarCrossSection,
                           confidence, classification, isNewDetection);
    }
} 