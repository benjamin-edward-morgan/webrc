package webrc.robot.controller;

import webrc.WebRcLog;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

class SocketController extends Controller {

    WebRcLog log = WebRcLog.getLog(this);

    String host;
    int port;

    public SocketController() {
    }

    @PostConstruct
    private void init() {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Socket s = new Socket(host, port);
                        //s.setKeepAlive(true);
                        s.setTcpNoDelay(true);

                        InputStream is = s.getInputStream();

                        for (; ; ) {
                            Map<String, Object> values = JsonToMap(streamToString(is));

                            log.log("recieved:" + values.toString());

                            if (s.isClosed() || values == null) {
                                log.log("socket closed");
                                break;

                            }

                            publish(values);
                        }

                    } catch (UnknownHostException e) {
                        log.log(e);

                    } catch (IOException e) {
                        log.log(e);

                    }

                    log.log("attemptng reconnection in 10 seconds...");
                    sleep(10000);


                }
            }
        });

        t.setDaemon(false);
        t.setName("socket messaging thread");
        t.start();

    }

    public static Map<String, Object> JsonToMap(String json) {

        //todo: replace with something cooler
        json = json.trim();
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
            //ok
        }
    }

    public static String streamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\n");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void notify(Map<String, Object> values) {
        //TODO: relay sensor values to l
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
