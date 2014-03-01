package webrc.robot.control;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.springframework.beans.factory.annotation.Value;
import webrc.robot.util.I2C;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * a controller for a pwm device using the PCA9685 via i2c. This controller
 * represents the whole chip and controls chip-level state
 * 
 * also acts as a factory for PCA9685pinController
 * 
 * @author benjaminmorgan
 * 
 */
public class PCA9685Control extends Control<Map<String, Object>> {

	static final byte MODE1 = 0x00;
	static final byte MODE2 = 0x01;
	static final byte SUBADR1 = 0x02;
	static final byte SUBADR2 = 0x03;
	static final byte SUBADR3 = 0x04;
	static final byte ALLCALLADR = 0x05;

	static final byte ALL_LED_ON_L = (byte) 0xFA;
	static final byte ALL_LED_ON_H = (byte) 0xFB;
	static final byte ALL_LED_OFF_L = (byte) 0xFC;
	static final byte ALL_LED_OFF_H = (byte) 0xFD;
	static final byte PRE_SCALE = (byte) 0xFE;
	static final byte TESTMODE = (byte) 0xFF;

	//return the register number for the "on" time lowbyte
	// LED_ON_HIGH is one higher
	static byte LED_ON_LOW(int i) {
		return (byte) (4 * i + 6);
	}

	//return the register number for the "off" time lowbyte
	// LED_OFF_HIGH is one higher
	static byte LED_OFF_LOW(int i) {
		return (byte) (4 * i + 8);
	}


    @Value("${testMode}")
    private static boolean testMode=true;

    I2CDevice dev = null;

    int bus;
    int device;

	public PCA9685Control() {
    }

    @PostConstruct
    public void init() {
        log.log("Created PCA control!!");

		if (!testMode) {
			I2CBus i2c = I2C.geti2cBus(bus);
			if (i2c != null) {
				try {
					dev = i2c.getDevice(device);

					// TODO do this via the Set method
					//enable register autoincrement
					//turn off sleep mode
					//turn off sub1, sub2, sub3, allcall
					byte mode1 = Byte.parseByte("00100000", 2);
					
					byte[] modes = new byte[]{mode1};
					I2C.writeBytesToRegister(dev, modes, MODE1);
					
					//put the board to sleep on shutdown
					Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
						@Override
						public void run() {
							alloff();
						}}));
					
				} catch (IOException e) {
					log.log("Error: could not get device " + device + " from bus " + bus);
					log.log(e);
				}
			}
		}
	}

	@Override
	public void set(Map<String, Object> values) {
		//TODO: use allcall and board level flags
	}
	
	public void alloff()
	{
		//TODO: use above board-level set function
		//set the sleep flag
		byte mode1 = Byte.parseByte("00110000", 2);		
		byte[] modes = new byte[]{mode1};
		I2C.writeBytesToRegister(dev, modes, MODE1);
	}



    public void setBus(int bus) {
        this.bus = bus;
    }

    public void setDevice(int device) {
        this.device = device;
    }
}
