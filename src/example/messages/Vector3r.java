package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class Vector3r extends AirSimRpcMessageTrait {
	public float w_val;
	public float x_val;
	public float y_val;
	public float z_val;
	
	public Vector3r() {
		this.w_val=0f;
		this.x_val=0f;
		this.y_val=0f;
		this.z_val=0f;
	}
	
}
