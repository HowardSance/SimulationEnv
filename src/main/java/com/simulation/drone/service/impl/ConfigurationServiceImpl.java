package com.simulation.drone.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulation.drone.adapter.kafka.EventProducer;
import com.simulation.drone.entity.config.DeviceTemplate;
import com.simulation.drone.entity.config.EnvironmentConfig;
import com.simulation.drone.entity.config.TargetCharacteristic;
import com.simulation.drone.repository.DeviceTemplateRepository;
import com.simulation.drone.repository.EnvironmentConfigRepository;
import com.simulation.drone.repository.TargetCharacteristicRepository;
import com.simulation.drone.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    
    private final DeviceTemplateRepository deviceTemplateRepository;
    private final EnvironmentConfigRepository environmentConfigRepository;
    private final TargetCharacteristicRepository targetCharacteristicRepository;
    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;
    
    // 设备模板管理
    @Override
    public List<DeviceTemplate> getAllDeviceTemplates() {
        return deviceTemplateRepository.findAll();
    }
    
    @Override
    public DeviceTemplate getDeviceTemplate(String name) {
        return deviceTemplateRepository.findByName(name);
    }
    
    @Override
    @Transactional
    public DeviceTemplate createDeviceTemplate(DeviceTemplate template) {
        return deviceTemplateRepository.save(template);
    }
    
    @Override
    @Transactional
    public DeviceTemplate updateDeviceTemplate(String name, DeviceTemplate template) {
        DeviceTemplate existing = deviceTemplateRepository.findByName(name);
        if (existing != null) {
            template.setId(existing.getId());
            return deviceTemplateRepository.save(template);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteDeviceTemplate(String name) {
        DeviceTemplate template = deviceTemplateRepository.findByName(name);
        if (template != null) {
            deviceTemplateRepository.delete(template);
        }
    }
    
    // 环境配置管理
    @Override
    public List<EnvironmentConfig> getAllEnvironmentConfigs() {
        return environmentConfigRepository.findAll();
    }
    
    @Override
    public EnvironmentConfig getEnvironmentConfig(String name) {
        return environmentConfigRepository.findByName(name);
    }
    
    @Override
    @Transactional
    public EnvironmentConfig createEnvironmentConfig(EnvironmentConfig config) {
        return environmentConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    public EnvironmentConfig updateEnvironmentConfig(String name, EnvironmentConfig config) {
        EnvironmentConfig existing = environmentConfigRepository.findByName(name);
        if (existing != null) {
            config.setId(existing.getId());
            return environmentConfigRepository.save(config);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteEnvironmentConfig(String name) {
        EnvironmentConfig config = environmentConfigRepository.findByName(name);
        if (config != null) {
            environmentConfigRepository.delete(config);
        }
    }
    
    // 目标特性管理
    @Override
    public List<TargetCharacteristic> getAllTargetCharacteristics() {
        return targetCharacteristicRepository.findAll();
    }
    
    @Override
    public TargetCharacteristic getTargetCharacteristic(String name) {
        return targetCharacteristicRepository.findByName(name);
    }
    
    @Override
    @Transactional
    public TargetCharacteristic createTargetCharacteristic(TargetCharacteristic characteristic) {
        return targetCharacteristicRepository.save(characteristic);
    }
    
    @Override
    @Transactional
    public TargetCharacteristic updateTargetCharacteristic(String name, TargetCharacteristic characteristic) {
        TargetCharacteristic existing = targetCharacteristicRepository.findByName(name);
        if (existing != null) {
            characteristic.setId(existing.getId());
            return targetCharacteristicRepository.save(characteristic);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteTargetCharacteristic(String name) {
        TargetCharacteristic characteristic = targetCharacteristicRepository.findByName(name);
        if (characteristic != null) {
            targetCharacteristicRepository.delete(characteristic);
        }
    }
    
    // 配置热更新
    @Override
    public void updateDeviceParameters(String deviceName, Map<String, Object> parameters) {
        try {
            String message = objectMapper.writeValueAsString(parameters);
            eventProducer.sendMessage("device-commands", deviceName, message);
            log.info("设备参数更新已发送: {}", deviceName);
        } catch (Exception e) {
            log.error("设备参数更新失败: {}", deviceName, e);
        }
    }
    
    @Override
    public void updateEnvironmentParameters(Map<String, Object> parameters) {
        try {
            String message = objectMapper.writeValueAsString(parameters);
            eventProducer.sendMessage("config-updates", "environment", message);
            log.info("环境参数更新已发送");
        } catch (Exception e) {
            log.error("环境参数更新失败", e);
        }
    }
    
    @Override
    public void updateTargetParameters(String targetName, Map<String, Object> parameters) {
        try {
            String message = objectMapper.writeValueAsString(parameters);
            eventProducer.sendMessage("config-updates", targetName, message);
            log.info("目标参数更新已发送: {}", targetName);
        } catch (Exception e) {
            log.error("目标参数更新失败: {}", targetName, e);
        }
    }
} 