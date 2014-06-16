package webrc.robot.sensor;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to create sensors that change value
 * based on command line programs.
 * <p/>
 * We use regular expressions to target values
 * in command line output
 * User: benjaminmorgan
 * Date: 4/12/14
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineSensor extends Sensor {

    private String command;
    private Map<String, String> regexs;
    private int updatePeriod = 1000; //default to 1 second

    public CommandLineSensor() {
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setRegexs(Map<String, String> regexs) {
        this.regexs = regexs;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }


    @PostConstruct
    public void init() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {

                    //parse command line output
                    BufferedReader reader = getCommandLineOutput(command);
                    StringBuilder builder = new StringBuilder();
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null)
                            builder.append(line);
                    } catch (IOException e) {
                        log.error("error reading from command line", e);
                    }
                    Map<String, Object> values = parseRegexs(builder.toString());

                    for(String key : values.keySet()) {
                        blackbox.info(key+",{}", values.get(key));
                    }

                    //publish readings
                    publish(values);

                    //sleep for update period
                    try {
                        Thread.sleep(updatePeriod);
                    } catch (InterruptedException e) {
                        //ok
                    }

                }
            }
        });

        t.setDaemon(true);

        t.start();
    }

    public BufferedReader getCommandLineOutput(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);

            InputStream in = p.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            return reader;

        } catch (IOException e) {
            log.error("Error running command: " + command, e);
        }

        return null;
    }

    /**
     * Regular expressions that are not matched return a null value.
     * Regular expressions that match and have no capture groups return the entire match
     * Regular expressions that match and have at least 1 capture group return the first capture group
     *
     * @param input
     * @return
     */
    public Map<String, Object> parseRegexs(String input) {

        Map<String, Object> results = new HashMap<String, Object>();
        for (String key : regexs.keySet()) {

            String regex = regexs.get(key);
            Pattern p = Pattern.compile(regex);

            Matcher m = p.matcher(input);

            if (m.find()) {
                if (m.groupCount() == 0)
                    results.put(key, m.group(0));
                else
                    results.put(key, m.group(1));
            } else {
                results.put(key, null);
            }

        }
        return results;
    }

    String letters = "qwertyuiopasdfghjklzxcvbnm";

    public String randomString() {
        int l = (int) (Math.random() * 10);
        String s = "";
        for (int i = 0; i < l; i++)
            s += letters.charAt((int) (Math.random() * letters.length()));
        return s;
    }

    public static void main(String[] args) {
        CommandLineSensor sensor = new CommandLineSensor();

        sensor.setCommand("ifconfig");

        Map<String, String> regexs = new HashMap<String, String>();
        regexs.put("ip", "en1:.*inet (\\d+\\.\\d+\\.\\d+\\.\\d+)");

        sensor.setRegexs(regexs);

        sensor.init();
    }
}
