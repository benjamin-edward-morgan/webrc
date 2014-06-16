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
import webrc.robot.messaging.Pubscriber;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 5/25/14
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmbeddedServer extends Pubscriber{

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

        ServletHolder holder = context.addServlet(CometdServlet.class, "/bayeux/*");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resource_handler, context});
        server.setHandler(handlers);


        server.start();

        CometdServlet servlet = (CometdServlet) holder.getServlet();


        BayeuxServer bayeux = servlet.getBayeux();

        String channelName = "/service/data";
        bayeux.createChannelIfAbsent(channelName, new ServerChannel.Initializer() {
            @Override
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                configurableServerChannel.setPersistent(true);
            }
        });
        final ServerChannel channel = bayeux.getChannel(channelName);

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask(){
//            @Override
//            public void run() {
//
//                Map<String, Double> data = new HashMap<String,Double>();
//                data.put("random", Math.random() * 100);
//                channel.publish(null, data);
//
//            }
//        }, 5000, 500);


        channel.addListener(new ServerChannel.MessageListener(){
            @Override
            public boolean onMessage(ServerSession serverSession, ServerChannel serverChannel, ServerMessage.Mutable mutable) {

                log.trace("Received: " + mutable.getData() + " on channel " + serverChannel.getId());

                publish(mutable.getDataAsMap());

                return false;

            }
        });

        //server.join();

    }

    @Override
    protected void notify(Map<String, Object> values) {
        //TODO: publish new values to cometd
    }
}
