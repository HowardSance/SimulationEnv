package test;

import java.net.UnknownHostException;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

import api.CarClientInterface;
import messages.CarControls;
import messages.CarState;
import messages.DistanceSensorData;



public class Example {
	
	public static void main(String[] args) throws UnknownHostException {
		EventLoop loop = EventLoop.defaultEventLoop();
		Client client = new Client("127.0.0.1", 41451, loop);
		
		CarClientInterface carclient = client.proxy(CarClientInterface.class);

		//Enable the API to control the car
		carclient.enableApiControl(true, "");
		System.out.println("Control enabled="+carclient.isApiControlEnabled(""));
		//Control the car
		CarControls control = new CarControls();
		control.steering = 10.0f;
		carclient.setCarControls(control, "");
		//Disable API
		carclient.enableApiControl(true, "");

		
		while(true) {
			//Get the car state of vehicle with no name
			CarState cs = carclient.getCarState("");
			//Get the distance sensor data of vehicle with no name
			DistanceSensorData ds = carclient.getDistanceSensorData("", "");
	
			System.out.println(cs.speed);
			System.out.println(ds.distance);
		}
		
		
		
	}
	
}
