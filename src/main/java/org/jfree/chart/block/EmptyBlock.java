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
 * EmptyBlock.java
 * ---------------
 * (C) Copyright 2004-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.ui.Size2D;
import org.jfree.chart.util.PublicCloneable;

/**
 * An empty block with a fixed size.
 */
public class EmptyBlock extends AbstractBlock
        implements Block, Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4083197869412648579L;

    /**
     * Creates a new block with the specified width and height.
     *
     * @param width  the width.
     * @param height  the height.
     */
    public EmptyBlock(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Arranges the contents of the block, within the given constraints, and
     * returns the block size.
     *
     * @param g2  the graphics device.
     * @param constraint  the constraint ({@code null} not permitted).
     *
     * @return The block size (in Java2D units, never {@code null}).
     */
    @Override
    public Size2D arrange(Graphics2D g2, RectangleConstraint constraint) {
        Size2D base = new Size2D(calculateTotalWidth(getWidth()),
                calculateTotalHeight(getHeight()));
        return constraint.calculateConstrainedSize(base);
    }

    /**
     * Draws the block.  Since the block is empty, there is nothing to draw
     * except the optional border.
     *
     * @param g2  the graphics device.
     * @param area  the area.
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area) {
        draw(g2, area, null);
    }

    /**
     * Draws the block within the specified area.  Since the block is empty,
     * there is nothing to draw except the optional border.
     *
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  ignored ({@code null} permitted).
     *
     * @return Always {@code null}.
     */
    @Override
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        area = trimMargin(area);
        drawBorder(g2, area);
        return null;
    }

    /**
     * Returns a clone of the block.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if there is a problem cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
