package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.response.EntityStateDTO;
import com.JP.dronesim.application.dtos.response.DetectionLogEntryDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.device.model.ProbeDevice;
import com.JP.dronesim.domain.device.model.common.DetectionLog;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.UAV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询应用服务
 * 提供查询所有实体状态、特定设备详情、探测日志等用例协调
 */
@Service
public class QueryAppService {
    @Autowired
    private IAirspaceRepository airspaceRepository;

    /**
     * 获取空域内所有实体状态（设备+无人机）
     */
    public List<EntityStateDTO> getAllEntityStates(String airspaceId) {
        Airspace airspace = getAirspaceOrThrow(airspaceId);
        List<EntityStateDTO> result = new ArrayList<>();
        // 设备
        for (ProbeDevice device : airspace.getProbeDevices().values()) {
            result.add(toEntityStateDTO(device));
        }
        // 无人机
        for (UAV uav : airspace.getUAVs().values()) {
            result.add(toEntityStateDTO(uav));
        }
        return result;
    }

    /**
     * 获取特定设备详情
     */
    public Map<String, Object> getDeviceDetails(String airspaceId, String deviceId) {
        Airspace airspace = getAirspaceOrThrow(airspaceId);
        ProbeDevice device = airspace.getProbeDevices().get(deviceId);
        if (device == null) throw new RuntimeException("设备不存在: " + deviceId);
        Map<String, Object> details = new HashMap<>();
        details.put("id", device.getId());
        details.put("type", device.getType());
        details.put("status", device.getStatus());
        details.put("position", device.getPosition());
        details.put("parameters", device.getDetectionParameters());
        details.put("detectionCount", device.getDetectionLog().getEventCount());
        details.put("lastDetectionTime", getLastDetectionTime(device.getDetectionLog()));
        return details;
    }

    /**
     * 获取探测日志（可按设备、时间过滤）
     */
    public List<DetectionLogEntryDTO> getDetectionLogs(String airspaceId, String deviceId, LocalDateTime start, LocalDateTime end) {
        Airspace airspace = getAirspaceOrThrow(airspaceId);
        List<DetectionEvent> events = new ArrayList<>();
        if (deviceId != null) {
            ProbeDevice device = airspace.getProbeDevices().get(deviceId);
            if (device == null) throw new RuntimeException("设备不存在: " + deviceId);
            DetectionLog log = device.getDetectionLog();
            if (start != null && end != null) {
                events.addAll(log.getEventsBetween(start, end));
            } else {
                events.addAll(log.getAllEvents());
            }
        } else {
            for (ProbeDevice device : airspace.getProbeDevices().values()) {
                DetectionLog log = device.getDetectionLog();
                if (start != null && end != null) {
                    events.addAll(log.getEventsBetween(start, end));
                } else {
                    events.addAll(log.getAllEvents());
                }
            }
        }
        return events.stream().map(this::toDetectionLogEntryDTO).collect(Collectors.toList());
    }

    // ====== 私有工具方法 ======
    private Airspace getAirspaceOrThrow(String airspaceId) {
        return airspaceRepository.findById(airspaceId)
                .orElseThrow(() -> new RuntimeException("空域不存在: " + airspaceId));
    }

    private EntityStateDTO toEntityStateDTO(ProbeDevice device) {
        EntityStateDTO dto = new EntityStateDTO();
        dto.setId(device.getId());
        dto.setType(device.getType().name());
        dto.setName(device.getName());
        dto.setPosition(device.getPosition());
        dto.setStatus(device.getStatus().name());
        dto.setActive(device.isActive());
        dto.setProperties(Collections.singletonMap("detectionParameters", device.getDetectionParameters()));
        return dto;
    }

    private EntityStateDTO toEntityStateDTO(UAV uav) {
        EntityStateDTO dto = new EntityStateDTO();
        dto.setId(uav.getId());
        dto.setType("UAV");
        dto.setName(uav.getName());
        dto.setPosition(uav.getPosition());
        dto.setVelocity(uav.getVelocity());
        dto.setOrientation(uav.getOrientation());
        dto.setStatus(uav.getStatus().name());
        dto.setActive(uav.getStatus().name().equals("MOVING") || uav.getStatus().name().equals("HOVERING") || uav.getStatus().name().equals("STANDBY"));
        dto.setLastUpdateTime(uav.getLastUpdatedAt());
        // 可扩展更多属性
        return dto;
    }

    private DetectionLogEntryDTO toDetectionLogEntryDTO(DetectionEvent event) {
        DetectionLogEntryDTO dto = new DetectionLogEntryDTO();
        dto.setEventId(event.getEventId());
        dto.setDeviceId(event.getDetectorId());
        dto.setDeviceType("UNKNOWN"); // 可根据detectorId映射类型
        dto.setTargetId(event.getDetectedUavId());
        dto.setTimestamp(event.getTimestamp());
        dto.setDetectionPosition(event.getDetectedPosition());
        dto.setDistance(event.getDetectionDistance());
        dto.setConfidence(event.getConfidence());
        dto.setRemarks(event.getDescription());
        return dto;
    }

    private LocalDateTime getLastDetectionTime(DetectionLog log) {
        List<DetectionEvent> events = log.getAllEvents();
        if (events.isEmpty()) return null;
        return events.get(events.size() - 1).getTimestamp();
    }
} 