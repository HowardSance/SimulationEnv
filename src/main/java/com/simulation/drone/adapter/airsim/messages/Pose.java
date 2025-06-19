package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;

import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

@Message
public class Pose extends AirSimRpcMessageTrait{
	public Vector3r position;
	public Quaternionr orientation;
	
	public Pose() {
		this.position = new Vector3r();
		this.orientation = new Quaternionr();
	}

}
