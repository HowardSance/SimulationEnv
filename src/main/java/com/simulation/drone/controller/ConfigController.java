package com.simulation.drone.controller;

import com.simulation.drone.entity.config.DeviceTemplate;
import com.simulation.drone.entity.config.EnvironmentConfig;
import com.simulation.drone.entity.config.TargetCharacteristic;
import com.simulation.drone.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {
    
    private final ConfigurationService configurationService;
    
    // 设备模板管理
    @GetMapping("/devices/templates")
    public ResponseEntity<List<DeviceTemplate>> getAllDeviceTemplates() {
        return ResponseEntity.ok(configurationService.getAllDeviceTemplates());
    }
    
    @GetMapping("/devices/templates/{name}")
    public ResponseEntity<DeviceTemplate> getDeviceTemplate(@PathVariable String name) {
        DeviceTemplate template = configurationService.getDeviceTemplate(name);
        return template != null ? ResponseEntity.ok(template) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/devices/templates")
    public ResponseEntity<DeviceTemplate> createDeviceTemplate(@RequestBody DeviceTemplate template) {
        return ResponseEntity.ok(configurationService.createDeviceTemplate(template));
    }
    
    @PutMapping("/devices/templates/{name}")
    public ResponseEntity<DeviceTemplate> updateDeviceTemplate(
            @PathVariable String name,
            @RequestBody DeviceTemplate template) {
        DeviceTemplate updated = configurationService.updateDeviceTemplate(name, template);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/devices/templates/{name}")
    public ResponseEntity<Void> deleteDeviceTemplate(@PathVariable String name) {
        configurationService.deleteDeviceTemplate(name);
        return ResponseEntity.ok().build();
    }
    
    // 环境配置管理
    @GetMapping("/environments")
    public ResponseEntity<List<EnvironmentConfig>> getAllEnvironmentConfigs() {
        return ResponseEntity.ok(configurationService.getAllEnvironmentConfigs());
    }
    
    @GetMapping("/environments/{name}")
    public ResponseEntity<EnvironmentConfig> getEnvironmentConfig(@PathVariable String name) {
        EnvironmentConfig config = configurationService.getEnvironmentConfig(name);
        return config != null ? ResponseEntity.ok(config) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/environments")
    public ResponseEntity<EnvironmentConfig> createEnvironmentConfig(@RequestBody EnvironmentConfig config) {
        return ResponseEntity.ok(configurationService.createEnvironmentConfig(config));
    }
    
    @PutMapping("/environments/{name}")
    public ResponseEntity<EnvironmentConfig> updateEnvironmentConfig(
            @PathVariable String name,
            @RequestBody EnvironmentConfig config) {
        EnvironmentConfig updated = configurationService.updateEnvironmentConfig(name, config);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/environments/{name}")
    public ResponseEntity<Void> deleteEnvironmentConfig(@PathVariable String name) {
        configurationService.deleteEnvironmentConfig(name);
        return ResponseEntity.ok().build();
    }
    
    // 目标特性管理
    @GetMapping("/targets")
    public ResponseEntity<List<TargetCharacteristic>> getAllTargetCharacteristics() {
        return ResponseEntity.ok(configurationService.getAllTargetCharacteristics());
    }
    
    @GetMapping("/targets/{name}")
    public ResponseEntity<TargetCharacteristic> getTargetCharacteristic(@PathVariable String name) {
        TargetCharacteristic characteristic = configurationService.getTargetCharacteristic(name);
        return characteristic != null ? ResponseEntity.ok(characteristic) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/targets")
    public ResponseEntity<TargetCharacteristic> createTargetCharacteristic(@RequestBody TargetCharacteristic characteristic) {
        return ResponseEntity.ok(configurationService.createTargetCharacteristic(characteristic));
    }
    
    @PutMapping("/targets/{name}")
    public ResponseEntity<TargetCharacteristic> updateTargetCharacteristic(
            @PathVariable String name,
            @RequestBody TargetCharacteristic characteristic) {
        TargetCharacteristic updated = configurationService.updateTargetCharacteristic(name, characteristic);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/targets/{name}")
    public ResponseEntity<Void> deleteTargetCharacteristic(@PathVariable String name) {
        configurationService.deleteTargetCharacteristic(name);
        return ResponseEntity.ok().build();
    }
    
    // 配置热更新
    @PostMapping("/devices/{deviceName}/parameters")
    public ResponseEntity<Void> updateDeviceParameters(
            @PathVariable String deviceName,
            @RequestBody Map<String, Object> parameters) {
        configurationService.updateDeviceParameters(deviceName, parameters);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/environments/parameters")
    public ResponseEntity<Void> updateEnvironmentParameters(@RequestBody Map<String, Object> parameters) {
        configurationService.updateEnvironmentParameters(parameters);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/targets/{targetName}/parameters")
    public ResponseEntity<Void> updateTargetParameters(
            @PathVariable String targetName,
            @RequestBody Map<String, Object> parameters) {
        configurationService.updateTargetParameters(targetName, parameters);
        return ResponseEntity.ok().build();
    }
} 