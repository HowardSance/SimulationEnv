package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class Pose extends AirSimRpcMessageTrait{
	public Vector3r position;
	public Quaternionr orientation;
	
	public Pose() {
		this.position = new Vector3r();
		this.orientation = new Quaternionr();
	}

}
