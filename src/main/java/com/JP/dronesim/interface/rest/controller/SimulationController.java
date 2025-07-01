package com.JP.dronesim.interface.rest.controller;

import com.JP.dronesim.application.dtos.request.SimulationControlCommandDTO;
import com.JP.dronesim.application.dtos.response.SimulationStatusDTO;
import com.JP.dronesim.application.services.SimulationAppService;
import com.JP.dronesim.domain.airspace.model.Airspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 仿真控制REST控制�?
 * 处理仿真启动、暂停、停止、时间步进等操作
 *
 * @author JP Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @Autowired
    private SimulationAppService simulationAppService;

    /**
     * 启动仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    @PostMapping("/{airspaceId}/start")
    public ResponseEntity<SimulationStatusDTO> startSimulation(@PathVariable String airspaceId) {
        try {
            SimulationStatusDTO status = simulationAppService.startSimulation(airspaceId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 暂停仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    @PostMapping("/{airspaceId}/pause")
    public ResponseEntity<SimulationStatusDTO> pauseSimulation(@PathVariable String airspaceId) {
        try {
            SimulationStatusDTO status = simulationAppService.pauseSimulation(airspaceId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 恢复仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    @PostMapping("/{airspaceId}/resume")
    public ResponseEntity<SimulationStatusDTO> resumeSimulation(@PathVariable String airspaceId) {
        try {
            SimulationStatusDTO status = simulationAppService.resumeSimulation(airspaceId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 停止仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    @PostMapping("/{airspaceId}/stop")
    public ResponseEntity<SimulationStatusDTO> stopSimulation(@PathVariable String airspaceId) {
        try {
            SimulationStatusDTO status = simulationAppService.stopSimulation(airspaceId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 时间步进
     *
     * @param airspaceId 空域ID
     * @param command 控制指令
     * @return 仿真状�?
     */
    @PostMapping("/{airspaceId}/step")
    public ResponseEntity<SimulationStatusDTO> stepSimulation(
            @PathVariable String airspaceId,
            @RequestBody @Valid SimulationControlCommandDTO command) {
        try {
            SimulationStatusDTO status = simulationAppService.stepSimulation(airspaceId, command.getTimeStep());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取仿真状�?
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    @GetMapping("/{airspaceId}/status")
    public ResponseEntity<SimulationStatusDTO> getSimulationStatus(@PathVariable String airspaceId) {
        try {
            SimulationStatusDTO status = simulationAppService.getSimulationStatus(airspaceId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 设置仿真时间步长
     *
     * @param airspaceId 空域ID
     * @param command 控制指令
     * @return 仿真状�?
     */
    @PutMapping("/{airspaceId}/timestep")
    public ResponseEntity<SimulationStatusDTO> setTimeStep(
            @PathVariable String airspaceId,
            @RequestBody @Valid SimulationControlCommandDTO command) {
        try {
            SimulationStatusDTO status = simulationAppService.setTimeStep(airspaceId, command.getTimeStep());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
