package com.JP.dronesim.domain.device.model;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceStatus;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.common.DetectionLog;
import com.JP.dronesim.domain.device.model.common.SensorParameters;
import com.JP.dronesim.domain.device.model.events.DetectionEvent;
import com.JP.dronesim.domain.airspace.model.Airspace;
import com.JP.dronesim.domain.uav.model.UAV;

import java.util.List;

/**
 * 抽象探测设备基类
 * 定义所有探测设备的共同属性和行为，并提供通用实现
 * 适用于雷达、光电摄像头、无线电探测器、GPS诱导器等设备
 * 
 * @author JP
 * @version 1.0
 */
public abstract class AbstractProbeDevice {
    
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
    
    // ================ 基础属性访问方法 ================
    
    /**
     * 获取设备唯一标识符
     * 
     * @return 设备ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * 获取设备名称
     * 
     * @return 设备名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取设备类型
     * 
     * @return 设备类型枚举
     */
    public DeviceType getType() {
        return type;
    }
    
    /**
     * 获取设备位置坐标
     * 
     * @return NED坐标系中的三维位置
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 设置设备位置坐标
     * 
     * @param position NED坐标系中的三维位置
     */
    public void setPosition(Position position) {
        validatePosition(position);
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
        validateOrientation(orientation);
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
        validateElevation(elevation);
        this.elevation = elevation;
    }
    
    /**
     * 获取设备姿态信息
     * 
     * @return 设备的三维姿态
     */
    public Orientation getAttitude() {
        return attitude;
    }
    
    /**
     * 设置设备姿态信息
     * 
     * @param attitude 设备的三维姿态
     */
    public void setAttitude(Orientation attitude) {
        this.attitude = attitude;
    }
    
    /**
     * 获取设备有效探测距离
     * 
     * @return 最大有效探测/影响距离（米）
     */
    public double getDetectionRange() {
        return detectionRange;
    }
    
    /**
     * 设置设备有效探测距离
     * 
     * @param detectionRange 最大有效探测/影响距离（米）
     */
    public void setDetectionRange(double detectionRange) {
        validateDetectionRange(detectionRange);
        this.detectionRange = detectionRange;
    }
    
    /**
     * 获取设备视场角
     * 
     * @return 探测设备的视场角度（例如光电摄像头的角度，雷达的波束宽度）
     */
    public double getFieldOfView() {
        return fieldOfView;
    }
    
    /**
     * 设置设备视场角
     * 
     * @param fieldOfView 探测设备的视场角度
     */
    public void setFieldOfView(double fieldOfView) {
        validateFieldOfView(fieldOfView);
        this.fieldOfView = fieldOfView;
    }
    
    /**
     * 获取设备运行状态
     * 
     * @return 设备状态枚举（active/inactive/error）
     */
    public DeviceStatus getStatus() {
        return status;
    }
    
