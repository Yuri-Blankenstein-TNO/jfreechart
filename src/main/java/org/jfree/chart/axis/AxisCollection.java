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
 * -------------------
 * AxisCollection.java
 * -------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import java.util.List;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.Args;

/**
 * A collection of axes that have been assigned to the TOP, BOTTOM, LEFT or
 * RIGHT of a chart.  This class is used internally by JFreeChart, you won't
 * normally need to use it yourself.
 */
public class AxisCollection {

    /** The axes that need to be drawn at the top of the plot area. */
    private final List axesAtTop;

    /** The axes that need to be drawn at the bottom of the plot area. */
    private final List axesAtBottom;

    /** The axes that need to be drawn at the left of the plot area. */
    private final List axesAtLeft;

    /** The axes that need to be drawn at the right of the plot area. */
    private final List axesAtRight;

    /**
     * Creates a new empty collection.
     */
    public AxisCollection() {
        this.axesAtTop = new java.util.ArrayList();
        this.axesAtBottom = new java.util.ArrayList();
        this.axesAtLeft = new java.util.ArrayList();
        this.axesAtRight = new java.util.ArrayList();
    }

    /**
     * Returns a list of the axes (if any) that need to be drawn at the top of
     * the plot area.
     *
     * @return A list of axes.
     */
    public List getAxesAtTop() {
        return this.axesAtTop;
    }

   /**
    * Returns a list of the axes (if any) that need to be drawn at the bottom
    * of the plot area.
    *
    * @return A list of axes.
    */
   public List getAxesAtBottom() {
        return this.axesAtBottom;
    }

    /**
     * Returns a list of the axes (if any) that need to be drawn at the left
     * of the plot area.
     *
     * @return A list of axes.
     */
    public List getAxesAtLeft() {
        return this.axesAtLeft;
    }

    /**
    * Returns a list of the axes (if any) that need to be drawn at the right
    * of the plot area.
    *
    * @return A list of axes.
    */
    public List getAxesAtRight() {
        return this.axesAtRight;
    }

    /**
     * Adds an axis to the collection.
     *
     * @param axis  the axis ({@code null} not permitted).
     * @param edge  the edge of the plot that the axis should be drawn on
     *              ({@code null} not permitted).
     */
    public void add(Axis axis, RectangleEdge edge) {
        Args.nullNotPermitted(axis, "axis");
        Args.nullNotPermitted(edge, "edge");
        if (edge == RectangleEdge.TOP) {
            this.axesAtTop.add(axis);
        }
        else if (edge == RectangleEdge.BOTTOM) {
            this.axesAtBottom.add(axis);
        }
        else if (edge == RectangleEdge.LEFT) {
            this.axesAtLeft.add(axis);
        }
        else if (edge == RectangleEdge.RIGHT) {
            this.axesAtRight.add(axis);
        }
    }

}
