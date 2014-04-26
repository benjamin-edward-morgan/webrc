package webrc.robot.control;

import webrc.WebRcLog;
import webrc.messaging.Pubscriber;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * A controller contains one or more controls and dispatches changes to them
 *
 * @author benjaminmorgan
 */
public class Controls extends Pubscriber {

    WebRcLog log = WebRcLog.getLog(this);

    private Map<String, Control> controls = null;

    public void setControls(Map<String, Control> controls) {
        this.controls = controls;
        log.log("configured controls with keys: " + controls.keySet().toString());

    }

    @PostConstruct
    public void init() {
        //subscribe to each control based on the name in the autowired map
        subscribe(controls.keySet());
        log.log("subscribed to keys: " + controls.keySet().toString());
    }

    public void notify(Map<String, Object> values) {
        if (values != null)
            for (String key : values.keySet()) {

                if (controls.containsKey(key)) {
                    Object value = values.get(key);

                    try {
                        Control control = controls.get(key);
                        control.set(key, value);
                    } catch (Exception e) {
                        // class cast exception possible here
                        log.log("Could not set control value");
                        log.log(e);
                    }
                }

            }
    }
}
