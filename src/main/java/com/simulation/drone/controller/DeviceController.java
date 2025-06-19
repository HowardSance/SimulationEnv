package com.simulation.drone.controller;

import com.simulation.drone.dto.device.DeviceStateDTO;
import com.simulation.drone.dto.device.RadarDeviceDTO;
import com.simulation.drone.dto.device.OpticalDeviceDTO;
import com.simulation.drone.dto.device.RadioDeviceDTO;
import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.service.DeviceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "设备管理", description = "提供探测设备的CRUD操作、状态更新和参数配置")
public class DeviceController {

    @Autowired
    private DeviceManagementService deviceService;

    @GetMapping
    @Operation(summary = "获取设备列表", description = "分页获取所有探测设备")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<List<DeviceStateDTO>> getDevices(
            @Parameter(description = "页码", required = true)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", required = true)
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "设备类型")
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(deviceService.getDevices(page, size, type));
    }

    @GetMapping("/{deviceId}")
    @Operation(summary = "获取设备详情", description = "获取指定设备的详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> getDevice(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getDevice(deviceId));
    }

    @PostMapping
    @Operation(summary = "创建设备", description = "创建新的探测设备")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "创建成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "400", description = "参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> createDevice(
            @Parameter(description = "设备信息", required = true)
            @RequestBody DeviceStateDTO device) {
        return ResponseEntity.ok(deviceService.createDevice(device));
    }

    @PutMapping("/{deviceId}")
    @Operation(summary = "更新设备", description = "更新指定设备的信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> updateDevice(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId,
            @Parameter(description = "设备信息", required = true)
            @RequestBody DeviceStateDTO device) {
        return ResponseEntity.ok(deviceService.updateDevice(deviceId, device));
    }

    @DeleteMapping("/{deviceId}")
    @Operation(summary = "删除设备", description = "删除指定的探测设备")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{deviceId}/position")
    @Operation(summary = "更新设备位置", description = "更新指定设备的位置")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> updateDevicePosition(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId,
            @Parameter(description = "位置信息", required = true)
            @RequestBody Map<String, Double> position) {
        return ResponseEntity.ok(deviceService.updateDevicePosition(deviceId, position));
    }

    @PutMapping("/{deviceId}/orientation")
    @Operation(summary = "更新设备朝向", description = "更新指定设备的朝向")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> updateDeviceOrientation(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId,
            @Parameter(description = "朝向信息", required = true)
            @RequestBody Map<String, Double> orientation) {
        return ResponseEntity.ok(deviceService.updateDeviceOrientation(deviceId, orientation));
    }

    @PutMapping("/{deviceId}/parameters")
    @Operation(summary = "更新设备参数", description = "更新指定设备的参数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> updateDeviceParameters(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId,
            @Parameter(description = "参数信息", required = true)
            @RequestBody Map<String, Object> parameters) {
        return ResponseEntity.ok(deviceService.updateDeviceParameters(deviceId, parameters));
    }

    @GetMapping("/{deviceId}/status")
    @Operation(summary = "获取设备状态", description = "获取指定设备的当前状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> getDeviceStatus(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceStatus(deviceId));
    }

    @PostMapping("/{deviceId}/calibrate")
    @Operation(summary = "校准设备", description = "对指定设备进行校准")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "校准成功",
            content = @Content(schema = @Schema(implementation = DeviceStateDTO.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<DeviceStateDTO> calibrateDevice(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.calibrateDevice(deviceId));
    }

    @GetMapping("/{deviceId}/detection-range")
    @Operation(summary = "获取探测范围", description = "获取指定设备的探测范围信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404", description = "设备不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<Map<String, Object>> getDeviceDetectionRange(
            @Parameter(description = "设备ID", required = true)
            @PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceDetectionRange(deviceId));
    }
} 