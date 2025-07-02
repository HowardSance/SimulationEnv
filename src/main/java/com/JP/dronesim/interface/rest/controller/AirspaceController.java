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
     * 获取唯一空域详情
     */
    @GetMapping
    public ResponseEntity<AirspaceDetailsDTO> getAirspace() {
        try {
            AirspaceDetailsDTO airspace = airspaceManagementAppService.getAirspace();
            return ResponseEntity.ok(airspace);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新环境参数
     *
     * @param params 环境参数
     * @return 更新结果
     */
    @PutMapping("/environment")
    public ResponseEntity<String> updateEnvironment(
            @RequestBody @Valid EnvironmentUpdateParamsDTO params) {
        try {
            airspaceManagementAppService.updateEnvironment(params);
            return ResponseEntity.ok("环境参数更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取空域边界信息
     *
     * @return 边界信息
     */
    @GetMapping("/boundary")
    public ResponseEntity<Object> getAirspaceBoundary() {
        try {
            Object boundary = airspaceManagementAppService.getAirspaceBoundary();
            return ResponseEntity.ok(boundary);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 重置空域
     *
     * @return 重置结果
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetAirspace() {
        try {
            airspaceManagementAppService.resetAirspace();
            return ResponseEntity.ok("空域重置成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
