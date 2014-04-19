package webrc.robot.control;

/**
 * @author benjaminmorgan
 *         Date: 4/17/14
 */
public class InterpolationTransformControl extends Control {


    private float maxVelocity = 10.0f; //in units/sec
    private float maxAcceleration = 1.0f; //in units/(sec^2)
    private int updatePeriod = 100; //ms

    private float goal = 0.0f; //the end position (where we have zero velocity)
    private float x = 0.0f; //position
    private float v = 0.0f; //velocity
    private float a = 0.0f; //acceleration

    Control innerController = null;





    public InterpolationTransformControl() {
    }

    public void setMaxAcceleration(float macAcceleration) {
        this.maxAcceleration = macAcceleration;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public void setInnerController(Control innerController) {
        this.innerController = innerController;
    }

    @Override
    public void set(Object value) {
        //TODO:
    }
}
