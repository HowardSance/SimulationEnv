package com.JP.dronesim.domain.airspace.model;

/**
 * 仿真状态枚举
 * 定义仿真系统的运行状态
 * 
 * @author JP Team
 * @version 1.0
 */
public enum SimulationState {
    
    /**
     * 停止状态 - 仿真未运行
     */
    STOPPED("已停止", "仿真系统未运行"),
    
    /**
     * 运行状态 - 仿真正在进行
     */
    RUNNING("运行中", "仿真系统正在运行"),
    
    /**
     * 暂停状态 - 仿真已暂停
     */
    PAUSED("已暂停", "仿真系统已暂停"),
    
    /**
     * 错误状态 - 仿真出现错误
     */
    ERROR("错误", "仿真系统出现错误");
    
    /**
     * 状态名称
     */
    private final String displayName;
    
    /**
     * 状态描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param displayName 状态显示名称
     * @param description 状态描述
     */
    SimulationState(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * 获取状态显示名称
     * 
     * @return 状态显示名称
     */
    public String getDisplayName() {
        return displayName;
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
     * 判断是否为运行状态
     * 
     * @return 是否运行中
     */
    public boolean isRunning() {
        return this == RUNNING;
    }
    
    /**
     * 判断是否为停止状态
     * 
     * @return 是否已停止
     */
    public boolean isStopped() {
        return this == STOPPED;
    }
    
    /**
     * 判断是否为暂停状态
     * 
     * @return 是否已暂停
     */
    public boolean isPaused() {
        return this == PAUSED;
    }
    
    /**
     * 判断是否为错误状态
     * 
     * @return 是否出错
     */
    public boolean isError() {
        return this == ERROR;
    }
    
    /**
     * 判断是否可以启动仿真
     * 
     * @return 是否可以启动
     */
    public boolean canStart() {
        return this == STOPPED || this == ERROR;
    }
    
    /**
     * 判断是否可以暂停仿真
     * 
     * @return 是否可以暂停
     */
    public boolean canPause() {
        return this == RUNNING;
    }
    
    /**
     * 判断是否可以恢复仿真
     * 
     * @return 是否可以恢复
     */
    public boolean canResume() {
        return this == PAUSED;
    }
    
    /**
     * 判断是否可以停止仿真
     * 
     * @return 是否可以停止
     */
    public boolean canStop() {
        return this == RUNNING || this == PAUSED || this == ERROR;
    }
    
    /**
     * 获取下一个有效状态（用于状态转换验证）
     * 
     * @param targetState 目标状态
     * @return 是否可以转换到目标状态
     */
    public boolean canTransitionTo(SimulationState targetState) {
        switch (this) {
            case STOPPED:
                return targetState == RUNNING;
            case RUNNING:
                return targetState == PAUSED || targetState == STOPPED || targetState == ERROR;
            case PAUSED:
                return targetState == RUNNING || targetState == STOPPED;
            case ERROR:
                return targetState == STOPPED || targetState == RUNNING;
            default:
                return false;
        }
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 