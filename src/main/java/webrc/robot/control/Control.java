package webrc.robot.control;

import webrc.RobotLog;

public abstract class Control {

	RobotLog log = RobotLog.getLog(this);
	
	/**
	 * changes some value (a motor or something)
	 * @param value
	 */
	public abstract void set(Object value);
	
}
