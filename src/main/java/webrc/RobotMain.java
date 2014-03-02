package webrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/24/14
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
//public class RobotMain {
//    public static void main(String[] args) {
//        SpringApplication.run(new ClassPathResource("server.xml"), args);
//        //SpringApplication.run(RobotConfigClass.class, args);
//    }
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class RobotMain {

    public static void main(String[] args) {
        SpringApplication.run(RobotMain.class, args);
    }

}
