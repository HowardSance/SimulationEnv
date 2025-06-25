package com.JP.dronesim.application.dtos.request;

import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.device.model.common.SensorParameters;

/**
 * 设备初始化参数DTO
 * 用于传递探测设备初始化所需的所有参数
 * 
 * @author JP
 * @version 1.0
 */
public class DeviceInitParamsDTO {
    
    /**
     * 设备唯一标识符
     */
    private String deviceId;
    
    /**
     * 设备名称
     */
    private String deviceName;
    
    /**
     * 设备类型
     */
    private DeviceType deviceType;
    
    /**
     * 设备位置坐标（NED坐标系）
     */
    private Position position;
    
    /**
     * 设备朝向（0-360度）
     */
    private double orientation;
    
    /**
     * 设备仰角（0-90度）
     */
    private double elevation;
    
    /**
     * 设备姿态信息
     */
    private Orientation attitude;
    
    /**
     * 设备有效探测距离（米）
     */
    private double detectionRange;
    
    /**
     * 探测视场角度（度）
     */
    private double fieldOfView;
    
    /**
     * 特定传感器类型的详细参数
     */
    private SensorParameters detectionParameters;
    
    /**
     * 默认构造函数
     */
    public DeviceInitParamsDTO() {
    }
    
    /**
     * 全参数构造函数
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param deviceType 设备类型
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param attitude 设备姿态
     * @param detectionRange 探测距离
     * @param fieldOfView 视场角
     * @param detectionParameters 探测参数
     */
    public DeviceInitParamsDTO(String deviceId, String deviceName, DeviceType deviceType,
                              Position position, double orientation, double elevation,
                              Orientation attitude, double detectionRange, double fieldOfView,
                              SensorParameters detectionParameters) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.position = position;
        this.orientation = orientation;
        this.elevation = elevation;
        this.attitude = attitude;
        this.detectionRange = detectionRange;
        this.fieldOfView = fieldOfView;
        this.detectionParameters = detectionParameters;
    }
    
    /**
     * 获取设备ID
     * 
     * @return 设备唯一标识符
     */
    public String getDeviceId() {
        return deviceId;
    }
    
    /**
     * 设置设备ID
     * 
     * @param deviceId 设备唯一标识符
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    /**
     * 获取设备名称
     * 
     * @return 设备名称
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    /**
     * 设置设备名称
     * 
     * @param deviceName 设备名称
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    /**
     * 获取设备类型
     * 
     * @return 设备类型
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }
    
    /**
     * 设置设备类型
     * 
     * @param deviceType 设备类型
     */
    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
    
    /**
     * 获取设备位置
     * 
     * @return 设备位置坐标
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 设置设备位置
     * 
     * @param position 设备位置坐标
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    
    /**
     * 获取设备朝向
     * 
     * @return 设备朝向角度（0-360度）
     */
    public double getOrientation() {
        return orientation;
    }
    
    /**
     * 设置设备朝向
     * 
     * @param orientation 设备朝向角度（0-360度）
     */
    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }
    
    /**
     * 获取设备仰角
     * 
     * @return 设备仰角（0-90度）
     */
    public double getElevation() {
        return elevation;
    }
    
    /**
     * 设置设备仰角
     * 
     * @param elevation 设备仰角（0-90度）
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    
    /**
     * 获取设备姿态
     * 
     * @return 设备姿态信息
     */
    public Orientation getAttitude() {
        return attitude;
    }
    
    /**
     * 设置设备姿态
     * 
     * @param attitude 设备姿态信息
     */
    public void setAttitude(Orientation attitude) {
        this.attitude = attitude;
    }
    
    /**
     * 获取探测距离
     * 
     * @return 设备有效探测距离（米）
     */
    public double getDetectionRange() {
        return detectionRange;
    }
    
    /**
     * 设置探测距离
     * 
     * @param detectionRange 设备有效探测距离（米）
     */
    public void setDetectionRange(double detectionRange) {
        this.detectionRange = detectionRange;
    }
    
    /**
     * 获取视场角
     * 
     * @return 探测视场角度（度）
     */
    public double getFieldOfView() {
        return fieldOfView;
    }
    
    /**
     * 设置视场角
     * 
     * @param fieldOfView 探测视场角度（度）
     */
    public void setFieldOfView(double fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
    
    /**
     * 获取探测参数
     * 
     * @return 特定传感器类型的详细参数
     */
    public SensorParameters getDetectionParameters() {
        return detectionParameters;
    }
    
    /**
     * 设置探测参数
     * 
     * @param detectionParameters 特定传感器类型的详细参数
     */
    public void setDetectionParameters(SensorParameters detectionParameters) {
        this.detectionParameters = detectionParameters;
    }
    
    /**
     * 验证参数的有效性
     * 
     * @return true表示参数有效，false表示参数无效
     */
    public boolean isValid() {
        if (deviceId == null || deviceId.trim().isEmpty() ||
            deviceName == null || deviceName.trim().isEmpty() ||
            deviceType == null || position == null) {
            return false;
        }
        
        if (orientation < 0.0 || orientation >= 360.0) {
            return false;
        }
        
        if (elevation < 0.0 || elevation > 90.0) {
            return false;
        }
        
        if (detectionRange <= 0.0 || fieldOfView <= 0.0 || fieldOfView > 360.0) {
            return false;
        }
        
        return detectionParameters == null || detectionParameters.isValid();
    }
    
    @Override
    public String toString() {
        return "DeviceInitParamsDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType=" + deviceType +
                ", position=" + position +
                ", orientation=" + orientation +
                ", elevation=" + elevation +
                ", attitude=" + attitude +
                ", detectionRange=" + detectionRange +
                ", fieldOfView=" + fieldOfView +
                ", detectionParameters=" + detectionParameters +
                '}';
    }
} 