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
 * ------------------
 * AbstractBlock.java
 * ------------------
 * (C) Copyright 2004-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.Size2D;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SerialUtils;
import org.jfree.chart.util.ShapeUtils;

import org.jfree.data.Range;

/**
 * A convenience class for creating new classes that implement
 * the {@link Block} interface.
 */
public class AbstractBlock implements Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7689852412141274563L;

    /** The id for the block. */
    private String id;

    /** The margin around the outside of the block. */
    private RectangleInsets margin;

    /** The frame (or border) for the block. */
    private BlockFrame frame;

    /** The padding between the block content and the border. */
    private RectangleInsets padding;

    /**
     * The natural width of the block (may be overridden if there are
     * constraints in sizing).
     */
    private double width;

    /**
     * The natural height of the block (may be overridden if there are
     * constraints in sizing).
     */
    private double height;

    /**
     * The current bounds for the block (position of the block in Java2D space).
     */
    private transient Rectangle2D bounds;

    /**
     * Creates a new block.
     */
    protected AbstractBlock() {
        this.id = null;
        this.width = 0.0;
        this.height = 0.0;
        this.bounds = new Rectangle2D.Float();
        this.margin = RectangleInsets.ZERO_INSETS;
        this.frame = BlockBorder.NONE;
        this.padding = RectangleInsets.ZERO_INSETS;
    }

    /**
     * Returns the id.
     *
     * @return The id (possibly {@code null}).
     *
     * @see #setID(String)
     */
    public String getID() {
        return this.id;
    }

    /**
     * Sets the id for the block.
     *
     * @param id  the id ({@code null} permitted).
     *
     * @see #getID()
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Returns the natural width of the block, if this is known in advance.
     * The actual width of the block may be overridden if layout constraints
     * make this necessary.
     *
     * @return The width.
     *
     * @see #setWidth(double)
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Sets the natural width of the block, if this is known in advance.
     *
     * @param width  the width (in Java2D units)
     *
     * @see #getWidth()
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the natural height of the block, if this is known in advance.
     * The actual height of the block may be overridden if layout constraints
     * make this necessary.
     *
     * @return The height.
     *
     * @see #setHeight(double)
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Sets the natural width of the block, if this is known in advance.
     *
     * @param height  the width (in Java2D units)
     *
     * @see #getHeight()
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Returns the margin.
     *
     * @return The margin (never {@code null}).
     *
     * @see #getMargin()
     */
    public RectangleInsets getMargin() {
        return this.margin;
    }

    /**
     * Sets the margin (use {@link RectangleInsets#ZERO_INSETS} for no
     * padding).
     *
     * @param margin  the margin ({@code null} not permitted).
     *
     * @see #getMargin()
     */
    public void setMargin(RectangleInsets margin) {
        Args.nullNotPermitted(margin, "margin");
        this.margin = margin;
    }

    /**
     * Sets the margin.
     *
     * @param top  the top margin.
     * @param left  the left margin.
     * @param bottom  the bottom margin.
     * @param right  the right margin.
     *
     * @see #getMargin()
     */
    public void setMargin(double top, double left, double bottom, 
            double right) {
        setMargin(new RectangleInsets(top, left, bottom, right));
    }

    /**
     * Sets a black border with the specified line widths.
     *
     * @param top  the top border line width.
     * @param left  the left border line width.
     * @param bottom  the bottom border line width.
     * @param right  the right border line width.
     */
    public void setBorder(double top, double left, double bottom,
                          double right) {
        setFrame(new BlockBorder(top, left, bottom, right));
    }

    /**
     * Returns the current frame (border).
     *
     * @return The frame.
     *
     * @see #setFrame(BlockFrame)
     */
    public BlockFrame getFrame() {
        return this.frame;
    }

    /**
     * Sets the frame (or border).
     *
     * @param frame  the frame ({@code null} not permitted).
     *
     * @see #getFrame()
     */
    public void setFrame(BlockFrame frame) {
        Args.nullNotPermitted(frame, "frame");
        this.frame = frame;
    }

    /**
     * Returns the padding.
     *
     * @return The padding (never {@code null}).
     *
     * @see #setPadding(RectangleInsets)
     */
    public RectangleInsets getPadding() {
        return this.padding;
    }

    /**
     * Sets the padding (use {@link RectangleInsets#ZERO_INSETS} for no
     * padding).
     *
     * @param padding  the padding ({@code null} not permitted).
     *
     * @see #getPadding()
     */
    public void setPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.padding = padding;
    }

    /**
     * Sets the padding.
     *
     * @param top  the top padding.
     * @param left  the left padding.
     * @param bottom  the bottom padding.
     * @param right  the right padding.
     */
    public void setPadding(double top, double left, double bottom,
                           double right) {
        setPadding(new RectangleInsets(top, left, bottom, right));
    }

    /**
     * Returns the x-offset for the content within the block.
     *
     * @return The x-offset.
     *
     * @see #getContentYOffset()
     */
    public double getContentXOffset() {
        return this.margin.getLeft() + this.frame.getInsets().getLeft()
            + this.padding.getLeft();
    }

    /**
     * Returns the y-offset for the content within the block.
     *
     * @return The y-offset.
     *
     * @see #getContentXOffset()
     */
    public double getContentYOffset() {
        return this.margin.getTop() + this.frame.getInsets().getTop()
            + this.padding.getTop();
    }

    /**
     * Arranges the contents of the block, with no constraints, and returns
     * the block size.
     *
     * @param g2  the graphics device.
     *
     * @return The block size (in Java2D units, never {@code null}).
     */
    public Size2D arrange(Graphics2D g2) {
        return arrange(g2, RectangleConstraint.NONE);
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
    public Size2D arrange(Graphics2D g2, RectangleConstraint constraint) {
        Size2D base = new Size2D(getWidth(), getHeight());
        return constraint.calculateConstrainedSize(base);
    }

    /**
     * Returns the current bounds of the block.
     *
     * @return The bounds.
     *
     * @see #setBounds(Rectangle2D)
     */
    public Rectangle2D getBounds() {
        return this.bounds;
    }

    /**
     * Sets the bounds of the block.
     *
     * @param bounds  the bounds ({@code null} not permitted).
     *
     * @see #getBounds()
     */
    public void setBounds(Rectangle2D bounds) {
        Args.nullNotPermitted(bounds, "bounds");
        this.bounds = bounds;
    }

    /**
     * Calculate the width available for content after subtracting
     * the margin, border and padding space from the specified fixed
     * width.
     *
     * @param fixedWidth  the fixed width.
     *
     * @return The available space.
     *
     * @see #trimToContentHeight(double)
     */
    protected double trimToContentWidth(double fixedWidth) {
        double result = this.margin.trimWidth(fixedWidth);
        result = this.frame.getInsets().trimWidth(result);
        result = this.padding.trimWidth(result);
        return Math.max(result, 0.0);
    }

    /**
     * Calculate the height available for content after subtracting
     * the margin, border and padding space from the specified fixed
     * height.
     *
     * @param fixedHeight  the fixed height.
     *
     * @return The available space.
     *
     * @see #trimToContentWidth(double)
     */
    protected double trimToContentHeight(double fixedHeight) {
        double result = this.margin.trimHeight(fixedHeight);
        result = this.frame.getInsets().trimHeight(result);
        result = this.padding.trimHeight(result);
        return Math.max(result, 0.0);
    }

    /**
     * Returns a constraint for the content of this block that will result in
     * the bounds of the block matching the specified constraint.
     *
     * @param c  the outer constraint ({@code null} not permitted).
     *
     * @return The content constraint.
     */
    protected RectangleConstraint toContentConstraint(RectangleConstraint c) {
        Args.nullNotPermitted(c, "c");
        if (c.equals(RectangleConstraint.NONE)) {
            return c;
        }
        double w = c.getWidth();
        Range wr = c.getWidthRange();
        double h = c.getHeight();
        Range hr = c.getHeightRange();
        double ww = trimToContentWidth(w);
        double hh = trimToContentHeight(h);
        Range wwr = trimToContentWidth(wr);
        Range hhr = trimToContentHeight(hr);
        return new RectangleConstraint(ww, wwr, c.getWidthConstraintType(),
            hh, hhr, c.getHeightConstraintType());
    }

    private Range trimToContentWidth(Range r) {
        if (r == null) {
            return null;
        }
        double lowerBound = 0.0;
        double upperBound = Double.POSITIVE_INFINITY;
        if (r.getLowerBound() > 0.0) {
            lowerBound = trimToContentWidth(r.getLowerBound());
        }
        if (r.getUpperBound() < Double.POSITIVE_INFINITY) {
            upperBound = trimToContentWidth(r.getUpperBound());
        }
        return new Range(lowerBound, upperBound);
    }

    private Range trimToContentHeight(Range r) {
        if (r == null) {
            return null;
        }
        double lowerBound = 0.0;
        double upperBound = Double.POSITIVE_INFINITY;
        if (r.getLowerBound() > 0.0) {
            lowerBound = trimToContentHeight(r.getLowerBound());
        }
        if (r.getUpperBound() < Double.POSITIVE_INFINITY) {
            upperBound = trimToContentHeight(r.getUpperBound());
        }
        return new Range(lowerBound, upperBound);
    }

    /**
     * Adds the margin, border and padding to the specified content width.
     *
     * @param contentWidth  the content width.
     *
     * @return The adjusted width.
     */
    protected double calculateTotalWidth(double contentWidth) {
        double result = contentWidth;
        result = this.padding.extendWidth(result);
        result = this.frame.getInsets().extendWidth(result);
        result = this.margin.extendWidth(result);
        return result;
    }

    /**
     * Adds the margin, border and padding to the specified content height.
     *
     * @param contentHeight  the content height.
     *
     * @return The adjusted height.
     */
    protected double calculateTotalHeight(double contentHeight) {
        double result = contentHeight;
        result = this.padding.extendHeight(result);
        result = this.frame.getInsets().extendHeight(result);
        result = this.margin.extendHeight(result);
        return result;
    }

    /**
     * Reduces the specified area by the amount of space consumed
     * by the margin.
     *
     * @param area  the area ({@code null} not permitted).
     *
     * @return The trimmed area.
     */
    protected Rectangle2D trimMargin(Rectangle2D area) {
        // defer argument checking...
        this.margin.trim(area);
        return area;
    }

    /**
     * Reduces the specified area by the amount of space consumed
     * by the border.
     *
     * @param area  the area ({@code null} not permitted).
     *
     * @return The trimmed area.
     */
    protected Rectangle2D trimBorder(Rectangle2D area) {
        // defer argument checking...
        this.frame.getInsets().trim(area);
        return area;
    }

    /**
     * Reduces the specified area by the amount of space consumed
     * by the padding.
     *
     * @param area  the area ({@code null} not permitted).
     *
     * @return The trimmed area.
     */
    protected Rectangle2D trimPadding(Rectangle2D area) {
        // defer argument checking...
        this.padding.trim(area);
        return area;
    }

    /**
     * Draws the border around the perimeter of the specified area.
     *
     * @param g2  the graphics device.
     * @param area  the area.
     */
    protected void drawBorder(Graphics2D g2, Rectangle2D area) {
        this.frame.draw(g2, area);
    }

    /**
     * Tests this block for equality with an arbitrary object.
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
        if (!(obj instanceof AbstractBlock)) {
            return false;
        }
        AbstractBlock that = (AbstractBlock) obj;
        if (!Objects.equals(this.id, that.id)) {
            return false;
        }
        if (!Objects.equals(this.frame, that.frame)) {
            return false;
        }
        if (!Objects.equals(this.bounds, that.bounds)) {
            return false;
        }
        if (!Objects.equals(this.margin, that.margin)) {
            return false;
        }
        if (!Objects.equals(this.padding, that.padding)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) !=
            Double.doubleToLongBits(that.height)) {
            return false;
        }
        if (Double.doubleToLongBits(this.width) !=
            Double.doubleToLongBits(that.width)) {
            return false;
        }
        
        // fix the "equals not symmetric" problem
        if (!that.canEqual(this)) {
            return false;
        }
        return true;
    }

    /**
     * Ensures symmetry between super/subclass implementations of equals. For
     * more detail, see http://jqno.nl/equalsverifier/manual/inheritance.
     *
     * @param other Object
     * 
     * @return true ONLY if the parameter is THIS class type
     */
    public boolean canEqual(Object other) {
        // fix the "equals not symmetric" problem
        return (other instanceof AbstractBlock);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.margin);
        hash = 89 * hash + Objects.hashCode(this.frame);
        hash = 89 * hash + Objects.hashCode(this.padding);
        hash = 89 * hash + Objects.hashCode(this.bounds);
        hash = 89 * hash +
               (int) (Double.doubleToLongBits(this.width) ^
                     (Double.doubleToLongBits(this.width) >>> 32));
        hash = 89 * hash +
               (int) (Double.doubleToLongBits(this.height) ^
                     (Double.doubleToLongBits(this.height) >>> 32));
        return hash;
    }

    /**
     * Returns a clone of this block.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if there is a problem creating the
     *         clone.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        AbstractBlock clone = (AbstractBlock) super.clone();
        clone.bounds = (Rectangle2D) ShapeUtils.clone(this.bounds);
        if (this.frame instanceof PublicCloneable) {
            PublicCloneable pc = (PublicCloneable) this.frame;
            clone.frame = (BlockFrame) pc.clone();
        }
        return clone;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtils.writeShape(this.bounds, stream);
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
        this.bounds = (Rectangle2D) SerialUtils.readShape(stream);
    }

}
