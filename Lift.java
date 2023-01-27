import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Lift implements Behavior {
    private EV3LargeRegulatedMotor claw;
    private EV3UltrasonicSensor distance;
    public static boolean objectDetected;
    BaseRegulatedMotor left;
    BaseRegulatedMotor right;
    int angle;

	public Lift(EV3LargeRegulatedMotor claw, EV3UltrasonicSensor distance, BaseRegulatedMotor left, BaseRegulatedMotor right) 
	{// motors and sensors are passed into the constructor
		this.claw = claw;
		this.distance = distance;
		this.angle = 0;
		this.left = left;
		this.right = right;
	}
	
	public void action() {
		Sound.beepSequenceUp();
		if (State.lineEnded) {
			this.claw.forward();//the motor is moved forward to close the arm
			Delay.msDelay(2000);
	        this.claw.stop();
	        this.angle = 90;
	        State.objectDetected = false;
	        suppress();
		}
		if (this.angle == 0) {
			this.claw.forward();
			Delay.msDelay(2000);
	        this.claw.stop();
	        this.angle = 90;
			left.setSpeed(100);
			right.setSpeed(100);
			left.forward();
			right.forward();
			Delay.msDelay(1000);
			left.stop();
			right.stop();
		}
        if (this.angle == 90)  {
        	this.claw.rotate(-90);
        	this.angle = 0;
        	State.objectDetected = false;
        } 
	}
	
	
	
	@Override
	public boolean takeControl() 
	{
		return State.objectDetected; //behaviour is run when variable object is detected
	}
	
	
	
	@Override
	public void suppress() {
	// TODO Auto-generated method stub
	}
	    }
	       