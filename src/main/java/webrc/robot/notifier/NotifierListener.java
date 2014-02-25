package webrc.robot.notifier;

import java.util.Map;

public interface NotifierListener {

	public void changed(Map<String, Object> values);
	
}