    /**
     * 设置设备运行状态
     * 
     * @param status 设备状态枚举
     */
    public void setStatus(DeviceStatus status) {
        this.status = status;
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
     * @param parameters 特定传感器类型的详细参数
     */
    public void setDetectionParameters(SensorParameters parameters) {
        if (parameters != null && !parameters.isValid()) {
            throw new IllegalArgumentException("探测参数无效");
        }
        this.detectionParameters = parameters;
    }
    
    /**
     * 获取探测日志
     * 
     * @return 探测事件的存储列表
     */
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
    
    // ================ 行为方法 ================
    
    /**
     * 初始化探测设备
     * 设置设备的初始位置、姿态和特定类型参数
     * 
     * @param params 包含设备ID、名称、位置、姿态和特定类型参数的初始化参数对象
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备已经初始化
     */
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
    
    /**
     * 执行探测扫描/影响逻辑
     * 此方法将与AirSim API和内部探测/影响算法结合，
     * 尝试探测/影响空域中的无人机
     * 
     * @param airspace 当前空域环境对象
     * @param uavs 当前空域中的所有无人机列表
     * @return 成功探测到的无人机事件列表（GPS诱导器可能返回受影响的UAV列表或影响事件）
     * @throws IllegalStateException 如果设备未初始化或处于错误状态
     */
    public List<DetectionEvent> performDetection(Airspace airspace, List<UAV> uavs) {
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
    
    /**
     * 运行时动态调整探测设备的参数
     * 
     * @param newParams 包含需要更新的参数及其新值的参数对象
     * @throws IllegalArgumentException 如果新参数无效或类型不匹配
     * @throws IllegalStateException 如果设备不支持参数调整
     */
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
    
    /**
     * 通用设备参数热更新（用于DTO参数Map场景）
     * @param paramMap 参数键值对
     */
    public void updateParameters(java.util.Map<String, Object> paramMap) {
        // 默认实现：简化的参数更新逻辑
        if (paramMap == null || paramMap.isEmpty()) {
            return;
        }
        
        try {
            // 更新基础参数
            if (paramMap.containsKey("detectionRange")) {
                Object value = paramMap.get("detectionRange");
                if (value instanceof Number) {
                    this.setDetectionRange(((Number) value).doubleValue());
                }
            }
            
            if (paramMap.containsKey("fieldOfView")) {
                Object value = paramMap.get("fieldOfView");
                if (value instanceof Number) {
                    this.setFieldOfView(((Number) value).doubleValue());
                }
            }
            
            if (paramMap.containsKey("orientation")) {
                Object value = paramMap.get("orientation");
                if (value instanceof Number) {
                    this.setOrientation(((Number) value).doubleValue());
                }
            }
            
            if (paramMap.containsKey("elevation")) {
                Object value = paramMap.get("elevation");
                if (value instanceof Number) {
                    this.setElevation(((Number) value).doubleValue());
                }
            }
            
        } catch (Exception e) {
            throw new IllegalArgumentException("参数更新失败: " + e.getMessage(), e);
        }
    }
    
    // ================ 状态检查方法 ================
    
    /**
     * 检查设备是否已初始化
     * 
     * @return true表示设备已初始化，false表示未初始化
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 检查设备是否处于活跃状态
     * 
     * @return true表示设备可以执行探测任务，false表示不可以
     */
    public boolean isActive() {
        return status == DeviceStatus.ACTIVE;
    }
    
    /**
     * 检查设备是否处于错误状态
     * 
     * @return true表示设备有故障，false表示正常
     */
    public boolean hasError() {
        return status == DeviceStatus.ERROR;
    }
    
    /**
     * 启用设备
     * 将设备状态设置为活跃状态
     * 
     * @throws IllegalStateException 如果设备未初始化或处于错误状态
     */
    public void enable() {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        if (status == DeviceStatus.ERROR) {
            throw new IllegalStateException("设备处于错误状态，无法启用");
        }
        this.status = DeviceStatus.ACTIVE;
    }
    
    /**
     * 禁用设备
     * 将设备状态设置为非活跃状态
     */
    public void disable() {
        this.status = DeviceStatus.INACTIVE;
    }
    
    /**
     * 重置设备
     * 清除探测日志，重置设备状态
     * 
     * @throws IllegalStateException 如果设备未初始化
     */
    public void reset() {
        if (!initialized) {
            throw new IllegalStateException("设备未初始化");
        }
        
        detectionLog.clearEvents();
        this.status = DeviceStatus.INACTIVE;
        
        // 执行特定类型的重置逻辑
        doReset();
    }
    
    // ================ 验证方法 ================
    
    /**
     * 验证设备配置的有效性
     * 检查所有参数是否在合理范围内
     * 
     * @return true表示配置有效，false表示配置无效
     */
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
    
    /**
     * 获取设备配置的描述信息
     * 用于日志记录和调试
     * 
     * @return 设备配置的字符串描述
     */
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
    protected abstract List<DetectionEvent> doPerformDetection(Airspace airspace, List<UAV> uavs);
    
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