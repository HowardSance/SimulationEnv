package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 图像请求
 * 用于指定获取图像的参数
 */
@Message
public class ImageRequest extends AirSimRpcMessageTrait {
    
    /** 相机名称 */
    public String camera_name;
    
    /** 图像类型 */
    public int image_type;
    
    /** 是否像素为浮点数 */
    public boolean pixels_as_float;
    
    /** 是否压缩 */
    public boolean compress;
    
    public ImageRequest() {
        this.camera_name = "";
        this.image_type = 0; // Scene = 0
        this.pixels_as_float = false;
        this.compress = true;
    }
    
    public ImageRequest(String cameraName, int imageType, boolean pixelsAsFloat, boolean compress) {
        this.camera_name = cameraName;
        this.image_type = imageType;
        this.pixels_as_float = pixelsAsFloat;
        this.compress = compress;
    }
    
    // 图像类型常量
    public static final int SCENE = 0;
    public static final int DEPTH_PLANNER = 1;
    public static final int DEPTH_PERSPECTIVE = 2;
    public static final int DEPTH_VIS = 3;
    public static final int DISPARITY_NORMALIZED = 4;
    public static final int SEGMENTATION = 5;
    public static final int SURFACE_NORMALS = 6;
    public static final int INFRARED = 7;
} 