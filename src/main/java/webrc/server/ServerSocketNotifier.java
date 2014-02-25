package webrc.server;

import java.awt.Container;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import webrc.robot.notifier.AbstractNotifier;

/**
 * Accepts socket connections on a specified port and pushes json data packets
 * to clients immediately after connection, then periodically and immediately
 * after notified that parameters have changed
 * 
 * @author benjaminmorgan
 * 
 */
public class ServerSocketNotifier {
	//
	// public static void main(String[] args) {
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("pan", 0);
	// values.put("tilt", 0);
	//
	// final ServerSocketNotifier ssn = new ServerSocketNotifier(8081, values);
	//
	//
	// JFrame jframe = new JFrame("sliders");
	//
	// final JSlider pan = new JSlider(-100,100,50);
	// pan.addChangeListener(new ChangeListener(){
	// @Override
	// public void stateChanged(ChangeEvent arg0) {
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("pan", pan.getValue());
	// ssn.update(values);
	// }});
	//
	// final JSlider tilt = new JSlider(-100,100,50);
	// tilt.addChangeListener(new ChangeListener(){
	// @Override
	// public void stateChanged(ChangeEvent arg0) {
	// Map<String, Object> values = new HashMap<String, Object>();
	// values.put("tilt", tilt.getValue());
	// ssn.update(values);
	// }});
	//
	// Container container = new Container();
	// container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	//
	// container.add(pan);
	// container.add(tilt);
	//
	// jframe.add(container);
	//
	// jframe.setVisible(true);
	//
	// jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// }

	private static ServerSocketNotifier SSN = null;

	public static ServerSocketNotifier getSSN() {
		if (SSN == null) {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("pan", 0);
			values.put("tilt", 0);

			SSN = new ServerSocketNotifier(8081, values);
		}

		return SSN;

	}

	
	
	int port = -1;

	Map<String, Object> values = null;

	final static int pollTimeout = 5000; // ms

	public ServerSocketNotifier(int port, Map<String, Object> defaults) {

		values = defaults;
		this.port = port;

		new Thread(new Runnable() {

			@Override
			public void run() {
				acceptConnections();
			}
		}).start();
	}

	public void update(Map<String, Object> values) {
		this.values.putAll(values);
		Sleeper.interruptSleepingThreads(null);
	}

	private void acceptConnections() {

		try {
			ServerSocket ss = new ServerSocket(8081);

			for (;;) {

				try {

					Socket socket = ss.accept();

					System.out.println("Connection Accepted!");
					final OutputStream outputStream = socket.getOutputStream();

					// TODO: read hashed car serial number
					// socket.getInputStream();

					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								for (;;) {

									outputStream.write((toJson(values) + "\n").getBytes());
									System.out.println(toJson(values));
									Sleeper.sleep("car1", pollTimeout);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}, "Socket Feeder");

					t.setDaemon(true);
					t.start();

				} catch (IOException e) {
					e.printStackTrace();

				}

			}

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public String toJson(Map<String, Object> map) {
		if (map == null)
			return "{}";
		StringBuilder sb = new StringBuilder("{");
		Iterator<String> keyIter = map.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			sb.append(key + "=" + map.get(key) + (keyIter.hasNext() ? "," : "}"));
		}
		return sb.toString();
	}

}
