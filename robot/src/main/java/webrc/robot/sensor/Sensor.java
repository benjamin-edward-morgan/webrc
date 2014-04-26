package webrc.robot.sensor;

import webrc.WebRcLog;
import webrc.messaging.Pubscriber;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sensor extends Pubscriber {

    WebRcLog log = WebRcLog.getLog(this);

    @Override
    protected void notify(Map<String, Object> values) {
        //Sensors are not notified
    }


}
