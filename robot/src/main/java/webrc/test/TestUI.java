package webrc.test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webrc.robot.messaging.Pubscriber;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;
import java.util.Scanner;


public class TestUI extends Pubscriber /*implements ApplicationContextAware*/{

//    ApplicationContext appContext;
    Logger log = LoggerFactory.getLogger(TestUI.class);
    ch.qos.logback.classic.Logger blackbox = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("blackbox");

    private TestUI() {
    }


//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.appContext=applicationContext;
//
//        String[] beans = appContext.getBeanDefinitionNames();
//        for(String s : beans)
//            log.info(s);
//    }

    @Override
    protected void notify(Map<String, Object> values) {

    }

    public void plot(String key, double x, double y) {

        if(key.equals("pan")) {
            trace.addPoint(x, y);
        }
    }

    ITrace2D trace = new Trace2DLtd(200);


    private void initBlackboxAppender() throws Exception {

        /**Hook into the 'blackbox' log appender**/
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder patternLayout = new PatternLayoutEncoder();

        patternLayout.setPattern("%r,%msg%n");
        patternLayout.setContext(loggerContext);
        patternLayout.start();

        PipedInputStream inputStream = new PipedInputStream();
        final Scanner inputScanner = new Scanner(inputStream);

        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(patternLayout);
        appender.setOutputStream(outputStream);
        appender.start();

        blackbox.addAppender(appender);

        //
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for(;;) {
                    String line = inputScanner.nextLine();

                    log.info("next line:");
                    log.info(line);

                    String[] parts = line.split(",");


                }
            }
        });

        thread.start();
    }


    @PostConstruct
    public void init(){

        try {
            initBlackboxAppender();
        } catch (Exception e) {
            e.printStackTrace();  //TODO:
        }

        // Create a chart:
        Chart2D chart = new Chart2D();
        trace.setColor(Color.RED);


        // Add the trace to the chart. This has to be done before adding points (deadlock prevention):
        chart.addTrace(trace);

        // Make it visible:
        // Create a frame.
        JFrame frame = new JFrame("MinimalDynamicChart");
        // add the chart to the frame:
        frame.getContentPane().add(chart);
        frame.setSize(400,300);
        // Enable the termination button [cross on the upper right edge]:
        frame.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
        frame.setVisible(true);
    }


}