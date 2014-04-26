package webrc.robot.control;

import webrc.util.Conversion;

/**
 * Applies a 1D linear transform mapping 
 * [iMin,iMax] -> [oMin,oMax]
 * @author benjaminmorgan
 *
 */
public class LinearTransformControl extends Control {
	
	//linear transform parameters
	float m = 0;
	float b = 0;

    float outputMin;
    float outputMax;
    boolean clamp = true;
	
	Control innerController = null;
	
	public LinearTransformControl(float inputMin, float inputMax, float outputMin, float outputMax, Control innerController) {
		m = (outputMax-outputMin)/(inputMax-inputMin);
		b = outputMin - inputMin*m;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
		this.innerController = innerController;

        log.log("created linear transform around: " + innerController);
	}
	
	public float transform(float value)
	{
		float y = m*value+b;

        if(clamp) {
            y = (y > outputMax ? outputMax : y);
            y = (y < outputMin ? outputMin : y);
        }

        return y;
	}
	
	@Override
    public void set(String key, Object value)
    {
        set(key, Conversion.toFloat(value));
    }

    private void set(String key, Float value) {
		
		value = transform(value);
		innerController.set(key, value);
    }

}
