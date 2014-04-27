package webrc.robot.control;

/**
 * @author benjaminmorgan
 *         Date: 4/17/14
 */
public class InterpolationTransformControl extends Control {

    //properties (strictly positive)
    private float maxVelocity = 75.0f; //in units/sec
    private float maxAcceleration = 150f; //in units/(sec^2)
    private int updatePeriod = 5; //ms

    Control innerController = null;


    //state variables
    private float goal = 0.0f; //the end position (where we have zero velocity)
    private float x = 0.0f; //position
    private float v = 0.0f; //velocity


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

    public void begin() {

        //TODO: save this thread and stop it when we get a goal change
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(x != goal) {

                    float frameTime = getFrameTime();

                    //check that we don't exceed the speed limit
                    if(v > maxVelocity)
                        v = maxVelocity;
                    else if(v < -maxVelocity)
                        v = -maxVelocity;

                    float dx = v*frameTime;

                    //TODO: i feel like the second part shoud be '<'
                    if((goal-x)*(goal-(x+dx)) < 0 && Math.abs(v) < maxAcceleration*frameTime) {
                        //if we're about to cross the goal and the velocity is small,
                        //just go ahead and set X to the goal and velocity to zero
                        //and be done with it.
                        x=goal;
                        v=0;

                        System.out.println(x + "," + v);
                        break;

                    } else {
                        //move based on current velocity
                        x+=dx;

                        //TODO: write to inner control
                        System.out.println(x + "," + v);
                    }

                    //accelerate/decelerate
                    if(v != 0) {
                        //if we're moving

                        float displacement = goal-x;

                        if(displacement * v < 0) {
                            //we're going the wrong way!
                            //so we slow down
                            v += (v < 0 ? maxAcceleration : -maxAcceleration)*frameTime;
                        } else {
                            //we're going the right way
                            if(Math.abs(getStoppingDistance() - v*frameTime) >= Math.abs(displacement)) {
                                //do we need to hit the brakes?
                                v += (v < 0 ? maxAcceleration : -maxAcceleration)*frameTime;
                            } else {
                                //keep accelerating!
                                v += (v < 0 ? -maxAcceleration : maxAcceleration)*frameTime;
                            }
                        }

                    } else {
                        //if we're not moving should we?
                        if(x < goal)
                            v=maxAcceleration*frameTime;
                        else if(x > goal)
                            v=-maxAcceleration*frameTime;
                    }

                    //wait for a few milliseconds
                    try {
                        Thread.sleep(updatePeriod);
                    } catch (InterruptedException e) {
                        //ok
                    }

                }
            }
        });

        t.setDaemon(false);
        t.setName("Interpolation Thread");
        t.start();
    }



    @Override
    public void set(String key, Object value) {

    }

    //number of seconds in 1 frame
    public float getFrameTime() {
        return (float)(updatePeriod/1000.0);
    }

    //distance required to stop assuming we
    //begin decelerating on the next step
    //traveling at the current velocity
    public float getStoppingDistance() {
        //TODO: closed form
        float dx = 0;
        float v0 = v;

        float frameTime=getFrameTime();

        if(v0>0) {
            while(v0>0) {
                v0-=maxAcceleration*frameTime;
                if(v0>0)
                    dx+=v0*frameTime;
            }
        } else if(v0<0) {
            while(v0<0) {
                v0+=maxAcceleration*frameTime;
                if(v0<0)
                    dx+=v0*frameTime;
            }
        }

        return dx;
    }



    public static void main(String[] args)
    {
        InterpolationTransformControl itc = new InterpolationTransformControl();

        itc.goal=100;

        itc.begin();

    }
}
