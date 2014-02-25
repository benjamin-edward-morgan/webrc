package webrc.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RCMessageService {

	public static Map<String, Set<RCMessageHandler>> messageHandlers = new HashMap<String, Set<RCMessageHandler>>();

	public static synchronized void addMessageHandler(String topic, RCMessageHandler handler) {
		if (messageHandlers.containsKey(topic)) {
			Set<RCMessageHandler> topicHandlers = messageHandlers.get(topic);
			topicHandlers.add(handler);
		} else {
			Set<RCMessageHandler> topicHandlers = new HashSet<RCMessageHandler>();
			topicHandlers.add(handler);
			messageHandlers.put(topic, topicHandlers);
		}
	}

	public static void removeMessageHandler(final String topic, RCMessageHandler handler) {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (messageHandlers.containsKey(topic)) {
					
					Set<RCMessageHandler> topicHandlers = messageHandlers.get(topic);
					topicHandlers.remove(topic);
					if (topicHandlers.isEmpty()) {
						messageHandlers.remove(topic);
					}
				}
			}
		});
		
		t.start();

	}

	public static synchronized void sendMessage(String topic, String message) {
		if (messageHandlers.containsKey(topic)) {
			Set<RCMessageHandler> topicHandlers = messageHandlers.get(topic);
			for (RCMessageHandler handler : topicHandlers) {
				handler.handleMessage(message);
			}
		}
	}

}
