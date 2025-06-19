package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 碰撞信息
 * 包含是否发生碰撞以及碰撞相关的详细信息
 */
@Message
public class CollisionInfo extends AirSimRpcMessageTrait {
    
    /** 是否发生碰撞 */
    public boolean has_collided;
    
    /** 碰撞正常向量 */
    public Vector3r normal;
    
    /** 碰撞冲击 */
    public Vector3r impact_point;
    
    /** 碰撞位置 */
    public Vector3r position;
    
    /** 穿透深度 */
    public float penetration_depth;
    
    /** 时间戳 */
    public long time_stamp;
    
    /** 碰撞对象ID */
    public int object_id;
    
    /** 碰撞对象名称 */
    public String object_name;
    
    public CollisionInfo() {
        this.has_collided = false;
        this.normal = new Vector3r();
        this.impact_point = new Vector3r();
        this.position = new Vector3r();
        this.penetration_depth = 0.0f;
        this.time_stamp = 0;
        this.object_id = -1;
        this.object_name = "";
    }
} 