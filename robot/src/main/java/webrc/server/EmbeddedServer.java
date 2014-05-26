package webrc.server;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 5/25/14
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmbeddedServer {

    public EmbeddedServer() {

    }

    @PostConstruct
    public void init() throws Exception {

// Configure Jetty
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

// Configure Cometd
        CometdServlet servlet = new CometdServlet();
        context.addServlet(new ServletHolder(servlet), "/bayeux");
// Start Jetty
        //server.start(context);


// Create a channel for our Java server to publish on
        BayeuxServer bayeux = servlet.getBayeux();
        String channelName = "/data";
        bayeux.createIfAbsent(channelName, new ServerChannel.Initializer()
        {
            public void configureChannel(ConfigurableServerChannel channel)
            {
                channel.setPersistent(true);
            }
        });
        final ServerChannel channel = bayeux.getChannel(channelName);



// publish some random data
        byte [] bytes = new byte[40];
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)(Math.random() * 255);
        }

        BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        final String data = base64Encoder.encode(bytes);

        final Map<String, String> firstData = new HashMap<>();
        firstData.put("a", "b");
        firstData.put("something", "val");

// Maps are converted to JavaScript objects
        final Map<String, String> secondData = new HashMap<>();
        secondData.put("base64data", data);


//        exec.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                if (Math.random() < 0.5) {
//                    channel.publish(null, firstData, "1");
//                } else {
//                    channel.publish(null, secondData , "2");
//                }
//            }
//        }, 1, 1, TimeUnit.SECONDS);

// Keep running until the server dies
        server.join();
    }
}
