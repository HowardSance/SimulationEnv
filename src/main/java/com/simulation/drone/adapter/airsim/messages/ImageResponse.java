package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 图像响应
 * 包含请求的图像数据和相关信息
 */
@Message
public class ImageResponse extends AirSimRpcMessageTrait {
    
    /** 图像数据(压缩或未压缩) */
    public byte[] image_data_uint8;
    
    /** 浮点像素数据 */
    public float[] image_data_float;
    
    /** 相机名称 */
    public String camera_name;
    
    /** 相机位置 */
    public Vector3r camera_position;
    
    /** 相机姿态 */
    public Quaternionr camera_orientation;
    
    /** 时间戳 */
    public long time_stamp;
    
    /** 消息 */
    public String message;
    
    /** 是否像素为浮点数 */
    public boolean pixels_as_float;
    
    /** 是否压缩 */
    public boolean compress;
    
    /** 图像宽度 */
    public int width;
    
    /** 图像高度 */
    public int height;
    
    /** 图像类型 */
    public int image_type;
    
    public ImageResponse() {
        this.image_data_uint8 = new byte[0];
        this.image_data_float = new float[0];
        this.camera_name = "";
        this.camera_position = new Vector3r();
        this.camera_orientation = new Quaternionr();
        this.time_stamp = 0;
        this.message = "";
        this.pixels_as_float = false;
        this.compress = true;
        this.width = 0;
        this.height = 0;
        this.image_type = 0;
    }
} 