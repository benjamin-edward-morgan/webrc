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
	
	Control innerController = null;
	
	public LinearTransformControl(float inputMin, float inputMax, float outputMin, float outputMax, Control innerController) {
		m = (outputMax-outputMin)/(inputMax-inputMin);
		b = outputMin - inputMin*m;
		this.innerController = innerController;

        log.log("created linear transform around: " + innerController);
	}
	
	public float transform(float value)
	{
		return m*value+b;
	}
	
	@Override
    public void set(Object value)
    {
        set(Conversion.toFloat(value));
    }

	private void set(Float value) {
		
		value = transform(value);
		innerController.set(value);
    }

}
