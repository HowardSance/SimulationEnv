package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.request.UAVStateDTO;
import com.JP.dronesim.application.dtos.response.EntityStateDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.uav.model.UAV;
import com.JP.dronesim.domain.uav.model.Waypoint;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 无人机管理应用服务
 * 负责无人机的部署、删除、修改、飞行路径设置等用例协调
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class UAVManagementAppService {

    @Autowired
    private IAirspaceRepository airspaceRepository;

    /**
     * 部署无人机
     *
     * @param airspaceId 空域ID
     * @param uavState 无人机状态
     * @return 无人机状态
     */
    public EntityStateDTO deployUAV(String airspaceId, UAVStateDTO uavState) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateUAVState(uavState);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 校验无人机位置是否在空域边界
        validateUAVPosition(airspace, uavState.getPosition());

        // 创建无人机
        UAV uav = createUAV(uavState);

        // 添加到空域
        airspace.addEntity(uav);
        airspaceRepository.save(airspace);

        // 转换为DTO
        return convertToEntityStateDTO(uav);
    }

    /**
     * 批量部署无人机
     *
     * @param airspaceId 空域ID
     * @param uavStates 无人机状态列表
     * @return 无人机状态列表
     */
    public List<EntityStateDTO> batchDeployUAVs(String airspaceId, List<UAVStateDTO> uavStates) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateUAVStateList(uavStates);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 批量创建无人机
        List<UAV> uavs = uavStates.stream()
                .map(state -> {
                    validateUAVPosition(airspace, state.getPosition());
                    return createUAV(state);
                })
                .collect(Collectors.toList());

        // 批量添加到空域
        uavs.forEach(airspace::addEntity);
        airspaceRepository.save(airspace);

        // 转换为DTO
        return uavs.stream()
                .map(this::convertToEntityStateDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取无人机状态
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
    * @return 无人机状态
     */
    public EntityStateDTO getUAV(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 转换为DTO
        return convertToEntityStateDTO(uav);
    }

    /**
     * 获取空域内所有无人机
     *
     * @param airspaceId 空域ID
     * @return 无人机列表
     */
    public List<EntityStateDTO> getAllUAVs(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取所有无人机
        List<UAV> uavs = airspace.getAllUAVs();

        // 转换为DTO
        return uavs.stream()
                .map(this::convertToEntityStateDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新无人机状态
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param uavState 无人机状态
     */
    public void updateUAV(String airspaceId, String uavId, UAVStateDTO uavState) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateUAVState(uavState);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 校验新位置是否在空域边界
        validateUAVPosition(airspace, uavState.getPosition());

        // 更新无人机状态
        uav.setPosition(uavState.getPosition());
        uav.setVelocity(uavState.getVelocity());
        uav.setOrientation(uavState.getOrientation());
        uav.setStatus(uavState.getStatus());

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 设置无人机飞行路径
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @param waypoints 航点列表
     */
    public void setFlightPath(String airspaceId, String uavId, List<Object> waypoints) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateWaypoints(airspaceId, waypoints);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 设置飞行路径
        List<Waypoint> waypointList = waypoints.stream()
                .map(this::convertToWaypoint)
                .collect(Collectors.toList());
        uav.setFlightPath(waypointList);

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 获取无人机飞行路径
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     * @return 飞行路径
     */
    public Object getFlightPath(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 返回飞行路径
        return uav.getFlightPath();
    }

    /**
        * 控制无人机起飞
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     */
    public void takeoffUAV(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 控制起飞
        uav.takeoff();

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 控制无人机降落
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     */
    public void landUAV(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 控制降落
        uav.land();

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 控制无人机悬停
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     */
    public void hoverUAV(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 获取无人机
        UAV uav = airspace.getUAV(uavId);
        if (uav == null) {
            throw new RuntimeException("无人机不存在: " + uavId);
        }

        // 控制悬停
        uav.hover();

        // 保存空域
        airspaceRepository.save(airspace);
    }

    /**
     * 删除无人机
     *
     * @param airspaceId 空域ID
     * @param uavId 无人机ID
     */
    public void removeUAV(String airspaceId, String uavId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 删除无人机
        airspace.removeEntity(uavId);
        airspaceRepository.save(airspace);
    }

    /**
     * 创建无人机
     *
     * @param uavState 无人机状态
     * @return 无人机
     */
    private UAV createUAV(UAVStateDTO uavState) {
        String uavId = uavState.getId() != null ? uavState.getId() : UUID.randomUUID().toString();

        UAV uav = new UAV();
        uav.setId(uavId);
        uav.setName(uavState.getName());
        uav.setPosition(uavState.getPosition());
        uav.setVelocity(uavState.getVelocity());
        uav.setOrientation(uavState.getOrientation());
        uav.setStatus(uavState.getStatus());

        return uav;
    }

    /**
     * 转换为航点
     *
     * @param waypointObj 航点对象
     * @return 航点
     */
    private Waypoint convertToWaypoint(Object waypointObj) {
        // 这里需要根据实际的航点数据结构进行转换
        // 暂时返回null，实际实现时需要根据具体的数据格式进行解析
        return null;
    }

    /**
     * 校验空域是否存在
     *
     * @param airspaceId 空域ID
     */
    private void validateAirspaceExists(String airspaceId) {
        if (!airspaceRepository.existsById(airspaceId)) {
            throw new RuntimeException("空域不存在: " + airspaceId);
        }
    }

    /**
     * 校验无人机状态
     *
     * @param uavState 无人机状态
     */
    private void validateUAVState(UAVStateDTO uavState) {
        if (uavState.getPosition() == null) {
            throw new RuntimeException("无人机位置不能为空");
        }
        if (uavState.getVelocity() == null) {
            throw new RuntimeException("无人机速度不能为空");
        }
        if (uavState.getOrientation() == null) {
            throw new RuntimeException("无人机姿态不能为空");
        }
    }

    /**
     * 校验无人机状态列表
     *
     * @param uavStates 无人机状态列表
     */
    private void validateUAVStateList(List<UAVStateDTO> uavStates) {
        if (uavStates == null || uavStates.isEmpty()) {
            throw new RuntimeException("无人机状态列表不能为空");
        }
        uavStates.forEach(this::validateUAVState);
    }

    /**
     * 校验航点
     *
     * @param airspaceId 空域ID
     * @param waypoints 航点列表
     */
    private void validateWaypoints(String airspaceId, List<Object> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) {
            throw new RuntimeException("航点列表不能为空");
        }
        // 这里可以添加更多的航点校验逻辑
    }

    /**
     * 校验无人机位置
     *
     * @param airspace 空域
     * @param position 无人机位置
     */
    private void validateUAVPosition(Airspace airspace, Position position) {
        Position min = airspace.getBoundaryMin();
        Position max = airspace.getBoundaryMax();

        if (position.getX() < min.getX() || position.getX() > max.getX() ||
            position.getY() < min.getY() || position.getY() > max.getY() ||
            position.getZ() < min.getZ() || position.getZ() > max.getZ()) {
            throw new RuntimeException("无人机位置超出空域边界");
        }
    }

    /**
     * 转换为实体状态DTO
     *
     * @param uav 无人机
     * @return 实体状态DTO
     */
    private EntityStateDTO convertToEntityStateDTO(UAV uav) {
        EntityStateDTO dto = new EntityStateDTO();
        dto.setId(uav.getId());
        dto.setType("UAV");
        dto.setName(uav.getName());
        dto.setPosition(uav.getPosition());
        dto.setVelocity(uav.getVelocity());
        dto.setOrientation(uav.getOrientation());
        dto.setStatus(uav.getStatus().toString());
        return dto;
    }
}
