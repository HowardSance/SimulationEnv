package com.JP.dronesim.domain.common.enums;

/**
 * 探测设备运行状态枚举
 * 定义探测设备的所有可能运行状态
 *
 * @author JP
 * @version 1.0
 */
public enum DeviceStatus {

    /**
     * 设备正常运行状态
     * 设备可以正常执行探测任务
     */
    ACTIVE("active", "运行中"),

    /**
     * 设备非活跃状态
     * 设备处于待机或关闭状态，不执行探测任务
     */
    INACTIVE("inactive", "非活跃"),

    /**
     * 设备错误状态
     * 设备出现故障，无法正常工作
     */
    ERROR("error", "错误");

    /**
     * 状态代码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param code 状态代码
     * @param description 状态描述
     */
    DeviceStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取状态代码
     * 
     * @return 状态代码
     */
    public String getCode() {
        return code;
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
     * 根据代码获取设备状态
     * 
     * @param code 状态代码
     * @return 对应的设备状态枚举
     * @throws IllegalArgumentException 如果代码不存在
     */
    public static DeviceStatus fromCode(String code) {
        for (DeviceStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的设备状态代码: " + code);
    }
    
    /**
     * 检查设备是否处于活跃状态
     * 
     * @return true表示设备可以执行探测任务，false表示不可以
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 检查设备是否处于错误状态
     * 
     * @return true表示设备有故障，false表示正常
     */
    public boolean isError() {
        return this == ERROR;
    }
} 