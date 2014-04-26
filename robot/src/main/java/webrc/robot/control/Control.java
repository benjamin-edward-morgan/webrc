package webrc.robot.control;

import webrc.WebRcLog;

public abstract class Control {

	WebRcLog log = WebRcLog.getLog(this);
	
	/**
	 * changes some value (a motor or something)
	 * @param value
	 */
    public abstract void set(String key, Object value);
	
}
