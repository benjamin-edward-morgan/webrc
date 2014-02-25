package webrc.robot.control;

import webrc.robot.RobotLog;

public abstract class Control<X extends Object> {

	RobotLog log = RobotLog.getLog(this);
	
	/**
	 * changes some value (a motor or something)
	 * @param value
	 */
	public abstract void set(X value);
	
}
