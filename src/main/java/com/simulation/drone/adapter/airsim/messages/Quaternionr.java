package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;

import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

@Message
public class Quaternionr extends AirSimRpcMessageTrait{
	public float w_val;
	public float x_val;
	public float y_val;
	public float z_val;
	
	public Quaternionr() {
		this.w_val=0f;
		this.x_val=0f;
		this.y_val=0f;
		this.z_val=0f;
	}
}
