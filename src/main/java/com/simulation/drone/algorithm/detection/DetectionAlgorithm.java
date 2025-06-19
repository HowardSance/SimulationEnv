package com.simulation.drone.algorithm.detection;

import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.entity.simulation.DetectionDevice;
import com.simulation.drone.entity.simulation.DroneEntity;

import java.util.Map;

/**
 * 探测算法基础接口
 * 定义所有探测算法的通用方法
 */
public interface DetectionAlgorithm {
    
    /**
     * 执行探测算法
     * @param device 探测设备
     * @param drone 目标无人机
     * @param environment 环境参数
     * @return 探测结果
     */
    DetectionResultDTO detect(DetectionDevice device, DroneEntity drone, Map<String, Object> environment);
    
    /**
     * 获取算法类型
     * @return 算法类型标识
     */
    default String getAlgorithmType() {
        return this.getClass().getSimpleName();
    }
    
    /**
     * 检查设备是否支持此算法
     * @param device 探测设备
     * @return 是否支持
     */
    default boolean supportsDevice(DetectionDevice device) {
        return true; // 默认支持所有设备，子类可以重写
    }
    
    /**
     * 获取算法配置参数
     * @return 配置参数映射
     */
    default Map<String, Object> getAlgorithmConfiguration() {
        return Map.of(
            "algorithmType", getAlgorithmType(),
            "version", "1.0",
            "description", "基础探测算法"
        );
    }
} 