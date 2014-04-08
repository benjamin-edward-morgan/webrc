package webrc.robot.util;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 3/29/14
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class RaspiConstants {

    public static Pin getPin(int pinNumber)
    {
        switch(pinNumber)
        {
            case 0:
                return RaspiPin.GPIO_00;
            case 1:
                return RaspiPin.GPIO_01;
            case 2:
                return RaspiPin.GPIO_02;
            case 3:
                return RaspiPin.GPIO_03;
            case 4:
                return RaspiPin.GPIO_04;
            case 5:
                return RaspiPin.GPIO_05;
            case 6:
                return RaspiPin.GPIO_06;
            case 7:
                return RaspiPin.GPIO_07;
            case 8:
                return RaspiPin.GPIO_08;
            case 9:
                return RaspiPin.GPIO_09;
            case 10:
                return RaspiPin.GPIO_10;
            case 11:
                return RaspiPin.GPIO_11;
            case 12:
                return RaspiPin.GPIO_12;
            case 13:
                return RaspiPin.GPIO_13;
            case 14:
                return RaspiPin.GPIO_14;
            case 15:
                return RaspiPin.GPIO_15;
            case 16:
                return RaspiPin.GPIO_16;
            case 17:
                return RaspiPin.GPIO_17;
            case 18:
                return RaspiPin.GPIO_18;
            case 19:
                return RaspiPin.GPIO_19;
            case 20:
                return RaspiPin.GPIO_20;
            default:
                return null;

        }

    }

    public static int getBus(int busNumber)
    {
         switch(busNumber)
         {
             case 0:
                 return I2CBus.BUS_0;
             case 1:
                 return I2CBus.BUS_1;
             default:
                 return -1;
         }
    }

}
