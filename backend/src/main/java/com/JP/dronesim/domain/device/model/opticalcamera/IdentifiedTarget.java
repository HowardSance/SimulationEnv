package com.JP.dronesim.domain.device.model.opticalcamera;

import com.JP.dronesim.domain.common.valueobjects.Position;
import java.time.LocalDateTime;

/**
 * 识别目标值对象
 * 表示经过图像处理算法识别出的无人机目标
 * 包含位置、大小、置信度等详细信息
 * 
 * @author JP
 * @version 1.0
 */
public class IdentifiedTarget {
    
    /**
     * 目标唯一标识符
     */
    private final String targetId;
    
    /**
     * 目标识别时间戳
     */
    private final LocalDateTime identifiedTime;
    
    /**
     * 执行识别的摄像头ID
     */
    private final String cameraId;
    
    /**
     * 目标在三维空间中的位置
     */
    private final Position worldPosition;
    
    /**
     * 目标在图像中的像素位置X坐标
     */
    private final int imagePixelX;
    
    /**
     * 目标在图像中的像素位置Y坐标
     */
    private final int imagePixelY;
    
    /**
     * 目标在图像中的像素宽度
     */
    private final int targetPixelWidth;
    
    /**
     * 目标在图像中的像素高度
     */
    private final int targetPixelHeight;
    
    /**
     * 目标与摄像头的直线距离（米）
     */
    private final double distance;
    
    /**
     * 目标相对于摄像头的方位角（度）
     */
    private final double azimuth;
    
    /**
     * 目标相对于摄像头的仰角（度）
     */
    private final double elevation;
    
    /**
     * 识别置信度（0.0-1.0）
     */
    private final double confidence;
    
    /**
     * 目标类型（例如：无人机型号）
     */
    private final String targetType;
    
    /**
     * 目标估计尺寸（米）
     */
    private final double estimatedSize;
    
    /**
     * 目标运动状态
     */
    private final TargetMotionState motionState;
    
    /**
     * 目标可见度评分（0.0-1.0）
     */
    private final double visibilityScore;
    
    /**
     * 额外的识别属性
     */
    private final String additionalInfo;
    
    /**
     * 构造函数
     * 
     * @param targetId 目标ID
     * @param identifiedTime 识别时间
     * @param cameraId 摄像头ID
     * @param worldPosition 世界坐标位置
     * @param imagePixelX 图像X坐标
     * @param imagePixelY 图像Y坐标
     * @param targetPixelWidth 目标像素宽度
     * @param targetPixelHeight 目标像素高度
     * @param distance 距离
     * @param azimuth 方位角
     * @param elevation 仰角
     * @param confidence 置信度
     * @param targetType 目标类型
     * @param estimatedSize 估计尺寸
     * @param motionState 运动状态
     * @param visibilityScore 可见度评分
     * @param additionalInfo 额外信息
     */
    public IdentifiedTarget(String targetId, LocalDateTime identifiedTime, String cameraId,
                          Position worldPosition, int imagePixelX, int imagePixelY,
                          int targetPixelWidth, int targetPixelHeight, double distance,
                          double azimuth, double elevation, double confidence,
                          String targetType, double estimatedSize, TargetMotionState motionState,
                          double visibilityScore, String additionalInfo) {
        this.targetId = targetId;
        this.identifiedTime = identifiedTime;
        this.cameraId = cameraId;
        this.worldPosition = worldPosition;
        this.imagePixelX = imagePixelX;
        this.imagePixelY = imagePixelY;
        this.targetPixelWidth = targetPixelWidth;
        this.targetPixelHeight = targetPixelHeight;
        this.distance = distance;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.confidence = confidence;
        this.targetType = targetType;
        this.estimatedSize = estimatedSize;
        this.motionState = motionState;
        this.visibilityScore = visibilityScore;
        this.additionalInfo = additionalInfo;
        
        // 参数验证
        validateParameters();
    }
    
