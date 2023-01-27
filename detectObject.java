import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
public class detectObject implements Behavior
{
	
	private float[] samples;
	private EV3UltrasonicSensor ultrasensor;
	private SampleProvider sp;
	private double OBJECT_DETECTION_DISTANCE;
	
	
	public detectObject(EV3UltrasonicSensor sensor) { //Ultrasonic sensor passed into EV3
		this.ultrasensor = sensor;
		this.sp = ultrasensor.getDistanceMode();
		this.samples = new float[1];
		
	}
	public void action() {
		State.objectDetected = true; //when an object is detected, variable turn to true
			
	}
	public void suppress() {
		
	}
	
	public boolean takeControl() {
		this.sp.fetchSample(this.samples, 0);
		return (this.samples[0]*100.0 <= OBJECT_DETECTION_DISTANCE);		
	}

	
	
	
}
