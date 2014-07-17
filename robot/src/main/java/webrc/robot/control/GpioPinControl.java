package webrc.robot.control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CBus;
import org.springframework.beans.factory.annotation.Autowired;
import webrc.robot.RobotProperties;
import webrc.robot.messaging.MessageService;
import webrc.robot.util.Conversion;
import webrc.robot.util.I2C;
import webrc.robot.util.RaspiConstants;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bemorgan on 7/16/14.
 */
public class GpioPinControl extends Control {


    @Autowired
    RobotProperties robotProperties;

    @Autowired
    MessageService messageService;

    @Autowired
    I2C i2c;

    int pin;
    int bus;
    int defaultStatus = 0;

    GpioPinDigitalOutput gpioPin;


    @PostConstruct
    public void init() {
        if (!robotProperties.isTestMode()) {
            I2CBus i2cbus = i2c.geti2cBus(bus);

            GpioController gpio = GpioFactory.getInstance();

            gpioPin = gpio.provisionDigitalOutputPin(RaspiConstants.getPin(pin));

            if(defaultStatus <=0 )
                gpioPin.setState(PinState.LOW);
            else
                gpioPin.setState(PinState.HIGH);
        }
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }


    @Override
    public void set(String key, Object value) {
        float floatVal = Conversion.toFloat(value);

        if(! robotProperties.isTestMode()) {

            if (floatVal > 0)
                gpioPin.setState(PinState.HIGH);
            else
                gpioPin.setState(PinState.LOW);

        }
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(key + "_status", floatVal > 0 ? 1 : 0);
        messageService.publish(values);

    }
}