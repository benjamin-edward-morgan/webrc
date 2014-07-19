package webrc.robot.sensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.springframework.beans.factory.annotation.Autowired;
import webrc.robot.RobotProperties;
import webrc.robot.util.I2C;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bemorgan on 7/18/14.
 */
public class ADXL345Sensor extends Sensor{

    private static byte DEVID = 0x00;
    private static byte THRESH_TAP = 0x1D;
    private static byte OFSX = 0x1E;
    private static byte OFSY = 0x1F;
    private static byte OFSZ = 0x20;
    private static byte DUR = 0x21;
    private static byte Latent = 0x22;
    private static byte Window = 0x23;
    private static byte THRESH_ACT = 0x24;
    private static byte THRESH_INACT = 0x25;
    private static byte TIME_INACT = 0x26;
    private static byte ACT_INACT_CTL = 0x27;
    private static byte THRESH_FF = 0x28;
    private static byte TIME_FF = 0x29;
    private static byte TAP_AXES = 0x2A;
    private static byte ACT_TAP_STATUS = 0x2B;
    private static byte BW_RATE = 0x2C;
    private static byte POWER_CTL = 0x2D;
    private static byte INT_ENABLE = 0x2E;
    private static byte INT_MAP = 0x2F;
    private static byte INT_SOURCE = 0x30;
    private static byte DATA_FORMAT = 0x31;
    private static byte DATAX0 = 0x32;
    private static byte DATAX1 = 0x33;
    private static byte DATAY0 = 0x34;
    private static byte DATAY1 = 0x35;
    private static byte DATAZ0 = 0x36;
    private static byte DATAZ1 = 0x37;
    private static byte FIFO_CTL = 0x38;
    private static byte FIFO_STATUS = 0x39;

    @Autowired
    RobotProperties robotProperties;

    @Autowired
    I2C i2c;

    private int bus;
    private int device;
    private int updatePeriod = 100; //default to .1 second

    private String key = "accelerometer";

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
        if (!robotProperties.isTestMode()) {
            I2CBus i2cBus = i2c.geti2cBus(bus);
            if (i2c != null) {

                try {
                    dev = i2cBus.getDevice(device);


                    //turn on the accelerometer, place it in 100hz mode

                    byte[] bytes = new byte[]{Byte.parseByte("00001000",2)};
                    i2c.writeBytesToRegister(dev, bytes, POWER_CTL);


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

                        byte[] bytes = new byte[6];
                        try {
                            dev.read(DATAX0, bytes, 0, 6);

                            int x = ((bytes[0]&0xff) | (bytes[1])<<8);
                            int y = ((bytes[2]&0xff) | (bytes[3])<<8);
                            int z = ((bytes[4]&0xff) | (bytes[5])<<8);


                            Map<String, Object> values = new HashMap<String, Object>();
                            values.put(key+"_x",x*lsb);
                            values.put(key+"_y",y*lsb);
                            values.put(key+"_z",z*lsb);

                            //log sensor readings
                            for(String key : values.keySet()) {
                                blackbox.info(key+",{}", values.get(key));
                            }

                            publish(values);

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
        t.setName("ADXL345 Polling Thread: " + key);
        t.start();
    }



}
