/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------
 * ChartFrame.java
 * ---------------
 * (C) Copyright 2001-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * A frame for displaying a chart.
 */
public class ChartFrame extends JFrame {

    /** The chart panel. */
    private final ChartPanel chartPanel;

    /**
     * Constructs a frame for a chart.
     *
     * @param title  the frame title.
     * @param chart  the chart.
     */
    public ChartFrame(String title, JFreeChart chart) {
        this(title, chart, false);
    }

    /**
     * Constructs a frame for a chart.
     *
     * @param title  the frame title.
     * @param chart  the chart.
     * @param scrollPane  if {@code true}, put the Chart(Panel) into a
     *                    JScrollPane.
     */
    public ChartFrame(String title, JFreeChart chart, boolean scrollPane) {
        super(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.chartPanel = new ChartPanel(chart);
        if (scrollPane) {
            setContentPane(new JScrollPane(this.chartPanel));
        }
        else {
            setContentPane(this.chartPanel);
        }
    }

    /**
     * Returns the chart panel for the frame.
     *
     * @return The chart panel.
     */
    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }

}
