package webrc.robot.control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.springframework.beans.factory.annotation.Autowired;
import webrc.robot.RobotProperties;
import webrc.robot.util.I2C;
import webrc.robot.util.RaspiConstants;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class SSD1306Control extends Control{
	
	I2CDevice dev;
	GpioPinDigitalOutput pin;

    @Autowired
    RobotProperties robotProperties;

    @Autowired
    I2C i2c;


    public int bus;
    public int device;
    public int resetPin;


    public void setBus(int bus) {
        this.bus = bus;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setResetPin(int resetPin) {
        this.resetPin = resetPin;
    }

	public SSD1306Control() {
    }

    @PostConstruct
    public void init() {
		if (!robotProperties.isTestMode()) {
			I2CBus i2cbus = i2c.geti2cBus(bus);
				try {
					dev = i2cbus.getDevice(device);
					
					GpioController gpio = GpioFactory.getInstance();


			        // provision gpio pin #01 as an output pin and toggle
			        pin = gpio.provisionDigitalOutputPin(RaspiConstants.getPin(resetPin));
			        pin.setState(PinState.HIGH);
			        
			        try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
			        
			        pin.setState(PinState.LOW);
			        
			        try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
			        
			        pin.setState(PinState.HIGH);


                    //displayoff
					i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xAE});

                    //set display-clock-div
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xD5, (byte)0x80});

                    //set multiplex
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0XA8, (byte)0x3F});

                    //set display offset
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xD3, (byte)0x00});

                    //line #0
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x40});

                    //**charge pump
                    //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x8D, (byte)0x10}); //ext vcc
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x8D, (byte)0x14});

                    //segremap
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA1});

                    //set com output scan direction
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xC8});

                    //set com pins
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xDA, (byte)0x12});

                    //**set contrast
                    //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x81, (byte)0x9F}); //ext vcc
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x81, (byte)0xCF});

                    //**set precharge
                    //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xD9, (byte)0x22}); //ext vcc
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xD9, (byte)0xF1});

                    //set vcomh deselect level ?
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xDB, (byte)0x40});

                    //"entire" display on
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA4});

                    //normal display (not inverse)
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA6});

                    //clear screen?

                    //display on
                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xAF});


//
//                    //memory mode
//                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x20, (byte)0x00});
//
//                    //**external/internal vcc ?
//                    //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x9F}); //ext vcc
//                    i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xCF});

                    //all on
                    //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA5});


                    log.log("Turned screen on!!??");
					//fuck yea it did!!!

                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            for(;;)
                            {
                                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x40, (byte)(Math.random()*256)});
                                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x22, (byte)0x00, (byte)0x07});

//                                i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA6});

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                }

                                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x21, 0x00, 0x3F});
                                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x22, 0x00, 0x07});

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                }



                            }
                        }
                    });
                    t.start();

					
				} catch (IOException e) {
					log.log("Error: could not get device " + device + " from bus " + bus);
					log.log(e);
				}
			}
	}
	
	
	@Override
	public void set(Object value) {
		// TODO Auto-generated method stub
		
	}
	
//	public void writeBytes(byte[] bytes) {
//
//		{
//			StringBuilder sb = new StringBuilder();
//			for(byte b : bytes)
//			{
//				String hex = Integer.toHexString(b);
//
//				sb.append((hex.length()<2 ? "0" : "") + Integer.toHexString(b&0xff)+" ");
//			}
//			log.log("Writing: " + sb.toString());
//		}
//
//		if (!Vars.getBoolean("testMode", false)) {
//			try {
//				//dev.write(bytes, 0, bytes.length);
//				//for (byte b : bytes)
//				//{
//					dev.write(bytes, 0, bytes.length);
//				//}
//			} catch (IOException e) {
//				log.log("Error writing to I2C Device");
//				log.log(e);
//			}
//		}
//		//else
//
//	}
}
