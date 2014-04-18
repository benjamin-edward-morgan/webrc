package webrc.robot.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 4/12/14
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineSensor extends Sensor {

    public NetworkStatusSensor() {

    }

//    @PostConstruct
//    public void init()
//    {
//
//    }

    public void getNetworkInterfaces()
    {
        try {
            Process p = Runtime.getRuntime().exec("ifconfig");

            InputStream in = p.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while(reader.ready())
              System.out.println(reader.readLine());



        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }




//    {
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while(interfaces.hasMoreElements()) {
//                NetworkInterface inter = interfaces.nextElement();
//                System.out.println(inter);
//                System.out.println("displayname:" + inter.getDisplayName());
//                System.out.println("hardware addres:" + inter.getHardwareAddress());
//                System.out.println("index:" + inter.getIndex());
//                System.out.println("mtu:" + inter.getMTU());
//                System.out.println("name:" + inter.getName());
//                System.out.println("parent" + inter.getParent());
//
//                Enumeration<InetAddress> ipaddresses = inter.getInetAddresses();
//                while(ipaddresses.hasMoreElements())
//                {
//                    InetAddress inet = ipaddresses.nextElement();
//                    System.out.println(inet);
//                    System.out.println(inet.getCanonicalHostName());
//                    System.out.println(inet.getHostAddress());
//                    System.out.println(inet.getHostName());
//                }
//
//                System.out.println();
//            }
//
//
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args)
    {
            new NetworkStatusSensor().getNetworkInterfaces();
    }
}
