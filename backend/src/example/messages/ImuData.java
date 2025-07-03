package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class ImuData extends AirSimRpcMessageTrait{
	public long time_stamp;
	public Quaternionr orientation;
	
}

