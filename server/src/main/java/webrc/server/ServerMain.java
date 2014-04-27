package webrc.server;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 3/9/14
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableAutoConfiguration
public class ServerMain {

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(ServerMain.class, "beans.xml");
        sa.run(args);
    }
}
