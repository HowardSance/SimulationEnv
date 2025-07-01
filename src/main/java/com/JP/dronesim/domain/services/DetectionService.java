package com.JP.dronesim.domain.services;

import com.JP.dronesim.domain.device.model.ProbeDevice;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 探测服务领域服务
 * 执行探测判定逻辑（雷达方程、光电识别、无线电信号传播等）
 *
 * @author JP Team
 * @version 1.0
 */
@Service
public class DetectionService {

    /**
     * 执行单个设备对空域内UAV的探测判定
     *
     * @param device 探测设备
     * @param uavs 空域内UAV列表
     * @param environment 空域环境
     * @return 探测事件列表
     */
    public List<DetectionEvent> performDetection(ProbeDevice device, List<UAV> uavs, AirspaceEnvironment environment) {
        if (device == null || !device.isActive()) {
            throw new IllegalArgumentException("设备为空或未激活");
        }

        // 调用设备自身的探测逻辑
        return device.performDetection(environment, uavs);
    }
}
