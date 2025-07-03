package com.JP.dronesim.domain.common.enums;

/**
 * 无人机运行状态枚举
 * 定义无人机在仿真过程中的各种状态
 * 
 * @author JP Team
 * @version 1.0
 */
public enum UAVStatus {
    
    /**
     * 悬停状态
     * 无人机保持在固定位置，速度为零或接近零
     */
    HOVERING("悬停", "无人机保持在固定位置"),
    
    /**
     * 运动状态
     * 无人机正在按照飞行路径移动
     */
    MOVING("运动", "无人机正在移动"),
    
    /**
     * 侦测状态
     * 无人机正在执行侦察或监视任务
     */
    DETECTING("侦测", "无人机正在执行侦测任务"),
    
    /**
     * 待机状态
     * 无人机已准备就绪但未执行任务
     */
    STANDBY("待机", "无人机处于待机状态"),
    
    /**
     * 返航状态
     * 无人机正在返回起始位置或指定位置
     */
    RETURNING("返航", "无人机正在返航"),
    
    /**
     * 故障状态
     * 无人机出现故障，无法正常工作
     */
    MALFUNCTION("故障", "无人机出现故障"),
    
    /**
     * 被探测状态
     * 无人机被探测设备发现
     */
    DETECTED("被探测", "无人机被探测设备发现"),
    
    /**
     * 受损状态
     * 无人机受到干扰或攻击，性能受损
     */
    DAMAGED("受损", "无人机性能受损"),
    
    /**
     * 失联状态
     * 无人机与控制中心失去联系
     */
    LOST_CONNECTION("失联", "无人机与控制中心失去联系"),
    
    /**
     * 电量不足状态
     * 无人机电量低，需要返航或降落
     */
    LOW_BATTERY("电量不足", "无人机电量不足"),
    
    /**
     * 降落状态
     * 无人机正在降落
     */
    LANDING("降落", "无人机正在降落"),
    
    /**
     * 已降落状态
     * 无人机已成功降落
     */
    LANDED("已降落", "无人机已降落"),
    
    /**
     * 起飞状态
     * 无人机正在起飞
     */
    TAKING_OFF("起飞", "无人机正在起飞"),
    
    /**
     * 离线状态
     * 无人机未激活或已关闭
     */
    OFFLINE("离线", "无人机未激活");
    
    /**
     * 状态名称
     */
    private final String name;
    
    /**
     * 状态描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param name 状态名称
     * @param description 状态描述
     */
    UAVStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * 获取状态名称
     * 
     * @return 状态名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取状态描述
     * 
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 检查是否为正常运行状态
     * 
     * @return 是否为正常状态
     */
    public boolean isOperational() {
        return this == HOVERING || this == MOVING || this == DETECTING || 
               this == STANDBY || this == RETURNING || this == TAKING_OFF;
    }
    
    /**
     * 检查是否为异常状态
     * 
     * @return 是否为异常状态
     */
    public boolean isAbnormal() {
        return this == MALFUNCTION || this == DAMAGED || this == LOST_CONNECTION || 
               this == LOW_BATTERY;
    }
    
    /**
     * 检查是否为移动状态
     * 
     * @return 是否正在移动
     */
    public boolean isMoving() {
        return this == MOVING || this == RETURNING || this == TAKING_OFF || this == LANDING;
    }
    
    /**
     * 检查是否为静止状态
     * 
     * @return 是否为静止状态
     */
    public boolean isStationary() {
        return this == HOVERING || this == STANDBY || this == LANDED || this == OFFLINE;
    }
    
    /**
     * 检查是否可以接受新指令
     * 
     * @return 是否可以接受指令
     */
    public boolean canAcceptCommands() {
        return isOperational() && this != MALFUNCTION && this != LOST_CONNECTION;
    }
    
    /**
     * 检查是否需要紧急处理
     * 
     * @return 是否需要紧急处理
     */
    public boolean requiresEmergencyAction() {
        return this == MALFUNCTION || this == LOST_CONNECTION || this == LOW_BATTERY;
    }
    
    /**
     * 根据状态名称获取枚举值
     * 
     * @param name 状态名称
     * @return 对应的枚举值，如果未找到则返回null
     */
    public static UAVStatus fromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        for (UAVStatus status : values()) {
            if (status.name.equals(name.trim())) {
                return status;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", name, description);
    }
} 