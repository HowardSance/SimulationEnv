package com.simulation.drone.engine;

import com.simulation.drone.entity.DetectionDevice;
import com.simulation.drone.entity.Drone;
import com.simulation.drone.entity.EnvironmentConfig;
import com.simulation.drone.entity.TargetCharacteristic;

import java.util.List;
import java.util.Map;

/**
 * 探测引擎接口
 * 负责处理探测设备的探测逻辑，包括目标检测、跟踪等
 */
public interface DetectionEngine {
    
    /**
     * 初始化探测引擎
     * @param config 环境配置
     */
    void initialize(EnvironmentConfig config);
    
    /**
     * 执行探测
     * @param device 探测设备
     * @param targets 目标列表
     * @param environment 环境参数
     * @return 探测结果
     */
    Map<String, Object> detect(DetectionDevice device, List<Drone> targets, Map<String, Object> environment);
    
    /**
     * 计算探测概率
     * @param device 探测设备
     * @param target 目标
     * @param environment 环境参数
     * @return 探测概率
     */
    double calculateDetectionProbability(DetectionDevice device, Drone target, Map<String, Object> environment);
    
    /**
     * 计算探测范围
     * @param device 探测设备
     * @param environment 环境参数
     * @return 探测范围信息
     */
    Map<String, Object> calculateDetectionRange(DetectionDevice device, Map<String, Object> environment);
    
    /**
     * 更新设备参数
     * @param device 探测设备
     * @param parameters 参数映射
     */
    void updateDeviceParameters(DetectionDevice device, Map<String, Object> parameters);
    
    /**
     * 更新目标特征
     * @param characteristics 目标特征列表
     */
    void updateTargetCharacteristics(List<TargetCharacteristic> characteristics);
    
    /**
     * 获取设备状态
     * @param device 探测设备
     * @return 设备状态信息
     */
    Map<String, Object> getDeviceStatus(DetectionDevice device);
    
    /**
     * 校准设备
     * @param device 探测设备
     */
    void calibrateDevice(DetectionDevice device);
} 