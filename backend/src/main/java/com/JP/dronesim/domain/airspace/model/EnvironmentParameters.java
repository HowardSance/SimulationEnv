package com.JP.dronesim.domain.airspace.model;

/**
 * 环境参数值对象（极简版）
 * 只支持四种天气枚举，参数固定
 *
 * @author JP
 * @version 2.0
 */
public class EnvironmentParameters {
    public enum WeatherType {
        DAY_CLEAR, NIGHT, RAINY, CLOUDY
    }

    private final double temperature;
    private final double humidity;
    private final double windSpeed;
    private final double windDirection;
    private final double atmosphericPressure;
    private final double visibility;
    private final double lightIntensity;
    private final double sunElevation;
    private final double sunAzimuth;
    private final double cloudCover;
    private final double precipitationRate;
    private final double atmosphericClarity;
    private final boolean isNightTime;
    private final WeatherType weatherType;

    private EnvironmentParameters(double temperature, double humidity, double windSpeed, double windDirection, double atmosphericPressure, double visibility, double lightIntensity, double sunElevation, double sunAzimuth, double cloudCover, double precipitationRate, double atmosphericClarity, boolean isNightTime, WeatherType weatherType) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.atmosphericPressure = atmosphericPressure;
        this.visibility = visibility;
        this.lightIntensity = lightIntensity;
        this.sunElevation = sunElevation;
        this.sunAzimuth = sunAzimuth;
        this.cloudCover = cloudCover;
        this.precipitationRate = precipitationRate;
        this.atmosphericClarity = atmosphericClarity;
        this.isNightTime = isNightTime;
        this.weatherType = weatherType;
    }

    public static EnvironmentParameters of(WeatherType type) {
        switch (type) {
            case DAY_CLEAR:
                return new EnvironmentParameters(
                        22.0, 0.5, 2.0, 180.0, 101325.0, 12000.0, 0.9, 50.0, 180.0, 0.1, 0.0, 0.95, false, WeatherType.DAY_CLEAR
                );
            case NIGHT:
                return new EnvironmentParameters(
                        15.0, 0.7, 1.5, 180.0, 101200.0, 6000.0, 0.1, -20.0, 0.0, 0.2, 0.0, 0.8, true, WeatherType.NIGHT
                );
            case RAINY:
                return new EnvironmentParameters(
                        18.0, 0.95, 8.0, 200.0, 100800.0, 300.0, 0.3, 30.0, 170.0, 0.9, 8.0, 0.4, false, WeatherType.RAINY
                );
            case CLOUDY:
                return new EnvironmentParameters(
                        20.0, 0.8, 3.0, 160.0, 101000.0, 5000.0, 0.5, 40.0, 160.0, 0.7, 0.0, 0.7, false, WeatherType.CLOUDY
                );
            default:
                throw new IllegalArgumentException("未知天气类型");
        }
    }

    public WeatherType getWeatherType() { return weatherType; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public double getWindDirection() { return windDirection; }
    public double getAtmosphericPressure() { return atmosphericPressure; }
    public double getVisibility() { return visibility; }
    public double getLightIntensity() { return lightIntensity; }
    public double getSunElevation() { return sunElevation; }
    public double getSunAzimuth() { return sunAzimuth; }
    public double getCloudCover() { return cloudCover; }
    public double getPrecipitationRate() { return precipitationRate; }
    public double getAtmosphericClarity() { return atmosphericClarity; }
    public boolean isNightTime() { return isNightTime; }

    @Override
    public String toString() {
        return String.format("EnvironmentParameters[%s, temp=%.1f°C, humidity=%.1f%%, wind=%.1fm/s@%.1f°, pressure=%.0fPa, visibility=%.0fm, light=%.2f, sun=(%.1f°,%.1f°), cloud=%.1f%%, rain=%.1fmm/h, clarity=%.2f, night=%s]",
                weatherType, temperature, humidity * 100, windSpeed, windDirection, atmosphericPressure, visibility, lightIntensity, sunElevation, sunAzimuth, cloudCover * 100, precipitationRate, atmosphericClarity, isNightTime);
    }
}