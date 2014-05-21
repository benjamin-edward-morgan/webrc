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
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 5/15/14
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class INA219Sensor extends Sensor{

    //indexes of two-byte registers
    public static final int CONFIG = 0x00;
    public static final int SHUNT_VOLTAGE = 0x01;
    public static final int BUS_VOLTAGE = 0x02;
    public static final int POWER = 0x03;
    public static final int CURRENT = 0x04;
    public static final int CALIBRATION = 0x05;

    @Autowired
    RobotProperties robotProperties;

    @Autowired
    I2C i2c;

    I2CDevice dev = null;
    public int bus;
    public int device;
    public int updatePeriod = 1000; //default to 1 second

    private int calibration = 2730;
    private double shuntLSB = 0.00001;
    private double busLSB = 0.004;
    private double currentLSB = 0.00015;
    private double powerLSB = 0.003;

    public String key = "current sensor";


    public void setBus(int bus) {
        this.bus = bus;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setUpdatePeriod(int ms) {
        this.updatePeriod=ms;
    }

    public void setKey(String k) {
        this.key=k;
    }

    public INA219Sensor() {
    }

    @PostConstruct
    public void init() {
        log.log("Created PCA control!!");

        if (!robotProperties.isTestMode()) {
            I2CBus i2cBus = i2c.geti2cBus(bus);
            if (i2c != null) {

                try {
                    dev = i2cBus.getDevice(device);

                    //set calibration register
                    byte[] cal = new byte[2];
                    cal[1]=(byte)((calibration>>8)&0xff);
                    cal[0]=(byte)(calibration&0xff);
                    dev.write(CALIBRATION, cal, 0, 2);

                } catch (IOException e) {
                    log.log(e);
                }


            }
        }

        begin();
    }

    private void begin() {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {

                for(;;) {

                    if(!robotProperties.isTestMode()) {

                        byte[] bytes = new byte[2];

                        try {
                            dev.read(SHUNT_VOLTAGE, bytes, 0, 2);
                            int shuntVoltage = ((bytes[0]<<8)&0xff00) | (bytes[1]&0xff);

                            dev.read(BUS_VOLTAGE, bytes, 0, 2);
                            int busVoltage = ((bytes[0]<<8)&0xff00) | (bytes[1]&0xff);

                            dev.read(POWER, bytes, 0, 2);
                            long power = ((bytes[0]<<8)&0xff00) | (bytes[1]&0xff);

                            dev.read(CURRENT, bytes, 0, 2);
                            long current = ((bytes[0]<<8)&0xff00) | (bytes[1]&0xff);

                            //TODO: notify

                            log.log(key + " shunt: " + shuntVoltage*shuntLSB + "V (" + Integer.toHexString(shuntVoltage) + ")");
                            log.log(key + " bus: " + (busVoltage>>3)*busLSB + "V (" + Integer.toHexString(busVoltage) + ")");
//                            log.log(key + " power: " + power*powerLSB + "W (" + Long.toHexString(power) + ")");
//                            log.log(key + " current: " + current*currentLSB + "A (" + Long.toHexString(current) + ")");

                            double v = busVoltage*busLSB;
                            double c = shuntVoltage*shuntLSB/0.1;
                            double p = c*v;


                            Map<String, Object> values = new HashMap<String, Object>();
                            values.put(key+".voltage", v + "(V)");
                            values.put(key+".current", c + "(A)");
                            values.put(key+".power", p + "(W)");



                        } catch (IOException e) {
                            log.log(e);
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
        t.setName("INA219 Polling Thread: " + key);
        t.start();
    }

}
