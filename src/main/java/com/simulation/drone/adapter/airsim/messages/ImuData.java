package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;

import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

@Message
public class ImuData extends AirSimRpcMessageTrait{
	public long time_stamp;
	public Quaternionr orientation;
	
}

