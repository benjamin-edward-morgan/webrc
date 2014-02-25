package webrc.robot.control;

/**
 * Applies a 1D linear transform mapping 
 * [iMin,iMax] -> [oMin,oMax]
 * @author benjaminmorgan
 *
 */
public class LinearTransformControl extends Control<Float> {
	
	//linear transform parameters
	float m = 0;
	float b = 0;
	
	Control<Float> innerController = null;
	
	public LinearTransformControl(float inputMin, float inputMax, float outputMin, float outputMax, Control<Float> innerController) {
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
	public void set(Float value) {
		
		value = transform(value);
		innerController.set(value);
	}

}
