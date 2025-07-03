package com.JP.dronesim.interface.rest.controller;

import com.JP.dronesim.application.dtos.request.UAVStateDTO;
import com.JP.dronesim.application.dtos.response.EntityStateDTO;
import com.JP.dronesim.application.services.UAVManagementAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 无人机管理REST控制器
 * 处理无人机的部署、删除、修改、飞行路径设置等操作
 *
 * @author JP Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/airspace/{airspaceId}/uavs")
public class UavController {

    @Autowired
    private UAVManagementAppService uavManagementAppService;

    /**
     * 部署无人机
     *
     * @param airspaceId 空域ID
     * @param uavState 无人机状态
     * @return 无人机状态
     */
    @PostMapping
    public ResponseEntity<EntityStateDTO> deployUAV(
            @PathVariable String airspaceId,
            @RequestBody @Valid UAVStateDTO uavState) {
        try {
            EntityStateDTO uav = uavManagementAppService.deployUAV(airspaceId, uavState);
            return ResponseEntity.ok(uav);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 批量部署无人机
     *
     * @param airspaceId 空域ID
     * @param uavStates 无人机状态列表
     * @return 无人机状态列表
     */
    @PostMapping("/batch")
    public ResponseEntity<List<EntityStateDTO>> batchDeployUAVs(
            @PathVariable String airspaceId,
            @RequestBody @Valid List<UAVStateDTO> uavStates) {
        try {
            List<EntityStateDTO> uavs = uavManagementAppService.batchDeployUAVs(airspaceId, uavStates);
            return ResponseEntity.ok(uavs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取无人机状态
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 无人机状态
     */
    @GetMapping("/{uavId}")
    public ResponseEntity<EntityStateDTO> getUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            EntityStateDTO uav = uavManagementAppService.getUAV(airspaceId, uavId);
            return ResponseEntity.ok(uav);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取空域内所有无人机
     *
     * @param airspaceId 空域ID
     * @return 无人机列表
     */
    @GetMapping
    public ResponseEntity<List<EntityStateDTO>> getAllUAVs(@PathVariable String airspaceId) {
        try {
            List<EntityStateDTO> uavs = uavManagementAppService.getAllUAVs(airspaceId);
            return ResponseEntity.ok(uavs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新无人机状态
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param uavState 无人机状态
     * @return 更新结果
     */
    @PutMapping("/{uavId}")
    public ResponseEntity<String> updateUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId,
            @RequestBody @Valid UAVStateDTO uavState) {
        try {
            uavManagementAppService.updateUAV(airspaceId, uavId, uavState);
            return ResponseEntity.ok("无人机状态更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 设置无人机飞行路径
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param waypoints 航点列表
     * @return 设置结果
     */
    @PostMapping("/{uavId}/waypoints")
    public ResponseEntity<String> setFlightPath(
            @PathVariable String airspaceId,
            @PathVariable String uavId,
            @RequestBody List<Object> waypoints) {
        try {
            uavManagementAppService.setFlightPath(airspaceId, uavId, waypoints);
            return ResponseEntity.ok("飞行路径设置成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
    * 获取无人机飞行路径
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 飞行路径
     */
    @GetMapping("/{uavId}/waypoints")
    public ResponseEntity<Object> getFlightPath(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            Object waypoints = uavManagementAppService.getFlightPath(airspaceId, uavId);
            return ResponseEntity.ok(waypoints);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 控制无人机起飞
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 控制结果
     */
    @PostMapping("/{uavId}/takeoff")
    public ResponseEntity<String> takeoffUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            uavManagementAppService.takeoffUAV(airspaceId, uavId);
            return ResponseEntity.ok("无人机起飞成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 控制无人机降落
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 控制结果
     */
    @PostMapping("/{uavId}/land")
    public ResponseEntity<String> landUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            uavManagementAppService.landUAV(airspaceId, uavId);
            return ResponseEntity.ok("无人机降落成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 控制无人机悬停
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 控制结果
     */
    @PostMapping("/{uavId}/hover")
    public ResponseEntity<String> hoverUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            uavManagementAppService.hoverUAV(airspaceId, uavId);
            return ResponseEntity.ok("无人机悬停成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除无人机
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 删除结果
     */
    @DeleteMapping("/{uavId}")
    public ResponseEntity<String> removeUAV(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            uavManagementAppService.removeUAV(airspaceId, uavId);
            return ResponseEntity.ok("无人机删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取无人机物理特性
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
    * @return 物理特性
     */
    @GetMapping("/{uavId}/properties")
    public ResponseEntity<Object> getUAVProperties(
            @PathVariable String airspaceId,
            @PathVariable String uavId) {
        try {
            Object properties = uavManagementAppService.getUAVProperties(airspaceId, uavId);
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
