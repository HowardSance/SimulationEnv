package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class KinematicsState extends AirSimRpcMessageTrait{
	public Vector3r position;
	public Quaternionr orientation;
	 public Vector3r linear_velocity;
	public Vector3r angular_velocity;
	public Vector3r linear_acceleration;
	public Vector3r angular_acceleration;
	
	public KinematicsState() {
		this.position = new Vector3r();
		this.orientation = new Quaternionr();
		this.linear_velocity = new Vector3r();
		this.angular_velocity = new Vector3r();
		this.linear_acceleration = new Vector3r();
		this.angular_acceleration = new Vector3r();
	}
}
