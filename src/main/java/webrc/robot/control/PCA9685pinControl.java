package webrc.robot.control;

import webrc.robot.util.I2C;

import javax.annotation.PostConstruct;

public class PCA9685pinControl extends Control<Float> {
	

	PCA9685Control pca9685Control;
   	int pin;
	
	public PCA9685pinControl()
	{
	}

    @PostConstruct
    public void init()
    {
        log.log("created pin controller for pwm pin " + pin);
    }

	/**
	 * takes a value in [0,1]
	 */
	@Override
	public void set(Float value) {
		if(value < 0)
			value = 0.0f;
		if(value > 1)
			value = 1.0f;
		
		int intVal = (int)(value*4095);
		
		byte[] onbytes = new byte[]{(byte)(intVal&0xff),(byte)((intVal >> 8) & 0xff)};
		byte[] offbytes = new byte[]{0x0,0x0};
		
		I2C.writeBytesToRegister(pca9685Control.dev, onbytes, PCA9685Control.LED_ON_LOW(pin));
		I2C.writeBytesToRegister(pca9685Control.dev, offbytes, PCA9685Control.LED_OFF_LOW(pin));
	}

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setPca9685Control(PCA9685Control pca9685Control) {
        this.pca9685Control = pca9685Control;
    }


}
