package com.JP.dronesim.domain.airspace.model;

/**
 * 环境参数值对象
 * 封装天气、温度、风向量、光照、能见度等环境属性
 * 影响探测设备的探测效果
 *
 * @author JP
 * @version 1.0
 */
public class EnvironmentParameters {

    /**
     * 环境温度（摄氏度）
     */
    private final double temperature;

    /**
     * 相对湿度（0.0-1.0）
     */
    private final double humidity;

    /**
     * 风速（米/秒）
     */
    private final double windSpeed;

    /**
     * 风向（度，0-360）
     */
    private final double windDirection;

    /**
     * 大气压力（帕斯卡）
     */
    private final double atmosphericPressure;

    /**
     * 能见度（米）
     */
    private final double visibility;

    /**
     * 环境光照强度（0.0-1.0）
     * 0.0表示完全黑暗，1.0表示最亮的日光
     */
    private final double lightIntensity;

    /**
     * 太阳高度角（度，-90到90）
     * 负值表示太阳在地平线以下
     */
    private final double sunElevation;

    /**
     * 太阳方位角（度，0-360）
     */
    private final double sunAzimuth;

    /**
     * 云层覆盖率（0.0-1.0）
     * 0.0表示晴空，1.0表示完全阴天
     */
    private final double cloudCover;

    /**
     * 降水强度（毫米/小时）
     */
    private final double precipitationRate;

    /**
     * 大气透明度（0.0-1.0）
     * 影响光线传播和图像质量
     */
    private final double atmosphericClarity;

    /**
     * 是否为夜间模式
     */
    private final boolean isNightTime;

    /**
     * 构造函数
     */
    public EnvironmentParameters() {
        this.temperature = 20.0;
        this.humidity = 0.6;
        this.windSpeed = 3.0;
        this.windDirection = 180.0;
        this.atmosphericPressure = 101325.0;
        this.visibility = 10000.0;
        this.lightIntensity = 0.8;
        this.sunElevation = 45.0;
        this.sunAzimuth = 180.0;
        this.cloudCover = 0.2;
        this.precipitationRate = 0.0;
        this.atmosphericClarity = 0.9;
        this.isNightTime = false;
    }

    /**
     * 构造函数
     *
     *
     * @param temperature 环境温度
     * @param humidity 相对湿度
     * @param windSpeed 风速
     * @param windDirection 风向
     * @param atmosphericPressure 大气压力
     * @param visibility 能见度
     * @param lightIntensity 光照强度
     * @param sunElevation 太阳高度角
     * @param sunAzimuth 太阳方位角
     * @param cloudCover 云层覆盖率
     * @param precipitationRate 降水强度
     * @param atmosphericClarity 大气透明度
     * @param isNightTime 是否夜间
     */
    public EnvironmentParameters(double temperature, double humidity, double windSpeed, double windDirection, double atmosphericPressure, double visibility, double lightIntensity, double sunElevation, double sunAzimuth, double cloudCover, double precipitationRate, double atmosphericClarity, boolean isNightTime) {
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

        // 参数验证
        validateParameters();
    }

    /**
     * 验证参数有效性
     */
    private void validateParameters() {
        if (humidity < 0.0 || humidity > 1.0) {
            throw new IllegalArgumentException("湿度必须在0.0到1.0之间");
        }
        if (windSpeed < 0.0) {
            throw new IllegalArgumentException("风速不能为负数");
        }
        if (windDirection < 0.0 || windDirection >= 360.0) {
            throw new IllegalArgumentException("风向必须在0到360度之间");
        }
        if (visibility < 0.0) {
            throw new IllegalArgumentException("能见度不能为负数");
        }
        if (lightIntensity < 0.0 || lightIntensity > 1.0) {
            throw new IllegalArgumentException("光照强度必须在0.0到1.0之间");
        }
        if (sunElevation < -90.0 || sunElevation > 90.0) {
            throw new IllegalArgumentException("太阳高度角必须在-90到90度之间");
        }
        if (sunAzimuth < 0.0 || sunAzimuth >= 360.0) {
            throw new IllegalArgumentException("太阳方位角必须在0到360度之间");
        }
        if (cloudCover < 0.0 || cloudCover > 1.0) {
            throw new IllegalArgumentException("云层覆盖率必须在0.0到1.0之间");
        }
        if (precipitationRate < 0.0) {
            throw new IllegalArgumentException("降水强度不能为负数");
        }
        if (atmosphericClarity < 0.0 || atmosphericClarity > 1.0) {
            throw new IllegalArgumentException("大气透明度必须在0.0到1.0之间");
        }
    }

