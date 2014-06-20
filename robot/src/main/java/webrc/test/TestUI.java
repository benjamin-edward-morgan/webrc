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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    ch.qos.logback.classic.Logger blackbox = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("blackbox");

    //1 tab for each ChartPanel
    JTabbedPane tabs = new JTabbedPane();

    //charts by key prefix
    Map<String, ChartPanel> charts = new HashMap<>();


    @Override
    protected void notify(Map<String, Object> values) {
        //TODO: add ui for manipulating sensor values
    }

    //create a hook into blackbox log, and chart out values
    //that parse as numbers
    private void initBlackboxAppender() throws Exception {

        /**Hook into the 'blackbox' log appender**/
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder patternLayout = new PatternLayoutEncoder();

        patternLayout.setPattern("%r,%msg%n");
        patternLayout.setContext(loggerContext);
        patternLayout.start();

        final PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(patternLayout);
        appender.setOutputStream(outputStream);
        appender.start();

        blackbox.addAppender(appender);

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

    //create on display chart window
    @PostConstruct
    public void init() {

        try {
            initBlackboxAppender();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("WebRC TestUI");
        frame.getContentPane().add(tabs);
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


}