package webrc.robot.notifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import webrc.server.Sleeper;

public class SocketNotifier extends AbstractNotifier {

	String host = null;
	int port = 0;

	public SocketNotifier(String host, int port, Map<String, Object> defaults/*, String... additionalParams*/) {
		super(defaults);
		this.host = host;
		this.port = port;

		begin();
	}

	private void begin() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (;;) {
					try {
						Socket s = new Socket(host, port);
						//s.setKeepAlive(true);
						s.setTcpNoDelay(true);

						InputStream is = s.getInputStream();

						for (;;) {
							Map<String, Object> values = JsonToMap(streamToString(is));

							//TODO: sleep by topic
							//Sleeper.sleep(null, 20000);
							if(s.isClosed() || values == null)
							{
								System.out.println("socket closed");
								break;

							}
							
							update(values);
						}

					} catch (UnknownHostException e) {
						e.printStackTrace();

					} catch (IOException e) {
						e.printStackTrace();

					}
					
					System.out.println("attemptng reconnection in 10 seconds...");
					sleep(10000);

					
				}
			}
		});

		t.setDaemon(false);
		t.setName("socket notifier thread");
		t.start();

	}

	public static Map<String, Object> JsonToMap(String json) {
		
		//todo: replace with something cooler
		json = json.trim();
		System.out.println("json: " + json);
		if (json.startsWith("{") && json.endsWith("}")) {
			Map<String, Object> map = new HashMap<String, Object>();

			json = json.substring(1, json.length() - 1);
			String[] params = json.split(",");
			for (String param : params) {
				String[] ab = param.split("=");
				if (ab.length == 2) {
					String a = ab[0];

					Object b = ab[1];

					try {
						b = Float.parseFloat(ab[1].trim());
					} catch (Throwable t) {
						
						b = ab[1];
					}

					map.put(a, b);
				}
			}
			return map;
		}

		return null;
	}

	public static void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String streamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\n");
		return s.hasNext() ? s.next() : "";
	}

}
