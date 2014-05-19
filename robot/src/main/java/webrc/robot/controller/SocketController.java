package webrc.robot.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.JsonGenerator;

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

    ObjectMapper objMapper = new ObjectMapper();

    public SocketController() {
//        objMapper.disable(DeserializationConfig.Feature.AUTO_CLOSE_TARGET);
//        objMapper.disable(SerializationConfig.Feature.CLOSE_CLOSEABLE);
        objMapper = objMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

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
                            String received = streamToString(is);
//                            Map<String, Object> values = JsonToMap(received);

                            HashMap<String, Object> rootNode = objMapper.readValue(received, HashMap.class);

                            if(rootNode != null) {

                                log.log("recieved:" + rootNode.toString());

                                if (s.isClosed() || rootNode == null) {
                                    log.log("socket closed");
                                    break;

                                }

                                publish(rootNode);
                            }      else {
                                log.log("**VALUES WAS NULL**");
                            }

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
