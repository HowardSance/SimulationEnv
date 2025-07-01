package com.JP.dronesim.interface.rest.controller;

import com.JP.dronesim.application.dtos.response.EntityStateDTO;
import com.JP.dronesim.application.dtos.response.DetectionLogEntryDTO;
import com.JP.dronesim.application.services.QueryAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 查询REST控制�?
 * 处理数据查询相关的HTTP请求
 *
 * @author JP Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private QueryAppService queryAppService;

    /**
     * 获取空域内所有实体状�?
     *
     * @param airspaceId 空域ID
     * @return 实体状态列�?
     */
    @GetMapping("/airspace/{airspaceId}/entities")
    public ResponseEntity<List<EntityStateDTO>> getAllEntityStates(@PathVariable String airspaceId) {
        try {
            List<EntityStateDTO> entities = queryAppService.getAllEntityStates(airspaceId);
            return ResponseEntity.ok(entities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取特定实体状�?
     *
     * @param airspaceId 空域ID
     * @param entityId 实体ID
     * @return 实体状�?
     */
    @GetMapping("/airspace/{airspaceId}/entities/{entityId}")
    public ResponseEntity<EntityStateDTO> getEntityState(
            @PathVariable String airspaceId,
            @PathVariable String entityId) {
        try {
            EntityStateDTO entity = queryAppService.getEntityState(airspaceId, entityId);
            return ResponseEntity.ok(entity);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据实体类型查询实体
     *
     * @param airspaceId 空域ID
     * @param entityType 实体类型
     * @return 实体状态列�?
     */
    @GetMapping("/airspace/{airspaceId}/entities/type/{entityType}")
    public ResponseEntity<List<EntityStateDTO>> getEntitiesByType(
            @PathVariable String airspaceId,
            @PathVariable String entityType) {
        try {
            List<EntityStateDTO> entities = queryAppService.getEntitiesByType(airspaceId, entityType);
            return ResponseEntity.ok(entities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取特定设备详情
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 设备详情
     */
    @GetMapping("/airspace/{airspaceId}/devices/{deviceId}/details")
    public ResponseEntity<Object> getDeviceDetails(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            Object details = queryAppService.getDeviceDetails(airspaceId, deviceId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取探测日志
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param limit 日志条数限制
     * @return 探测日志列表
     */
    @GetMapping("/airspace/{airspaceId}/detection-logs")
    public ResponseEntity<List<DetectionLogEntryDTO>> getDetectionLogs(
            @PathVariable String airspaceId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "100") int limit) {
        try {
            List<DetectionLogEntryDTO> logs = queryAppService.getDetectionLogs(
                    airspaceId, deviceId, startTime, endTime, limit);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取实时探测数据
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 实时探测数据
     */
    @GetMapping("/airspace/{airspaceId}/devices/{deviceId}/realtime")
    public ResponseEntity<Object> getRealTimeDetectionData(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            Object data = queryAppService.getRealTimeDetectionData(airspaceId, deviceId);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取空域统计信息
     *
     * @param airspaceId 空域ID
     * @return 统计信息
     */
    @GetMapping("/airspace/{airspaceId}/statistics")
    public ResponseEntity<Map<String, Object>> getAirspaceStatistics(@PathVariable String airspaceId) {
        try {
            Map<String, Object> statistics = queryAppService.getAirspaceStatistics(airspaceId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取设备性能指标
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 性能指标
     */
    @GetMapping("/airspace/{airspaceId}/devices/{deviceId}/performance")
    public ResponseEntity<Map<String, Object>> getDevicePerformance(
            @PathVariable String airspaceId,
            @PathVariable String deviceId) {
        try {
            Map<String, Object> performance = queryAppService.getDevicePerformance(airspaceId, deviceId);
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取无人机飞行轨�?
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param timeRange 时间范围（可选）
     * @return 飞行轨迹
     */
    @GetMapping("/airspace/{airspaceId}/uavs/{uavId}/trajectory")
    public ResponseEntity<Object> getUAVTrajectory(
            @PathVariable String airspaceId,
            @PathVariable String uavId,
            @RequestParam(required = false) String timeRange) {
        try {
            Object trajectory = queryAppService.getUAVTrajectory(airspaceId, uavId, timeRange);
            return ResponseEntity.ok(trajectory);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取环境参数
     *
     * @param airspaceId 空域ID
     * @return 环境参数
     */
    @GetMapping("/airspace/{airspaceId}/environment")
    public ResponseEntity<Object> getEnvironmentParameters(@PathVariable String airspaceId) {
        try {
            Object environment = queryAppService.getEnvironmentParameters(airspaceId);
            return ResponseEntity.ok(environment);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 搜索实体
     *
     * @param airspaceId 空域ID
     * @param keyword 搜索关键�?
     * @param entityType 实体类型（可选）
     * @return 搜索结果
     */
    @GetMapping("/airspace/{airspaceId}/search")
    public ResponseEntity<List<EntityStateDTO>> searchEntities(
            @PathVariable String airspaceId,
            @RequestParam String keyword,
            @RequestParam(required = false) String entityType) {
        try {
            List<EntityStateDTO> results = queryAppService.searchEntities(airspaceId, keyword, entityType);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
