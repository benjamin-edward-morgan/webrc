package webrc.robot.control;

import webrc.robot.util.Conversion;

/**
 * @author B.E. Morgan
 * @since 12/6/14
 */
public class HBridgeControl extends Control
{

    Control innerController1 = null;
    Control innerController2 = null;

    public HBridgeControl() {

    }

    public void setInnerController1(Control innerController1) {
        this.innerController1 = innerController1;
    }

    public void setInnerController2(Control innerController2) {
        this.innerController2 = innerController2;
    }

    @Override
    public void set(String key, Object value) {
        float conversion = Conversion.toFloat(value);
        if(conversion < 0) conversion = 0;
        else if(conversion > 1) conversion = 1;

        if(conversion >= 0.5) {
            //controller 1 off
            innerController1.set(key+".HBridge-A", 0);

            //controller 2 on
            innerController2.set(key+".HBridge-B", (conversion-0.5)*2);
        } else {
            //controller 2 off
            innerController2.set(key+".HBridge-B", 0);

            //controller 1 on
            innerController1.set(key+".HBridge-A", 1-conversion*2);
        }
    }
}
