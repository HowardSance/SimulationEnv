package com.JP.dronesim.interface.rest.controller;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.application.dtos.request.AdjustDeviceParamDTO;
import com.JP.dronesim.application.dtos.response.DeviceDetailsDTO;
import com.JP.dronesim.application.services.DeviceManagementAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备管理REST控制�?
 * 处理探测设备的部署、删除、修改等操作
 *
 * @author JP Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/airspace/{airspaceId}/devices")
public class DeviceController {

    @Autowired
    private DeviceManagementAppService deviceManagementAppService;

    /**
     * 部署探测设备
     *
     * @param airspaceId 空域ID
     * @param deviceParams 设备初始化参�?
     * @return 设备详情
     */
    @PostMapping
    public ResponseEntity<DeviceDetailsDTO> deployDevice(
            @PathVariable String airspaceId,
            @RequestBody @Valid DeviceInitParamsDTO deviceParams) {
        try {
            DeviceDetailsDTO device = deviceManagementAppService.deployDevice(airspaceId, deviceParams);
            return ResponseEntity.ok(device);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 批量部署设备
     *
     * @param airspaceId 空域ID
     * @param deviceParamsList 设备参数列表
     * @return 设备详情列表
     */
    @PostMapping("/batch")
    public ResponseEntity<List<DeviceDetailsDTO>> batchDeployDevices(
            @PathVariable String airspaceId,
            @RequestBody @Valid List<DeviceInitParamsDTO> deviceParamsList) {
        try {
            List<DeviceDetailsDTO> devices = deviceManagementAppService.batchDeployDevices(airspaceId, deviceParamsList);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取设备详情
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 设备详情
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            DeviceDetailsDTO device = deviceManagementAppService.getDevice(airspaceId, deviceId);
            return ResponseEntity.ok(device);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取空域内所有设�?
     *
     * @param airspaceId 空域ID
     * @return 设备列表
     */
    @GetMapping
    public ResponseEntity<List<DeviceDetailsDTO>> getAllDevices(@PathVariable String airspaceId) {
        try {
            List<DeviceDetailsDTO> devices = deviceManagementAppService.getAllDevices(airspaceId);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 根据设备类型获取设备列表
     *
     * @param airspaceId 空域ID
     * @param deviceType 设备类型
     * @return 设备列表
     */
    @GetMapping("/type/{deviceType}")
    public ResponseEntity<List<DeviceDetailsDTO>> getDevicesByType(
            @PathVariable String airspaceId,
            @PathVariable String deviceType) {
        try {
            List<DeviceDetailsDTO> devices = deviceManagementAppService.getDevicesByType(airspaceId, deviceType);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新设备参数
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @param params 设备参数
     * @return 更新结果
     */
    @PutMapping("/{deviceId}/params")
    public ResponseEntity<String> updateDeviceParams(
            @PathVariable String airspaceId,
            @PathVariable String deviceId,
            @RequestBody @Valid AdjustDeviceParamDTO params) {
        try {
            deviceManagementAppService.updateDeviceParams(airspaceId, deviceId, params);
            return ResponseEntity.ok("设备参数更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 激活设�?
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 激活结�?
     */
    @PostMapping("/{deviceId}/activate")
    public ResponseEntity<String> activateDevice(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            deviceManagementAppService.activateDevice(airspaceId, deviceId);
            return ResponseEntity.ok("Device activated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 停用设备
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 停用结果
     */
    @PostMapping("/{deviceId}/deactivate")
    public ResponseEntity<String> deactivateDevice(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            deviceManagementAppService.deactivateDevice(airspaceId, deviceId);
            return ResponseEntity.ok("设备停用成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除设备
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 删除结果
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<String> removeDevice(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            deviceManagementAppService.removeDevice(airspaceId, deviceId);
            return ResponseEntity.ok("设备删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取设备探测日志
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @param limit 日志条数限制
     * @return 探测日志
     */
    @GetMapping("/{deviceId}/logs")
    public ResponseEntity<Object> getDeviceLogs(
            @PathVariable String airspaceId,
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "100") int limit) {
        try {
            Object logs = deviceManagementAppService.getDeviceLogs(airspaceId, deviceId, limit);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