    /**
     * 获取环境温度
     *
     * @return 环境温度（摄氏度）
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * 获取相对湿度
     *
     * @return 相对湿度（0.0-1.0）
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * 获取风速
     *
     * @return 风速（米/秒）
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * 获取风向
     *
     * @return 风向（度，0-360）
     */
    public double getWindDirection() {
        return windDirection;
    }

    /**
     * 获取大气压力
     *
     * @return 大气压力（帕斯卡）
     */
    public double getAtmosphericPressure() {
        return atmosphericPressure;
    }

    /**
     * 获取能见度
     *
     * @return 能见度（米）
     */
    public double getVisibility() {
        return visibility;
    }

    /**
     * 获取光照强度
     *
     * @return 环境光照强度（0.0-1.0）
     */
    public double getLightIntensity() {
        return lightIntensity;
    }

    /**
     * 获取太阳高度角
     *
     * @return 太阳高度角（度，-90到90）
     */
    public double getSunElevation() {
        return sunElevation;
    }

    /**
     * 获取太阳方位角
     *
     * @return 太阳方位角（度，0-360）
     */
    public double getSunAzimuth() {
        return sunAzimuth;
    }

    /**
     * 获取云层覆盖率
     *
     * @return 云层覆盖率（0.0-1.0）
     */
    public double getCloudCover() {
        return cloudCover;
    }

    /**
     * 获取降水强度
     *
     * @return 降水强度（毫米/小时）
     */
    public double getPrecipitationRate() {
        return precipitationRate;
    }

    /**
     * 获取大气透明度
     *
     * @return 大气透明度（0.0-1.0）
     */
    public double getAtmosphericClarity() {
        return atmosphericClarity;
    }

    /**
     * 是否为夜间模式
     *
     * @return true表示夜间，false表示白天
     */
    public boolean isNightTime() {
        return isNightTime;
    }

    /**
     * 检查光照条件是否良好
     *
     * @return true表示光照条件适合光电摄像头探测
     */
    public boolean hasGoodLightConditions() {
        return lightIntensity >= 0.3 && !isRaining();
    }

    /**
     * 检查能见度是否良好
     *
     * @return true表示能见度足够（>=500米）
     */
    public boolean hasGoodVisibility() {
        return visibility >= 500.0;
    }

    /**
     * 检查是否正在下雨
     *
     * @return true表示正在下雨（降水强度>0.1mm/h）
     */
    public boolean isRaining() {
        return precipitationRate > 0.1;
    }

    /**
     * 检查是否多云
     *
     * @return true表示多云（云层覆盖率>0.5）
     */
    public boolean isCloudy() {
        return cloudCover > 0.5;
    }

    /**
     * 检查是否有强风
     *
     * @return true表示强风（风速>10m/s）
     */
    public boolean isWindy() {
        return windSpeed > 10.0;
    }

    /**
     * 计算光电摄像头的环境适应性评分
     *
     * @return 环境适应性评分（0.0-1.0），1.0表示最适合
     */
    public double getOpticalDetectionSuitability() {
        double score = 1.0;

        // 光照因子
        if (lightIntensity < 0.2) {
            score *= 0.3; // 光线太暗
        } else if (lightIntensity < 0.5) {
            score *= 0.7; // 光线偏暗
        }

        // 能见度因子
        if (visibility < 100) {
            score *= 0.2; // 能见度极差
        } else if (visibility < 500) {
            score *= 0.6; // 能见度较差
        }

        // 降水因子
        if (precipitationRate > 5.0) {
            score *= 0.3; // 大雨
        } else if (precipitationRate > 1.0) {
            score *= 0.7; // 小雨
        }

        // 云层因子
        if (cloudCover > 0.8) {
            score *= 0.8; // 阴天
        }

        // 大气透明度因子
        score *= atmosphericClarity;

        return Math.max(0.0, Math.min(1.0, score));
    }

