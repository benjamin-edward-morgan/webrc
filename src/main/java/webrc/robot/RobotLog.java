package webrc.robot;

public class RobotLog {
	
	Object object;

	private RobotLog(Object object) {
		this.object=object;
	}
	
	public static RobotLog getLog(Object o)
	{
		return new RobotLog(o);
	}
	
	public void log(String s)
	{
		System.out.println(object.toString() + "\n" + s);
	}
	
	public void log(Exception e)
	{
		System.out.println(object.toString());
		e.printStackTrace(System.out);
	}
}
