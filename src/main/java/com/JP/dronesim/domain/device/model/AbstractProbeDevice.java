package com.JP.dronesim.domain.device.model;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceStatus;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.common.DetectionLog;
import com.JP.dronesim.domain.device.model.common.SensorParameters;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;

import java.util.List;

/**
 * 抽象探测设备基类
 * 实现探测设备接口中的通用逻辑
 * 为具体的探测设备类型提供基础实现
 * 
 * @author JP
 * @version 1.0
 */
public abstract class AbstractProbeDevice implements ProbeDevice {
    
    /**
     * 设备唯一标识符
     */
    protected String id;
    
    /**
     * 设备名称
     */
    protected String name;
    
    /**
     * 设备类型
     */
    protected DeviceType type;
    
    /**
     * 设备位置坐标（NED坐标系）
     */
    protected Position position;
    
    /**
     * 设备朝向（0-360度）
     */
    protected double orientation;
    
    /**
     * 设备仰角（0-90度）
     */
    protected double elevation;
    
    /**
     * 设备姿态信息
     */
    protected Orientation attitude;
    
    /**
     * 设备有效探测距离（米）
     */
    protected double detectionRange;
    
    /**
     * 探测视场角度（度）
     */
    protected double fieldOfView;
    
    /**
     * 设备运行状态
     */
    protected DeviceStatus status;
    
    /**
     * 特定传感器类型的详细参数
     */
    protected SensorParameters detectionParameters;
    
    /**
     * 探测事件的存储列表
     */
    protected DetectionLog detectionLog;
    
    /**
     * 设备是否已初始化标志
     */
    protected boolean initialized;
    
    /**
     * 构造函数
     * 
     * @param type 设备类型
     */
    protected AbstractProbeDevice(DeviceType type) {
        this.type = type;
        this.status = DeviceStatus.INACTIVE;
        this.detectionLog = new DetectionLog();
        this.initialized = false;
    }
    
    // ================ 基础属性访问方法实现 ================
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public DeviceType getType() {
        return type;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Position position) {
        validatePosition(position);
        this.position = position;
    }
    
    @Override
    public double getOrientation() {
        return orientation;
    }
    
    @Override
    public void setOrientation(double orientation) {
        validateOrientation(orientation);
        this.orientation = orientation;
    }
    
    @Override
    public double getElevation() {
        return elevation;
    }
    
    @Override
    public void setElevation(double elevation) {
        validateElevation(elevation);
        this.elevation = elevation;
    }
    
    @Override
    public Orientation getAttitude() {
        return attitude;
    }
    
    @Override
    public void setAttitude(Orientation attitude) {
        this.attitude = attitude;
    }
    
    @Override
    public double getDetectionRange() {
        return detectionRange;
    }
    
    @Override
    public void setDetectionRange(double detectionRange) {
        validateDetectionRange(detectionRange);
        this.detectionRange = detectionRange;
    }
    
    @Override
    public double getFieldOfView() {
        return fieldOfView;
    }
    
    @Override
    public void setFieldOfView(double fieldOfView) {
        validateFieldOfView(fieldOfView);
        this.fieldOfView = fieldOfView;
    }
    
    @Override
    public DeviceStatus getStatus() {
        return status;
    }
    
    @Override
    public void setStatus(DeviceStatus status) {
        this.status = status;
    }
    
    @Override
    public SensorParameters getDetectionParameters() {
        return detectionParameters;
    }
    
    @Override
    public void setDetectionParameters(SensorParameters parameters) {
        if (parameters != null && !parameters.isValid()) {
            throw new IllegalArgumentException("探测参数无效");
        }
        this.detectionParameters = parameters;
    }
    
    @Override
    public DetectionLog getDetectionLog() {
        return detectionLog;
    }
    
    /**
     * 获取该设备的历史探测事件记录列表
     * 
     * @return 该设备产生的探测事件列表
     */
    public List<DetectionEvent> getDetectionEvents() {
        return detectionLog.getAllEvents();
    }
    
    // ================ 行为方法实现 ================
    
    @Override
    public void initialize(DeviceInitParamsDTO params) {
        if (initialized) {
            throw new IllegalStateException("设备已经初始化");
        }
        
        validateInitParams(params);
        
        this.id = params.getDeviceId();
        this.name = params.getDeviceName();
        this.position = params.getPosition();
        this.orientation = params.getOrientation();
        this.elevation = params.getElevation();
        this.attitude = params.getAttitude();
        this.detectionRange = params.getDetectionRange();
        this.fieldOfView = params.getFieldOfView();
        this.detectionParameters = params.getDetectionParameters();
        
        // 执行特定类型的初始化逻辑
        doSpecificInitialization(params);
        
        this.status = DeviceStatus.INACTIVE;
        this.initialized = true;
    }
    
    @Override
    public List<DetectionEvent> performDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        if (status != DeviceStatus.ACTIVE) {
            throw new IllegalStateException("设备未处于活跃状态");
        }
        
