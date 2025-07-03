package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * 航点实体
 * 表示无人机飞行路径上的一个航点
 * 
 * @author JP Team
 * @version 1.0
 */
public class Waypoint {
    
    /**
     * 航点唯一标识符
     */
    private final String waypointId;
    
    /**
     * 航点序号（在飞行路径中的顺序）
     */
    private final int sequenceNumber;
    
    /**
     * 航点位置
     */
    private final Position position;
    
    /**
     * 目标高度（米）
     */
    private final double altitude;
    
    /**
     * 目标速度（m/s）
     */
    private final double targetSpeed;
    
    /**
     * 在此航点的悬停时间（秒）
     * 0表示不悬停，直接通过
     */
    private final double hoverDuration;
    
    /**
     * 航点类型
     */
    private final WaypointType type;
    
    /**
     * 航点名称或描述
     */
    private final String name;
    
    /**
     * 预计到达时间
     */
    private final LocalDateTime estimatedArrivalTime;
    
    /**
     * 实际到达时间
     */
    private LocalDateTime actualArrivalTime;
    
    /**
     * 是否已到达此航点
     */
    private boolean reached;
    
    /**
     * 航点动作（可选）
     */
    private final WaypointAction action;
    
    /**
     * 构造函数
     * 
     * @param sequenceNumber 航点序号
     * @param position 航点位置
     * @param altitude 目标高度
     * @param targetSpeed 目标速度
     * @param hoverDuration 悬停时间
     * @param type 航点类型
     * @param name 航点名称
     * @param estimatedArrivalTime 预计到达时间
     * @param action 航点动作
     */
    public Waypoint(int sequenceNumber, Position position, double altitude, 
                   double targetSpeed, double hoverDuration, WaypointType type,
                   String name, LocalDateTime estimatedArrivalTime, WaypointAction action) {
        
        // 参数验证
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("航点序号不能为负数");
        }
        if (position == null) {
            throw new IllegalArgumentException("航点位置不能为空");
        }
        if (targetSpeed < 0) {
            throw new IllegalArgumentException("目标速度不能为负数");
        }
        if (hoverDuration < 0) {
            throw new IllegalArgumentException("悬停时间不能为负数");
        }
        if (type == null) {
            throw new IllegalArgumentException("航点类型不能为空");
        }
        
