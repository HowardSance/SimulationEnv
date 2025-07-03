package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class CarState extends AirSimRpcMessageTrait{
	public float speed;
	public int gear;
	public float rpm;
	public float maxrpm;
	public boolean handbrake;
	public KinematicsState kinematics_estimated;
	public long timestamp;
	
	public CarState() {
		this.speed = 0f;
		this.gear=0;
		this.rpm = 0f;
		this.maxrpm = 0f;
		this.handbrake = false;
		this.kinematics_estimated = new KinematicsState();
		this.timestamp=0;
	}
}