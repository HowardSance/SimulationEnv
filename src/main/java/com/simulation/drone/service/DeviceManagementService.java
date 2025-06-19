package com.simulation.drone.service;

import com.simulation.drone.entity.simulation.DetectionDevice;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 设备管理服务接口
 * 提供探测设备的完整管理功能
 */
public interface DeviceManagementService {
    
    /**
     * 获取设备列表
     * @param page 页码
     * @param size 每页大小
     * @param type 设备类型（可选）
     * @return 设备分页列表
     */
    Page<DetectionDevice> getDevices(int page, int size, String type);
    
    /**
     * 获取指定设备
     * @param deviceId 设备ID
     * @return 设备信息
     */
    DetectionDevice getDevice(String deviceId);
    
    /**
     * 创建设备
     * @param device 设备信息
     * @return 创建后的设备
     */
    DetectionDevice createDevice(DetectionDevice device);
    
    /**
     * 更新设备
     * @param deviceId 设备ID
     * @param device 设备信息
     * @return 更新后的设备
     */
    DetectionDevice updateDevice(String deviceId, DetectionDevice device);
    
    /**
     * 删除设备
     * @param deviceId 设备ID
     */
    void deleteDevice(String deviceId);
    
    /**
     * 更新设备位置
     * @param deviceId 设备ID
     * @param position 位置信息（x, y, z）
     */
    void updateDevicePosition(String deviceId, Map<String, Double> position);
    
    /**
     * 更新设备姿态
     * @param deviceId 设备ID
     * @param orientation 姿态信息（pitch, yaw, roll）
     */
    void updateDeviceOrientation(String deviceId, Map<String, Double> orientation);
    
    /**
     * 更新设备参数
     * @param deviceId 设备ID
     * @param parameters 参数信息
     */
    void updateDeviceParameters(String deviceId, Map<String, Object> parameters);
    
    /**
     * 获取设备状态
     * @param deviceId 设备ID
     * @return 设备状态信息
     */
    Map<String, Object> getDeviceStatus(String deviceId);
    
    /**
     * 校准设备
     * @param deviceId 设备ID
     */
    void calibrateDevice(String deviceId);
    
    /**
     * 获取设备探测范围
     * @param deviceId 设备ID
     * @return 探测范围信息
     */
    Map<String, Object> getDeviceDetectionRange(String deviceId);
} 