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
 * MultiplePiePlot.java
 * --------------------
 * (C) Copyright 2004-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Brian Cabana (patch 1943021);
 *
 */

package org.jfree.chart.plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.PaintUtils;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.SerialUtils;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.general.PieDataset;

/**
 * A plot that displays multiple pie plots using data from a
 * {@link CategoryDataset}.
 */
public class MultiplePiePlot extends Plot implements Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -355377800470807389L;

    /** The chart object that draws the individual pie charts. */
    private JFreeChart pieChart;

    /** The dataset. */
    private CategoryDataset dataset;

    /** The data extract order (by row or by column). */
    private TableOrder dataExtractOrder;

    /** The pie section limit percentage. */
    private double limit = 0.0;

    /**
     * The key for the aggregated items.
     */
    private Comparable aggregatedItemsKey;

    /**
     * The paint for the aggregated items.
     */
    private transient Paint aggregatedItemsPaint;

    /**
     * The colors to use for each section.
     */
    private transient Map sectionPaints;

    /**
     * The legend item shape (never null).
     */
    private transient Shape legendItemShape;

    /**
     * Creates a new plot with no data.
     */
    public MultiplePiePlot() {
        this(null);
    }

    /**
     * Creates a new plot.
     *
     * @param dataset  the dataset ({@code null} permitted).
     */
    public MultiplePiePlot(CategoryDataset dataset) {
        super();
        setDataset(dataset);
        PiePlot piePlot = new PiePlot(null);
        piePlot.setIgnoreNullValues(true);
        this.pieChart = new JFreeChart(piePlot);
        this.pieChart.removeLegend();
        this.dataExtractOrder = TableOrder.BY_COLUMN;
        this.pieChart.setBackgroundPaint(null);
        TextTitle seriesTitle = new TextTitle("Series Title",
                new Font("SansSerif", Font.BOLD, 12));
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        this.pieChart.setTitle(seriesTitle);
        this.aggregatedItemsKey = "Other";
        this.aggregatedItemsPaint = Color.LIGHT_GRAY;
        this.sectionPaints = new HashMap();
        this.legendItemShape = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
    }

    /**
     * Returns the dataset used by the plot.
     *
     * @return The dataset (possibly {@code null}).
     */
    public CategoryDataset getDataset() {
        return this.dataset;
    }

    /**
     * Sets the dataset used by the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param dataset  the dataset ({@code null} permitted).
     */
    public void setDataset(CategoryDataset dataset) {
        // if there is an existing dataset, remove the plot from the list of
        // change listeners...
        if (this.dataset != null) {
            this.dataset.removeChangeListener(this);
        }

        // set the new dataset, and register the chart as a change listener...
        this.dataset = dataset;
        if (dataset != null) {
            setDatasetGroup(dataset.getGroup());
            dataset.addChangeListener(this);
        }

        // send a dataset change event to self to trigger plot change event
        datasetChanged(new DatasetChangeEvent(this, dataset));
    }

    /**
     * Returns the pie chart that is used to draw the individual pie plots.
     * Note that there are some attributes on this chart instance that will
     * be ignored at rendering time (for example, legend item settings).
     *
     * @return The pie chart (never {@code null}).
     *
     * @see #setPieChart(JFreeChart)
     */
    public JFreeChart getPieChart() {
        return this.pieChart;
    }

    /**
     * Sets the chart that is used to draw the individual pie plots.  The
     * chart's plot must be an instance of {@link PiePlot}.
     *
     * @param pieChart  the pie chart ({@code null} not permitted).
     *
     * @see #getPieChart()
     */
    public void setPieChart(JFreeChart pieChart) {
        Args.nullNotPermitted(pieChart, "pieChart");
        if (!(pieChart.getPlot() instanceof PiePlot)) {
            throw new IllegalArgumentException("The 'pieChart' argument must "
                    + "be a chart based on a PiePlot.");
        }
        this.pieChart = pieChart;
        fireChangeEvent();
    }

    /**
     * Returns the data extract order (by row or by column).
     *
     * @return The data extract order (never {@code null}).
     */
    public TableOrder getDataExtractOrder() {
        return this.dataExtractOrder;
    }

    /**
     * Sets the data extract order (by row or by column) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param order  the order ({@code null} not permitted).
     */
    public void setDataExtractOrder(TableOrder order) {
        Args.nullNotPermitted(order, "order");
        this.dataExtractOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the limit (as a percentage) below which small pie sections are
     * aggregated.
     *
     * @return The limit percentage.
     */
    public double getLimit() {
        return this.limit;
    }

    /**
     * Sets the limit below which pie sections are aggregated.
     * Set this to 0.0 if you don't want any aggregation to occur.
     *
     * @param limit  the limit percent.
     */
    public void setLimit(double limit) {
        this.limit = limit;
        fireChangeEvent();
    }

    /**
     * Returns the key for aggregated items in the pie plots, if there are any.
     * The default value is "Other".
     *
     * @return The aggregated items key.
     */
    public Comparable getAggregatedItemsKey() {
        return this.aggregatedItemsKey;
    }

    /**
     * Sets the key for aggregated items in the pie plots.  You must ensure
     * that this doesn't clash with any keys in the dataset.
     *
     * @param key  the key ({@code null} not permitted).
     */
    public void setAggregatedItemsKey(Comparable key) {
        Args.nullNotPermitted(key, "key");
        this.aggregatedItemsKey = key;
        fireChangeEvent();
    }

    /**
     * Returns the paint used to draw the pie section representing the
     * aggregated items.  The default value is {code Color.LIGHT_GRAY}.
     *
     * @return The paint.
     */
    public Paint getAggregatedItemsPaint() {
        return this.aggregatedItemsPaint;
    }

    /**
     * Sets the paint used to draw the pie section representing the aggregated
     * items and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint ({@code null} not permitted).
     */
    public void setAggregatedItemsPaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.aggregatedItemsPaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns a short string describing the type of plot.
     *
     * @return The plot type.
     */
    @Override
    public String getPlotType() {
        return "Multiple Pie Plot";
         // TODO: need to fetch this from localised resources
    }

    /**
     * Returns the shape used for legend items.
     *
     * @return The shape (never {@code null}).
     *
     * @see #setLegendItemShape(Shape)
     */
    public Shape getLegendItemShape() {
        return this.legendItemShape;
    }

    /**
     * Sets the shape used for legend items and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param shape  the shape ({@code null} not permitted).
     *
     * @see #getLegendItemShape()
     */
    public void setLegendItemShape(Shape shape) {
        Args.nullNotPermitted(shape, "shape");
        this.legendItemShape = shape;
        fireChangeEvent();
    }

    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     * @param anchor  the anchor point ({@code null} permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param info  collects info about the drawing.
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
            PlotState parentState, PlotRenderingInfo info) {

        // adjust the drawing area for the plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);
        drawBackground(g2, area);
        drawOutline(g2, area);

        // check that there is some data to display...
        if (DatasetUtils.isEmptyOrNull(this.dataset)) {
            drawNoDataMessage(g2, area);
            return;
        }

        int pieCount;
        if (this.dataExtractOrder == TableOrder.BY_ROW) {
            pieCount = this.dataset.getRowCount();
        }
        else {
            pieCount = this.dataset.getColumnCount();
        }

        // the columns variable is always >= rows
        int displayCols = (int) Math.ceil(Math.sqrt(pieCount));
        int displayRows
            = (int) Math.ceil((double) pieCount / (double) displayCols);

        // swap rows and columns to match plotArea shape
        if (displayCols > displayRows && area.getWidth() < area.getHeight()) {
            int temp = displayCols;
            displayCols = displayRows;
            displayRows = temp;
        }

        prefetchSectionPaints();

        int x = (int) area.getX();
        int y = (int) area.getY();
        int width = ((int) area.getWidth()) / displayCols;
        int height = ((int) area.getHeight()) / displayRows;
        int row = 0;
        int column = 0;
        int diff = (displayRows * displayCols) - pieCount;
        int xoffset = 0;
        Rectangle rect = new Rectangle();

        for (int pieIndex = 0; pieIndex < pieCount; pieIndex++) {
            rect.setBounds(x + xoffset + (width * column), y + (height * row),
                    width, height);

            String title;
            if (this.dataExtractOrder == TableOrder.BY_ROW) {
                title = this.dataset.getRowKey(pieIndex).toString();
            }
            else {
                title = this.dataset.getColumnKey(pieIndex).toString();
            }
            this.pieChart.setTitle(title);

            PieDataset piedataset;
            PieDataset dd = new CategoryToPieDataset(this.dataset,
                    this.dataExtractOrder, pieIndex);
            if (this.limit > 0.0) {
                piedataset = DatasetUtils.createConsolidatedPieDataset(
                        dd, this.aggregatedItemsKey, this.limit);
            }
            else {
                piedataset = dd;
            }
            PiePlot piePlot = (PiePlot) this.pieChart.getPlot();
            piePlot.setDataset(piedataset);
            piePlot.setPieIndex(pieIndex);

            // update the section colors to match the global colors...
            for (int i = 0; i < piedataset.getItemCount(); i++) {
                Comparable key = piedataset.getKey(i);
                Paint p;
                if (key.equals(this.aggregatedItemsKey)) {
                    p = this.aggregatedItemsPaint;
                }
                else {
                    p = (Paint) this.sectionPaints.get(key);
                }
                piePlot.setSectionPaint(key, p);
            }

            ChartRenderingInfo subinfo = null;
            if (info != null) {
                subinfo = new ChartRenderingInfo();
            }
            this.pieChart.draw(g2, rect, subinfo);
            if (info != null) {
                assert subinfo != null;
                info.getOwner().getEntityCollection().addAll(
                        subinfo.getEntityCollection());
                info.addSubplotInfo(subinfo.getPlotInfo());
            }

            ++column;
            if (column == displayCols) {
                column = 0;
                ++row;

                if (row == displayRows - 1 && diff != 0) {
                    xoffset = (diff * width) / 2;
                }
            }
        }

    }

    /**
     * For each key in the dataset, check the {@code sectionPaints}
     * cache to see if a paint is associated with that key and, if not,
     * fetch one from the drawing supplier.  These colors are cached so that
     * the legend and all the subplots use consistent colors.
     */
    private void prefetchSectionPaints() {

        // pre-fetch the colors for each key...this is because the subplots
        // may not display every key, but we need the coloring to be
        // consistent...

        PiePlot piePlot = (PiePlot) getPieChart().getPlot();

        if (this.dataExtractOrder == TableOrder.BY_ROW) {
            // column keys provide potential keys for individual pies
            for (int c = 0; c < this.dataset.getColumnCount(); c++) {
                Comparable key = this.dataset.getColumnKey(c);
                Paint p = piePlot.getSectionPaint(key);
                if (p == null) {
                    p = (Paint) this.sectionPaints.get(key);
                    if (p == null) {
                        p = getDrawingSupplier().getNextPaint();
                    }
                }
                this.sectionPaints.put(key, p);
            }
        }
        else {
            // row keys provide potential keys for individual pies
            for (int r = 0; r < this.dataset.getRowCount(); r++) {
                Comparable key = this.dataset.getRowKey(r);
                Paint p = piePlot.getSectionPaint(key);
                if (p == null) {
                    p = (Paint) this.sectionPaints.get(key);
                    if (p == null) {
                        p = getDrawingSupplier().getNextPaint();
                    }
                }
                this.sectionPaints.put(key, p);
            }
        }

    }

    /**
     * Returns a collection of legend items for the pie chart.
     *
     * @return The legend items.
     */
    @Override
    public LegendItemCollection getLegendItems() {

        LegendItemCollection result = new LegendItemCollection();
        if (this.dataset == null) {
            return result;
        }

        List keys = null;
        prefetchSectionPaints();
        if (this.dataExtractOrder == TableOrder.BY_ROW) {
            keys = this.dataset.getColumnKeys();
        }
        else if (this.dataExtractOrder == TableOrder.BY_COLUMN) {
            keys = this.dataset.getRowKeys();
        }
        if (keys == null) {
            return result;
        }
        int section = 0;
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Comparable key = (Comparable) iterator.next();
            String label = key.toString();  // TODO: use a generator here
            String description = label;
            Paint paint = (Paint) this.sectionPaints.get(key);
            LegendItem item = new LegendItem(label, description, null,
                    null, getLegendItemShape(), paint,
                    Plot.DEFAULT_OUTLINE_STROKE, paint);
            item.setSeriesKey(key);
            item.setSeriesIndex(section);
            item.setDataset(getDataset());
            result.add(item);
            section++;
        }
        if (this.limit > 0.0) {
            LegendItem a = new LegendItem(this.aggregatedItemsKey.toString(),
                    this.aggregatedItemsKey.toString(), null, null,
                    getLegendItemShape(), this.aggregatedItemsPaint,
                    Plot.DEFAULT_OUTLINE_STROKE, this.aggregatedItemsPaint);
            result.add(a);
        }
        return result;
    }

    /**
     * Tests this plot for equality with an arbitrary object.  Note that the
     * plot's dataset is not considered in the equality test.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return {@code true} if this plot is equal to {@code obj}, and
     *     {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiplePiePlot)) {
            return false;
        }
        MultiplePiePlot that = (MultiplePiePlot) obj;
        if (this.dataExtractOrder != that.dataExtractOrder) {
            return false;
        }
        if (this.limit != that.limit) {
            return false;
        }
        if (!this.aggregatedItemsKey.equals(that.aggregatedItemsKey)) {
            return false;
        }
        if (!PaintUtils.equal(this.aggregatedItemsPaint,
                that.aggregatedItemsPaint)) {
            return false;
        }
        if (!Objects.equals(this.pieChart, that.pieChart)) {
            return false;
        }
        if (!ShapeUtils.equal(this.legendItemShape, that.legendItemShape)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if some component of the plot does
     *         not support cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        MultiplePiePlot clone = (MultiplePiePlot) super.clone();
        clone.pieChart = (JFreeChart) this.pieChart.clone();
        clone.sectionPaints = new HashMap(this.sectionPaints);
        clone.legendItemShape = ShapeUtils.clone(this.legendItemShape);
        return clone;
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
        SerialUtils.writePaint(this.aggregatedItemsPaint, stream);
        SerialUtils.writeShape(this.legendItemShape, stream);
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
        this.aggregatedItemsPaint = SerialUtils.readPaint(stream);
        this.legendItemShape = SerialUtils.readShape(stream);
        this.sectionPaints = new HashMap();
    }

}
