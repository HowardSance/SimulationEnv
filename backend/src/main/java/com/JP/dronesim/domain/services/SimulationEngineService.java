package com.JP.dronesim.domain.services;

import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.device.model.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.UAV;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿真引擎服务领域服务
 * 推进仿真时间，协调Airspace、UAV移动和ProbeDevice探测
 *
 * @author JP Team
 * @version 1.0
 */
public class SimulationEngineService {

    /**
     * 启动仿真
     *
     * @param airspace 空域聚合根
     */
    public void startSimulation(Airspace airspace) {
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }

        // 启动空域仿真
        airspace.startSimulation();

        // 初始化仿真状态
        initializeSimulationState(airspace);
    }

    /**
     * 暂停仿真
     *
     * @param airspace 空域聚合根
     */
    public void pauseSimulation(Airspace airspace) {
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }

        airspace.pauseSimulation();
    }

    /**
     * 恢复仿真
     *
     * @param airspace 空域聚合根
     */
    public void resumeSimulation(Airspace airspace) {
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }

        airspace.startSimulation();
    }

    /**
     * 停止仿真
     *
     * @param airspace 空域聚合根
     */
    public void stopSimulation(Airspace airspace) {
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }
        
        airspace.stopSimulation();
    }
    
    /**
     * 执行时间步进
     * 推进仿真时间，更新所有实体状态，执行探测判定
     *
     * @param airspace 空域聚合根
     * @param deltaTime 时间步长（秒）
     */
    public void stepSimulation(Airspace airspace, double deltaTime) {
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("时间步长必须大于0");
        }
        
        // 1. 推进仿真时间
        airspace.stepSimulation();
        
        // 2. 更新所有UAV状态
        updateUAVStates(airspace, deltaTime);
        
        // 3. 执行探测判定
        performDetections(airspace);
    }
    
    /**
     * 初始化仿真状态
     * 
     * @param airspace 空域
     */
    private void initializeSimulationState(Airspace airspace) {
        // 获取所有UAV并初始化状态
        List<UAV> uavs = new ArrayList<>(airspace.getUAVs().values());
        for (UAV uav : uavs) {
            // 如果UAV有任务，开始执行任务
            if (uav.getStatus().name().equals("STANDBY") && !uav.getFlightPath().isEmpty()) {
                uav.startMission();
            }
        }
        
        // 启用所有活跃的探测设备
        List<AbstractProbeDevice> devices = new ArrayList<>(airspace.getProbeDevices().values());
        for (AbstractProbeDevice device : devices) {
            if (device.validateConfiguration()) {
                device.enable();
            }
        }
    }
    
    /**
     * 更新所有UAV状态
     * 
     * @param airspace 空域
     * @param deltaTime 时间步长
     */
    private void updateUAVStates(Airspace airspace, double deltaTime) {
        List<UAV> uavs = new ArrayList<>(airspace.getUAVs().values());
        
        for (UAV uav : uavs) {
            // 更新UAV移动状态 - 使用正确的方法
            uav.updateState(deltaTime);
            
            // 更新空域中的UAV位置索引
            airspace.updateEntityPosition(uav.getId(), uav.getPosition());
        }
    }
    
    /**
     * 执行所有设备的探测判定
     *
     * @param airspace 空域
     */
    private void performDetections(Airspace airspace) {
        List<ProbeDevice> devices = new ArrayList<>(airspace.getProbeDevices().values());
        List<UAV> uavs = new ArrayList<>(airspace.getUAVs().values());
        
        for (ProbeDevice device : devices) {
            if (device.isActive()) {
                try {
                    // 直接调用设备的探测方法，无需中间层
                    List<DetectionEvent> events = device.performDetection(airspace, uavs);
                    
                    // 探测事件已经在设备内部添加到日志，无需重复添加
                    
                } catch (Exception e) {
                    // 记录探测错误，但不中断仿真
                    System.err.println("设备 " + device.getId() + " 探测失败: " + e.getMessage());
                }
            }
        }
    }
} 
