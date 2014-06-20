package webrc.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 3/3/14
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if(args.length >1)
            log.error("Only 1 Argument for Spring Config Xml is Allowed");

        String file = "robot.xml";
        if(args.length == 1)
            file=args[0];



        FileSystemXmlApplicationContext fsxc = new FileSystemXmlApplicationContext(file);
        fsxc.start();

    }
}
