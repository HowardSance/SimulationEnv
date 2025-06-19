package com.simulation.drone.service;

import com.simulation.drone.entity.config.DeviceTemplate;
import com.simulation.drone.entity.config.EnvironmentConfig;
import com.simulation.drone.entity.config.TargetCharacteristic;

import java.util.List;
import java.util.Map;

public interface ConfigurationService {
    
    // 设备模板管理
    List<DeviceTemplate> getAllDeviceTemplates();
    DeviceTemplate getDeviceTemplate(String name);
    DeviceTemplate createDeviceTemplate(DeviceTemplate template);
    DeviceTemplate updateDeviceTemplate(String name, DeviceTemplate template);
    void deleteDeviceTemplate(String name);
    
    // 环境配置管理
    List<EnvironmentConfig> getAllEnvironmentConfigs();
    EnvironmentConfig getEnvironmentConfig(String name);
    EnvironmentConfig createEnvironmentConfig(EnvironmentConfig config);
    EnvironmentConfig updateEnvironmentConfig(String name, EnvironmentConfig config);
    void deleteEnvironmentConfig(String name);
    
    // 目标特性管理
    List<TargetCharacteristic> getAllTargetCharacteristics();
    TargetCharacteristic getTargetCharacteristic(String name);
    TargetCharacteristic createTargetCharacteristic(TargetCharacteristic characteristic);
    TargetCharacteristic updateTargetCharacteristic(String name, TargetCharacteristic characteristic);
    void deleteTargetCharacteristic(String name);
    
    // 配置热更新
    void updateDeviceParameters(String deviceName, Map<String, Object> parameters);
    void updateEnvironmentParameters(Map<String, Object> parameters);
    void updateTargetParameters(String targetName, Map<String, Object> parameters);
} 