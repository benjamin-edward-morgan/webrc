package webrc.robot.notifier;

import java.util.Map;


public interface Notifier {
	
	public void addListener(NotifierListener listener);
	
	public void update(Map<String, Object> values);
}
