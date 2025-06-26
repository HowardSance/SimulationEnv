package com.JP.dronesim.domain.device.model.radio;

import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.device.model.radio.RadioDetector;
import com.JP.dronesim.domain.device.model.radio.RadioParameters;

/**
 * 无线电监测器工厂
 */
public class RadioDetectorFactory {

    /**
     * 创建一个标准无线电监测器
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备高度
     * @param detectionRange 监测范围
     * @return 创建的RadioDetector对象
     */
    public static RadioDetector createStandardDetector(String deviceId, String deviceName,
                                                       Position position, double orientation,
                                                       double elevation, double detectionRange) {
        RadioDetector detector = new RadioDetector();

        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.RADIO_DETECTOR,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0, 0.0),
                detectionRange, 360.0, // 全向侦测
                new RadioParameters(100e6, 6e9, -80.0, 5.0, 2.4e9, 100e6)
        );

        detector.initialize(initParams);
        return detector;
    }
}

