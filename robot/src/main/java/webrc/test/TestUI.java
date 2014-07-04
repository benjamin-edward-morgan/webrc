package webrc.test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webrc.robot.messaging.Pubscriber;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * This is a window that graphs input signals for debugging and
 * replaying logged data. It can be used for debugging by including
 * as a bean in robot.xml. It then programatically creates a Logback
 * appender for the 'blackbox' logger and parses the output. Numerical
 * values are graphed visually in real time.
 *
 * TODO: create an easy way to use this to replay data from the blackbox log file
 * TODO: add iu elements to manipulate sensor values
 */
public class TestUI extends Pubscriber {

    Logger log = LoggerFactory.getLogger(TestUI.class);
    ch.qos.logback.classic.Logger blackbox = null;
    //1 tab for each ChartPanel
    JTabbedPane tabs = new JTabbedPane();

    //charts by key prefix
    Map<String, ChartPanel> charts = new HashMap<>();

    Set<String> sensors;
    public void setSensors(Set<String> sensors) {
        this.sensors = sensors;
    }

    /**Run to replay a log file
     *
     * @param args -filename
     */
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TestUI.class);

        if(args.length != 1) {
            logger.error("Please provide a filename.");
            return;
        }

        TestUI testUI = new TestUI();

        try {
            testUI.initFile(args[0]);
            testUI.constructFrame();
        } catch (Exception e) {
            logger.error("Error!", e);
        }


    }


    @Override
    protected void notify(Map<String, Object> values) {
        //TODO: add ui for manipulating sensor values in debug mode
    }

    /**
     * begin reading from the Logback blackbox logger
     * this is intended for real-time graphing when debugging
     * @throws Exception
     */
    private void initBlackboxAppender() throws Exception {

        blackbox = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("blackbox");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder patternLayout = new PatternLayoutEncoder();

        patternLayout.setPattern("%r,%msg%n");
        patternLayout.setContext(loggerContext);
        patternLayout.start();

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(patternLayout);
        appender.setOutputStream(outputStream);
        appender.start();

        blackbox.addAppender(appender);

        begin(inputStream);
    }

    /**
     * begin reading from a log file,
     * send data with appropriate time delay
     * to the graphing thread
     * this is mean for graphing from a log file
     * after-the-fact
     * @throws Exception
     */
    private void initFile(String filename) throws Exception {
        final Scanner fileScanner = new Scanner(new FileInputStream(new File(filename)));

        final PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                    double lastTime = -1;
                    while(fileScanner.hasNextLine()) {
                        try {

                            String line = fileScanner.nextLine();

                            String[] parts = line.split(",");

                            double time = Double.parseDouble(parts[0]);
                            String key = parts[1];
                            double value = Double.parseDouble(parts[2]);

                            if(lastTime < 0)
                                lastTime = time;

                            log.warn(line);

                            if(time-lastTime > 0) {
                                Thread.sleep((int)(time-lastTime));
                            }

                            lastTime=time;

                            String keyPrefix = key.split("\\.")[0];

                            if(charts.containsKey(keyPrefix)) {
                                charts.get(keyPrefix).plot(key, time, value);
                            } else {
                                ChartPanel chart = new ChartPanel();
                                charts.put(keyPrefix, chart);
                                tabs.addTab(keyPrefix, chart);
                                chart.plot(key, time, value);
                            }
                        } catch(Exception e) {
                            log.error("Error reading blackbox log", e);
                        }
                    }

                fileScanner.close();
            }
        });

        t.setDaemon(true);
        t.setName("Test UI File reading");
        t.start();

        begin(pipeIn);
    }


    private void begin(final InputStream inputStream) {
        //read and parse text from the log appender
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (; ; ) {
                    Scanner inputScanner = new Scanner(inputStream);

                    while (inputScanner.hasNextLine()) {
                        try {

                            String line = inputScanner.nextLine();

                            String[] parts = line.split(",");

                            double time = Double.parseDouble(parts[0]);
                            String key = parts[1];
                            double value = Double.parseDouble(parts[2]);

                            String keyPrefix = key.split("\\.")[0];

                            if(charts.containsKey(keyPrefix)) {
                                charts.get(keyPrefix).plot(key, time, value);
                            } else {
                                ChartPanel chart = new ChartPanel();
                                charts.put(keyPrefix, chart);
                                tabs.addTab(keyPrefix, chart);
                                chart.plot(key, time, value);
                            }

                        } catch (Exception e) {
                            //ok for now
                        }
                    }
                    Thread.yield();
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    /**
     * When spring constructs the testui, we configure
     * the blackbox log appender
     */
    @PostConstruct
    public void init() {

        try {
            initBlackboxAppender();
        } catch (Exception e) {
            e.printStackTrace();
        }

        constructFrame();

    }

    private void constructFrame() {
        JFrame frame = new JFrame("WebRC TestUI");

        Container c = new Container();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        c.add(tabs);

        List<Component> sliders = buildSensorSliders();
        for(Component slider : sliders) {
            c.add(slider);
        }

        frame.getContentPane().add(c);
        frame.setSize(1200, 400);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.setVisible(true);
    }

    private java.util.List<Component> buildSensorSliders() {

        ArrayList<Component> sliders = new ArrayList<>();

        if(sensors != null)
        for(String sensorString : sensors) {
            String[] parts = sensorString.split(",");
            if(parts.length == 3) {
                final String name = parts[0];
                final double min = Double.parseDouble(parts[1]);
                final double max = Double.parseDouble(parts[2]);

                Container a = new Container();
                a.setLayout(new BoxLayout(a, BoxLayout.X_AXIS));
                a.add(new JLabel(name));

                final JSlider slider = new JSlider(0, 100, 50);
                a.add(slider);

                final JLabel valueLabel = new JLabel("--");
                a.add(valueLabel);

                slider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        Map<String, Object> values = new HashMap<String, Object>();

                        int sliderValue = slider.getValue();
                        double value = min + (max-min)*(sliderValue/100.0);

                        valueLabel.setText(value + "");

                        values.put(name, value);
                        publish(values);
                    }
                });

                sliders.add(a);

            } else {
                log.warn("TestUI could not understand: \"" + sensorString + "\" the sensor description must be of the form \"name,min,max\"");
            }
        }

        return sliders;

    }


}