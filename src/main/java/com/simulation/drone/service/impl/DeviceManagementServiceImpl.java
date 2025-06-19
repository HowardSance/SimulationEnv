package com.simulation.drone.service.impl;

import com.simulation.drone.entity.simulation.DetectionDevice;
import com.simulation.drone.repository.DeviceRepository;
import com.simulation.drone.service.DeviceManagementService;
import com.simulation.drone.service.EventPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceManagementServiceImpl implements DeviceManagementService {
    
    private final DeviceRepository deviceRepository;
    private final EventPublishService eventService;
    
    @Override
    public Page<DetectionDevice> getDevices(int page, int size, String type) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (type != null && !type.isEmpty()) {
            return deviceRepository.findByType(type, pageRequest);
        }
        return deviceRepository.findAll(pageRequest);
    }
    
    @Override
    public DetectionDevice getDevice(String deviceId) {
        return deviceRepository.findById(deviceId).orElse(null);
    }
    
    @Override
    @Transactional
    public DetectionDevice createDevice(DetectionDevice device) {
        DetectionDevice savedDevice = deviceRepository.save(device);
        log.info("创建探测设备: {}", savedDevice.getId());
        eventService.publishDeviceCreated(savedDevice);
        return savedDevice;
    }
    
    @Override
    @Transactional
    public DetectionDevice updateDevice(String deviceId, DetectionDevice device) {
        return deviceRepository.findById(deviceId)
                .map(existingDevice -> {
                    device.setId(deviceId);
                    DetectionDevice updatedDevice = deviceRepository.save(device);
                    log.info("更新探测设备: {}", deviceId);
                    eventService.publishDeviceUpdated(updatedDevice);
                    return updatedDevice;
                })
                .orElse(null);
    }
    
    @Override
    @Transactional
    public void deleteDevice(String deviceId) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            deviceRepository.delete(device);
            log.info("删除探测设备: {}", deviceId);
            eventService.publishDeviceDeleted(deviceId);
        });
    }
    
    @Override
    @Transactional
    public void updateDevicePosition(String deviceId, Map<String, Double> position) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            device.setPositionX(position.get("x"));
            device.setPositionY(position.get("y"));
            device.setPositionZ(position.get("z"));
            deviceRepository.save(device);
            log.info("更新设备位置: {}", deviceId);
            eventService.publishDevicePositionUpdated(deviceId, position);
        });
    }
    
    @Override
    @Transactional
    public void updateDeviceOrientation(String deviceId, Map<String, Double> orientation) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            device.setPitch(orientation.get("pitch"));
            device.setYaw(orientation.get("yaw"));
            device.setRoll(orientation.get("roll"));
            deviceRepository.save(device);
            log.info("更新设备姿态: {}", deviceId);
            eventService.publishDeviceOrientationUpdated(deviceId, orientation);
        });
    }
    
    @Override
    @Transactional
    public void updateDeviceParameters(String deviceId, Map<String, Object> parameters) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            device.setParameters(parameters);
            deviceRepository.save(device);
            log.info("更新设备参数: {}", deviceId);
            eventService.publishDeviceParametersUpdated(deviceId, parameters);
        });
    }
    
    @Override
    public Map<String, Object> getDeviceStatus(String deviceId) {
        return deviceRepository.findById(deviceId)
                .map(device -> {
                    Map<String, Object> status = new HashMap<>();
                    status.put("id", device.getId());
                    status.put("type", device.getType());
                    status.put("position", Map.of(
                            "x", device.getPositionX(),
                            "y", device.getPositionY(),
                            "z", device.getPositionZ()
                    ));
                    status.put("orientation", Map.of(
                            "pitch", device.getPitch(),
                            "yaw", device.getYaw(),
                            "roll", device.getRoll()
                    ));
                    status.put("parameters", device.getParameters());
                    status.put("enabled", device.isEnabled());
                    return status;
                })
                .orElse(null);
    }
    
    @Override
    @Transactional
    public void calibrateDevice(String deviceId) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            // 执行设备校准逻辑
            log.info("校准设备: {}", deviceId);
            eventService.publishDeviceCalibrated(deviceId);
        });
    }
    
    @Override
    public Map<String, Object> getDeviceDetectionRange(String deviceId) {
        return deviceRepository.findById(deviceId)
                .map(device -> {
                    Map<String, Object> range = new HashMap<>();
                    // 根据设备类型计算探测范围
                    switch (device.getType()) {
                        case "RADAR":
                            range.put("maxRange", 10000.0); // 10km
                            range.put("minRange", 100.0);   // 100m
                            range.put("azimuth", 120.0);    // 120度
                            range.put("elevation", 60.0);   // 60度
                            break;
                        case "OPTICAL":
                            range.put("maxRange", 5000.0);  // 5km
                            range.put("minRange", 50.0);    // 50m
                            range.put("fov", 90.0);         // 90度
                            break;
                        case "RADIO":
                            range.put("maxRange", 20000.0); // 20km
                            range.put("minRange", 200.0);   // 200m
                            range.put("azimuth", 360.0);    // 360度
                            break;
                    }
                    return range;
                })
                .orElse(null);
    }
} 