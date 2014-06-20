package webrc.robot.control;

import webrc.robot.util.Conversion;

/**
 * @author benjaminmorgan
 *         Date: 6/16/14
 *
 *         maps [-maxValue,maxValue] -> [-maxValue,maxValue]
 *         using a cubic function
 *         so that the slope is smaller close to zero and
 *         larger closer to the edges
 *
 *         values are not clamped
 */
public class CubicTransformControl extends Control {

    private Control innerControl;
    private float maxValue;

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setInnerControl(Control innerControl) {
        this.innerControl = innerControl;
    }

    @Override
    public void set(String key, Object value) {
        key += ".cubicTransform";
        Float floatVal = Conversion.toFloat(value);

        //transform
        floatVal = (float)(Math.pow(floatVal, 3.0)/Math.pow(maxValue, 2));
        if(floatVal > maxValue) floatVal = maxValue;
        else if(floatVal < -maxValue) floatVal = -maxValue;
        blackbox.info(key+",{}", floatVal);

        innerControl.set(key, floatVal);
    }
}
