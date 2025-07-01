package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.request.AirspaceConfigDTO;
import com.JP.dronesim.application.dtos.request.EnvironmentUpdateParamsDTO;
import com.JP.dronesim.application.dtos.response.AirspaceDetailsDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.common.valueobjects.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 空域管理应用服务
 * 负责场景的创建、加载、保存、查询等用例协调
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class AirspaceManagementAppService {

    @Autowired
    private IAirspaceRepository airspaceRepository;

    /**
     * 创建空域
     *
     * @param config 空域配置
     * @return 空域详情
     */
    public AirspaceDetailsDTO createAirspace(AirspaceConfigDTO config) {
        // 业务规则校验
        validateAirspaceConfig(config);

        // 生成空域ID
        String airspaceId = UUID.randomUUID().toString();

        // 创建环境参数
        EnvironmentParameters environmentParams = new EnvironmentParameters();
        environmentParams.setTemperature(config.getTemperature());
        environmentParams.setHumidity(config.getHumidity());
        environmentParams.setWindSpeed(config.getWindSpeed());
        environmentParams.setWindDirection(config.getWindDirection());
        environmentParams.setVisibility(config.getVisibility());
        environmentParams.setPressure(config.getPressure());

        // 创建空域
        Airspace airspace = new Airspace();
        airspace.setId(airspaceId);
        airspace.setName(config.getName());
        airspace.setDescription(config.getDescription());
        airspace.setBoundaryMin(new Position(config.getMinX(), config.getMinY(), config.getMinZ()));
        airspace.setBoundaryMax(new Position(config.getMaxX(), config.getMaxY(), config.getMaxZ()));
        airspace.setEnvironmentParameters(environmentParams);
        airspace.setTimeStep(config.getTimeStep());
        airspace.setCurrentTime(0.0);
        airspace.setCreatedAt(LocalDateTime.now());
        airspace.setUpdatedAt(LocalDateTime.now());

        // 保存空域
        airspaceRepository.save(airspace);

        // 转换为DTO
        return convertToAirspaceDetailsDTO(airspace);
    }

    /**
     * 加载空域
     *
     * @param airspaceId 空域ID
     * @return 空域详情
     */
    public AirspaceDetailsDTO loadAirspace(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 转换为DTO
        return convertToAirspaceDetailsDTO(airspace);
    }

    /**
     * 保存空域
     *
     * @param airspaceId 空域ID
     */
    public void saveAirspace(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 更新修改时间
        airspace.setUpdatedAt(LocalDateTime.now());

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 删除空域
     *
     * @param airspaceId 空域ID
     */
    public void deleteAirspace(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 删除空域
        airspaceRepository.deleteById(airspaceId);
    }

    /**
     * 获取所有空域列�?
     *
     * @return 空域列表
     */
    public List<AirspaceDetailsDTO> getAllAirspaces() {
        List<Airspace> airspaces = airspaceRepository.findAll();
        return airspaces.stream()
                .map(this::convertToAirspaceDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新环境参数
     *
     * @param airspaceId 空域ID
     * @param params 环境参数
     */
    public void updateEnvironment(String airspaceId, EnvironmentUpdateParamsDTO params) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateEnvironmentParams(params);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 更新环境参数
        EnvironmentParameters environmentParams = airspace.getEnvironmentParameters();
        if (params.getTemperature() != null) {
            environmentParams.setTemperature(params.getTemperature());
        }
        if (params.getHumidity() != null) {
            environmentParams.setHumidity(params.getHumidity());
        }
        if (params.getWindSpeed() != null) {
            environmentParams.setWindSpeed(params.getWindSpeed());
        }
        if (params.getWindDirection() != null) {
            environmentParams.setWindDirection(params.getWindDirection());
        }
        if (params.getVisibility() != null) {
            environmentParams.setVisibility(params.getVisibility());
        }
        if (params.getPressure() != null) {
            environmentParams.setPressure(params.getPressure());
        }

        // 更新修改时间
        airspace.setUpdatedAt(LocalDateTime.now());

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 获取空域边界信息
     *
     * @param airspaceId 空域ID
     * @return 边界信息
     */
    public Object getAirspaceBoundary(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 构建边界信息
        return new Object() {
            public final Position min = airspace.getBoundaryMin();
            public final Position max = airspace.getBoundaryMax();
            public final double width = max.getX() - min.getX();
            public final double height = max.getY() - min.getY();
            public final double depth = max.getZ() - min.getZ();
        };
    }

    /**
     * 重置空域
     *
     * @param airspaceId 空域ID
     */
    public void resetAirspace(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 重置空域状�?
        airspace.setCurrentTime(0.0);
        airspace.clearEntities();
        airspace.setUpdatedAt(LocalDateTime.now());

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 校验空域配置
     *
     * @param config 空域配置
     */
    private void validateAirspaceConfig(AirspaceConfigDTO config) {
        if (config.getName() == null || config.getName().trim().isEmpty()) {
            throw new RuntimeException("空域名称不能为空");
        }
        if (config.getMaxX() <= config.getMinX()) {
            throw new RuntimeException("空域X轴边界设置错�?);
        }
        if (config.getMaxY() <= config.getMinY()) {
            throw new RuntimeException("空域Y轴边界设置错�?);
        }
        if (config.getMaxZ() <= config.getMinZ()) {
            throw new RuntimeException("空域Z轴边界设置错�?);
        }
        if (config.getTimeStep() <= 0) {
            throw new RuntimeException("时间步长必须大于0");
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
     * 校验环境参数
     *
     * @param params 环境参数
     */
    private void validateEnvironmentParams(EnvironmentUpdateParamsDTO params) {
        if (params.getTemperature() != null && (params.getTemperature() < -100 || params.getTemperature() > 100)) {
            throw new RuntimeException("温度超出有效范围");
        }
        if (params.getHumidity() != null && (params.getHumidity() < 0 || params.getHumidity() > 100)) {
            throw new RuntimeException("湿度超出有效范围");
        }
        if (params.getWindSpeed() != null && params.getWindSpeed() < 0) {
            throw new RuntimeException("风速不能为负数");
        }
        if (params.getWindDirection() != null && (params.getWindDirection() < 0 || params.getWindDirection() > 360)) {
            throw new RuntimeException("风向角度超出有效范围");
        }
        if (params.getVisibility() != null && params.getVisibility() < 0) {
            throw new RuntimeException("能见度不能为负数");
        }
        if (params.getPressure() != null && params.getPressure() < 0) {
            throw new RuntimeException("气压不能为负�?);
        }
    }

    /**
     * 转换为空域详情DTO
     *
     * @param airspace 空域
     * @return 空域详情DTO
     */
    private AirspaceDetailsDTO convertToAirspaceDetailsDTO(Airspace airspace) {
        AirspaceDetailsDTO dto = new AirspaceDetailsDTO();
        dto.setId(airspace.getId());
        dto.setName(airspace.getName());
        dto.setDescription(airspace.getDescription());
        dto.setBoundaryMin(airspace.getBoundaryMin());
        dto.setBoundaryMax(airspace.getBoundaryMax());
        dto.setEnvironmentParameters(airspace.getEnvironmentParameters());
        dto.setTimeStep(airspace.getTimeStep());
        dto.setCurrentTime(airspace.getCurrentTime());
        dto.setEntityCount(airspace.getEntityCount());
        dto.setCreatedAt(airspace.getCreatedAt());
        dto.setUpdatedAt(airspace.getUpdatedAt());
        return dto;
    }
}
