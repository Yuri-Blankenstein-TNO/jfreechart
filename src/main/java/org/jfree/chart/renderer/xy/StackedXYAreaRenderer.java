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
 * --------------------------
 * StackedXYAreaRenderer.java
 * --------------------------
 * (C) Copyright 2003-present, by Richard Atkinson and Contributors.
 *
 * Original Author:  Richard Atkinson;
 * Contributor(s):   Christian W. Zuckschwerdt;
 *                   David Gilbert;
 *                   Ulrich Voigt (patch #312);
 *
 */

package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Stack;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.chart.util.PaintUtils;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SerialUtils;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * A stacked area renderer for the {@link XYPlot} class.
 * <br><br>
 * The example shown here is generated by the
 * {@code StackedXYAreaRendererDemo1.java} program included in the
 * JFreeChart demo collection:
 * <br><br>
 * <img src="doc-files/StackedXYAreaRendererSample.png"
 * alt="StackedXYAreaRendererSample.png">
 * <br><br>
 * SPECIAL NOTE:  This renderer does not currently handle negative data values
 * correctly.  This should get fixed at some point, but the current workaround
 * is to use the {@link StackedXYAreaRenderer2} class instead.
 */
public class StackedXYAreaRenderer extends XYAreaRenderer
        implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 5217394318178570889L;

     /**
     * A state object for use by this renderer.
     */
    static class StackedXYAreaRendererState extends XYItemRendererState {

        /** The area for the current series. */
        private Polygon seriesArea;

        /** The line. */
        private Line2D line;

        /** The points from the last series. */
        private Stack lastSeriesPoints;

        /** The points for the current series. */
        private Stack currentSeriesPoints;

        /**
         * Creates a new state for the renderer.
         *
         * @param info  the plot rendering info.
         */
        public StackedXYAreaRendererState(PlotRenderingInfo info) {
            super(info);
            this.seriesArea = null;
            this.line = new Line2D.Double();
            this.lastSeriesPoints = new Stack();
            this.currentSeriesPoints = new Stack();
        }

        /**
         * Returns the series area.
         *
         * @return The series area.
         */
        public Polygon getSeriesArea() {
            return this.seriesArea;
        }

        /**
         * Sets the series area.
         *
         * @param area  the area.
         */
        public void setSeriesArea(Polygon area) {
            this.seriesArea = area;
        }

        /**
         * Returns the working line.
         *
         * @return The working line.
         */
        public Line2D getLine() {
            return this.line;
        }

        /**
         * Returns the current series points.
         *
         * @return The current series points.
         */
        public Stack getCurrentSeriesPoints() {
            return this.currentSeriesPoints;
        }

        /**
         * Sets the current series points.
         *
         * @param points  the points.
         */
        public void setCurrentSeriesPoints(Stack points) {
            this.currentSeriesPoints = points;
        }

        /**
         * Returns the last series points.
         *
         * @return The last series points.
         */
        public Stack getLastSeriesPoints() {
            return this.lastSeriesPoints;
        }

        /**
         * Sets the last series points.
         *
         * @param points  the points.
         */
        public void setLastSeriesPoints(Stack points) {
            this.lastSeriesPoints = points;
        }

    }

    /**
     * Custom Paint for drawing all shapes, if null defaults to series shapes
     */
    private transient Paint shapePaint = null;

    /**
     * Custom Stroke for drawing all shapes, if null defaults to series
     * strokes.
     */
    private transient Stroke shapeStroke = null;

    /**
     * Creates a new renderer.
     */
    public StackedXYAreaRenderer() {
        this(AREA);
    }

    /**
     * Constructs a new renderer.
     *
     * @param type  the type of the renderer.
     */
    public StackedXYAreaRenderer(int type) {
        this(type, null, null);
    }

    /**
     * Constructs a new renderer.  To specify the type of renderer, use one of
     * the constants: {@code SHAPES}, {@code LINES}, {@code SHAPES_AND_LINES}, 
     * {@code AREA} or {@code AREA_AND_SHAPES}.
     *
     * @param type  the type of renderer.
     * @param labelGenerator  the tool tip generator ({@code null} permitted).
     * @param urlGenerator  the URL generator ({@code null} permitted).
     */
    public StackedXYAreaRenderer(int type, XYToolTipGenerator labelGenerator,
            XYURLGenerator urlGenerator) {
        super(type, labelGenerator, urlGenerator);
    }

    /**
     * Returns the paint used for rendering shapes, or {@code null} if
     * using series paints.
     *
     * @return The paint (possibly {@code null}).
     *
     * @see #setShapePaint(Paint)
     */
    public Paint getShapePaint() {
        return this.shapePaint;
    }

    /**
     * Sets the paint for rendering shapes and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param shapePaint  the paint ({@code null} permitted).
     *
     * @see #getShapePaint()
     */
    public void setShapePaint(Paint shapePaint) {
        this.shapePaint = shapePaint;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used for rendering shapes, or {@code null} if
     * using series strokes.
     *
     * @return The stroke (possibly {@code null}).
     *
     * @see #setShapeStroke(Stroke)
     */
    public Stroke getShapeStroke() {
        return this.shapeStroke;
    }

    /**
     * Sets the stroke for rendering shapes and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param shapeStroke  the stroke ({@code null} permitted).
     *
     * @see #getShapeStroke()
     */
    public void setShapeStroke(Stroke shapeStroke) {
        this.shapeStroke = shapeStroke;
        fireChangeEvent();
    }

    /**
     * Initialises the renderer. This method will be called before the first
     * item is rendered, giving the renderer an opportunity to initialise any
     * state information it wants to maintain.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area inside the axes.
     * @param plot  the plot.
     * @param data  the data.
     * @param info  an optional info collection object to return data back to
     *              the caller.
     *
     * @return A state object that should be passed to subsequent calls to the
     *         drawItem() method.
     */
    @Override
    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea,
            XYPlot plot, XYDataset data, PlotRenderingInfo info) {

        XYItemRendererState state = new StackedXYAreaRendererState(info);
        // in the rendering process, there is special handling for item
        // zero, so we can't support processing of visible data items only
        state.setProcessVisibleItemsOnly(false);
        return state;
    }

    /**
     * Returns the number of passes required by the renderer.
     *
     * @return 2.
     */
    @Override
    public int getPassCount() {
        return 2;
    }

    /**
     * Returns the range of values the renderer requires to display all the
     * items from the specified dataset.
     *
     * @param dataset  the dataset ({@code null} permitted).
     *
     * @return The range ([0.0, 0.0] if the dataset contains no values, and
     *         {@code null} if the dataset is {@code null}).
     *
     * @throws ClassCastException if {@code dataset} is not an instance
     *         of {@link TableXYDataset}.
     */
    @Override
    public Range findRangeBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtils.findStackedRangeBounds(
                (TableXYDataset) dataset);
        }
        else {
            return null;
        }
    }

    /**
     * Draws the visual representation of a single data item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the area within which the data is being drawn.
     * @param info  collects information about the drawing.
     * @param plot  the plot (can be used to obtain standard color information
     *              etc).
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     * @param crosshairState  information about crosshairs on a plot.
     * @param pass  the pass index.
     *
     * @throws ClassCastException if {@code state} is not an instance of
     *         {@code StackedXYAreaRendererState} or {@code dataset}
     *         is not an instance of {@link TableXYDataset}.
     */
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {

        PlotOrientation orientation = plot.getOrientation();
        StackedXYAreaRendererState areaState
            = (StackedXYAreaRendererState) state;
        // Get the item count for the series, so that we can know which is the
        // end of the series.
        TableXYDataset tdataset = (TableXYDataset) dataset;
        int itemCount = tdataset.getItemCount();

        // get the data point...
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        boolean nullPoint = false;
        if (Double.isNaN(y1)) {
            y1 = 0.0;
            nullPoint = true;
        }

        //  Get height adjustment based on stack and translate to Java2D values
        double ph1 = getPreviousHeight(tdataset, series, item);
        double transX1 = domainAxis.valueToJava2D(x1, dataArea,
                plot.getDomainAxisEdge());
        double transY1 = rangeAxis.valueToJava2D(y1 + ph1, dataArea,
                plot.getRangeAxisEdge());

        //  Get series Paint and Stroke
        Paint seriesPaint = getItemPaint(series, item);
        Paint seriesFillPaint = seriesPaint;
        if (getUseFillPaint()) {
            seriesFillPaint = getItemFillPaint(series, item);
        }
        Stroke seriesStroke = getItemStroke(series, item);

        if (pass == 0) {
            //  On first pass render the areas, line and outlines

            if (item == 0) {
                // Create a new Area for the series
                areaState.setSeriesArea(new Polygon());
                areaState.setLastSeriesPoints(
                        areaState.getCurrentSeriesPoints());
                areaState.setCurrentSeriesPoints(new Stack());

                // start from previous height (ph1)
                double transY2 = rangeAxis.valueToJava2D(ph1, dataArea,
                        plot.getRangeAxisEdge());

                // The first point is (x, 0)
                if (orientation == PlotOrientation.VERTICAL) {
                    areaState.getSeriesArea().addPoint((int) transX1,
                            (int) transY2);
                }
                else if (orientation == PlotOrientation.HORIZONTAL) {
                    areaState.getSeriesArea().addPoint((int) transY2,
                            (int) transX1);
                }
            }

            // Add each point to Area (x, y)
            if (orientation == PlotOrientation.VERTICAL) {
                Point point = new Point((int) transX1, (int) transY1);
                areaState.getSeriesArea().addPoint((int) point.getX(),
                        (int) point.getY());
                areaState.getCurrentSeriesPoints().push(point);
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                areaState.getSeriesArea().addPoint((int) transY1,
                        (int) transX1);
            }

            if (getPlotLines()) {
                if (item > 0) {
                    // get the previous data point...
                    double x0 = dataset.getXValue(series, item - 1);
                    double y0 = dataset.getYValue(series, item - 1);
                    double ph0 = getPreviousHeight(tdataset, series, item - 1);
                    double transX0 = domainAxis.valueToJava2D(x0, dataArea,
                            plot.getDomainAxisEdge());
                    double transY0 = rangeAxis.valueToJava2D(y0 + ph0,
                            dataArea, plot.getRangeAxisEdge());

                    if (orientation == PlotOrientation.VERTICAL) {
                        areaState.getLine().setLine(transX0, transY0, transX1,
                                transY1);
                    }
                    else if (orientation == PlotOrientation.HORIZONTAL) {
                        areaState.getLine().setLine(transY0, transX0, transY1,
                                transX1);
                    }
                    g2.setPaint(seriesPaint);
                    g2.setStroke(seriesStroke);
                    g2.draw(areaState.getLine());
                }
            }

            // Check if the item is the last item for the series and number of
            // items > 0.  We can't draw an area for a single point.
            if (getPlotArea() && item > 0 && item == (itemCount - 1)) {

                double transY2 = rangeAxis.valueToJava2D(ph1, dataArea,
                        plot.getRangeAxisEdge());

                if (orientation == PlotOrientation.VERTICAL) {
                    // Add the last point (x,0)
                    areaState.getSeriesArea().addPoint((int) transX1,
                            (int) transY2);
                }
                else if (orientation == PlotOrientation.HORIZONTAL) {
                    // Add the last point (x,0)
                    areaState.getSeriesArea().addPoint((int) transY2,
                            (int) transX1);
                }

                // Add points from last series to complete the base of the
                // polygon
                if (series != 0) {
                    Stack points = areaState.getLastSeriesPoints();
                    while (!points.empty()) {
                        Point point = (Point) points.pop();
                        areaState.getSeriesArea().addPoint((int) point.getX(),
                                (int) point.getY());
                    }
                }

                //  Fill the polygon
                g2.setPaint(seriesFillPaint);
                g2.setStroke(seriesStroke);
                g2.fill(areaState.getSeriesArea());

                //  Draw an outline around the Area.
                if (isOutline()) {
                    g2.setStroke(lookupSeriesOutlineStroke(series));
                    g2.setPaint(lookupSeriesOutlinePaint(series));
                    g2.draw(areaState.getSeriesArea());
                }
            }

            int datasetIndex = plot.indexOf(dataset);
            updateCrosshairValues(crosshairState, x1, ph1 + y1, datasetIndex,
                    transX1, transY1, orientation);

        }
        else if (pass == 1) {
            // On second pass render shapes and collect entity and tooltip
            // information

            Shape shape = null;
            if (getPlotShapes()) {
                shape = getItemShape(series, item);
                if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    shape = ShapeUtils.createTranslatedShape(shape,
                            transX1, transY1);
                }
                else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                    shape = ShapeUtils.createTranslatedShape(shape,
                            transY1, transX1);
                }
                if (!nullPoint) {
                    if (getShapePaint() != null) {
                        g2.setPaint(getShapePaint());
                    }
                    else {
                        g2.setPaint(seriesPaint);
                    }
                    if (getShapeStroke() != null) {
                        g2.setStroke(getShapeStroke());
                    }
                    else {
                        g2.setStroke(seriesStroke);
                    }
                    g2.draw(shape);
                }
            }
            else {
                if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    shape = new Rectangle2D.Double(transX1 - 3, transY1 - 3,
                            6.0, 6.0);
                }
                else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                    shape = new Rectangle2D.Double(transY1 - 3, transX1 - 3,
                            6.0, 6.0);
                }
            }

            // collect entity and tool tip information...
            if (state.getInfo() != null) {
                EntityCollection entities = state.getEntityCollection();
                if (entities != null && shape != null && !nullPoint) {
                    // limit the entity hotspot area to the data area
                    Area dataAreaHotspot = new Area(shape);
                    dataAreaHotspot.intersect(new Area(dataArea));
                    if (!dataAreaHotspot.isEmpty()) {
                        String tip = null;
                        XYToolTipGenerator generator = getToolTipGenerator(
                                series, item);
                        if (generator != null) {
                            tip = generator.generateToolTip(dataset, series, 
                                    item);
                        }
                        String url = null;
                        if (getURLGenerator() != null) {
                            url = getURLGenerator().generateURL(dataset, series, 
                                    item);
                        }
                        XYItemEntity entity = new XYItemEntity(dataAreaHotspot, 
                                dataset, series, item, tip, url);
                        entities.add(entity);
                    }
                }
            }

        }
    }

    /**
     * Calculates the stacked value of the all series up to, but not including
     * {@code series} for the specified item. It returns 0.0 if
     * {@code series} is the first series, i.e. 0.
     *
     * @param dataset  the dataset.
     * @param series  the series.
     * @param index  the index.
     *
     * @return The cumulative value for all series' values up to but excluding
     *         {@code series} for {@code index}.
     */
    protected double getPreviousHeight(TableXYDataset dataset,
                                       int series, int index) {
        double result = 0.0;
        for (int i = 0; i < series; i++) {
            double value = dataset.getYValue(i, index);
            if (!Double.isNaN(value)) {
                result += value;
            }
        }
        return result;
    }

    /**
     * Tests the renderer for equality with an arbitrary object.
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
        if (!(obj instanceof StackedXYAreaRenderer) || !super.equals(obj)) {
            return false;
        }
        StackedXYAreaRenderer that = (StackedXYAreaRenderer) obj;
        if (!PaintUtils.equal(this.shapePaint, that.shapePaint)) {
            return false;
        }
        if (!Objects.equals(this.shapeStroke, that.shapeStroke)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the renderer.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if the renderer cannot be cloned.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
        this.shapePaint = SerialUtils.readPaint(stream);
        this.shapeStroke = SerialUtils.readStroke(stream);
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
        SerialUtils.writePaint(this.shapePaint, stream);
        SerialUtils.writeStroke(this.shapeStroke, stream);
    }

}