    /**
     * 创建默认的环境参数（晴朗白天）
     *
     * @return 默认环境参数
     */
    public static EnvironmentParameters createDefault() {
        return new EnvironmentParameters(
                20.0,    // 20°C
                0.6,     // 60%湿度
                3.0,     // 3m/s风速
                180.0,   // 南风
                101325.0, // 标准大气压
                10000.0, // 10km能见度
                0.8,     // 80%光照强度
                45.0,    // 45°太阳高度角
                180.0,   // 太阳在南方
                0.2,     // 20%云层覆盖
                0.0,     // 无降水
                0.9,     // 90%大气透明度
                false    // 白天
        );
    }

    /**
     * 创建夜间环境参数
     *
     * @return 夜间环境参数
     */
    public static EnvironmentParameters createNightTime() {
        return new EnvironmentParameters(
                15.0,    // 15°C
                0.7,     // 70%湿度
                2.0,     // 2m/s风速
                180.0,   // 南风
                101325.0, // 标准大气压
                5000.0,  // 5km能见度
                0.1,     // 10%光照强度（月光）
                -30.0,   // 太阳在地平线以下
                0.0,     // 太阳方位角无意义
                0.3,     // 30%云层覆盖
                0.0,     // 无降水
                0.8,     // 80%大气透明度
                true     // 夜间
        );
    }

    /**
     * 创建恶劣天气环境参数
     *
     * @return 恶劣天气环境参数
     */
    public static EnvironmentParameters createBadWeather() {
        return new EnvironmentParameters(
                10.0,    // 10°C
                0.9,     // 90%湿度
                15.0,    // 15m/s强风
                270.0,   // 西风
                100000.0, // 低气压
                200.0,   // 200m能见度（雾霾）
                0.3,     // 30%光照强度
                30.0,    // 30°太阳高度角
                180.0,   // 太阳在南方
                0.9,     // 90%云层覆盖
                5.0,     // 5mm/h降水
                0.4,     // 40%大气透明度
                false    // 白天
        );
    }

    @Override
    public String toString() {
        return String.format("EnvironmentParameters[temp=%.1f°C, humidity=%.1f%%, " +
                           "wind=%.1fm/s@%.1f°, pressure=%.0fPa, visibility=%.0fm, " +
                           "light=%.2f, sun=(%.1f°,%.1f°), cloud=%.1f%%, " +
                           "rain=%.1fmm/h, clarity=%.2f, night=%s]",
                           temperature, humidity * 100, windSpeed, windDirection,
                           atmosphericPressure, visibility, lightIntensity,
                           sunElevation, sunAzimuth, cloudCover * 100,
                           precipitationRate, atmosphericClarity, isNightTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnvironmentParameters that = (EnvironmentParameters) o;

        return Double.compare(that.temperature, temperature) == 0 &&
               Double.compare(that.humidity, humidity) == 0 &&
               Double.compare(that.windSpeed, windSpeed) == 0 &&
               Double.compare(that.windDirection, windDirection) == 0 &&
               Double.compare(that.atmosphericPressure, atmosphericPressure) == 0 &&
               Double.compare(that.visibility, visibility) == 0 &&
               Double.compare(that.lightIntensity, lightIntensity) == 0 &&
               Double.compare(that.sunElevation, sunElevation) == 0 &&
               Double.compare(that.sunAzimuth, sunAzimuth) == 0 &&
               Double.compare(that.cloudCover, cloudCover) == 0 &&
               Double.compare(that.precipitationRate, precipitationRate) == 0 &&
               Double.compare(that.atmosphericClarity, atmosphericClarity) == 0 &&
               isNightTime == that.isNightTime;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(temperature);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(humidity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(windSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lightIntensity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(visibility);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isNightTime ? 1 : 0);
        return result;
    }
}