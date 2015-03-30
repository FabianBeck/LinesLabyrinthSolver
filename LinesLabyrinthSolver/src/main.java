import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class main {

	public static void main(String[] args) throws InterruptedException, IOException {
		FileOutputStream out = null; // declare outside the try block
	    File data = new File("meas.dat");
	    out = new FileOutputStream(data);
	    DataOutputStream dataOut = new DataOutputStream(out);
	    
		LightSensor ls=new LightSensor(SensorPort.S3);
		ls.setFloodlight(true);
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
	    dataOut.writeUTF("t lightValue sonicValue \n");
		int i=0;
		int lightValue=0;
		int sonicValue=0;
		float speedl=0;
		float speedr=0;
		float KP=1f;
		float KD=0;
	    Motor.A.backward();
	    Motor.B.backward();
		while(!Button.ENTER.isPressed()){
			i++;
			lightValue=ls.readNormalizedValue();
			sonicValue=sonic.getDistance();
			LCD.clear();
		    LCD.drawString("lightValue: "+lightValue,0,0);
		    LCD.drawString("sonicValue: "+sonicValue,0,1);
		    LCD.drawString("speedl: "+speedl,0,2);
		    LCD.drawString("speedr: "+speedr,0,3);
		    speedl=KP*(lightValue-435)+150;
		    speedr=KP*(-lightValue+435)+150;
		    Motor.A.setSpeed(speedl);
		    Motor.B.setSpeed(speedr);
		    dataOut.writeUTF(i+" "+lightValue+" "+sonicValue+"\n");
		    Thread.sleep(1);
		}
		out.close(); 
	}

}
