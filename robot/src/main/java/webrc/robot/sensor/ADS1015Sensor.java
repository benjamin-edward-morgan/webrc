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

    private float voltage=6.114f; //todo: make programable via PGA gain
    private String key = "voltage";

    I2CDevice dev = null;

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

        if (!robotProperties.isTestMode()) {
            I2CBus i2cBus = i2c.geti2cBus(bus);
            if (i2c != null) {

                try {
                    dev = i2cBus.getDevice(device);

                    byte[] bytes = new byte[]{(byte)Integer.parseInt("01000000", 2), (byte)Integer.parseInt("10000011",2)};
                    i2c.writeBytesToRegister(dev, bytes, CONFIG);



                    begin();

                } catch (IOException e) {
                    log.error("error writing to i2c bus", e);
                }
            }
        }
    }

    //todo: configure names for each input and poll all configured inputs
    private void begin() {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {

                for(;;) {

                    if(!robotProperties.isTestMode()) {

                        byte[] bytes = new byte[2];
                        try {
                            dev.read(CONVERSION, bytes, 0, 2);

                            int intVal = (bytes[0]&0xff)<<8 | bytes[1]&0xff;

                            String hexVal = Integer.toHexString(intVal);

                            if(intVal>=0x8000) {
                                intVal |= 0xFFFF0000;
                            }

                            float unitVal = (float)intVal/(0x7FF0);

                            float voltageVal = unitVal*voltage;

                            int distanceMM = (int)(voltageVal/(4.6/5120));

                            //log sensor readings
                            blackbox.info(key+",{},{},{}V,{}mm", hexVal, intVal,voltageVal, distanceMM);

                            publishOne(key, hexVal);

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
