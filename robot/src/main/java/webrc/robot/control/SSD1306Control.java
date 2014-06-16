package webrc.robot.control;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import org.springframework.beans.factory.annotation.Autowired;
import webrc.robot.RobotProperties;
import webrc.robot.domain.ImageOverlay;
import webrc.robot.util.I2C;
import webrc.robot.util.RaspiConstants;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SSD1306Control extends Control {

    I2CDevice dev;
    GpioPinDigitalOutput pin;

    @Autowired
    RobotProperties robotProperties;

    @Autowired
    I2C i2c;


    public int bus;
    public int device;
    public int resetPin;

    //copy of display's memory
    private byte[] pixels = new byte[128 * 64 / 8];

    private MaskType mask = MaskType.ATOP;

    private enum MaskType {
        XOR,
        OR,
        AND,
        ATOP
    }

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

                /**Boilerplate code to activate screen**/

                // provision gpio pin #01 as an output pin and toggle
                pin = gpio.provisionDigitalOutputPin(RaspiConstants.getPin(resetPin));
                pin.setState(PinState.HIGH);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //ok
                }

                pin.setState(PinState.LOW);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //okm
                }

                pin.setState(PinState.HIGH);


                //displayoff
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xAE});

                //set display-clock-div
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xD5, (byte) 0x80});

                //set multiplex
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0XA8, (byte) 0x3F});

                //set display offset
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xD3, (byte) 0x00});

                //line #0
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0x40});

                //**charge pump
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x8D, (byte)0x10}); //ext vcc
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0x8D, (byte) 0x14});

                //segremap
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xA1});

                //set com output scan direction
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xC8});

                //set com pins
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xDA, (byte) 0x12});

                //**set contrast
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x81, (byte)0x9F}); //ext vcc
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0x81, (byte) 0xCF});

                //**set precharge
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xD9, (byte)0x22}); //ext vcc
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xD9, (byte) 0xF1});

                //set vcomh deselect level ?
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xDB, (byte) 0x40});

                //"entire" display on
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xA4});

                //normal display (not inverse)
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xA6});

                //clear screen?

                //display on
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0xAF});

                //memory mode: vertical
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x20, (byte)0x01});

                //horizontal
                i2c.writeBytes(dev, new byte[]{(byte) 0x00, (byte) 0x20, (byte) 0x00});

                //paging
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x20, (byte)0x10});

                //**external/internal vcc ?
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0x9F}); //ext vcc
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xCF});

                //all on
                //i2c.writeBytes(dev, new byte[]{(byte)0x00, (byte)0xA5});

            } catch (IOException e) {
                log.error("Error: could not get device " + device + " from bus " + bus, e);
            }
        }
    }

    public void blitImage(BufferedImage img, int x, int y) {
        int w = img.getWidth();
        int h = img.getHeight();

        for (int page = Math.max(0, y / 8); page < Math.min(8, Math.ceil((y + h) / 8.0)); page++) {
            for (int seg = Math.max(0, x); seg < Math.min(128, x + w); seg++) {
                byte i = getImageByte(img, seg - x, page * 8 - y);
                byte m = getMaskByte(img, seg - x, page * 8 - y);
                pixels[page * 128 + seg] = (byte) ((pixels[page * 128 + seg] & (m ^ 0xff)) | (mask(pixels[page * 128 + seg], i) & m));
            }
        }

        if(!robotProperties.isTestMode()) {
            //TODO: don't write the whole goddamn screen if you don't have to.
            byte[] data = new byte[pixels.length + 1];
            data[0] = 0x40;
            for (int i = 0; i < pixels.length; i++)
                data[i + 1] = pixels[i];
            i2c.writeBytes(dev, data);
        }
    }

    public byte mask(byte original, byte overlay) {
        switch (mask) {
            case XOR:
                return (byte) (original ^ overlay);
            case AND:
                return (byte) (original & overlay);
            case OR:
                return (byte) (original | overlay);
            case ATOP:
                return overlay;
        }
        return overlay;
    }

    public byte getMaskByte(BufferedImage img, int x, int y) {
        int b = 0x00;
        for (int i = y + 8 - 1; i >= y; i--) {
            b = b << 1;
            if (i >= 0 && i < img.getHeight()) {
                b |= 0x01;
            }
        }
        return (byte) b;
    }

    public byte getImageByte(BufferedImage img, int x, int y) {
        int b = 0x00;
        for (int i = y + 8 - 1; i >= y; i--) {
            b = b << 1;
            if (i >= 0 && i < img.getHeight()) {
                b |= convertPixel(img.getRGB(x, i));
            }
        }
        return (byte) b;
    }

    public int convertPixel(int rgb) {
        return (((rgb >> 0) & 0xff) > 0 ||
                ((rgb >> 8) & 0xff) > 0 ||
                ((rgb >> 16) & 0xff) > 0
                ? 0x01 :
                0x00
        );
    }

    @Override
    public void set(String key, Object value) {

        if (value instanceof ImageOverlay) {
            ImageOverlay overlay = (ImageOverlay) value;
            blitImage(overlay.image, overlay.x, overlay.y);
        } else {
            log.error("expected ImageOverlay but got " + value.getClass());
        }

    }
}
