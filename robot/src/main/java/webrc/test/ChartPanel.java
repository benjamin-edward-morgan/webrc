package webrc.test;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * a chart panel contains a live chart and some checkboxes
 * to turn off individual traces.
 *
 * @author benjaminmorgan
 *         Date: 6/20/14
 */
public class ChartPanel extends JPanel {

    Logger log = LoggerFactory.getLogger(TestUI.class);
    Chart2D chart = new Chart2D();

    //color or traces in the order they are added
    static final Color[] traceColors = new Color[]{Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN,  Color.CYAN,  Color.MAGENTA, Color.BLACK, Color.GRAY, Color.PINK};

    //char background color
    static final Color background = Color.WHITE;

    //char axis color
    static final Color axis = Color.BLACK;

    //map of traces by key name
    Map<String, ITrace2D> traces = new HashMap<String, ITrace2D>();

    //container for checkboxes
    JPanel checkboxes = new JPanel();

    public ChartPanel() {
        super();
        LayoutManager layout1 = new BorderLayout();
        this.setLayout(layout1);

        chart.setBackground(background);
        chart.setGridColor(axis);
        chart.setPaintLabels(false);
        chart.getAxisX().setPaintGrid(false);
        chart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time (ms)"));

        chart.getAxisY().setPaintGrid(false);
        chart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

        BoxLayout checkLayout = new BoxLayout(checkboxes, BoxLayout.X_AXIS);
        checkboxes.setLayout(checkLayout);

        this.add(chart, BorderLayout.CENTER);
        this.add(checkboxes, BorderLayout.SOUTH);

    }

    //adds a point to a trace, adds a trace if it does
    //not exist and there is another color to use
    public boolean plot(String key, double x, double y) {

        if(traces.containsKey(key)) {
            traces.get(key).addPoint(x, y);
            return true;
        } else if(traces.size() < traceColors.length) {
            Color color = traceColors[traces.size()];

            final ITrace2D trace = new Trace2DLtd(500);
            trace.setColor(color);
            traces.put(key, trace);
            chart.addTrace(trace);

            final JCheckBox checkbox = new JCheckBox(key);
            checkbox.setBackground(background);
            checkbox.setForeground(color);
            checkbox.setSelected(true);

            checkbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(checkbox.isSelected()) {
                        trace.setVisible(true);
                    } else {
                        trace.setVisible(false);
                    }
                }
            });

            checkboxes.add(checkbox);
            checkboxes.revalidate();
            trace.addPoint(x, y);
            return true;
        }

        return false;
    }


}
