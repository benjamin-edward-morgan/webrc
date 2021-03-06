package webrc.robot.util;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webrc.robot.RobotProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class I2C {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
            log.error("Error getting I2C bus: " + busId, e);
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
//        log.debug(dev.toString() + " writing raw bytes:\n" + sb.toString());

        if (!robotProperties.isTestMode()) {
            try {
                dev.write(bytes, 0, bytes.length);
            } catch (IOException e) {
                log.error("Error writing to I2C Device", e);
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

//        log.debug("writing " + sb.toString() + " to " + register);

        if (!robotProperties.isTestMode()) {
            try {
                dev.write(register, bytes, 0, bytes.length);
            } catch (IOException e) {
                log.error("Error writing to I2C Device", e);
            }
        }
    }

}
