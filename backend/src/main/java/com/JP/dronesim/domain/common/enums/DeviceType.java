package com.JP.dronesim.domain.common.enums;

/**
 * 探测设备类型枚举
 * 定义系统支持的所有探测设备类型
 * 
 * @author JP
 * @version 1.0
 */
public enum DeviceType {
    
    /**
     * 雷达设备
     * 用于电磁波探测无人机
     */
    RADAR("Radar", "雷达设备"),
    
    /**
     * 光电摄像头
     * 用于光学探测无人机
     */
    OPTICAL_CAMERA("OpticalCamera", "光电摄像头"),
    
    /**
     * 无线电探测器
     * 用于无线电信号探测无人机
     */
    RADIO_DETECTOR("RadioDetector", "无线电探测器"),
    
    /**
     * GPS诱导器
     * 用于干扰无人机GPS信号
     */
    GPS_JAMMER("GPSJammer", "GPS诱导器");
    
    /**
     * 设备类型代码
     */
    private final String code;
    
    /**
     * 设备类型描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 设备类型代码
     * @param description 设备类型描述
     */
    DeviceType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取设备类型代码
     * 
     * @return 设备类型代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取设备类型描述
     * 
     * @return 设备类型描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取设备类型
     * 
     * @param code 设备类型代码
     * @return 对应的设备类型枚举
     * @throws IllegalArgumentException 如果代码不存在
     */
    public static DeviceType fromCode(String code) {
        for (DeviceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的设备类型代码: " + code);
    }
} 