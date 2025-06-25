package com.JP.dronesim.application.dtos.request;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.radar.RadarParameters;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;

/**
 * 雷达部署请求DTO
 * 用于接收前端发送的雷达部署请求参数
 * 
 * @author JP
 * @version 1.0
 */
public class RadarDeploymentRequest {
    
    /**
     * 设备ID
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    
    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;
    
    /**
     * 雷达类型
     * standard: 标准雷达
     * high_precision: 高精度雷达
     * long_range: 远程监视雷达
     * custom: 自定义配置雷达
     */
    @NotBlank(message = "雷达类型不能为空")
    private String radarType;
    
    /**
     * 部署位置
     */
    @NotNull(message = "部署位置不能为空")
    private Position position;
    
    /**
     * 设备朝向（度，0-360）
     */
    @DecimalMin(value = "0.0", message = "朝向角度必须大于等于0度")
    @DecimalMax(value = "360.0", message = "朝向角度必须小于等于360度")
    private double orientation;
    
    /**
     * 设备仰角（度，-90到90）
     */
    @DecimalMin(value = "-90.0", message = "仰角必须大于等于-90度")
    @DecimalMax(value = "90.0", message = "仰角必须小于等于90度")
    private double elevation;
    
    /**
     * 探测距离（米）
     */
    @Positive(message = "探测距离必须大于0")
    private double detectionRange;
    
    /**
     * 自定义雷达参数（仅当radarType为custom时使用）
     */
    private RadarParameters customParameters;
    
    /**
     * 默认构造函数
     */
    public RadarDeploymentRequest() {
    }
    
    /**
     * 构造函数
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param radarType 雷达类型
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     */
    public RadarDeploymentRequest(String deviceId, String deviceName, String radarType,
                                Position position, double orientation, double elevation,
                                double detectionRange) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.radarType = radarType;
        this.position = position;
        this.orientation = orientation;
        this.elevation = elevation;
        this.detectionRange = detectionRange;
    }
    
    /**
     * 构造函数（包含自定义参数）
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param radarType 雷达类型
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @param customParameters 自定义参数
     */
    public RadarDeploymentRequest(String deviceId, String deviceName, String radarType,
                                Position position, double orientation, double elevation,
                                double detectionRange, RadarParameters customParameters) {
        this(deviceId, deviceName, radarType, position, orientation, elevation, detectionRange);
        this.customParameters = customParameters;
    }
    
    /**
     * 获取设备ID
     * 
     * @return 设备ID
     */
    public String getDeviceId() {
        return deviceId;
    }
    
    /**
     * 设置设备ID
     * 
     * @param deviceId 设备ID
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
     * 获取雷达类型
     * 
     * @return 雷达类型
     */
    public String getRadarType() {
        return radarType;
    }
    
    /**
     * 设置雷达类型
     * 
     * @param radarType 雷达类型
     */
    public void setRadarType(String radarType) {
        this.radarType = radarType;
    }
    
    /**
     * 获取部署位置
     * 
     * @return 部署位置
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 设置部署位置
     * 
     * @param position 部署位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    
    /**
     * 获取朝向
     * 
     * @return 朝向角度
     */
    public double getOrientation() {
        return orientation;
    }
    
    /**
     * 设置朝向
     * 
     * @param orientation 朝向角度
     */
    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }
    
    /**
     * 获取仰角
     * 
     * @return 仰角
     */
    public double getElevation() {
        return elevation;
    }
    
    /**
     * 设置仰角
     * 
     * @param elevation 仰角
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    
    /**
     * 获取探测距离
     * 
     * @return 探测距离
     */
    public double getDetectionRange() {
        return detectionRange;
    }
    
    /**
     * 设置探测距离
     * 
     * @param detectionRange 探测距离
     */
    public void setDetectionRange(double detectionRange) {
        this.detectionRange = detectionRange;
    }
    
    /**
     * 获取自定义参数
     * 
     * @return 自定义雷达参数
     */
    public RadarParameters getCustomParameters() {
        return customParameters;
    }
    
    /**
     * 设置自定义参数
     * 
     * @param customParameters 自定义雷达参数
     */
    public void setCustomParameters(RadarParameters customParameters) {
        this.customParameters = customParameters;
    }
    
    /**
     * 检查是否为自定义配置雷达
     * 
     * @return true表示自定义配置
     */
    public boolean isCustomConfiguration() {
        return "custom".equalsIgnoreCase(radarType);
    }
    
    /**
     * 验证请求参数
     * 
     * @return true表示参数有效
     */
    public boolean isValid() {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            return false;
        }
        if (deviceName == null || deviceName.trim().isEmpty()) {
            return false;
        }
        if (radarType == null || radarType.trim().isEmpty()) {
            return false;
        }
        if (position == null) {
            return false;
        }
        if (orientation < 0.0 || orientation > 360.0) {
            return false;
        }
        if (elevation < -90.0 || elevation > 90.0) {
            return false;
        }
        if (detectionRange <= 0.0) {
            return false;
        }
        
        // 如果是自定义配置，检查自定义参数
        if (isCustomConfiguration() && (customParameters == null || !customParameters.isValid())) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("RadarDeploymentRequest[id=%s, name=%s, type=%s, pos=%s, " +
                           "orient=%.1f°, elev=%.1f°, range=%.0fm, custom=%s]",
                           deviceId, deviceName, radarType, position, orientation,
                           elevation, detectionRange, customParameters != null);
    }
} 