    /**
     * 验证构造参数
     */
    private void validateParameters() {
        if (targetId == null || targetId.trim().isEmpty()) {
            throw new IllegalArgumentException("目标ID不能为空");
        }
        if (identifiedTime == null) {
            throw new IllegalArgumentException("识别时间不能为空");
        }
        if (cameraId == null || cameraId.trim().isEmpty()) {
            throw new IllegalArgumentException("摄像头ID不能为空");
        }
        if (worldPosition == null) {
            throw new IllegalArgumentException("世界位置不能为空");
        }
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("置信度必须在0.0到1.0之间");
        }
        if (distance < 0.0) {
            throw new IllegalArgumentException("距离不能为负数");
        }
        if (estimatedSize < 0.0) {
            throw new IllegalArgumentException("估计尺寸不能为负数");
        }
        if (visibilityScore < 0.0 || visibilityScore > 1.0) {
            throw new IllegalArgumentException("可见度评分必须在0.0到1.0之间");
        }
    }
    
    /**
     * 获取目标ID
     * 
     * @return 目标唯一标识符
     */
    public String getTargetId() {
        return targetId;
    }
    
    /**
     * 获取识别时间
     * 
     * @return 目标识别时间戳
     */
    public LocalDateTime getIdentifiedTime() {
        return identifiedTime;
    }
    
    /**
     * 获取摄像头ID
     * 
     * @return 执行识别的摄像头ID
     */
    public String getCameraId() {
        return cameraId;
    }
    
    /**
     * 获取世界坐标位置
     * 
     * @return 目标在三维空间中的位置
     */
    public Position getWorldPosition() {
        return worldPosition;
    }
    
    /**
     * 获取图像X坐标
     * 
     * @return 目标在图像中的像素位置X坐标
     */
    public int getImagePixelX() {
        return imagePixelX;
    }
    
    /**
     * 获取图像Y坐标
     * 
     * @return 目标在图像中的像素位置Y坐标
     */
    public int getImagePixelY() {
        return imagePixelY;
    }
    
    /**
     * 获取目标像素宽度
     * 
     * @return 目标在图像中的像素宽度
     */
    public int getTargetPixelWidth() {
        return targetPixelWidth;
    }
    
    /**
     * 获取目标像素高度
     * 
     * @return 目标在图像中的像素高度
     */
    public int getTargetPixelHeight() {
        return targetPixelHeight;
    }
    
    /**
     * 获取距离
     * 
     * @return 目标与摄像头的直线距离（米）
     */
    public double getDistance() {
        return distance;
    }
    
    /**
     * 获取方位角
     * 
     * @return 目标相对于摄像头的方位角（度）
     */
    public double getAzimuth() {
        return azimuth;
    }
    
    /**
     * 获取仰角
     * 
     * @return 目标相对于摄像头的仰角（度）
     */
    public double getElevation() {
        return elevation;
    }
    
    /**
     * 获取置信度
     * 
     * @return 识别置信度（0.0-1.0）
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * 获取目标类型
     * 
     * @return 目标类型（例如：无人机型号）
     */
    public String getTargetType() {
        return targetType;
    }
    
    /**
     * 获取估计尺寸
     * 
     * @return 目标估计尺寸（米）
     */
    public double getEstimatedSize() {
        return estimatedSize;
    }
    
    /**
     * 获取运动状态
     * 
     * @return 目标运动状态
     */
    public TargetMotionState getMotionState() {
        return motionState;
    }
    
    /**
     * 获取可见度评分
     * 
     * @return 目标可见度评分（0.0-1.0）
     */
    public double getVisibilityScore() {
        return visibilityScore;
    }
    
    /**
     * 获取额外信息
     * 
     * @return 额外的识别属性
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    /**
     * 检查是否为高置信度目标
     * 
     * @return true表示高置信度（>=0.8），false表示低置信度
     */
    public boolean isHighConfidence() {
        return confidence >= 0.8;
    }
    
    /**
     * 检查目标是否在近距离范围内
     * 
     * @return true表示近距离（<=500米），false表示远距离
     */
    public boolean isCloseRange() {
        return distance <= 500.0;
    }
    
    /**
     * 检查目标是否清晰可见
     * 
     * @return true表示清晰可见（可见度评分>=0.7），false表示不清晰
     */
    public boolean isClearlyVisible() {
        return visibilityScore >= 0.7;
    }
    
    /**
     * 检查目标是否适合跟踪
     * 
     * @return true表示适合跟踪，false表示不适合
     */
    public boolean isSuitableForTracking() {
        return isHighConfidence() && isClearlyVisible() && 
               targetPixelWidth >= 10 && targetPixelHeight >= 10;
    }
    
    /**
     * 计算目标在图像中的面积（像素）
     * 
     * @return 目标像素面积
     */
    public int getPixelArea() {
        return targetPixelWidth * targetPixelHeight;
    }
    
    @Override
    public String toString() {
        return String.format("IdentifiedTarget[id=%s, camera=%s, time=%s, " +
                           "worldPos=%s, imagePos=(%d,%d), size=%dx%d, " +
                           "distance=%.1fm, azimuth=%.1f°, elevation=%.1f°, " +
                           "confidence=%.2f, type=%s, motion=%s, visibility=%.2f]",
                           targetId, cameraId, identifiedTime, worldPosition,
                           imagePixelX, imagePixelY, targetPixelWidth, targetPixelHeight,
                           distance, azimuth, elevation, confidence, targetType,
                           motionState, visibilityScore);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        IdentifiedTarget that = (IdentifiedTarget) o;
        return targetId.equals(that.targetId);
    }
    
    @Override
    public int hashCode() {
        return targetId.hashCode();
    }
    
    /**
     * 目标运动状态枚举
     */
    public enum TargetMotionState {
        /**
         * 静止状态
         */
        STATIONARY("静止"),
        
        /**
         * 缓慢移动
         */
        SLOW_MOVING("缓慢移动"),
        
        /**
         * 快速移动
         */
        FAST_MOVING("快速移动"),
        
        /**
         * 悬停状态
         */
        HOVERING("悬停"),
        
        /**
         * 未知状态
         */
        UNKNOWN("未知");
        
        private final String description;
        
        TargetMotionState(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 