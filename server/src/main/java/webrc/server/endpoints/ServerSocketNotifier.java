package webrc.server.endpoints;

import webrc.messaging.Pubscriber;
import webrc.util.Sleeper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Accepts socket connections on a specified port and pushes json data packets
 * to clients immediately after connection, then periodically and immediately
 * after notified that parameters have changed
 *
 * @author benjaminmorgan
 */
public class ServerSocketNotifier extends Pubscriber {

    public void setPort(int port) {
        this.port = port;
    }

    int port = 8081;

    //TODO: map car serial to key-value map
    Map<String, Object> values = null;

    final static int pollTimeout = 5000; // ms

    public ServerSocketNotifier() {

        values = new HashMap<String, Object>();
        this.port = port;

        new Thread(new Runnable() {

            @Override
            public void run() {
                acceptConnections();
            }
        }).start();
    }

    @Override
    protected void notify(Map<String, Object> values) {
        this.values.putAll(values);
        Sleeper.interruptSleepingThreads("");
    }

    private void acceptConnections() {

        try {
            ServerSocket ss = new ServerSocket(port);

            for (; ; ) {

                try {

                    Socket socket = ss.accept();

                    System.out.println("Connection Accepted!");
                    final OutputStream outputStream = socket.getOutputStream();

                    // TODO: read hashed car serial number
                    // socket.getInputStream();

                    this.subscribe("");


                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                for (; ; ) {

                                    outputStream.write((toJson((Map<String,Object>)values.get("")) + "\n").getBytes());
                                    System.out.println(toJson(values));
                                    Sleeper.sleep("", pollTimeout);
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
        //TODO: replace with jackson
        if (map == null)
            return "{}";
        StringBuilder sb = new StringBuilder("{");
        Iterator<String> keyIter = map.keySet().iterator();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            sb.append(key + "=" + map.get(key) + (keyIter.hasNext() ? "," : ""));
        }
        sb.append('}');
        return sb.toString();
    }
}
