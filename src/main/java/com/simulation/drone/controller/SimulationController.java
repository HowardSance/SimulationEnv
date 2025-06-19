package com.simulation.drone.controller;

import com.simulation.drone.dto.core.SimulationCoreDTO;
import com.simulation.drone.dto.core.SimulationSnapshotDTO;
import com.simulation.drone.dto.metrics.PerformanceMetricsDTO;
import com.simulation.drone.engine.EnvironmentEngine;
import com.simulation.drone.entity.simulation.DroneEntity;
import com.simulation.drone.model.message.control.DroneControl;
import com.simulation.drone.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "仿真控制", description = "仿真系统的核心控制接口，提供仿真启动、暂停、恢复、停止等基本操作")
@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {
    
    private final EnvironmentEngine environmentEngine;
    private final SimulationService simulationService;
    
    @Operation(
        summary = "获取所有活动无人机",
        description = "返回当前环境中所有活动无人机的状态信息",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功获取无人机列表",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DroneEntity.class)
                )
            )
        }
    )
    @GetMapping("/drones")
    public ResponseEntity<List<DroneEntity>> getAllDrones() {
        return ResponseEntity.ok(environmentEngine.getAllDrones());
    }
    
    @Operation(
        summary = "添加无人机到环境",
        description = "将指定ID的无人机添加到仿真环境中",
        responses = {
            @ApiResponse(responseCode = "200", description = "无人机添加成功"),
            @ApiResponse(responseCode = "400", description = "无效的无人机ID"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @PostMapping("/drones/{droneName}")
    public ResponseEntity<Void> addDrone(
            @Parameter(description = "无人机ID", required = true)
            @PathVariable String droneName) {
        environmentEngine.addDrone(droneName);
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "从环境中移除无人机",
        description = "将指定ID的无人机从仿真环境中移除",
        responses = {
            @ApiResponse(responseCode = "200", description = "无人机移除成功"),
            @ApiResponse(responseCode = "404", description = "无人机不存在")
        }
    )
    @DeleteMapping("/drones/{droneName}")
    public ResponseEntity<Void> removeDrone(
            @Parameter(description = "无人机ID", required = true)
            @PathVariable String droneName) {
        environmentEngine.removeDrone(droneName);
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "控制无人机移动",
        description = "发送控制命令到指定无人机，控制其运动",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "无人机控制命令",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DroneControl.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "控制命令发送成功"),
            @ApiResponse(responseCode = "404", description = "无人机不存在"),
            @ApiResponse(responseCode = "400", description = "无效的控制命令")
        }
    )
    @PostMapping("/drones/{droneName}/control")
    public ResponseEntity<Void> controlDrone(
            @Parameter(description = "无人机ID", required = true)
            @PathVariable String droneName,
            @RequestBody DroneControl control) {
        
        environmentEngine.controlDrone(
            droneName,
            control.getRoll(),
            control.getPitch(),
            control.getThrottle(),
            control.getYaw()
        );
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "获取相机图像",
        description = "获取指定无人机上指定相机的实时图像",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功获取图像",
                content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)
            ),
            @ApiResponse(responseCode = "404", description = "无人机或相机不存在")
        }
    )
    @GetMapping("/drones/{droneName}/cameras/{cameraName}/image")
    public ResponseEntity<byte[]> getCameraImage(
            @Parameter(description = "无人机ID", required = true)
            @PathVariable String droneName,
            @Parameter(description = "相机ID", required = true)
            @PathVariable String cameraName) {
        byte[] image = environmentEngine.getDroneCameraImage(droneName, cameraName);
        return image != null ? ResponseEntity.ok(image) : ResponseEntity.notFound().build();
    }
    
    /**
     * 更新环境参数
     */
    @PostMapping("/environment")
    public ResponseEntity<Void> updateEnvironment(@RequestBody Map<String, Object> parameters) {
        environmentEngine.updateEnvironmentParameters(parameters);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/start")
    @Operation(summary = "启动仿真", description = "初始化并启动仿真系统")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "仿真启动成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "400", description = "参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> startSimulation() {
        return ResponseEntity.ok(simulationService.start());
    }
    
    @PostMapping("/pause")
    @Operation(summary = "暂停仿真", description = "暂停当前运行的仿真")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "仿真暂停成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "400", description = "仿真未运行"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> pauseSimulation() {
        return ResponseEntity.ok(simulationService.pause());
    }
    
    @PostMapping("/resume")
    @Operation(summary = "恢复仿真", description = "恢复暂停的仿真")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "仿真恢复成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "400", description = "仿真未暂停"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> resumeSimulation() {
        return ResponseEntity.ok(simulationService.resume());
    }
    
    @PostMapping("/stop")
    @Operation(summary = "停止仿真", description = "停止当前运行的仿真")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "仿真停止成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "400", description = "仿真未运行"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> stopSimulation() {
        return ResponseEntity.ok(simulationService.stop());
    }
    
    @GetMapping("/status")
    @Operation(summary = "获取仿真状态", description = "获取当前仿真的运行状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取状态成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> getStatus() {
        return ResponseEntity.ok(simulationService.getStatus());
    }
    
    @PutMapping("/time-step")
    @Operation(summary = "设置时间步长", description = "设置仿真的时间步长")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "设置成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "400", description = "时间步长超出范围"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> setTimeStep(
            @Parameter(description = "时间步长（毫秒）", required = true)
            @RequestParam double timeStep) {
        return ResponseEntity.ok(simulationService.setTimeStep(timeStep));
    }
    
    @PostMapping("/snapshot")
    @Operation(summary = "创建快照", description = "创建当前仿真状态的快照")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "快照创建成功",
            content = @Content(schema = @Schema(implementation = SimulationSnapshotDTO.class))),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationSnapshotDTO> createSnapshot(
            @Parameter(description = "快照名称", required = true)
            @RequestParam String name,
            @Parameter(description = "快照描述")
            @RequestParam(required = false) String description) {
        return ResponseEntity.ok(simulationService.createSnapshot(name, description));
    }
    
    @PostMapping("/restore/{snapshotId}")
    @Operation(summary = "恢复快照", description = "从指定的快照恢复仿真状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "快照恢复成功",
            content = @Content(schema = @Schema(implementation = SimulationCoreDTO.class))),
        @ApiResponse(responseCode = "404", description = "快照不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<SimulationCoreDTO> restoreSnapshot(
            @Parameter(description = "快照ID", required = true)
            @PathVariable String snapshotId) {
        return ResponseEntity.ok(simulationService.restoreSnapshot(snapshotId));
    }
    
    @GetMapping("/metrics")
    @Operation(summary = "获取性能指标", description = "获取当前仿真的性能指标")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取指标成功",
            content = @Content(schema = @Schema(implementation = PerformanceMetricsDTO.class))),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<PerformanceMetricsDTO> getMetrics() {
        return ResponseEntity.ok(simulationService.getMetrics());
    }
} 