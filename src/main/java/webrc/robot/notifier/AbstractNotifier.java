package webrc.robot.notifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractNotifier implements Notifier{

	Set<NotifierListener> listeners = new HashSet<NotifierListener>();
	
	Map<String, Object> values = null;
	
	public AbstractNotifier(Map<String, Object> defaults)
	{
		this.values= defaults;
	}
	
	public synchronized void addListener(NotifierListener listener)
	{
		listener.changed(values);
		listeners.add(listener);
	}
	
	public synchronized void update(Map<String, Object> values)
	{
		this.values = values;
		for(NotifierListener listener : listeners)
			listener.changed(values);
	}
	
}
