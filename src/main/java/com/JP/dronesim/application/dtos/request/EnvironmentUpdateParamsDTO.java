package com.JP.dronesim.application.dtos.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;

/**
 * 环境更新参数DTO
 * 用于动��更新环境参�?
 *
 * @author JP Team
 * @version 1.0
 */
public class EnvironmentUpdateParamsDTO {

    /**
     * 温度（摄氏度�?
     */
    @DecimalMin(value = "-100", message = "温度不能低于-100摄氏�?)
    @DecimalMax(value = "100", message = "温度不能高于100摄氏�?)
    private Double temperature;

    /**
     * 湿度（百分比�?
     */
    @DecimalMin(value = "0", message = "湿度不能为负�?)
    @DecimalMax(value = "100", message = "湿度不能超过100%")
    private Double humidity;

    /**
     * 风��（�?秒）
     */
    @DecimalMin(value = "0", message = "风��不能为负数")
    private Double windSpeed;

    /**
     * 风向（度�?
     */
    @DecimalMin(value = "0", message = "风向角度不能为负�?)
    @DecimalMax(value = "360", message = "风向角度不能超过360�?)
    private Double windDirection;

    /**
     * 能见度（米）
     */
    @DecimalMin(value = "0", message = "能见度不能为负数")
    private Double visibility;

    /**
     * 气压（帕斯卡�?
     */
    @DecimalMin(value = "0", message = "气压不能为负�?)
    private Double pressure;

    /**
     * 云层覆盖率（百分比）
     */
    @DecimalMin(value = "0", message = "云层覆盖率不能为负数")
    @DecimalMax(value = "100", message = "云层覆盖率不能超�?00%")
    private Double cloudCover;

    /**
     * 降水强度（毫�?小时�?
     */
    @DecimalMin(value = "0", message = "降水强度不能为负�?)
    private Double precipitation;

    /**
     * 大气湍流强度
     */
    @DecimalMin(value = "0", message = "大气湍流强度不能为负�?)
    private Double turbulence;

    /**
     * 默认构��函�?
     */
    public EnvironmentUpdateParamsDTO() {
    }

    /**
     * 构��函�?
     *
     * @param temperature 温度
     * @param humidity 湿度
     * @param windSpeed 风��?
     * @param windDirection 风向
     */
    public EnvironmentUpdateParamsDTO(Double temperature, Double humidity,
                                     Double windSpeed, Double windDirection) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
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
     * 获取风��?
     *
     * @return 风��?
     */
    public Double getWindSpeed() {
        return windSpeed;
    }

    /**
     * 设置风��?
     *
     * @param windSpeed 风��?
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
     * 获取能见�?
     *
     * @return 能见�?
     */
    public Double getVisibility() {
        return visibility;
    }

    /**
     * 设置能见�?
     *
     * @param visibility 能见�?
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

    /**
     * 获取云层覆盖�?
     *
     * @return 云层覆盖�?
     */
    public Double getCloudCover() {
        return cloudCover;
    }

    /**
     * 设置云层覆盖�?
     *
     * @param cloudCover 云层覆盖�?
     */
    public void setCloudCover(Double cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * 获取降水强度
     *
     * @return 降水强度
     */
    public Double getPrecipitation() {
        return precipitation;
    }

    /**
     * 设置降水强度
     *
     * @param precipitation 降水强度
     */
    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * 获取大气湍流强度
     *
     * @return 大气湍流强度
     */
    public Double getTurbulence() {
        return turbulence;
    }

    /**
     * 设置大气湍流强度
     *
     * @param turbulence 大气湍流强度
     */
    public void setTurbulence(Double turbulence) {
        this.turbulence = turbulence;
    }

    @Override
    public String toString() {
        return "EnvironmentUpdateParamsDTO{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", visibility=" + visibility +
                ", pressure=" + pressure +
                ", cloudCover=" + cloudCover +
                ", precipitation=" + precipitation +
                ", turbulence=" + turbulence +
                '}';
    }
}
