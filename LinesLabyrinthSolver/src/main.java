import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;


public class main {
	public static void main(String[] args) throws InterruptedException,
			IOException {
		FileOutputStream out = null; // declare outside the try block
		File data = new File("meas.dat");
		out = new FileOutputStream(data);
		DataOutputStream dataOut = new DataOutputStream(out);

		LightSensor lsLine = new LightSensor(SensorPort.S1);
		lsLine.setFloodlight(true);
		LightSensor lsNavi = new LightSensor(SensorPort.S2);
		lsNavi.setFloodlight(true);

		dataOut.writeUTF("t lvLine lvNavi\n");

		PIDControllerLineFollowing pidController = new PIDControllerLineFollowing(
				0.04f, 435);
		pidController.KP = 1f;
		pidController.KD = 0.5f;
		pidController.KI = 0f;

		int i = 0;
		int lvLine = 0;
		int lvNavi = 0;

		float speedForward = 200; // degrees per second
		float MotorSpeedDiff=0;

		Motor.A.backward();
		Motor.B.backward();
		while (!Button.ENTER.isPressed()) {
			i++;
			lvLine = lsLine.readNormalizedValue();
			lvNavi = lsNavi.readNormalizedValue();


			MotorSpeedDiff=pidController.calcMotorSpeed(lvLine);
			
			Motor.A.setSpeed(speedForward+MotorSpeedDiff);
			Motor.B.setSpeed(speedForward-MotorSpeedDiff);
			
			//logging
			
			LCD.clear();
			LCD.drawString("lvLine: " + lvLine, 0, 0);
			LCD.drawString("lvNavi: " + lvNavi, 0, 1);
			LCD.drawString("speedl: " + String.valueOf((speedForward+MotorSpeedDiff)), 0, 2);
			LCD.drawString("speedr: " + String.valueOf((speedForward-MotorSpeedDiff)), 0, 3);
			LCD.drawString("x_pos: " + String.valueOf(pidController.x_pos_error), 0, 4);
			LCD.drawString("x_speed: " + String.valueOf(pidController.x_speed_error), 0, 5);
			
			dataOut.writeUTF(i + " " + lvLine + " " + lvNavi
					+ "\n");
			Thread.sleep(1);
		}
		out.close();
	}

}