        this.waypointId = UUID.randomUUID().toString();
        this.sequenceNumber = sequenceNumber;
        this.position = position;
        this.altitude = altitude;
        this.targetSpeed = targetSpeed;
        this.hoverDuration = hoverDuration;
        this.type = type;
        this.name = name != null ? name.trim() : "航点-" + sequenceNumber;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.action = action;
        this.reached = false;
    }
    
    /**
     * 获取航点ID
     * 
     * @return 航点ID
     */
    public String getWaypointId() {
        return waypointId;
    }
    
    /**
     * 获取航点序号
     * 
     * @return 航点序号
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * 获取航点位置
     * 
     * @return 航点位置
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 获取目标高度
     * 
     * @return 目标高度
     */
    public double getAltitude() {
        return altitude;
    }
    
    /**
     * 获取目标速度
     * 
     * @return 目标速度
     */
    public double getTargetSpeed() {
        return targetSpeed;
    }
    
    /**
     * 获取悬停时间
     * 
     * @return 悬停时间
     */
    public double getHoverDuration() {
        return hoverDuration;
    }
    
    /**
     * 获取航点类型
     * 
     * @return 航点类型
     */
    public WaypointType getType() {
        return type;
    }
    
    /**
     * 获取航点名称
     * 
     * @return 航点名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取预计到达时间
     * 
     * @return 预计到达时间
     */
    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }
    
    /**
     * 获取实际到达时间
     * 
     * @return 实际到达时间
     */
    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }
    
    /**
     * 检查是否已到达
     * 
     * @return 是否已到达
     */
    public boolean isReached() {
        return reached;
    }
    
    /**
     * 获取航点动作
     * 
     * @return 航点动作
     */
    public WaypointAction getAction() {
        return action;
    }
    
    /**
     * 标记航点已到达
     * 
     * @param arrivalTime 实际到达时间
     */
    public void markAsReached(LocalDateTime arrivalTime) {
        this.reached = true;
        this.actualArrivalTime = arrivalTime != null ? arrivalTime : LocalDateTime.now();
    }
    
    /**
     * 标记航点已到达（使用当前时间）
     */
    public void markAsReached() {
        markAsReached(LocalDateTime.now());
    }
    
    /**
     * 检查是否需要悬停
     * 
     * @return 是否需要悬停
     */
    public boolean requiresHover() {
        return hoverDuration > 0;
    }
    
    /**
     * 检查是否有特殊动作
     * 
     * @return 是否有特殊动作
     */
    public boolean hasAction() {
        return action != null && action != WaypointAction.NONE;
    }
    
    /**
     * 计算到另一个航点的距离
     * 
     * @param other 另一个航点
     * @return 距离（米）
     */
    public double distanceTo(Waypoint other) {
        if (other == null) {
            throw new IllegalArgumentException("目标航点不能为空");
        }
        return this.position.distanceTo(other.position);
    }
    
    /**
     * 计算到指定位置的距离
     * 
     * @param targetPosition 目标位置
     * @return 距离（米）
     */
    public double distanceTo(Position targetPosition) {
        if (targetPosition == null) {
            throw new IllegalArgumentException("目标位置不能为空");
        }
        return this.position.distanceTo(targetPosition);
    }
    
    /**
     * 检查无人机是否已接近此航点
     * 
     * @param currentPosition 当前位置
     * @param tolerance 容差距离（米）
     * @return 是否接近航点
     */
    public boolean isNearby(Position currentPosition, double tolerance) {
        if (currentPosition == null) {
            throw new IllegalArgumentException("当前位置不能为空");
        }
        if (tolerance <= 0) {
            throw new IllegalArgumentException("容差距离必须大于零");
        }
        
        return distanceTo(currentPosition) <= tolerance;
    }
    
    /**
     * 检查无人机是否已接近此航点（使用默认容差5米）
     * 
     * @param currentPosition 当前位置
     * @return 是否接近航点
     */
    public boolean isNearby(Position currentPosition) {
        return isNearby(currentPosition, 5.0); // 默认容差5米
    }
    
    /**
     * 航点类型枚举
     */
    public enum WaypointType {
        /**
         * 普通航点
         */
        NORMAL("普通航点"),
        
        /**
         * 起飞点
         */
        TAKEOFF("起飞点"),
        
        /**
         * 降落点
         */
        LANDING("降落点"),
        
        /**
         * 悬停点
         */
        HOVER("悬停点"),
        
        /**
         * 观察点
         */
        OBSERVATION("观察点"),
        
        /**
         * 转向点
         */
        TURN("转向点"),
        
        /**
         * 紧急点
         */
        EMERGENCY("紧急点");
        
        private final String description;
        
        WaypointType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 航点动作枚举
     */
    public enum WaypointAction {
        /**
         * 无动作
         */
        NONE("无动作"),
        
        /**
         * 拍照
         */
        TAKE_PHOTO("拍照"),
        
        /**
         * 录像
         */
        START_RECORDING("开始录像"),
        
        /**
         * 停止录像
         */
        STOP_RECORDING("停止录像"),
        
        /**
         * 扫描
         */
        SCAN("扫描"),
        
        /**
         * 发送信号
         */
        SEND_SIGNAL("发送信号"),
        
        /**
         * 等待指令
         */
        WAIT_FOR_COMMAND("等待指令");
        
        private final String description;
        
        WaypointAction(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Waypoint waypoint = (Waypoint) obj;
        return Objects.equals(waypointId, waypoint.waypointId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(waypointId);
    }
    
    @Override
    public String toString() {
        return String.format("Waypoint{序号=%d, 名称='%s', 位置=%s, 高度=%.1fm, " +
                           "速度=%.1fm/s, 类型=%s, 已到达=%s}",
                           sequenceNumber, name, position, altitude, targetSpeed, 
                           type.getDescription(), reached ? "是" : "否");
    }
} 