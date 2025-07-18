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
 * ------------------------------
 * CombinedRangeCategoryPlot.java
 * ------------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Nicolas Brodu;
 *                   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.chart.plot;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.ObjectUtils;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.ShadowGenerator;
import org.jfree.data.Range;

/**
 * A combined category plot where the range axis is shared.
 */
public class CombinedRangeCategoryPlot extends CategoryPlot
        implements PlotChangeListener {

    /** For serialization. */
    private static final long serialVersionUID = 7260210007554504515L;

    /** Storage for the subplot references. */
    private List subplots;

    /** The gap between subplots. */
    private double gap;

    /** Temporary storage for the subplot areas. */
    private transient Rectangle2D[] subplotArea;  // TODO: move to plot state

    /**
     * Default constructor.
     */
    public CombinedRangeCategoryPlot() {
        this(new NumberAxis());
    }

    /**
     * Creates a new plot.
     *
     * @param rangeAxis  the shared range axis.
     */
    public CombinedRangeCategoryPlot(ValueAxis rangeAxis) {
        super(null, null, rangeAxis, null);
        this.subplots = new java.util.ArrayList();
        this.gap = 5.0;
    }

    /**
     * Returns the space between subplots.
     *
     * @return The gap (in Java2D units).
     */
    public double getGap() {
        return this.gap;
    }

    /**
     * Sets the amount of space between subplots and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param gap  the gap between subplots (in Java2D units).
     */
    public void setGap(double gap) {
        this.gap = gap;
        fireChangeEvent();
    }

    /**
     * Adds a subplot (with a default 'weight' of 1) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <br><br>
     * You must ensure that the subplot has a non-null domain axis.  The range
     * axis for the subplot will be set to {@code null}.
     *
     * @param subplot  the subplot ({@code null} not permitted).
     */
    public void add(CategoryPlot subplot) {
        // defer argument checking
        add(subplot, 1);
    }

    /**
     * Adds a subplot and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * <br><br>
     * You must ensure that the subplot has a non-null domain axis.  The range
     * axis for the subplot will be set to {@code null}.
     *
     * @param subplot  the subplot ({@code null} not permitted).
     * @param weight  the weight (must be &gt;= 1).
     */
    public void add(CategoryPlot subplot, int weight) {
        Args.nullNotPermitted(subplot, "subplot");
        if (weight <= 0) {
            throw new IllegalArgumentException("Require weight >= 1.");
        }
        // store the plot and its weight
        subplot.setParent(this);
        subplot.setWeight(weight);
        subplot.setInsets(new RectangleInsets(0.0, 0.0, 0.0, 0.0));
        subplot.setRangeAxis(null);
        subplot.setOrientation(getOrientation());
        subplot.addChangeListener(this);
        this.subplots.add(subplot);
        // configure the range axis...
        ValueAxis axis = getRangeAxis();
        if (axis != null) {
            axis.configure();
        }
        fireChangeEvent();
    }

    /**
     * Removes a subplot from the combined chart.
     *
     * @param subplot  the subplot ({@code null} not permitted).
     */
    public void remove(CategoryPlot subplot) {
        Args.nullNotPermitted(subplot, "subplot");
        int position = -1;
        int size = this.subplots.size();
        int i = 0;
        while (position == -1 && i < size) {
            if (this.subplots.get(i) == subplot) {
                position = i;
            }
            i++;
        }
        if (position != -1) {
            this.subplots.remove(position);
            subplot.setParent(null);
            subplot.removeChangeListener(this);

            ValueAxis range = getRangeAxis();
            if (range != null) {
                range.configure();
            }

            ValueAxis range2 = getRangeAxis(1);
            if (range2 != null) {
                range2.configure();
            }
            fireChangeEvent();
        }
    }

    /**
     * Returns the list of subplots.  The returned list may be empty, but is
     * never {@code null}.
     *
     * @return An unmodifiable list of subplots.
     */
    public List getSubplots() {
        if (this.subplots != null) {
            return Collections.unmodifiableList(this.subplots);
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Calculates the space required for the axes.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     *
     * @return The space required for the axes.
     */
    @Override
    protected AxisSpace calculateAxisSpace(Graphics2D g2, 
            Rectangle2D plotArea) {

        AxisSpace space = new AxisSpace();
        PlotOrientation orientation = getOrientation();

        // work out the space required by the domain axis...
        AxisSpace fixed = getFixedRangeAxisSpace();
        if (fixed != null) {
            if (orientation == PlotOrientation.VERTICAL) {
                space.setLeft(fixed.getLeft());
                space.setRight(fixed.getRight());
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                space.setTop(fixed.getTop());
                space.setBottom(fixed.getBottom());
            }
        }
        else {
            ValueAxis valueAxis = getRangeAxis();
            RectangleEdge valueEdge = Plot.resolveRangeAxisLocation(
                    getRangeAxisLocation(), orientation);
            if (valueAxis != null) {
                space = valueAxis.reserveSpace(g2, this, plotArea, valueEdge,
                        space);
            }
        }

        Rectangle2D adjustedPlotArea = space.shrink(plotArea, null);
        // work out the maximum height or width of the non-shared axes...
        int n = this.subplots.size();
        int totalWeight = 0;
        for (int i = 0; i < n; i++) {
            CategoryPlot sub = (CategoryPlot) this.subplots.get(i);
            totalWeight += sub.getWeight();
        }
        // calculate plotAreas of all sub-plots, maximum vertical/horizontal
        // axis width/height
        this.subplotArea = new Rectangle2D[n];
        double x = adjustedPlotArea.getX();
        double y = adjustedPlotArea.getY();
        double usableSize = 0.0;
        if (orientation == PlotOrientation.VERTICAL) {
            usableSize = adjustedPlotArea.getWidth() - this.gap * (n - 1);
        }
        else if (orientation == PlotOrientation.HORIZONTAL) {
            usableSize = adjustedPlotArea.getHeight() - this.gap * (n - 1);
        }

        for (int i = 0; i < n; i++) {
            CategoryPlot plot = (CategoryPlot) this.subplots.get(i);

            // calculate sub-plot area
            if (orientation == PlotOrientation.VERTICAL) {
                double w = usableSize * plot.getWeight() / totalWeight;
                this.subplotArea[i] = new Rectangle2D.Double(x, y, w,
                        adjustedPlotArea.getHeight());
                x = x + w + this.gap;
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                double h = usableSize * plot.getWeight() / totalWeight;
                this.subplotArea[i] = new Rectangle2D.Double(x, y,
                        adjustedPlotArea.getWidth(), h);
                y = y + h + this.gap;
            }

            AxisSpace subSpace = plot.calculateDomainAxisSpace(g2,
                    this.subplotArea[i], null);
            space.ensureAtLeast(subSpace);

        }

        return space;
    }

    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).  Will perform all the placement calculations for each
     * sub-plots and then tell these to draw themselves.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot (including axis labels)
     *              should be drawn.
     * @param anchor  the anchor point ({@code null} permitted).
     * @param parentState  the parent state.
     * @param info  collects information about the drawing ({@code null}
     *              permitted).
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState,
                     PlotRenderingInfo info) {

        // set up info collection...
        if (info != null) {
            info.setPlotArea(area);
        }

        // adjust the drawing area for plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        // calculate the data area...
        AxisSpace space = calculateAxisSpace(g2, area);
        Rectangle2D dataArea = space.shrink(area, null);

        // set the width and height of non-shared axis of all sub-plots
        setFixedDomainAxisSpaceForSubplots(space);

        // draw the shared axis
        ValueAxis axis = getRangeAxis();
        RectangleEdge rangeEdge = getRangeAxisEdge();
        double cursor = RectangleEdge.coordinate(dataArea, rangeEdge);
        AxisState state = axis.draw(g2, cursor, area, dataArea, rangeEdge,
                info);
        if (parentState == null) {
            parentState = new PlotState();
        }
        parentState.getSharedAxisStates().put(axis, state);

        // draw all the charts
        for (int i = 0; i < this.subplots.size(); i++) {
            CategoryPlot plot = (CategoryPlot) this.subplots.get(i);
            PlotRenderingInfo subplotInfo = null;
            if (info != null) {
                subplotInfo = new PlotRenderingInfo(info.getOwner());
                info.addSubplotInfo(subplotInfo);
            }
            Point2D subAnchor = null;
            if (anchor != null && this.subplotArea[i].contains(anchor)) {
                subAnchor = anchor;
            }
            plot.draw(g2, this.subplotArea[i], subAnchor, parentState,
                    subplotInfo);
        }

        if (info != null) {
            info.setDataArea(dataArea);
        }

    }

    /**
     * Sets the orientation for the plot (and all the subplots).
     *
     * @param orientation  the orientation.
     */
    @Override
    public void setOrientation(PlotOrientation orientation) {
        super.setOrientation(orientation);
        Iterator iterator = this.subplots.iterator();
        while (iterator.hasNext()) {
            CategoryPlot plot = (CategoryPlot) iterator.next();
            plot.setOrientation(orientation);
        }
    }

    /**
     * Sets the shadow generator for the plot (and all subplots) and sends
     * a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param generator  the new generator ({@code null} permitted).
     */
    @Override
    public void setShadowGenerator(ShadowGenerator generator) {
        setNotify(false);
        super.setShadowGenerator(generator);
        Iterator iterator = this.subplots.iterator();
        while (iterator.hasNext()) {
            CategoryPlot plot = (CategoryPlot) iterator.next();
            plot.setShadowGenerator(generator);
        }
        setNotify(true);
    }

    /**
     * Returns a range representing the extent of the data values in this plot
     * (obtained from the subplots) that will be rendered against the specified
     * axis.  NOTE: This method is intended for internal JFreeChart use, and
     * is public only so that code in the axis classes can call it.  Since
     * only the range axis is shared between subplots, the JFreeChart code
     * will only call this method for the range values (although this is not
     * checked/enforced).
     *
     * @param axis the axis.
     *
     * @return The range.
     */
    @Override
    public Range getDataRange(ValueAxis axis) {
        Range result = null;
        if (this.subplots != null) {
            Iterator iterator = this.subplots.iterator();
            while (iterator.hasNext()) {
                CategoryPlot subplot = (CategoryPlot) iterator.next();
                result = Range.combine(result, subplot.getDataRange(axis));
            }
        }
        return result;
    }

    /**
     * Returns a collection of legend items for the plot.
     *
     * @return The legend items.
     */
    @Override
    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = getFixedLegendItems();
        if (result == null) {
            result = new LegendItemCollection();
            if (this.subplots != null) {
                Iterator iterator = this.subplots.iterator();
                while (iterator.hasNext()) {
                    CategoryPlot plot = (CategoryPlot) iterator.next();
                    LegendItemCollection more = plot.getLegendItems();
                    result.addAll(more);
                }
            }
        }
        return result;
    }

    /**
     * Sets the size (width or height, depending on the orientation of the
     * plot) for the domain axis of each subplot.
     *
     * @param space  the space.
     */
    protected void setFixedDomainAxisSpaceForSubplots(AxisSpace space) {
        Iterator iterator = this.subplots.iterator();
        while (iterator.hasNext()) {
            CategoryPlot plot = (CategoryPlot) iterator.next();
            plot.setFixedDomainAxisSpace(space, false);
        }
    }

    /**
     * Handles a 'click' on the plot by updating the anchor value.
     *
     * @param x  x-coordinate of the click.
     * @param y  y-coordinate of the click.
     * @param info  information about the plot's dimensions.
     *
     */
    @Override
    public void handleClick(int x, int y, PlotRenderingInfo info) {
        Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            for (int i = 0; i < this.subplots.size(); i++) {
                CategoryPlot subplot = (CategoryPlot) this.subplots.get(i);
                PlotRenderingInfo subplotInfo = info.getSubplotInfo(i);
                subplot.handleClick(x, y, subplotInfo);
            }
        }
    }

    /**
     * Receives a {@link PlotChangeEvent} and responds by notifying all
     * listeners.
     *
     * @param event  the event.
     */
    @Override
    public void plotChanged(PlotChangeEvent event) {
        notifyListeners(event);
    }

    /**
     * Tests the plot for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CombinedRangeCategoryPlot)) {
            return false;
        }
        CombinedRangeCategoryPlot that = (CombinedRangeCategoryPlot) obj;
        if (!that.canEqual(this)){
            return false;
        }
        if (Double.compare(this.gap, that.gap) != 0) {
            return false;
        }
        if (!Objects.equals(this.subplots, that.subplots)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Ensures symmetry between super/subclass implementations of equals. For
     * more detail, see http://jqno.nl/equalsverifier/manual/inheritance.
     *
     * @param other Object
     * 
     * @return true ONLY if the parameter is THIS class type
     */
    @Override
    public boolean canEqual(Object other) {
        // Solves Problem: equals not symmetric
        return (other instanceof CombinedRangeCategoryPlot);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 61 * hash + Objects.hashCode(this.subplots);
        hash = 61 * hash + (int) (Double.doubleToLongBits(this.gap) ^ 
                                 (Double.doubleToLongBits(this.gap) >>> 32));
        return hash;
    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  this class will not throw this
     *         exception, but subclasses (if any) might.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        CombinedRangeCategoryPlot result
            = (CombinedRangeCategoryPlot) super.clone();
        result.subplots = (List) ObjectUtils.deepClone(this.subplots);
        for (Iterator it = result.subplots.iterator(); it.hasNext();) {
            Plot child = (Plot) it.next();
            child.setParent(result);
        }

        // after setting up all the subplots, the shared range axis may need
        // reconfiguring
        ValueAxis rangeAxis = result.getRangeAxis();
        if (rangeAxis != null) {
            rangeAxis.configure();
        }

        return result;
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

        // the range axis is deserialized before the subplots, so its value
        // range is likely to be incorrect...
        ValueAxis rangeAxis = getRangeAxis();
        if (rangeAxis != null) {
            rangeAxis.configure();
        }

    }

}
