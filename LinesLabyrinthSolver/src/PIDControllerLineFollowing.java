import lejos.nxt.LCD;


public class PIDControllerLineFollowing {
	public float x_pos_error, x_speed_error;
	public float KP=0,KD=0,KI=0;
	private float integral;
	private float T;
	private Observer derivativeObserv ;
	private int  _setPoint;
	public PIDControllerLineFollowing(float sampleTime, int setPoint) {
		T=sampleTime;
		_setPoint=setPoint;
		derivativeObserv=new Observer(sampleTime);
		integral=0;
		
	}

	public float calcMotorSpeed(int inputValue){
		 //calc x_pos from lightValue
	    x_pos_error=(float) (inputValue-_setPoint);
	    x_speed_error = derivativeObserv.update(x_pos_error);
	    x_pos_error = derivativeObserv.getEstimated_x_pos(); 
	    integral+=T*x_pos_error;
		return KP*(x_pos_error+KI*integral+KD*x_speed_error);
		
	}
	
}
