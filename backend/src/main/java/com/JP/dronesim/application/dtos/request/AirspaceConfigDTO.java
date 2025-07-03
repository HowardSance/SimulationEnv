package com.JP.dronesim.application.dtos.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;

/**
 * 空域配置DTO
 * 用于初始化空域配置
 *
 * @author JP Team
 * @version 1.0
 */
public class AirspaceConfigDTO {

    /**
     * 空域名称
     */
    @NotBlank(message = "空域名称不能为空")
    private String name;

    /**
     * 空域描述
     */
    private String description;

    /**
     * 空域边界最小X坐标（米）
     */
    @NotNull(message = "空域边界最小X坐标不能为空")
    private Double minX;

    /**
     * 空域边界最小Y坐标（米）
     */
    @NotNull(message = "空域边界最小Y坐标不能为空")
    private Double minY;

    /**
     * 空域边界最小Z坐标（米）
     */
    @NotNull(message = "空域边界最小Z坐标不能为空")
    private Double minZ;

    /**
     * 空域边界最大X坐标（米）
     */
    @NotNull(message = "空域边界最大X坐标不能为空")
    private Double maxX;

    /**
     * 空域边界最大Y坐标（米）
     */
    @NotNull(message = "空域边界最大Y坐标不能为空")
    private Double maxY;

    /**
     * 空域边界最大Z坐标（米）
     */
    @NotNull(message = "空域边界最大Z坐标不能为空")
    private Double maxZ;

    /**
     * 时间步长（秒）
     */
    @NotNull(message = "时间步长不能为空")
    @DecimalMin(value = "0.1", message = "时间步长必须大于0.1")
    private Double timeStep;

    /**
     * 温度（摄氏度）
     */
    @DecimalMin(value = "-100", message = "温度不能低于-100摄氏度")
    @DecimalMax(value = "100", message = "温度不能高于100摄氏度")
    private Double temperature;

    /**
     * 湿度（百分比）
     */
    @DecimalMin(value = "0", message = "湿度不能为负数")
    @DecimalMax(value = "100", message = "湿度不能超过100%")
    private Double humidity;

    /**
     * 风速（米/秒）
     */
    @DecimalMin(value = "0", message = "风速不能为负数")
    private Double windSpeed;

    /**
     * 风向（度）
     */
    @DecimalMin(value = "0", message = "风向角度不能为负数")
    @DecimalMax(value = "360", message = "风向角度不能超过360度")
    private Double windDirection;

    /**
     * 能见度（米）
     */
    @DecimalMin(value = "0", message = "能见度不能为负数")
    private Double visibility;

    /**
     * 气压（帕斯卡）
     */
        @DecimalMin(value = "0", message = "气压不能为负数")
    private Double pressure;

    /**
     * 默认构造函数
     */
    public AirspaceConfigDTO() {
    }

    /**
     * 构造函数
     *
     * @param name 空域名称
     * @param description 空域描述
     * @param minX 最小X坐标
     * @param minY 最小Y坐标
     * @param minZ 最小Z坐标
     * @param maxX 最大X坐标
     * @param maxY 最大Y坐标
     * @param maxZ 最大Z坐标
     * @param timeStep 时间步长
     */
    public AirspaceConfigDTO(String name, String description, Double minX, Double minY, Double minZ,
                            Double maxX, Double maxY, Double maxZ, Double timeStep) {
        this.name = name;
        this.description = description;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.timeStep = timeStep;
    }

    /**
     * 获取空域名称
     *
     * @return 空域名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置空域名称
     *
     * @param name 空域名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取空域描述
     *
     * @return 空域描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置空域描述
     *
     * @param description 空域描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取空域边界最小X坐标
     *
     * @return 最小X坐标
     */
    public Double getMinX() {
        return minX;
    }

    /**
     * 设置空域边界最小X坐标
     *
     * @param minX 最小X坐标
     */
    public void setMinX(Double minX) {
        this.minX = minX;
    }

    /**
     * 获取空域边界最小Y坐标
     *
     * @return 最小Y坐标
     */
    public Double getMinY() {
        return minY;
    }

    /**
     * 设置空域边界最小Y坐标
     *
     * @param minY 最小Y坐标
     */
    public void setMinY(Double minY) {
        this.minY = minY;
    }

    /**
     * 获取空域边界最小Z坐标
     *
     * @return 最小Z坐标
     */
    public Double getMinZ() {
        return minZ;
    }

    /**
     * 设置空域边界最小Z坐标
     *
     * @param minZ 最小Z坐标
     */
    public void setMinZ(Double minZ) {
        this.minZ = minZ;
    }

    /**
     * 获取空域边界最大X坐标
     *
     * @return 最大X坐标
     */
    public Double getMaxX() {
        return maxX;
    }

    /**
     * 设置空域边界最大X坐标
     *
     * @param maxX 最大X坐标
     */
    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    /**
     * 获取空域边界最大Y坐标
     *
     * @return 最大Y坐标
     */
    public Double getMaxY() {
        return maxY;
    }

    /**
     * 设置空域边界最大Y坐标
     *
     * @param maxY 最大Y坐标
     */
    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    /**
     * 获取空域边界最大Z坐标
     *
     * @return 最大Z坐标
     */
    public Double getMaxZ() {
        return maxZ;
    }

    /**
     * 设置空域边界最大Z坐标
     *
     * @param maxZ 最大Z坐标
     */
    public void setMaxZ(Double maxZ) {
        this.maxZ = maxZ;
    }

    /**
     * 获取时间步长
     *
     * @return 时间步长
     */
    public Double getTimeStep() {
        return timeStep;
    }

    /**
     * 设置时间步长
     *
     * @param timeStep 时间步长
     */
    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    /**
     * 获取温度
     *
     * @return 温度
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * 设置温度
     *
     * @param temperature 温度
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * 获取湿度
     *
     * @return 湿度
     */
    public Double getHumidity() {
        return humidity;
    }

    /**
     * 设置湿度
     *
     * @param humidity 湿度
     */
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    /**
     * 获取风速
     *
     * @return 风速
     */
    public Double getWindSpeed() {
        return windSpeed;
    }

    /**
     * 设置风速
     *
     * @param windSpeed 风速
     */
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * 获取风向
     *
     * @return 风向
     */
    public Double getWindDirection() {
        return windDirection;
    }

    /**
     * 设置风向
     *
     * @param windDirection 风向
     */
    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * 获取能见度
     *
     * @return 能见度
     */
    public Double getVisibility() {
        return visibility;
    }

    /**
     * 设置能见度
     *
     * @param visibility 能见度
     */
    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    /**
     * 获取气压
     *
     * @return 气压
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * 设置气压
     *
     * @param pressure 气压
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "AirspaceConfigDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                ", timeStep=" + timeStep +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", visibility=" + visibility +
                ", pressure=" + pressure +
                '}';
    }
}
