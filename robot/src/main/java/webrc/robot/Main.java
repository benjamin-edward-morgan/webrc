package webrc.robot;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 3/3/14
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext fsxc = new FileSystemXmlApplicationContext("robot.xml");

        fsxc.start();

    }
}
