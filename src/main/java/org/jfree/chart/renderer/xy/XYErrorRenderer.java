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
 * --------------------
 * XYErrorRenderer.java
 * --------------------
 * (C) Copyright 2006-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.PaintUtils;
import org.jfree.chart.util.SerialUtils;
import org.jfree.data.Range;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * A line and shape renderer that can also display x and/or y-error values.
 * This renderer expects an {@link IntervalXYDataset}, otherwise it reverts
 * to the behaviour of the super class.  The example shown here is generated by
 * the {@code XYErrorRendererDemo1.java} program included in the
 * JFreeChart demo collection:
 * <br><br>
 * <img src="doc-files/XYErrorRendererSample.png" alt="XYErrorRendererSample.png">
 */
public class XYErrorRenderer extends XYLineAndShapeRenderer {

    /** For serialization. */
    static final long serialVersionUID = 5162283570955172424L;

    /** A flag that controls whether the x-error bars are drawn. */
    private boolean drawXError;

    /** A flag that controls whether the y-error bars are drawn. */
    private boolean drawYError;

    /** The length of the cap at the end of the error bars. */
    private double capLength;

    /**
     * The paint used to draw the error bars (if {@code null} we use the
     * series paint).
     */
    private transient Paint errorPaint;

    /**
     * The stroke used to draw the error bars (if {@code null} we use the
     * series outline stroke).
     */
    private transient Stroke errorStroke;

    /**
     * Creates a new {@code XYErrorRenderer} instance.
     */
    public XYErrorRenderer() {
        super(false, true);
        this.drawXError = true;
        this.drawYError = true;
        this.errorPaint = null;
        this.errorStroke = null;
        this.capLength = 4.0;
    }

    /**
     * Returns the flag that controls whether the renderer draws error
     * bars for the x-values.
     *
     * @return A boolean.
     *
     * @see #setDrawXError(boolean)
     */
    public boolean getDrawXError() {
        return this.drawXError;
    }

    /**
     * Sets the flag that controls whether the renderer draws error
     * bars for the x-values and, if the flag changes, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param draw  the flag value.
     *
     * @see #getDrawXError()
     */
    public void setDrawXError(boolean draw) {
        if (this.drawXError != draw) {
            this.drawXError = draw;
            fireChangeEvent();
        }
    }

    /**
     * Returns the flag that controls whether the renderer draws error
     * bars for the y-values.
     *
     * @return A boolean.
     *
     * @see #setDrawYError(boolean)
     */
    public boolean getDrawYError() {
        return this.drawYError;
    }

    /**
     * Sets the flag that controls whether the renderer draws error
     * bars for the y-values and, if the flag changes, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param draw  the flag value.
     *
     * @see #getDrawYError()
     */
    public void setDrawYError(boolean draw) {
        if (this.drawYError != draw) {
            this.drawYError = draw;
            fireChangeEvent();
        }
    }

    /**
     * Returns the length (in Java2D units) of the cap at the end of the error
     * bars.
     *
     * @return The cap length.
     *
     * @see #setCapLength(double)
     */
    public double getCapLength() {
        return this.capLength;
    }

    /**
     * Sets the length of the cap at the end of the error bars, and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param length  the length (in Java2D units).
     *
     * @see #getCapLength()
     */
    public void setCapLength(double length) {
        this.capLength = length;
        fireChangeEvent();
    }

    /**
     * Returns the paint used to draw the error bars.  If this is
     * {@code null} (the default), the item paint is used instead.
     *
     * @return The paint (possibly {@code null}).
     *
     * @see #setErrorPaint(Paint)
     */
    public Paint getErrorPaint() {
        return this.errorPaint;
    }

