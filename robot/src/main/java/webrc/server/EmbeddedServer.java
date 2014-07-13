package webrc.server;

import org.cometd.bayeux.server.*;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.CometdServlet;
import org.cometd.server.authorizer.GrantAuthorizer;
import org.cometd.websocket.server.WebSocketTransport;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
import webrc.robot.messaging.Pubscriber;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.*;

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

    //TODO: keep track of multiple users in a queue
    private ServerSession mainSession = null;

    private ServerChannel downChannel;

    private int port=8080;
    public void setPort(int port) {
        this.port = port;
    }

    //keys in 'relay' are relayed to the client
    public Set<String> relay;
    public void setRelay(Set<String> relay) {
        this.relay = relay;
    }


    @PostConstruct
    public void init() throws Exception {

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
        resource_handler.setWelcomeFiles(new String[]{"static/rc.html"});
        resource_handler.setResourceBase("static");

        //cometd context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/webrc");
        CometdServlet cometd = new CometdServlet();
        ServletHolder holder = new ServletHolder(cometd);
        context.addServlet(holder, "/bayeux/*");
        context.addFilter(CrossOriginFilter.class, "/bayeux/*", null);

        //configure and start the server
        Server server = new Server(port);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resource_handler, context});
        server.setHandler(handlers);
        server.start();

        BayeuxServerImpl bayeux = cometd.getBayeux();

        //enable websocket transport
        WebSocketTransport wsTransport = new WebSocketTransport(bayeux);
        wsTransport.init();
        bayeux.addTransport(wsTransport);
        bayeux.setAllowedTransports(new ArrayList<String>(bayeux.getKnownTransportNames()));

        //create 'up' channel (pi -> server)
        String upChannelName = "/data/up";
        bayeux.createChannelIfAbsent(upChannelName, new ServerChannel.Initializer() {
            @Override
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                configurableServerChannel.setPersistent(true);
                configurableServerChannel.addAuthorizer(GrantAuthorizer.GRANT_ALL);
            }
        });
        final ServerChannel upChannel = bayeux.getChannel(upChannelName);

        //create 'down' channel (server -> pi)
        String downChannelName = "/data/down";
        bayeux.createChannelIfAbsent(downChannelName, new ServerChannel.Initializer() {
            @Override
            public void configureChannel(ConfigurableServerChannel configurableServerChannel) {
                configurableServerChannel.setPersistent(true);
                configurableServerChannel.addAuthorizer(GrantAuthorizer.GRANT_ALL);
            }
        });
        downChannel = bayeux.getChannel(downChannelName);

        //send random data to the client
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask(){
//            @Override
//            public void run() {
//
//                    Map<String, Double> data = new HashMap<String,Double>();
//                    data.put("random", Math.random() * 100);
//                    downChannel.publish(null, data);
//                    log.info("published: " + data);
//
//            }
//        }, 5000, 500);

        //receive data from the client
        upChannel.addListener(new ServerChannel.MessageListener(){
            @Override
            public boolean onMessage(ServerSession serverSession, ServerChannel serverChannel, ServerMessage.Mutable mutable) {

                mainSession = serverSession;

                //publish received data to robot messaging
                publish(mutable.getDataAsMap());

                return true;
            }
        });

        //subscribe to 'relay' topics
        if(relay != null) {
            this.subscribe(relay);
        } else {
            log.warn("relay not set in embedded server. no data will be relayed to client");
        }

    }

    @Override
    protected void notify(Map<String, Object> values) {
        downChannel.publish(null, values);
    }
}
