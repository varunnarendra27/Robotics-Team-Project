import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Driver {
	public static void main(String[] args) {
		//Instantiates new objects of motors and sensors to be passed into the behaviours
		BaseRegulatedMotor mL = new EV3LargeRegulatedMotor(MotorPort.A);
		BaseRegulatedMotor mR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3LargeRegulatedMotor claw = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
		NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S3);
		EV3UltrasonicSensor ultrasensor = new EV3UltrasonicSensor(SensorPort.S4);
		
		//new instances of the behaviours are created to pass into the arbitrator
		Behavior lift = new Lift(claw, ultrasensor, mL, mR);
		Behavior detectObject = new detectObject(ultrasensor);
		Behavior clapDetection = new ClapDetection(sound);
		Behavior lineFollower = new LineBehaviour(mL, mR, color, ultrasensor);
		Behavior interrupt = new Interrupt();
		Behavior battery = new BatteryExit();
		Behavior[] ar = {lift, detectObject, lineFollower, clapDetection, interrupt, battery};//behaviours passed into arbitrator in order of importance
		int counter = 0; 
		while(Button.ENTER.isUp()) { //Welcome screen printed onto LCD screen
			if(counter == 0) {
				LCD.drawString("WELCOME", 3, 1);
				LCD.drawString("Authors:",1, 2);
				LCD.drawString("Noe Chen,", 1, 3);
				LCD.drawString("Taran Harmer,", 1, 4);
				LCD.drawString("Varun Narendra,", 1, 5);
				LCD.drawString("Oyku Pamukcu", 1, 6);
				LCD.drawString("Final Version", 3, 7);
			}counter += 1;
		}LCD.clear();
		Arbitrator ab = new Arbitrator(ar);
		
		ab.go();	//arbitrator is ran
	}
}
