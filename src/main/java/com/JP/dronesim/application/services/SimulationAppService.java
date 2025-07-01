package com.JP.dronesim.application.services;

import com.JP.dronesim.application.dtos.request.SimulationControlCommandDTO;
import com.JP.dronesim.application.dtos.response.SimulationStatusDTO;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.airspace.repository.IAirspaceRepository;
import com.JP.dronesim.domain.services.SimulationEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 仿真应用服务
 * 负责仿真的启动、暂停、恢复、停止、时间推进等用例协调
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class SimulationAppService {

    @Autowired
    private IAirspaceRepository airspaceRepository;

    @Autowired
    private SimulationEngineService simulationEngineService;

    /**
     * 仿真状态存�?
     */
    private final ConcurrentHashMap<String, SimulationStatusDTO> simulationStatusMap = new ConcurrentHashMap<>();

    /**
     * 仿真运行状�?
     */
    private final ConcurrentHashMap<String, AtomicBoolean> runningStatusMap = new ConcurrentHashMap<>();

    /**
     * 启动仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    public SimulationStatusDTO startSimulation(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateSimulationNotRunning(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 启动仿真引擎
        simulationEngineService.startSimulation(airspace);

        // 更新状�?
        SimulationStatusDTO status = new SimulationStatusDTO();
        status.setAirspaceId(airspaceId);
        status.setStatus("RUNNING");
        status.setStartTime(LocalDateTime.now());
        status.setCurrentTime(airspace.getCurrentTime());
        status.setTimeStep(airspace.getTimeStep());

        simulationStatusMap.put(airspaceId, status);
        runningStatusMap.put(airspaceId, new AtomicBoolean(true));

        return status;
    }

    /**
     * 暂停仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    public SimulationStatusDTO pauseSimulation(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateSimulationRunning(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 暂停仿真引擎
        simulationEngineService.pauseSimulation(airspace);

        // 更新状�?
        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status != null) {
            status.setStatus("PAUSED");
            status.setPauseTime(LocalDateTime.now());
        }

        runningStatusMap.get(airspaceId).set(false);

        return status;
    }

    /**
     * 恢复仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    public SimulationStatusDTO resumeSimulation(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateSimulationPaused(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 恢复仿真引擎
        simulationEngineService.resumeSimulation(airspace);

        // 更新状�?
        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status != null) {
            status.setStatus("RUNNING");
            status.setResumeTime(LocalDateTime.now());
        }

        runningStatusMap.get(airspaceId).set(true);

        return status;
    }

    /**
     * 停止仿真
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    public SimulationStatusDTO stopSimulation(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 停止仿真引擎
        simulationEngineService.stopSimulation(airspace);

        // 更新状�?
        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status != null) {
            status.setStatus("STOPPED");
            status.setEndTime(LocalDateTime.now());
        }

        runningStatusMap.remove(airspaceId);

        return status;
    }

    /**
     * 时间步进
     *
     * @param airspaceId 空域ID
     * @param timeStep 时间步长
     * @return 仿真状�?
     */
    public SimulationStatusDTO stepSimulation(String airspaceId, double timeStep) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateTimeStep(timeStep);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 执行时间步进
        simulationEngineService.stepSimulation(airspace, timeStep);

        // 更新状�?
        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status != null) {
            status.setCurrentTime(airspace.getCurrentTime());
            status.setLastStepTime(LocalDateTime.now());
        }

        return status;
    }

    /**
     * 获取仿真状�?
     *
     * @param airspaceId 空域ID
     * @return 仿真状�?
     */
    public SimulationStatusDTO getSimulationStatus(String airspaceId) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);

        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status == null) {
            // 如果状态不存在，创建默认状�?
            Airspace airspace = airspaceRepository.findById(airspaceId);
            status = new SimulationStatusDTO();
            status.setAirspaceId(airspaceId);
            status.setStatus("STOPPED");
            status.setCurrentTime(airspace.getCurrentTime());
            status.setTimeStep(airspace.getTimeStep());
            simulationStatusMap.put(airspaceId, status);
        }

        return status;
    }

    /**
     * 设置仿真时间步长
     *
     * @param airspaceId 空域ID
     * @param timeStep 时间步长
     * @return 仿真状�?
     */
    public SimulationStatusDTO setTimeStep(String airspaceId, double timeStep) {
        // 业务规则校验
        validateAirspaceExists(airspaceId);
        validateTimeStep(timeStep);

        // 获取空域
        Airspace airspace = airspaceRepository.findById(airspaceId);

        // 设置时间步长
        airspace.setTimeStep(timeStep);
        airspaceRepository.save(airspace);

        // 更新状�?
        SimulationStatusDTO status = simulationStatusMap.get(airspaceId);
        if (status != null) {
            status.setTimeStep(timeStep);
        }

        return status;
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
     * 校验仿真是否未运�?
     *
     * @param airspaceId 空域ID
     */
    private void validateSimulationNotRunning(String airspaceId) {
        AtomicBoolean running = runningStatusMap.get(airspaceId);
        if (running != null && running.get()) {
            throw new RuntimeException("仿真已在运行�? " + airspaceId);
        }
    }

    /**
     * 校验仿真是否正在运行
     *
     * @param airspaceId 空域ID
     */
    private void validateSimulationRunning(String airspaceId) {
        AtomicBoolean running = runningStatusMap.get(airspaceId);
        if (running == null || !running.get()) {
            throw new RuntimeException("仿真未在运行: " + airspaceId);
        }
    }

    /**
     * 校验仿真是否已暂�?
     *
     * @param airspaceId 空域ID
     */
    private void validateSimulationPaused(String airspaceId) {
        AtomicBoolean running = runningStatusMap.get(airspaceId);
        if (running == null || running.get()) {
            throw new RuntimeException("仿真未暂�? " + airspaceId);
        }
    }

    /**
     * 校验时间步长
     *
     * @param timeStep 时间步长
     */
    private void validateTimeStep(double timeStep) {
        if (timeStep <= 0) {
            throw new RuntimeException("时间步长必须大于0");
        }
        if (timeStep > 3600) { // 最�?小时
            throw new RuntimeException("时间步长不能超过1小时");
        }
    }
}
