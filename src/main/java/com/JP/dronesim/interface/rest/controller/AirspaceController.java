package com.JP.dronesim.interface.rest.controller;

import com.JP.dronesim.application.dtos.request.AirspaceConfigDTO;
import com.JP.dronesim.application.dtos.request.EnvironmentUpdateParamsDTO;
import com.JP.dronesim.application.dtos.response.AirspaceDetailsDTO;
import com.JP.dronesim.application.services.AirspaceManagementAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 空域管理REST控制�?
 * 处理空域创建、加载、保存、查询等操作
 *
 * @author JP Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/airspace")
public class AirspaceController {

    @Autowired
    private AirspaceManagementAppService airspaceManagementAppService;

    /**
     * 创建空域
     *
     * @param config 空域配置
     * @return 空域详情
     */
    @PostMapping
    public ResponseEntity<AirspaceDetailsDTO> createAirspace(@RequestBody @Valid AirspaceConfigDTO config) {
        try {
            AirspaceDetailsDTO airspace = airspaceManagementAppService.createAirspace(config);
            return ResponseEntity.ok(airspace);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 加载空域
     *
     * @param airspaceId 空域ID
     * @return 空域详情
     */
    @GetMapping("/{airspaceId}")
    public ResponseEntity<AirspaceDetailsDTO> loadAirspace(@PathVariable String airspaceId) {
        try {
            AirspaceDetailsDTO airspace = airspaceManagementAppService.loadAirspace(airspaceId);
            return ResponseEntity.ok(airspace);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 保存空域
     *
     * @param airspaceId 空域ID
     * @return 保存结果
     */
    @PostMapping("/{airspaceId}/save")
    public ResponseEntity<String> saveAirspace(@PathVariable String airspaceId) {
        try {
            airspaceManagementAppService.saveAirspace(airspaceId);
            return ResponseEntity.ok("空域保存成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除空域
     *
     * @param airspaceId 空域ID
     * @return 删除结果
     */
    @DeleteMapping("/{airspaceId}")
    public ResponseEntity<String> deleteAirspace(@PathVariable String airspaceId) {
        try {
            airspaceManagementAppService.deleteAirspace(airspaceId);
            return ResponseEntity.ok("空域删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取所有空域列�?
     *
     * @return 空域列表
     */
    @GetMapping
    public ResponseEntity<List<AirspaceDetailsDTO>> getAllAirspaces() {
        try {
            List<AirspaceDetailsDTO> airspaces = airspaceManagementAppService.getAllAirspaces();
            return ResponseEntity.ok(airspaces);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新环境参数
     *
     * @param airspaceId 空域ID
     * @param params 环境参数
     * @return 更新结果
     */
    @PutMapping("/{airspaceId}/environment")
    public ResponseEntity<String> updateEnvironment(
            @PathVariable String airspaceId,
            @RequestBody @Valid EnvironmentUpdateParamsDTO params) {
        try {
            airspaceManagementAppService.updateEnvironment(airspaceId, params);
            return ResponseEntity.ok("环境参数更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取空域边界信息
     *
     * @param airspaceId 空域ID
     * @return 边界信息
     */
    @GetMapping("/{airspaceId}/boundary")
    public ResponseEntity<Object> getAirspaceBoundary(@PathVariable String airspaceId) {
        try {
            Object boundary = airspaceManagementAppService.getAirspaceBoundary(airspaceId);
            return ResponseEntity.ok(boundary);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 重置空域
     *
     * @param airspaceId 空域ID
     * @return 重置结果
     */
    @PostMapping("/{airspaceId}/reset")
    public ResponseEntity<String> resetAirspace(@PathVariable String airspaceId) {
        try {
            airspaceManagementAppService.resetAirspace(airspaceId);
            return ResponseEntity.ok("空域重置成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
