import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ClapDetection implements Behavior {

	private NXTSoundSensor sound;
	private String[] rgb;
	private int i = -1;
	private SampleProvider sp;
	private float[] sample;
	private double MAX_SOUND_THRESHOLD = 0.7;

	public ClapDetection(NXTSoundSensor sound) {
		this.sound = sound;
		this.rgb = new String[3];
		this.rgb[0] = "RED";
		this.rgb[1] = "GREEN";
		this.rgb[2] = "BLUE";
		this.sp = sound.getDBAMode();
		this.sample = new float[1];
	}

	@Override
	public boolean takeControl() {
		this.sp.fetchSample(this.sample, 0);
		return (this.sample[0] > (float) MAX_SOUND_THRESHOLD);
	}

	@Override
	public void action() {
		this.i += 1;
		if (this.i == 3) {
			this.i = 0;								
		}
		LineBehaviour.colourHolder = this.rgb[i];	
		LCD.drawString("Following " + this.rgb[i], 2, 3);
		Delay.msDelay(1000);	
	}	

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
