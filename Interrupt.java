import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

public class Interrupt implements Behavior {

	@Override
	public boolean takeControl() {
		return Button.ESCAPE.isDown();//when ESCAPE button is pressed, the program ends
	}

	@Override
	public void action() {

		System.exit(0);
		
	}

	@Override
	public void suppress() {
		
	}

}