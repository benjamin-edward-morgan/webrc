package webrc.robot.sensor;

import webrc.messaging.Pubscriber;
import webrc.RobotLog;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sensors extends Pubscriber {

    RobotLog log = RobotLog.getLog(this);

    private Map<String, Sensor> sensors = null;

    public void setSensors(Map<String, Sensor> sensors) {
        this.sensors = sensors;
    }


    @Override
    protected void notify(Map<String, Object> values) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
