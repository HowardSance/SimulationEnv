package com.JP.dronesim.domain.device.model.radio;

import com.JP.dronesim.domain.device.model.common.SensorParameters;

/**
 * 无线电监测器参数
 */
public class RadioParameters implements SensorParameters {
    private double minFrequency; // Hz
    private double maxFrequency; // Hz
    private double sensitivity; // dBm
    private double directionAccuracy; // 度
    private double scanFrequency; // Hz
    private double scanBandwidth; // Hz

    // 构造方法和getter/setter
    public RadioParameters(double minFrequency, double maxFrequency,
                           double sensitivity, double directionAccuracy,
                           double scanFrequency, double scanBandwidth) {
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.sensitivity = sensitivity;
        this.directionAccuracy = directionAccuracy;
        this.scanFrequency = scanFrequency;
        this.scanBandwidth = scanBandwidth;
    }

    public double getMinFrequency() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getParametersDescription() {
        return null;
    }


    @Override
    public SensorParameters clone() {
        return null;
    }

    @Override
    public void updateFrom(SensorParameters newParameters) {

    }

    public double getMaxFrequency() {
        return 0;
    }

    public double getSensitivity() {
        return 0;
    }

    public double getDirectionAccuracy() {
        return 0;
    }

    public double getScanFrequency() {
        return 0;
    }

    public double getScanBandwidth() {
        return 0;
    }

    // 各属性的getter和setter方法...
}

