package com.JP.dronesim.domain.device.model.radar;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;

import java.time.LocalDateTime;

/**
 * 雷达接触值对象
 * 表示雷达探测到的目标信息，包含距离、方位、速度等信息
 * 
 * @author JP
 * @version 1.0
 */
public class RadarContact {
    
    /**
     * 接触唯一标识符
     */
    private final String contactId;
    
    /**
     * 探测时间戳
     */
    private final LocalDateTime detectionTime;
    
    /**
     * 执行探测的雷达ID
     */
    private final String radarId;
    
    /**
     * 目标ID（如果已知）
     */
    private final String targetId;
    
    /**
     * 目标与雷达的距离（米）
     */
    private final double range;
    
    /**
     * 目标相对于雷达的方位角（度，0-360）
     */
    private final double azimuth;
    
    /**
     * 目标相对于雷达的仰角（度，-90到90）
     */
    private final double elevation;
    
    /**
     * 径向速度（m/s，正值表示远离雷达）
     */
    private final double radialVelocity;
    
    /**
     * 目标在世界坐标系中的位置
     */
    private final Position worldPosition;
    
    /**
     * 目标的速度向量
     */
    private final Velocity velocity;
    
    /**
     * 信噪比（dB）
     */
    private final double signalToNoiseRatio;
    
    /**
     * 雷达截面积（m²）
     */
    private final double radarCrossSection;
    
    /**
     * 探测置信度（0.0-1.0）
     */
    private final double confidence;
    
    /**
     * 目标类型分类
     */
    private final TargetClassification classification;
    
    /**
     * 多普勒频移（Hz）
     */
    private final double dopplerShift;
    
    /**
     * 距离测量精度（米）
     */
    private final double rangeAccuracy;
    
    /**
     * 角度测量精度（度）
     */
    private final double angleAccuracy;
    
    /**
     * 速度测量精度（m/s）
     */
    private final double velocityAccuracy;
    
    /**
     * 是否为新探测目标
     */
    private final boolean isNewDetection;
    
    /**
     * 额外的探测信息
     */
    private final String additionalInfo;
    
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
    public RadarContact(String contactId, LocalDateTime detectionTime, String radarId, String targetId,
                       double range, double azimuth, double elevation, double radialVelocity,
                       Position worldPosition, Velocity velocity, double signalToNoiseRatio,
                       double radarCrossSection, double confidence, TargetClassification classification,
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
        
        // 参数验证
        validateParameters();
    }
    
    /**
     * 验证构造参数
     */
    private void validateParameters() {
        if (contactId == null || contactId.trim().isEmpty()) {
            throw new IllegalArgumentException("接触ID不能为空");
        }
        if (detectionTime == null) {
            throw new IllegalArgumentException("探测时间不能为空");
        }
        if (radarId == null || radarId.trim().isEmpty()) {
            throw new IllegalArgumentException("雷达ID不能为空");
        }
        if (range < 0.0) {
            throw new IllegalArgumentException("距离不能为负数");
        }
        if (azimuth < 0.0 || azimuth >= 360.0) {
            throw new IllegalArgumentException("方位角必须在0到360度之间");
        }
        if (elevation < -90.0 || elevation > 90.0) {
            throw new IllegalArgumentException("仰角必须在-90到90度之间");
        }
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("置信度必须在0.0到1.0之间");
        }
        if (radarCrossSection < 0.0) {
            throw new IllegalArgumentException("雷达截面积不能为负数");
        }
        if (worldPosition == null) {
            throw new IllegalArgumentException("世界位置不能为空");
        }
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
     * 获取探测时间
     * 
     * @return 探测时间戳
     */
    public LocalDateTime getDetectionTime() {
        return detectionTime;
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
     * 获取目标ID
     * 
     * @return 目标ID（可能为null）
     */
    public String getTargetId() {
        return targetId;
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
     * 获取方位角
     * 
     * @return 目标相对于雷达的方位角（度）
     */
    public double getAzimuth() {
        return azimuth;
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
     * 获取径向速度
     * 
     * @return 径向速度（m/s）
     */
    public double getRadialVelocity() {
        return radialVelocity;
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
     * 获取速度向量
     * 
     * @return 目标的速度向量
     */
    public Velocity getVelocity() {
        return velocity;
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
     * 获取雷达截面积
     * 
     * @return 雷达截面积（m²）
     */
    public double getRadarCrossSection() {
        return radarCrossSection;
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
     * 获取目标分类
     * 
     * @return 目标类型分类
     */
    public TargetClassification getClassification() {
        return classification;
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
     * 获取距离测量精度
     * 
     * @return 距离测量精度（米）
     */
    public double getRangeAccuracy() {
        return rangeAccuracy;
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
     * 获取速度测量精度
     * 
     * @return 速度测量精度（m/s）
     */
    public double getVelocityAccuracy() {
        return velocityAccuracy;
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
     * 获取额外信息
     * 
     * @return 额外的探测信息
     */
    public String getAdditionalInfo() {
        return additionalInfo;
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
    
    /**
     * 计算预测位置
     * 基于当前位置和速度预测未来指定时间后的位置
     * 
     * @param deltaTimeSeconds 预测时间间隔（秒）
     * @return 预测的未来位置
     */
    public Position predictPosition(double deltaTimeSeconds) {
        if (velocity == null) {
            return worldPosition; // 没有速度信息，返回当前位置
        }
        
        return new Position(
                worldPosition.getX() + velocity.getVx() * deltaTimeSeconds,
                worldPosition.getY() + velocity.getVy() * deltaTimeSeconds,
                worldPosition.getZ() + velocity.getVz() * deltaTimeSeconds
        );
    }
    
    @Override
    public String toString() {
        return String.format("RadarContact[id=%s, radar=%s, target=%s, time=%s, " +
                           "range=%.1fm, azimuth=%.1f°, elevation=%.1f°, radVel=%.1fm/s, " +
                           "worldPos=%s, SNR=%.1fdB, RCS=%.3fm², confidence=%.2f, " +
                           "class=%s, newDetect=%s]",
                           contactId, radarId, targetId, detectionTime, range, azimuth, elevation,
                           radialVelocity, worldPosition, signalToNoiseRatio, radarCrossSection,
                           confidence, classification, isNewDetection);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RadarContact that = (RadarContact) o;
        return contactId.equals(that.contactId);
    }
    
    @Override
    public int hashCode() {
        return contactId.hashCode();
    }
    
    /**
     * 目标分类枚举
     */
    public enum TargetClassification {
        /**
         * 未知目标
         */
        UNKNOWN("未知"),
        
        /**
         * 小型无人机
         */
        SMALL_UAV("小型无人机"),
        
        /**
         * 中型无人机
         */
        MEDIUM_UAV("中型无人机"),
        
        /**
         * 大型无人机
         */
        LARGE_UAV("大型无人机"),
        
        /**
         * 固定翼飞机
         */
        FIXED_WING("固定翼飞机"),
        
        /**
         * 旋翼飞机
         */
        ROTORCRAFT("旋翼飞机"),
        
        /**
         * 鸟类
         */
        BIRD("鸟类"),
        
        /**
         * 气象目标
         */
        WEATHER("气象目标"),
        
        /**
         * 杂波
         */
        CLUTTER("杂波"),
        
        /**
         * 干扰信号
         */
        INTERFERENCE("干扰信号");
        
        private final String description;
        
        TargetClassification(String description) {
            this.description = description;
        }
        
        /**
         * 获取分类描述
         * 
         * @return 分类描述
         */
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
} 