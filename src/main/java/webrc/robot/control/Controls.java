package webrc.robot.control;

import java.util.HashMap;
import java.util.Map;

import webrc.robot.RobotLog;
import webrc.robot.control.Control;
import webrc.robot.notifier.NotifierListener;

/**
 * A controller contains one or more controls and dispatches changes to them
 * 
 * @author benjaminmorgan
 * 
 */
public class Controls implements NotifierListener {

	RobotLog log = RobotLog.getLog(this);

	private Map<String, Control<?>> controls = new HashMap<String, Control<?>>();

	public void add(String key, Control<?> controller) {
		controls.put(key, controller);
	}

	public void changed(Map<String, Object> values) {
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
