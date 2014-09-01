package webrc.robot.sensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.springframework.beans.factory.annotation.Autowired;
import webrc.robot.RobotProperties;
import webrc.robot.util.I2C;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bemorgan on 8/30/14.
 */
public class ADS1015Sensor extends Sensor {

    private static final byte CONVERSION = 0x00;
    private static final byte CONFIG = 0x01;
    private static final byte LO_THRESH = 0x02;
    private static final byte HI_THRESH = 0x03;


    @Autowired
    RobotProperties robotProperties;

    @Autowired
    I2C i2c;

    private int bus;
    private int device;
    private int updatePeriod = 100; //default to .1 second

    private String key = "voltage";

    I2CDevice dev = null;
    float lsb = (float)(2/Math.pow(2.0, 9.0));

    public void setBus(int bus) {
        this.bus = bus;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public void setKey(String key) {
        this.key=key;
    }

    @PostConstruct
    public void init() {

        System.out.println("asdfasdfasdf");

        if (!robotProperties.isTestMode()) {
            I2CBus i2cBus = i2c.geti2cBus(bus);
            if (i2c != null) {

                try {
                    dev = i2cBus.getDevice(device);

                    System.out.println("asdfasdfasdf");

                    //turn on the accelerometer, place it in 100hz mode
                    byte[] bytes = new byte[]{Byte.parseByte("00000100",2), Byte.parseByte("10000011",2)};
                    i2c.writeBytesToRegister(dev, bytes, CONFIG);


                    begin();

                } catch (IOException e) {
                    log.error("error writing to i2c bus", e);
                }
            }
        }
    }

    private void begin() {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {

                for(;;) {

                    if(!robotProperties.isTestMode()) {

                        byte[] bytes = new byte[2];
                        try {
                            dev.read(CONVERSION, bytes, 0, 6);

                            String value = Integer.toHexString(bytes[0]<<8 & bytes[1]);

                            //log sensor readings
                            blackbox.info(key+",{}", value);

                            publishOne(key, value);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    try {
                        Thread.sleep(updatePeriod);
                    } catch (InterruptedException e) {
                        //ok
                    }
                }
            }
        });

        t.setDaemon(true);
        t.setName("ADS1015 Polling Thread: " + key);
        t.start();
    }
}
