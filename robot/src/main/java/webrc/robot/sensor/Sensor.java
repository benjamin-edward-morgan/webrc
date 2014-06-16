package webrc.robot.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webrc.robot.messaging.Pubscriber;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/25/14
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sensor extends Pubscriber {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void notify(Map<String, Object> values) {
        //Sensors are not notified
    }


}
