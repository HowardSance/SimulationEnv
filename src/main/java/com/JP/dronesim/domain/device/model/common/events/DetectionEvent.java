package com.JP.dronesim.domain.device.model.common.events;

import com.JP.dronesim.domain.common.valueobjects.Position;
import java.time.LocalDateTime;

/**
 * 探测事件值对象
 * 表示探测设备探测到无人机时产生的事件信息
 * 包含时间戳、探测器信息、被探测UAV信息和置信度等
 * 
 * @author JP
 * @version 1.0
 */
public class DetectionEvent {
    
    /**
     * 事件唯一标识符
     */
    private final String eventId;
    
    /**
     * 探测事件发生的时间戳
     */
    private final LocalDateTime timestamp;
    
    /**
     * 执行探测的设备ID
     */
    private final String detectorId;
    
    /**
     * 执行探测的设备名称
     */
    private final String detectorName;
    
    /**
     * 被探测到的无人机ID
     */
    private final String detectedUavId;
    
    /**
     * 被探测到的无人机名称
     */
    private final String detectedUavName;
    
    /**
     * 探测时无人机的位置
     */
    private final Position detectedPosition;
    
    /**
     * 探测的置信度 (0.0-1.0)
     * 1.0表示100%确信探测正确，0.0表示不确定
     */
    private final double confidence;
    
    /**
     * 探测距离（米）
     */
    private final double detectionDistance;
    
    /**
     * 探测事件的详细描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param eventId 事件唯一标识符
     * @param timestamp 事件时间戳
     * @param detectorId 探测器ID
     * @param detectorName 探测器名称
     * @param detectedUavId 被探测UAV的ID
     * @param detectedUavName 被探测UAV的名称
     * @param detectedPosition 探测时UAV的位置
     * @param confidence 探测置信度
     * @param detectionDistance 探测距离
     * @param description 事件描述
     */
    public DetectionEvent(String eventId, LocalDateTime timestamp, String detectorId, 
                         String detectorName, String detectedUavId, String detectedUavName,
                         Position detectedPosition, double confidence, double detectionDistance,
                         String description) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.detectorId = detectorId;
        this.detectorName = detectorName;
        this.detectedUavId = detectedUavId;
        this.detectedUavName = detectedUavName;
        this.detectedPosition = detectedPosition;
        this.confidence = confidence;
        this.detectionDistance = detectionDistance;
        this.description = description;
        
        // 参数验证
        validateParameters();
    }

    public DetectionEvent(String targetId, LocalDateTime detectionTime, String eventId, LocalDateTime timestamp, String detectorId, String detectorName, String detectedUavId, String detectedUavName, Position detectedPosition, double confidence, double detectionDistance, String description) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.detectorId = detectorId;
        this.detectorName = detectorName;
        this.detectedUavId = detectedUavId;
        this.detectedUavName = detectedUavName;
        this.detectedPosition = detectedPosition;
        this.confidence = confidence;
        this.detectionDistance = detectionDistance;
        this.description = description;
    }

    public DetectionEvent(String targetId, LocalDateTime detectionTime) {
    }

    /**
     * 验证构造参数的有效性
     * 
     * @throws IllegalArgumentException 如果参数无效
     */
    private void validateParameters() {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("事件ID不能为空");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("时间戳不能为空");
        }
        if (detectorId == null || detectorId.trim().isEmpty()) {
            throw new IllegalArgumentException("探测器ID不能为空");
        }
        if (detectedUavId == null || detectedUavId.trim().isEmpty()) {
            throw new IllegalArgumentException("被探测UAV的ID不能为空");
        }
        if (detectedPosition == null) {
            throw new IllegalArgumentException("探测位置不能为空");
        }
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("置信度必须在0.0到1.0之间");
        }
        if (detectionDistance < 0.0) {
            throw new IllegalArgumentException("探测距离不能为负数");
        }
    }
    
    /**
     * 获取事件ID
     * 
     * @return 事件唯一标识符
     */
    public String getEventId() {
        return eventId;
    }
    
    /**
     * 获取事件时间戳
     * 
     * @return 探测事件发生的时间
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取探测器ID
     * 
     * @return 执行探测的设备ID
     */
    public String getDetectorId() {
        return detectorId;
    }
    
    /**
     * 获取探测器名称
     * 
     * @return 执行探测的设备名称
     */
    public String getDetectorName() {
        return detectorName;
    }
    
    /**
     * 获取被探测UAV的ID
     * 
     * @return 被探测到的无人机ID
     */
    public String getDetectedUavId() {
        return detectedUavId;
    }
    
    /**
     * 获取被探测UAV的名称
     * 
     * @return 被探测到的无人机名称
     */
    public String getDetectedUavName() {
        return detectedUavName;
    }
    
    /**
     * 获取探测位置
     * 
     * @return 探测时无人机的位置
     */
    public Position getDetectedPosition() {
        return detectedPosition;
    }
    
    /**
     * 获取探测置信度
     * 
     * @return 探测的置信度 (0.0-1.0)
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * 获取探测距离
     * 
     * @return 探测距离（米）
     */
    public double getDetectionDistance() {
        return detectionDistance;
    }
    
    /**
     * 获取事件描述
     * 
     * @return 探测事件的详细描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 检查是否为高置信度探测
     * 
     * @return true表示高置信度（>=0.8），false表示低置信度
     */
    public boolean isHighConfidence() {
        return confidence >= 0.8;
    }
    
    @Override
    public String toString() {
        return "DetectionEvent{" +
                "eventId='" + eventId + '\'' +
                ", timestamp=" + timestamp +
                ", detectorId='" + detectorId + '\'' +
                ", detectorName='" + detectorName + '\'' +
                ", detectedUavId='" + detectedUavId + '\'' +
                ", detectedUavName='" + detectedUavName + '\'' +
                ", detectedPosition=" + detectedPosition +
                ", confidence=" + confidence +
                ", detectionDistance=" + detectionDistance +
                ", description='" + description + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        DetectionEvent that = (DetectionEvent) o;
        return eventId.equals(that.eventId);
    }
    
    @Override
    public int hashCode() {
        return eventId.hashCode();
    }
} 