        // 执行具体的探测逻辑（由子类实现）
        List<DetectionEvent> events = doPerformDetection(airspace, uavs);
        
        // 将探测事件添加到日志中
        if (events != null) {
            for (DetectionEvent event : events) {
                detectionLog.addEvent(event);
            }
        }
        
        return events;
    }
    
    @Override
    public void adjustParameters(SensorParameters newParams) {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        if (newParams == null || !newParams.isValid()) {
            throw new IllegalArgumentException("新参数无效");
        }
        
        // 执行特定类型的参数调整逻辑
        doAdjustParameters(newParams);
        
        this.detectionParameters = newParams;
    }
    
    // ================ 状态检查方法实现 ================
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public boolean isActive() {
        return status == DeviceStatus.ACTIVE;
    }
    
    @Override
    public boolean hasError() {
        return status == DeviceStatus.ERROR;
    }
    
    @Override
    public void enable() {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        if (status == DeviceStatus.ERROR) {
            throw new IllegalStateException("设备处于错误状态，无法启用");
        }
        this.status = DeviceStatus.ACTIVE;
    }
    
    @Override
    public void disable() {
        this.status = DeviceStatus.INACTIVE;
    }
    
    @Override
    public void reset() {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        
        detectionLog.clearEvents();
        this.status = DeviceStatus.INACTIVE;
        
        // 执行特定类型的重置逻辑
        doReset();
    }
    
    // ================ 验证方法实现 ================
    
    @Override
    public boolean validateConfiguration() {
        if (!initialized) {
            return false;
        }
        
        try {
            validatePosition(position);
            validateOrientation(orientation);
            validateElevation(elevation);
            validateDetectionRange(detectionRange);
            validateFieldOfView(fieldOfView);
            
            return detectionParameters == null || detectionParameters.isValid();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public String getConfigurationDescription() {
        return String.format("Device[id=%s, name=%s, type=%s, position=%s, orientation=%.2f, " +
                        "elevation=%.2f, range=%.2f, fov=%.2f, status=%s, initialized=%s]",
                id, name, type, position, orientation, elevation, 
                detectionRange, fieldOfView, status, initialized);
    }
    
    // ================ 抽象方法 - 由子类实现 ================
    
    /**
     * 执行特定类型的初始化逻辑
     * 由具体的探测设备类型实现
     * 
     * @param params 初始化参数
     */
    protected abstract void doSpecificInitialization(DeviceInitParamsDTO params);
    
    /**
     * 执行具体的探测逻辑
     * 由具体的探测设备类型实现
     * 
     * @param airspace 空域环境
     * @param uavs 无人机列表
     * @return 探测事件列表
     */
    protected abstract List<DetectionEvent> doPerformDetection(AirspaceEnvironment airspace, List<UAV> uavs);
    
    /**
     * 执行特定类型的参数调整逻辑
     * 由具体的探测设备类型实现
     * 
     * @param newParams 新参数
     */
    protected abstract void doAdjustParameters(SensorParameters newParams);
    
    /**
     * 执行特定类型的重置逻辑
     * 由具体的探测设备类型实现
     */
    protected abstract void doReset();
    
    // ================ 私有验证方法 ================
    
    /**
     * 验证初始化参数
     * 
     * @param params 初始化参数
     */
    private void validateInitParams(DeviceInitParamsDTO params) {
        if (params == null) {
            throw new IllegalArgumentException("初始化参数不能为null");
        }
        if (!params.isValid()) {
            throw new IllegalArgumentException("初始化参数无效");
        }
        if (params.getDeviceType() != this.type) {
            throw new IllegalArgumentException("设备类型不匹配");
        }
    }
    
    /**
     * 验证位置参数
     * 
     * @param position 位置
     */
    private void validatePosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("位置不能为null");
        }
    }
    
    /**
     * 验证朝向参数
     * 
     * @param orientation 朝向角度
     */
    private void validateOrientation(double orientation) {
        if (orientation < 0.0 || orientation >= 360.0) {
            throw new IllegalArgumentException("朝向角度必须在0到360度之间");
        }
    }
    
    /**
     * 验证仰角参数
     * 
     * @param elevation 仰角
     */
    private void validateElevation(double elevation) {
        if (elevation < 0.0 || elevation > 90.0) {
            throw new IllegalArgumentException("仰角必须在0到90度之间");
        }
    }
    
    /**
     * 验证探测距离参数
     * 
     * @param detectionRange 探测距离
     */
    private void validateDetectionRange(double detectionRange) {
        if (detectionRange <= 0.0) {
            throw new IllegalArgumentException("探测距离必须大于0");
        }
    }
    
    /**
     * 验证视场角参数
     * 
     * @param fieldOfView 视场角
     */
    private void validateFieldOfView(double fieldOfView) {
        if (fieldOfView <= 0.0 || fieldOfView > 360.0) {
            throw new IllegalArgumentException("视场角必须在0到360度之间");
        }
    }
} 