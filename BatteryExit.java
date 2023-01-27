import lejos.hardware.Battery;
import lejos.robotics.subsumption.Behavior;

public class BatteryExit implements Behavior {

	@Override
	public boolean takeControl() {
		
		float volt = Battery.getVoltage(); //gets current battery life
		return volt < 1; 
	}

	@Override
	public void action() {
		System.exit(0); //ends program if takeControl() = true
		
	}

	@Override
	public void suppress() {
		
		
	}

}