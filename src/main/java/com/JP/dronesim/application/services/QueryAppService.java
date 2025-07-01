package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.response.EntityStateDTO;
import com.JP.dronesim.application.dtos.response.DetectionLogEntryDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.uav.model.UAV;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询应用服务
 * 提供查询所有实体状态、特定设备详情、探测日志等用例协调
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class QueryAppService {

    @Autowired
    private IAirspaceRepository airspaceRepository;

    /**
     * 获取空域内所有实体状�?
     *
     * @param airspaceId 空域ID
     * @return 实体状态列�?
     */
    public List<EntityStateDTO> getAllEntityStates(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取所有实�?
        List<EntityStateDTO> entities = new ArrayList<>();

        // 添加设备实体
        List<AbstractProbeDevice> devices = airspace.getAllDevices();
        entities.addAll(devices.stream()
                .map(this::convertDeviceToEntityStateDTO)
                .collect(Collectors.toList()));

        // 添加无人机实�?
        List<UAV> uavs = airspace.getAllUAVs();
        entities.addAll(uavs.stream()
                .map(this::convertUAVToEntityStateDTO)
                .collect(Collectors.toList()));

        return entities;
    }

    /**
     * 获取特定实体状�?
     *
     * @param airspaceId 空域ID
     * @param entityId 实体ID
     * @return 实体状�?
     */
    public EntityStateDTO getEntityState(String airspaceId, String entityId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 先尝试获取设�?
        AbstractProbeDevice device = airspace.getDevice(entityId);
        if (device != null) {
            return convertDeviceToEntityStateDTO(device);
        }

        // 再尝试获取无人机
        UAV uav = airspace.getUAV(entityId);
        if (uav != null) {
            return convertUAVToEntityStateDTO(uav);
        }

        throw new RuntimeException("实体不存�? " + entityId);
    }

    /**
     * 根据实体类型查询实体
     *
     * @param airspaceId 空域ID
     * @param entityType 实体类型
     * @return 实体状态列�?
     */
    public List<EntityStateDTO> getEntitiesByType(String airspaceId, String entityType) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        switch (entityType.toUpperCase()) {
            case "DEVICE":
                return airspace.getAllDevices().stream()
                        .map(this::convertDeviceToEntityStateDTO)
                        .collect(Collectors.toList());
            case "UAV":
                return airspace.getAllUAVs().stream()
                        .map(this::convertUAVToEntityStateDTO)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("不支持的实体类型: " + entityType);
        }
    }

    /**
     * 获取特定设备详情
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 设备详情
     */
    public Object getDeviceDetails(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 构建设备详情
        Map<String, Object> details = new HashMap<>();
        details.put("id", device.getId());
        details.put("type", device.getType());
        details.put("status", device.getStatus());
        details.put("position", device.getPosition());
        details.put("parameters", device.getParameters());
        details.put("detectionCount", device.getDetectionLog().getEventCount());
        details.put("lastDetectionTime", device.getDetectionLog().getLastEventTime());
        details.put("detectionRange", device.getDetectionRange());
        details.put("accuracy", device.getAccuracy());

        return details;
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
    public List<DetectionLogEntryDTO> getDetectionLogs(String airspaceId, String deviceId,
                                                      String startTime, String endTime, int limit) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        List<DetectionLogEntryDTO> allLogs = new ArrayList<>();

        if (deviceId != null) {
            // 获取特定设备的探测日�?
            AbstractProbeDevice device = airspace.getDevice(deviceId);
            if (device == null) {
                throw new RuntimeException("设备不存�? " + deviceId);
            }
            allLogs.addAll(convertDetectionEventsToDTO(device.getDetectionLog().getEvents()));
        } else {
            // 获取所有设备的探测日志
            List<AbstractProbeDevice> devices = airspace.getAllDevices();
            for (AbstractProbeDevice device : devices) {
                allLogs.addAll(convertDetectionEventsToDTO(device.getDetectionLog().getEvents()));
            }
        }

        // 时间过滤
        if (startTime != null || endTime != null) {
            allLogs = filterLogsByTime(allLogs, startTime, endTime);
        }

        // 限制条数
        if (limit > 0 && allLogs.size() > limit) {
            allLogs = allLogs.subList(0, limit);
        }

        return allLogs;
    }

    /**
     * 获取实时探测数据
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 实时探测数据
     */
    public Object getRealTimeDetectionData(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 构建实时数据
        Map<String, Object> realTimeData = new HashMap<>();
        realTimeData.put("deviceId", device.getId());
        realTimeData.put("deviceType", device.getType());
        realTimeData.put("status", device.getStatus());
        realTimeData.put("position", device.getPosition());
        realTimeData.put("currentDetections", device.getCurrentDetections());
        realTimeData.put("lastDetectionTime", device.getDetectionLog().getLastEventTime());
        realTimeData.put("detectionCount", device.getDetectionLog().getEventCount());

        return realTimeData;
    }

    /**
     * 获取空域统计信息
     *
     * @param airspaceId 空域ID
     * @return 统计信息
     */
    public Map<String, Object> getAirspaceStatistics(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 构建统计信息
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("airspaceId", airspaceId);
        statistics.put("totalEntities", airspace.getEntityCount());
        statistics.put("deviceCount", airspace.getAllDevices().size());
        statistics.put("uavCount", airspace.getAllUAVs().size());
        statistics.put("currentTime", airspace.getCurrentTime());
        statistics.put("timeStep", airspace.getTimeStep());

        // 设备类型统计
        Map<String, Long> deviceTypeStats = airspace.getAllDevices().stream()
                .collect(Collectors.groupingBy(
                        device -> device.getType().toString(),
                        Collectors.counting()
                ));
        statistics.put("deviceTypeStats", deviceTypeStats);

        // 无人机状态统�?
        Map<String, Long> uavStatusStats = airspace.getAllUAVs().stream()
                .collect(Collectors.groupingBy(
                        uav -> uav.getStatus().toString(),
                        Collectors.counting()
                ));
        statistics.put("uavStatusStats", uavStatusStats);

        return statistics;
    }

    /**
     * 获取设备性能指标
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 性能指标
     */
    public Map<String, Object> getDevicePerformance(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 构建性能指标
        Map<String, Object> performance = new HashMap<>();
        performance.put("deviceId", device.getId());
        performance.put("deviceType", device.getType());
        performance.put("detectionCount", device.getDetectionLog().getEventCount());
        performance.put("accuracy", device.getAccuracy());
        performance.put("detectionRange", device.getDetectionRange());
        performance.put("uptime", device.getUptime());
        performance.put("lastMaintenance", device.getLastMaintenance());
        performance.put("status", device.getStatus());

        return performance;
    }

    /**
     * 获取无人机飞行轨�?
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param timeRange 时间范围（可选）
     * @return 飞行轨迹
     */
    public Object getUAVTrajectory(String airspaceId, String uavId, String timeRange) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人�?
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 构建轨迹信息
        Map<String, Object> trajectory = new HashMap<>();
        trajectory.put("uavId", uav.getId());
        trajectory.put("currentPosition", uav.getPosition());
        trajectory.put("flightPath", uav.getFlightPath());
        trajectory.put("currentWaypoint", uav.getCurrentWaypoint());
        trajectory.put("trajectoryHistory", uav.getTrajectoryHistory());

        return trajectory;
    }

    /**
     * 获取环境参数
     *
     * @param airspaceId 空域ID
     * @return 环境参数
     */
    public Object getEnvironmentParameters(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 返回环境参数
        return airspace.getEnvironmentParameters();
    }

    /**
     * 搜索实体
     *
     * @param airspaceId 空域ID
     * @param keyword 搜索关键�?
     * @param entityType 实体类型（可选）
     * @return 搜索结果
     */
    public List<EntityStateDTO> searchEntities(String airspaceId, String keyword, String entityType) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        List<EntityStateDTO> results = new ArrayList<>();

        if (entityType == null || "DEVICE".equalsIgnoreCase(entityType)) {
            // 搜索设备
            List<AbstractProbeDevice> devices = airspace.getAllDevices();
            results.addAll(devices.stream()
                    .filter(device -> device.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                     device.getId().toLowerCase().contains(keyword.toLowerCase()))
                    .map(this::convertDeviceToEntityStateDTO)
                    .collect(Collectors.toList()));
        }

        if (entityType == null || "UAV".equalsIgnoreCase(entityType)) {
            // 搜索无人�?
            List<UAV> uavs = airspace.getAllUAVs();
            results.addAll(uavs.stream()
                    .filter(uav -> uav.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                  uav.getId().toLowerCase().contains(keyword.toLowerCase()))
                    .map(this::convertUAVToEntityStateDTO)
                    .collect(Collectors.toList()));
        }

        return results;
    }

    /**
     * 校验空域是否存在
     *
     * @param airspaceId 空域ID
     */
    private void validateAirspaceExists(String airspaceId) {
        if (!airspaceRepository.existsById(airspaceId)) {
            throw new RuntimeException("空域不存�? " + airspaceId);
        }
    }

    /**
     * 转换设备为实体状态DTO
     *
     * @param device 设备
     * @return 实体状态DTO
     */
    private EntityStateDTO convertDeviceToEntityStateDTO(AbstractProbeDevice device) {
        EntityStateDTO dto = new EntityStateDTO();
        dto.setId(device.getId());
        dto.setType("DEVICE");
        dto.setName(device.getName());
        dto.setPosition(device.getPosition());
        dto.setStatus(device.getStatus().toString());
        dto.setProperties(device.getParameters());
        return dto;
    }

    /**
     * 转换无人机为实体状态DTO
     *
     * @param uav 无人�?
     * @return 实体状态DTO
     */
    private EntityStateDTO convertUAVToEntityStateDTO(UAV uav) {
        EntityStateDTO dto = new EntityStateDTO();
        dto.setId(uav.getId());
        dto.setType("UAV");
        dto.setName(uav.getName());
        dto.setPosition(uav.getPosition());
        dto.setVelocity(uav.getVelocity());
        dto.setOrientation(uav.getOrientation());
        dto.setStatus(uav.getStatus().toString());
        dto.setProperties(uav.getPhysicalProperties());
        return dto;
    }

    /**
     * 转换探测事件为DTO
     *
     * @param events 探测事件列表
     * @return 探测日志条目DTO列表
     */
    private List<DetectionLogEntryDTO> convertDetectionEventsToDTO(List<Object> events) {
        // 这里需要根据实际的探测事件数据结构进行转换
        // 暂时返回空列表，实际实现时需要根据具体的数据格式进行解析
        return new ArrayList<>();
    }

    /**
     * 根据时间过滤日志
     *
     * @param logs 日志列表
     * @param startTime 开始时�?
     * @param endTime 结束时间
     * @return 过滤后的日志列表
     */
    private List<DetectionLogEntryDTO> filterLogsByTime(List<DetectionLogEntryDTO> logs,
                                                       String startTime, String endTime) {
        // 这里需要根据实际的时间格式进行过滤
        // 暂时返回原列表，实际实现时需要根据具体的时间格式进行解析
        return logs;
    }
}
