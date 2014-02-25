package webrc.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 2/24/14
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RobotMain {
    public static void main(String[] args) {
        SpringApplication.run(new ClassPathResource("robot.xml"), args);
    }
}