    /**
     * Sets the paint used to draw the error bars and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param paint  the paint ({@code null} permitted).
     *
     * @see #getErrorPaint()
     */
    public void setErrorPaint(Paint paint) {
        this.errorPaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used to draw the error bars.  If this is 
     * {@code null} (the default), the item outline stroke is used 
     * instead.
     * 
     * @return The stroke (possibly {@code null}).
     *
     * @see #setErrorStroke(Stroke)
     */
    public Stroke getErrorStroke() {
        return this.errorStroke;
    }

    /**
     * Sets the stroke used to draw the error bars and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param stroke   the stroke ({@code null} permitted).
     *
     * @see #getErrorStroke()
     */
    public void setErrorStroke(Stroke stroke) {
        this.errorStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the range required by this renderer to display all the domain
     * values in the specified dataset.
     *
     * @param dataset  the dataset ({@code null} permitted).
     *
     * @return The range, or {@code null} if the dataset is
     *     {@code null}.
     */
    @Override
    public Range findDomainBounds(XYDataset dataset) {
        // include the interval if there is one
        return findDomainBounds(dataset, true);
    }

    /**
     * Returns the range required by this renderer to display all the range
     * values in the specified dataset.
     *
     * @param dataset  the dataset ({@code null} permitted).
     *
     * @return The range, or {@code null} if the dataset is
     *     {@code null}.
     */
    @Override
    public Range findRangeBounds(XYDataset dataset) {
        // include the interval if there is one
        return findRangeBounds(dataset, true);
    }

    /**
     * Draws the visual representation for one data item.
     *
     * @param g2  the graphics output target.
     * @param state  the renderer state.
     * @param dataArea  the data area.
     * @param info  the plot rendering info.
     * @param plot  the plot.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param series  the series index.
     * @param item  the item index.
     * @param crosshairState  the crosshair state.
     * @param pass  the pass index.
     */
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {

        if (pass == 0 && dataset instanceof IntervalXYDataset
                && getItemVisible(series, item)) {
            IntervalXYDataset ixyd = (IntervalXYDataset) dataset;
            PlotOrientation orientation = plot.getOrientation();
            if (this.drawXError) {
                // draw the error bar for the x-interval
                double x0 = ixyd.getStartXValue(series, item);
                double x1 = ixyd.getEndXValue(series, item);
                double y = ixyd.getYValue(series, item);
                RectangleEdge edge = plot.getDomainAxisEdge();
                double xx0 = domainAxis.valueToJava2D(x0, dataArea, edge);
                double xx1 = domainAxis.valueToJava2D(x1, dataArea, edge);
                double yy = rangeAxis.valueToJava2D(y, dataArea,
                        plot.getRangeAxisEdge());
                Line2D line;
                Line2D cap1;
                Line2D cap2;
                double adj = this.capLength / 2.0;
                if (orientation == PlotOrientation.VERTICAL) {
                    line = new Line2D.Double(xx0, yy, xx1, yy);
                    cap1 = new Line2D.Double(xx0, yy - adj, xx0, yy + adj);
                    cap2 = new Line2D.Double(xx1, yy - adj, xx1, yy + adj);
                }
                else {  // PlotOrientation.HORIZONTAL
                    line = new Line2D.Double(yy, xx0, yy, xx1);
                    cap1 = new Line2D.Double(yy - adj, xx0, yy + adj, xx0);
                    cap2 = new Line2D.Double(yy - adj, xx1, yy + adj, xx1);
                }
                if (this.errorPaint != null) {
                    g2.setPaint(this.errorPaint);
                }
                else {
                    g2.setPaint(getItemPaint(series, item));
                }
                if (this.errorStroke != null) {
                    g2.setStroke(this.errorStroke);
                }
                else {
                    g2.setStroke(getItemStroke(series, item));
                }
                g2.draw(line);
                g2.draw(cap1);
                g2.draw(cap2);
            }
            if (this.drawYError) {
                // draw the error bar for the y-interval
                double y0 = ixyd.getStartYValue(series, item);
                double y1 = ixyd.getEndYValue(series, item);
                double x = ixyd.getXValue(series, item);
                RectangleEdge edge = plot.getRangeAxisEdge();
                double yy0 = rangeAxis.valueToJava2D(y0, dataArea, edge);
                double yy1 = rangeAxis.valueToJava2D(y1, dataArea, edge);
                double xx = domainAxis.valueToJava2D(x, dataArea,
                        plot.getDomainAxisEdge());
                Line2D line;
                Line2D cap1;
                Line2D cap2;
                double adj = this.capLength / 2.0;
                if (orientation == PlotOrientation.VERTICAL) {
                    line = new Line2D.Double(xx, yy0, xx, yy1);
                    cap1 = new Line2D.Double(xx - adj, yy0, xx + adj, yy0);
                    cap2 = new Line2D.Double(xx - adj, yy1, xx + adj, yy1);
                }
                else {  // PlotOrientation.HORIZONTAL
                    line = new Line2D.Double(yy0, xx, yy1, xx);
                    cap1 = new Line2D.Double(yy0, xx - adj, yy0, xx + adj);
                    cap2 = new Line2D.Double(yy1, xx - adj, yy1, xx + adj);
                }
                if (this.errorPaint != null) {
                    g2.setPaint(this.errorPaint);
                }
                else {
                    g2.setPaint(getItemPaint(series, item));
                }
                if (this.errorStroke != null) {
                    g2.setStroke(this.errorStroke);
                }
                else {
                    g2.setStroke(getItemStroke(series, item));
                }
                g2.draw(line);
                g2.draw(cap1);
                g2.draw(cap2);
            }
        }
        super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis,
                dataset, series, item, crosshairState, pass);
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYErrorRenderer)) {
            return false;
        }
        XYErrorRenderer that = (XYErrorRenderer) obj;
        if (this.drawXError != that.drawXError) {
            return false;
        }
        if (this.drawYError != that.drawYError) {
            return false;
        }
        if (this.capLength != that.capLength) {
            return false;
        }
        if (!PaintUtils.equal(this.errorPaint, that.errorPaint)) {
            return false;
        }
        if (!Objects.equals(this.errorStroke, that.errorStroke)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.errorPaint = SerialUtils.readPaint(stream);
        this.errorStroke = SerialUtils.readStroke(stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtils.writePaint(this.errorPaint, stream);
        SerialUtils.writeStroke(this.errorStroke, stream);
    }

}
