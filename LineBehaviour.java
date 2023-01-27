import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
public class LineBehaviour implements Behavior {
	
	private boolean suppressed = false;
	private float[] samples = new float[1];
	private SampleProvider distance;
	private EV3UltrasonicSensor ultrasensor;
	private SampleProvider sp; //sample provider is used to get samples 
	static float[] level = new float[3];
	public static String colourHolder;
	BaseRegulatedMotor left;
	BaseRegulatedMotor right;
	EV3ColorSensor colour;
	int counter;
	private double OBJECT_DETECTION_DISTANCE;
	
	public LineBehaviour(BaseRegulatedMotor Left, BaseRegulatedMotor Right, EV3ColorSensor colour, EV3UltrasonicSensor sensor) {
		this.left = Left; //motors and sensors passed into motor
		this.right = Right;
		this.colour = colour;
		this.sp = colour.getRGBMode(); //this setting is used to read RGB light
		this.counter = 0;
		this.ultrasensor = sensor;
		this.distance = ultrasensor.getDistanceMode(); //gets distance of object from ultrasonic sensor
	}	
	
		@Override
		public boolean takeControl() {
			return ((colourHolder != null) && !(State.lineEnded)); //code run when there is a string of colour to be followed
		}
	
		@Override
		public void action() {
			while(!suppressed) {
				sp.fetchSample(level, 0); //colour is sensed
				distance.fetchSample(samples, 0);//distance is sensed
				if(level[0] > 0.1 && level[1] > 0.1 && level[2] > 0.1) { //distinguishes colour
					counter += 1;
					left.setSpeed(20); //if coloured line, turn left
					right.setSpeed(100);
					left.forward();
					right.forward();
				}else {
					counter = 0; //if white line, turn right
					right.setSpeed(20);
					left.setSpeed(100);
					right.forward();
					left.forward();
				}
				if(samples[0]*100.0 <= OBJECT_DETECTION_DISTANCE) { //if object is 4.9cm away, object detected
					LCD.drawInt(counter, 2, 3);
					left.close();
					right.close();
					State.lineEnded = true;
					State.objectDetected = true;
					break;
				}
			}
		}
	
		@Override
		public void suppress() {
			
		}
	}