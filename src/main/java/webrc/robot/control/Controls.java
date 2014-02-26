package webrc.robot.control;

import webrc.robot.RobotLog;
import webrc.robot.notifier.Subscriber;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * A controller contains one or more controls and dispatches changes to them
 * 
 * @author benjaminmorgan
 * 
 */
public class Controls extends Subscriber {

	RobotLog log = RobotLog.getLog(this);

	private Map<String, Control<?>> controls = null;

    public void setControls(Map<String, Control<?>> controls) {
        this.controls = controls;
    }

    @PostConstruct
    public void init()
    {
        //subscribe to each control based on the name in the autowired map
        subscribe(controls.keySet());
    }

	public void notify(Map<String, Object> values) {
		if (values != null)
			for (Object key : values.keySet()) {

				if (controls.containsKey(key)) {
					Object value = values.get(key);

					try {
						Control control = controls.get(key);
						control.set(value);
					} catch (Exception e) {
						// class cast exception possible here
						log.log("Could not set control value");
						log.log(e);
					}
				}
			}
	}
}
