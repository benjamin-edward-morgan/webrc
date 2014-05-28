package webrc.server;

import org.cometd.bayeux.server.*;
import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

        Server server = new Server(8080);

        //static resource handler
        ResourceHandler resource_handler = new ResourceHandler() {
            @Override
            public Resource getResource(String path)
                    throws MalformedURLException {
                Resource resource = Resource.newClassPathResource(path);
                if (resource == null || !resource.exists()) {
                    resource = Resource.newClassPathResource("META-INF/resources" + path);
                }
                return resource;
            }
        };
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] { "static/rc.html" });
        resource_handler.setResourceBase("static");
        resource_handler.setDirectoriesListed(true);

        //cometd context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/webrc");

        // Configure Cometd
//        CometdServlet servlet = new CometdServlet();
//
//        System.out.println(servlet.getServletConfig());
//        System.out.println(servlet.getServletInfo());
//
//        context.addServlet(new ServletHolder(servlet), "/bayeux");
//
        ServletHolder holder = context.addServlet(CometdServlet.class, "/bayeux");
        context.addServlet(CometdServlet.class, "/bayeux/handshake");
        context.addServlet(CometdServlet.class, "/bayeux/connect");
        context.addServlet(CometdServlet.class, "/bayeux/");




        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resource_handler, context});
        server.setHandler(handlers);


        server.start();

        CometdServlet servlet = (CometdServlet) holder.getServlet();

        BayeuxServer bayeux = servlet.getBayeux();

        String channelName = "/data";
        bayeux.createChannelIfAbsent(channelName, new ServerChannel.Initializer() {
            @Override
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                configurableServerChannel.setPersistent(true);
            }
        });
        final ServerChannel channel = bayeux.getChannel(channelName);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {

                Map<String, Double> data = new HashMap<String,Double>();
                data.put("random", Math.random() * 100);
                channel.publish(null, data);

            }
        }, 0, 500);


        channel.addListener(new ServerChannel.MessageListener(){
            @Override
            public boolean onMessage(ServerSession serverSession, ServerChannel serverChannel, ServerMessage.Mutable mutable) {

                System.out.println("Received: " + mutable.getData());

                return false;

            }
        });

        server.join();

// Configure Jetty
//        Server server = new Server(8080);
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//
//// Configure Cometd
//        CometdServlet servlet = new CometdServlet();
//        context.addServlet(new ServletHolder(servlet), "/bayeux");
//// Start Jetty
//        server.start();


// Create a channel for our Java server to publish on
//        BayeuxServer bayeux = servlet.getBayeux();
//        String channelName = "/data";
//        bayeux.createIfAbsent(channelName, new ServerChannel.Initializer()
//        {
//            public void configureChannel(ConfigurableServerChannel channel)
//            {
//                channel.setPersistent(true);
//            }
//        });
//        final ServerChannel channel = bayeux.getChannel(channelName);
//
//
//
//// publish some random data
//        byte [] bytes = new byte[40];
//        for (int i = 0; i < bytes.length; ++i) {
//            bytes[i] = (byte)(Math.random() * 255);
//        }
//
//        BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
//        final String data = base64Encoder.encode(bytes);
//
//        final Map<String, String> firstData = new HashMap<>();
//        firstData.put("a", "b");
//        firstData.put("something", "val");
//
//// Maps are converted to JavaScript objects
//        final Map<String, String> secondData = new HashMap<>();
//        secondData.put("base64data", data);


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
      //  server.join();
    }
}
