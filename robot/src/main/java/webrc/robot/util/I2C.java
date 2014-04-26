package webrc.robot.util;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webrc.WebRcLog;
import webrc.robot.RobotProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class I2C {

    WebRcLog log = WebRcLog.getLog(I2C.class);

    Map<Integer, I2CBus> busses = new HashMap<Integer, I2CBus>();

    @Autowired
    RobotProperties robotProperties;

    /**
     * @param busId I2CBus.BUS_0 or I2CBus.BUS_1
     * @return
     */
    public I2CBus geti2cBus(int busId) {
        if (busses.containsKey(busId))
            return busses.get(busId);
        try {
            I2CBus bus = I2CFactory.getInstance(RaspiConstants.getBus(busId));
            busses.put(busId, bus);
            return bus;
        } catch (IOException e) {
            log.log("Error getting I2C bus: " + busId);
            log.log(e);
        }
        return null;
    }

    public void writeBytes(I2CDevice dev, byte[] bytes) {

        // log writing bytes
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b);

            sb.append((hex.length() < 2 ? "0" : "") + Integer.toHexString(b & 0xff) + " ");
        }
        //log.log(dev.toString() + " writing raw bytes:\n" + sb.toString());

        if (!robotProperties.isTestMode()) {
            try {
                dev.write(bytes, 0, bytes.length);
            } catch (IOException e) {
                log.log("Error writing to I2C Device");
                log.log(e);
            }
        }
    }

    public void writeBytesToRegister(I2CDevice dev, byte[] bytes, byte register) {

        // log writing bytes
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b);

            sb.append((hex.length() < 2 ? "0" : "") + Integer.toHexString(b & 0xff) + " ");
        }
        log.log("Writing bytes to register:\n" + sb.toString() + " @ " + Integer.toHexString(register & 0xff) + ":" + dev.toString());

        if (!robotProperties.isTestMode()) {
            try {
                dev.write(register, bytes, 0, bytes.length);
            } catch (IOException e) {
                log.log("Error writing to I2C Device");
                log.log(e);
            }
        }
    }

}
