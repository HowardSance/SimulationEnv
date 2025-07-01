package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.application.dtos.request.AdjustDeviceParamDTO;
import com.JP.dronesim.application.dtos.response.DeviceDetailsDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.device.model.opticalcamera.OpticalCamera;
import com.JP.dronesim.domain.device.model.opticalcamera.OpticalCameraFactory;
import com.JP.dronesim.domain.device.model.radar.ElectromagneticRadar;
import com.JP.dronesim.domain.device.model.radar.RadarFactory;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 设备管理应用服务
 * 负责探测设备的部署、删除、修改等用例协调
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class DeviceManagementAppService {

    @Autowired
    private IAirspaceRepository airspaceRepository;

    @Autowired
    private OpticalCameraFactory opticalCameraFactory;

    @Autowired
    private RadarFactory radarFactory;

    /**
     * 部署探测设备
     *
     * @param airspaceId 空域ID
     * @param deviceParams 设备初始化参�?
     * @return 设备详情
     */
    public DeviceDetailsDTO deployDevice(String airspaceId, DeviceInitParamsDTO deviceParams) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateDeviceParams(deviceParams);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 校验设备位置是否在空域边界内
        validateDevicePosition(airspace, deviceParams.getPosition());

        // 创建设备
        AbstractProbeDevice device = createDevice(deviceParams);

        // 添加到空�?
        airspace.addEntity(device);
        airspaceRepository.save(airspace);

        // 转换为DTO
        return convertToDeviceDetailsDTO(device);
    }

    /**
     * 批量部署设备
     *
     * @param airspaceId 空域ID
     * @param deviceParamsList 设备参数列表
     * @return 设备详情列表
     */
    public List<DeviceDetailsDTO> batchDeployDevices(String airspaceId, List<DeviceInitParamsDTO> deviceParamsList) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateDeviceParamsList(deviceParamsList);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 批量创建设备
        List<AbstractProbeDevice> devices = deviceParamsList.stream()
                .map(params -> {
                    validateDevicePosition(airspace, params.getPosition());
                    return createDevice(params);
                })
                .collect(Collectors.toList());

        // 批量添加到空�?
        devices.forEach(airspace::addEntity);
        airspaceRepository.save(airspace);

        // 转换为DTO
        return devices.stream()
                .map(this::convertToDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取设备详情
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @return 设备详情
     */
    public DeviceDetailsDTO getDevice(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 转换为DTO
        return convertToDeviceDetailsDTO(device);
    }

    /**
     * 获取空域内所有设�?
     *
     * @param airspaceId 空域ID
     * @return 设备列表
     */
    public List<DeviceDetailsDTO> getAllDevices(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取所有设�?
        List<AbstractProbeDevice> devices = airspace.getAllDevices();

        // 转换为DTO
        return devices.stream()
                .map(this::convertToDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据设备类型获取设备列表
     *
     * @param airspaceId 空域ID
     * @param deviceType 设备类型
     * @return 设备列表
     */
    public List<DeviceDetailsDTO> getDevicesByType(String airspaceId, String deviceType) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateDeviceType(deviceType);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取指定类型的设�?
        List<AbstractProbeDevice> devices = airspace.getDevicesByType(DeviceType.valueOf(deviceType.toUpperCase()));

        // 转换为DTO
        return devices.stream()
                .map(this::convertToDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新设备参数
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @param params 设备参数
     */
    public void updateDeviceParams(String airspaceId, String deviceId, AdjustDeviceParamDTO params) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateDeviceParams(params);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 更新设备参数
        device.updateParameters(params.getParameters());

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 激活设�?
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     */
    public void activateDevice(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 激活设�?
        device.activate();

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 停用设备
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     */
    public void deactivateDevice(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 停用设备
        device.deactivate();

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 删除设备
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     */
    public void removeDevice(String airspaceId, String deviceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 删除设备
        airspace.removeEntity(deviceId);
        airspaceRepository.save(airspace);
    }

    /**
     * 获取设备探测日志
     *
     * @param airspaceId 空域ID
     * @param deviceId 设备ID
     * @param limit 日志条数限制
     * @return 探测日志
     */
    public Object getDeviceLogs(String airspaceId, String deviceId, int limit) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取设备
        AbstractProbeDevice device = airspace.getDevice(deviceId);
        if (device == null) {
            throw new RuntimeException("设备不存�? " + deviceId);
        }

        // 获取探测日志
        return device.getDetectionLog().getRecentEvents(limit);
    }

    /**
     * 创建设备
     *
     * @param deviceParams 设备参数
     * @return 设备实例
     */
    private AbstractProbeDevice createDevice(DeviceInitParamsDTO deviceParams) {
        String deviceId = UUID.randomUUID().toString();
        Position position = new Position(
                deviceParams.getPosition().getX(),
                deviceParams.getPosition().getY(),
                deviceParams.getPosition().getZ()
        );

        switch (deviceParams.getDeviceType()) {
            case "OPTICAL_CAMERA":
                return opticalCameraFactory.createOpticalCamera(
                        deviceId, position, deviceParams.getParameters());
            case "RADAR":
                return radarFactory.createRadar(
                        deviceId, position, deviceParams.getParameters());
            default:
                throw new RuntimeException("不支持的设备类型: " + deviceParams.getDeviceType());
        }
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
     * 校验设备参数
     *
     * @param deviceParams 设备参数
     */
    private void validateDeviceParams(DeviceInitParamsDTO deviceParams) {
        if (deviceParams.getDeviceType() == null || deviceParams.getDeviceType().trim().isEmpty()) {
            throw new RuntimeException("设备类型不能为空");
        }
        if (deviceParams.getPosition() == null) {
            throw new RuntimeException("设备位置不能为空");
        }
    }

    /**
     * 校验设备参数列表
     *
     * @param deviceParamsList 设备参数列表
     */
    private void validateDeviceParamsList(List<DeviceInitParamsDTO> deviceParamsList) {
        if (deviceParamsList == null || deviceParamsList.isEmpty()) {
            throw new RuntimeException("设备参数列表不能为空");
        }
        deviceParamsList.forEach(this::validateDeviceParams);
    }

    /**
     * 校验设备参数
     *
     * @param params 设备参数
     */
    private void validateDeviceParams(AdjustDeviceParamDTO params) {
        if (params.getParameters() == null || params.getParameters().isEmpty()) {
            throw new RuntimeException("设备参数不能为空");
        }
    }

    /**
     * 校验设备类型
     *
     * @param deviceType 设备类型
     */
    private void validateDeviceType(String deviceType) {
        try {
            DeviceType.valueOf(deviceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("不支持的设备类型: " + deviceType);
        }
    }

    /**
     * 校验设备位置
     *
     * @param airspace 空域
     * @param position 设备位置
     */
    private void validateDevicePosition(Airspace airspace, Position position) {
        Position min = airspace.getBoundaryMin();
        Position max = airspace.getBoundaryMax();

        if (position.getX() < min.getX() || position.getX() > max.getX() ||
            position.getY() < min.getY() || position.getY() > max.getY() ||
            position.getZ() < min.getZ() || position.getZ() > max.getZ()) {
            throw new RuntimeException("设备位置超出空域边界");
        }
    }

    /**
     * 转换为设备详情DTO
     *
     * @param device 设备
     * @return 设备详情DTO
     */
    private DeviceDetailsDTO convertToDeviceDetailsDTO(AbstractProbeDevice device) {
        DeviceDetailsDTO dto = new DeviceDetailsDTO();
        dto.setId(device.getId());
        dto.setType(device.getType());
        dto.setStatus(device.getStatus());
        dto.setPosition(device.getPosition());
        dto.setParameters(device.getParameters());
        dto.setDetectionCount(device.getDetectionLog().getEventCount());
        dto.setLastDetectionTime(device.getDetectionLog().getLastEventTime());
        return dto;
    }
}
