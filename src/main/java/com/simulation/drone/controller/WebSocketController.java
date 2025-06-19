package com.simulation.drone.controller;

import com.simulation.drone.dto.core.SimulationCoreDTO;
import com.simulation.drone.dto.device.DeviceStateDTO;
import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.dto.event.DetectionEventDTO;
import com.simulation.drone.dto.event.SimulationEventDTO;
import com.simulation.drone.dto.metrics.PerformanceMetricsDTO;
import com.simulation.drone.service.EventPublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Tag(
    name = "WebSocket实时数据推送",
    description = "提供WebSocket实时数据推送接口，用于推送仿真状态、设备状态、检测事件等实时数据"
)
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    
    private final EventPublishService eventPublishService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Operation(
        summary = "订阅仿真系统状态",
        description = "订阅仿真系统的实时状态更新，包括运行状态、性能指标等"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "订阅成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "status", type = "string", description = "仿真状态"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳"),
                        @Schema(name = "metrics", type = "object", description = "性能指标")
                    }
                )
            )
        )
    })
    @MessageMapping("/subscribe/status")
    @SendTo("/topic/simulation/status")
    public SimulationEventDTO handleSubscription() {
        return eventPublishService.getSimulationStatus();
    }
    
    @Operation(
        summary = "测试WebSocket连接",
        description = "测试WebSocket连接是否正常工作"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "连接正常",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "status", type = "string", description = "连接状态"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳")
                    }
                )
            )
        )
    })
    @GetMapping("/ws/test")
    @ResponseBody
    public Map<String, Object> testWebSocket() {
        return Map.of(
            "status", "connected",
            "timestamp", System.currentTimeMillis()
        );
    }
    
    @Operation(
        summary = "订阅设备状态",
        description = "订阅指定设备的实时状态更新，包括位置、方向、参数等"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "订阅成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "deviceId", type = "string", format = "uuid", description = "设备ID"),
                        @Schema(name = "status", type = "string", description = "设备状态"),
                        @Schema(name = "position", type = "object", description = "位置信息"),
                        @Schema(name = "orientation", type = "object", description = "方向信息"),
                        @Schema(name = "parameters", type = "object", description = "设备参数"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳")
                    }
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "设备不存在")
    })
    @MessageMapping("/subscribe/device/{deviceId}")
    @SendTo("/topic/device/{deviceId}/status")
    public DeviceStateDTO handleDeviceStatusRequest(
            @Parameter(
                description = "设备ID",
                required = true,
                schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String deviceId) {
        return eventPublishService.getDeviceStatus(deviceId);
    }
    
    @Operation(
        summary = "订阅检测事件",
        description = "订阅最新的检测事件，包括目标检测、跟踪等信息"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "订阅成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "eventId", type = "string", format = "uuid", description = "事件ID"),
                        @Schema(name = "eventType", type = "string", description = "事件类型"),
                        @Schema(name = "deviceId", type = "string", format = "uuid", description = "设备ID"),
                        @Schema(name = "targetId", type = "string", format = "uuid", description = "目标ID"),
                        @Schema(name = "position", type = "object", description = "目标位置"),
                        @Schema(name = "confidence", type = "number", description = "检测置信度"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳")
                    }
                )
            )
        )
    })
    @MessageMapping("/subscribe/detection")
    @SendTo("/topic/detection/events")
    public DetectionEventDTO handleDetectionEvents() {
        return eventPublishService.getLatestDetectionEvent();
    }
    
    @Operation(
        summary = "订阅仿真性能指标",
        description = "订阅仿真系统的实时性能指标，包括FPS、内存使用等"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "订阅成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "fps", type = "number", description = "帧率"),
                        @Schema(name = "memoryUsage", type = "number", description = "内存使用量（MB）"),
                        @Schema(name = "cpuUsage", type = "number", description = "CPU使用率（%）"),
                        @Schema(name = "entityCount", type = "integer", description = "实体数量"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳")
                    }
                )
            )
        )
    })
    @MessageMapping("/subscribe/metrics")
    @SendTo("/topic/simulation/metrics")
    public PerformanceMetricsDTO handleSimulationMetrics() {
        return eventPublishService.getPerformanceMetrics();
    }
    
    @Operation(
        summary = "订阅环境状态",
        description = "订阅仿真环境的实时状态，包括天气、光照等环境参数"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "订阅成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    type = "object",
                    properties = {
                        @Schema(name = "weather", type = "string", description = "天气状况"),
                        @Schema(name = "temperature", type = "number", description = "温度（℃）"),
                        @Schema(name = "humidity", type = "number", description = "湿度（%）"),
                        @Schema(name = "windSpeed", type = "number", description = "风速（m/s）"),
                        @Schema(name = "visibility", type = "number", description = "能见度（m）"),
                        @Schema(name = "timestamp", type = "string", format = "date-time", description = "时间戳")
                    }
                )
            )
        )
    })
    @MessageMapping("/subscribe/environment")
    @SendTo("/topic/simulation/environment")
    public Map<String, Object> handleEnvironmentStatus() {
        return eventPublishService.getEnvironmentStatus();
    }
} 