package com.JP.dronesim.application.dtos.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 设备参数热调整DTO
 * 用于设备参数热调�?
 *
 * @author JP Team
 * @version 1.0
 */
public class AdjustDeviceParamDTO {

    /**
     * 设备参数映射
     */
    @NotNull(message = "设备参数不能为空")
    @NotEmpty(message = "设备参数不能为空")
    private Map<String, Object> parameters;

    /**
     * 调整原因
     */
    private String reason;

    /**
     * 是否立即生效
     */
    private Boolean immediate;

    /**
     * 生效时间（可选，用于延迟调整�?
     */
    private String effectiveTime;

    /**
     * 默认构造函�?
     */
    public AdjustDeviceParamDTO() {
    }

    /**
     * 构造函�?
     *
     * @param parameters 设备参数
     */
    public AdjustDeviceParamDTO(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 构造函�?
     *
     * @param parameters 设备参数
     * @param reason 调整原因
     * @param immediate 是否立即生效
     */
    public AdjustDeviceParamDTO(Map<String, Object> parameters, String reason, Boolean immediate) {
        this.parameters = parameters;
        this.reason = reason;
        this.immediate = immediate;
    }

    /**
     * 获取设备参数
     *
     * @return 设备参数
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * 设置设备参数
     *
     * @param parameters 设备参数
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 获取调整原因
     *
     * @return 调整原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置调整原因
     *
     * @param reason 调整原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取是否立即生效
     *
     * @return 是否立即生效
     */
    public Boolean getImmediate() {
        return immediate;
    }

    /**
     * 设置是否立即生效
     *
     * @param immediate 是否立即生效
     */
    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    /**
     * 获取生效时间
     *
     * @return 生效时间
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * 设置生效时间
     *
     * @param effectiveTime 生效时间
     */
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    @Override
    public String toString() {
        return "AdjustDeviceParamDTO{" +
                "parameters=" + parameters +
                ", reason='" + reason + '\'' +
                ", immediate=" + immediate +
                ", effectiveTime='" + effectiveTime + '\'' +
                '}';
    }
}
