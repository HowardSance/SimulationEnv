package com.JP.dronesim.domain.uav.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 无人机任务值对象
 * 定义无人机的飞行任务
 * 
 * @author JP Team
 * @version 1.0
 */
public class UAVMission {
    
    /**
     * 任务名称
     */
    private final String missionName;
    
    /**
     * 任务类型
     */
    private final MissionType missionType;
    
    /**
     * 航点列表
     */
    private final List<Waypoint> waypoints;
    
    /**
     * 最大速度限制（m/s）
     */
    private final double maxSpeed;
    
    /**
     * 最大加速度限制（m/s²）
     */
    private final double maxAcceleration;
    
    /**
     * 任务优先级
     */
    private final Priority priority;
    
    /**
     * 任务描述
     */
    private final String description;
    
    /**
     * 预计执行时间（分钟）
     */
    private final int estimatedDurationMinutes;
    
    /**
     * 任务创建时间
     */
    private final LocalDateTime createdAt;
    
    /**
     * 构造函数
     * 
     * @param missionName 任务名称
     * @param missionType 任务类型
     * @param waypoints 航点列表
     * @param maxSpeed 最大速度
     * @param maxAcceleration 最大加速度
     * @param priority 优先级
     * @param description 描述
     * @param estimatedDurationMinutes 预计执行时间
     */
    public UAVMission(String missionName, MissionType missionType, List<Waypoint> waypoints,
                     double maxSpeed, double maxAcceleration, Priority priority,
                     String description, int estimatedDurationMinutes) {
        
        // 参数验证
        if (missionName == null || missionName.trim().isEmpty()) {
            throw new IllegalArgumentException("任务名称不能为空");
        }
        if (missionType == null) {
            throw new IllegalArgumentException("任务类型不能为空");
        }
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("航点列表不能为空");
        }
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException("最大速度必须大于零");
        }
        if (maxAcceleration <= 0) {
            throw new IllegalArgumentException("最大加速度必须大于零");
        }
        if (priority == null) {
            throw new IllegalArgumentException("优先级不能为空");
        }
        if (estimatedDurationMinutes <= 0) {
            throw new IllegalArgumentException("预计执行时间必须大于零");
        }
        
        this.missionName = missionName.trim();
        this.missionType = missionType;
        this.waypoints = new ArrayList<>(waypoints);
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
        this.priority = priority;
        this.description = description != null ? description.trim() : "";
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.createdAt = LocalDateTime.now();
        
        // 验证航点序列
        validateWaypointSequence();
    }
    
    /**
     * 获取任务名称
     * 
     * @return 任务名称
     */
    public String getMissionName() {
        return missionName;
    }
    
    /**
     * 获取任务类型
     * 
     * @return 任务类型
     */
    public MissionType getMissionType() {
        return missionType;
    }
    
    /**
     * 获取航点列表
     * 
     * @return 航点列表（只读）
     */
    public List<Waypoint> getWaypoints() {
        return Collections.unmodifiableList(waypoints);
    }
    
    /**
     * 获取最大速度
     * 
     * @return 最大速度
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }
    
    /**
     * 获取最大加速度
     * 
     * @return 最大加速度
     */
    public double getMaxAcceleration() {
        return maxAcceleration;
    }
    
    /**
     * 获取优先级
     * 
     * @return 优先级
     */
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * 获取描述
     * 
     * @return 描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 获取预计执行时间
     * 
     * @return 预计执行时间（分钟）
     */
    public int getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }
    
    /**
     * 获取创建时间
     * 
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * 获取航点数量
     * 
     * @return 航点数量
     */
    public int getWaypointCount() {
        return waypoints.size();
    }
    
    /**
     * 计算任务总距离
     * 
     * @return 总距离（米）
     */
    public double getTotalDistance() {
        if (waypoints.size() < 2) {
            return 0.0;
        }
        
        double totalDistance = 0.0;
        for (int i = 1; i < waypoints.size(); i++) {
            totalDistance += waypoints.get(i - 1).distanceTo(waypoints.get(i));
        }
        return totalDistance;
    }
    
    /**
     * 检查任务是否为高优先级
     * 
     * @return 是否为高优先级
     */
    public boolean isHighPriority() {
        return priority == Priority.HIGH || priority == Priority.URGENT;
    }
    
    /**
     * 检查任务是否需要紧急执行
     * 
     * @return 是否紧急
     */
    public boolean isUrgent() {
        return priority == Priority.URGENT;
    }
    
    /**
     * 检查任务是否为侦察类型
     * 
     * @return 是否为侦察任务
     */
    public boolean isReconnaissanceMission() {
        return missionType == MissionType.RECONNAISSANCE || 
               missionType == MissionType.SURVEILLANCE;
    }
    
    /**
     * 创建简单巡航任务
     * 
     * @param name 任务名称
     * @param waypoints 航点列表
     * @return 巡航任务
     */
    public static UAVMission createPatrolMission(String name, List<Waypoint> waypoints) {
        return new UAVMission(
            name,
            MissionType.PATROL,
            waypoints,
            15.0, // 默认最大速度15 m/s
            3.0,  // 默认最大加速度3 m/s²
            Priority.NORMAL,
            "标准巡航任务",
            calculateEstimatedDuration(waypoints, 15.0)
        );
    }
    
    /**
     * 创建侦察任务
     * 
     * @param name 任务名称
     * @param waypoints 航点列表
     * @return 侦察任务
     */
    public static UAVMission createReconnaissanceMission(String name, List<Waypoint> waypoints) {
        return new UAVMission(
            name,
            MissionType.RECONNAISSANCE,
            waypoints,
            10.0, // 侦察任务速度较慢
            2.0,  // 加速度较小
            Priority.HIGH,
            "侦察任务",
            calculateEstimatedDuration(waypoints, 10.0)
        );
    }
    
    /**
     * 创建紧急任务
     * 
     * @param name 任务名称
     * @param waypoints 航点列表
     * @return 紧急任务
     */
    public static UAVMission createEmergencyMission(String name, List<Waypoint> waypoints) {
        return new UAVMission(
            name,
            MissionType.EMERGENCY,
            waypoints,
            25.0, // 紧急任务最高速度
            8.0,  // 最大加速度
            Priority.URGENT,
            "紧急任务",
            calculateEstimatedDuration(waypoints, 25.0)
        );
    }
    
    /**
     * 验证航点序列
     */
    private void validateWaypointSequence() {
        // 检查航点序号是否连续
        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint waypoint = waypoints.get(i);
            if (waypoint.getSequenceNumber() != i) {
                throw new IllegalArgumentException("航点序号不连续：期望" + i + "，实际" + waypoint.getSequenceNumber());
            }
        }
        
        // 检查起飞点和降落点
        Waypoint firstWaypoint = waypoints.get(0);
        Waypoint lastWaypoint = waypoints.get(waypoints.size() - 1);
        
        if (firstWaypoint.getType() != Waypoint.WaypointType.TAKEOFF &&
            firstWaypoint.getType() != Waypoint.WaypointType.NORMAL) {
            throw new IllegalArgumentException("第一个航点必须是起飞点或普通航点");
        }
        
        if (lastWaypoint.getType() != Waypoint.WaypointType.LANDING &&
            lastWaypoint.getType() != Waypoint.WaypointType.NORMAL) {
            throw new IllegalArgumentException("最后一个航点必须是降落点或普通航点");
        }
    }
    
    /**
     * 计算预计执行时间
     * 
     * @param waypoints 航点列表
     * @param averageSpeed 平均速度
     * @return 预计执行时间（分钟）
     */
    private static int calculateEstimatedDuration(List<Waypoint> waypoints, double averageSpeed) {
        if (waypoints.size() < 2) {
            return 5; // 最少5分钟
        }
        
        double totalDistance = 0.0;
        double totalHoverTime = 0.0;
        
        for (int i = 1; i < waypoints.size(); i++) {
            totalDistance += waypoints.get(i - 1).distanceTo(waypoints.get(i));
            totalHoverTime += waypoints.get(i).getHoverDuration();
        }
        
        double flightTime = totalDistance / averageSpeed; // 秒
        double totalTime = flightTime + totalHoverTime; // 秒
        
        return Math.max(5, (int) Math.ceil(totalTime / 60.0)); // 转换为分钟，最少5分钟
    }
    
    /**
     * 任务类型枚举
     */
    public enum MissionType {
        /**
         * 巡航任务
         */
        PATROL("巡航"),
        
        /**
         * 侦察任务
         */
        RECONNAISSANCE("侦察"),
        
        /**
         * 监视任务
         */
        SURVEILLANCE("监视"),
        
        /**
         * 运输任务
         */
        TRANSPORT("运输"),
        
        /**
         * 搜救任务
         */
        SEARCH_RESCUE("搜救"),
        
        /**
         * 紧急任务
         */
        EMERGENCY("紧急"),
        
        /**
         * 测试任务
         */
        TEST("测试"),
        
        /**
         * 训练任务
         */
        TRAINING("训练");
        
        private final String description;
        
        MissionType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 优先级枚举
     */
    public enum Priority {
        /**
         * 低优先级
         */
        LOW("低", 1),
        
        /**
         * 普通优先级
         */
        NORMAL("普通", 2),
        
        /**
         * 高优先级
         */
        HIGH("高", 3),
        
        /**
         * 紧急优先级
         */
        URGENT("紧急", 4);
        
        private final String description;
        private final int level;
        
        Priority(String description, int level) {
            this.description = description;
            this.level = level;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getLevel() {
            return level;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UAVMission that = (UAVMission) obj;
        return Double.compare(that.maxSpeed, maxSpeed) == 0 &&
               Double.compare(that.maxAcceleration, maxAcceleration) == 0 &&
               estimatedDurationMinutes == that.estimatedDurationMinutes &&
               Objects.equals(missionName, that.missionName) &&
               missionType == that.missionType &&
               Objects.equals(waypoints, that.waypoints) &&
               priority == that.priority;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(missionName, missionType, waypoints, maxSpeed, 
                          maxAcceleration, priority, estimatedDurationMinutes);
    }
    
    @Override
    public String toString() {
        return String.format("UAVMission{name='%s', type=%s, waypoints=%d, priority=%s, duration=%dmin}",
                           missionName, missionType.getDescription(), waypoints.size(), 
                           priority.getDescription(), estimatedDurationMinutes);
    }
} 