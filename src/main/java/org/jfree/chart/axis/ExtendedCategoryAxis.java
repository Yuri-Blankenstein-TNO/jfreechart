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
 * -------------------------
 * ExtendedCategoryAxis.java
 * -------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.text.TextBlock;
import org.jfree.chart.text.TextFragment;
import org.jfree.chart.text.TextLine;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.PaintUtils;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.SerialUtils;

/**
 * An extended version of the {@link CategoryAxis} class that supports
 * sublabels on the axis.
 */
public class ExtendedCategoryAxis extends CategoryAxis {

    /** For serialization. */
    static final long serialVersionUID = -3004429093959826567L;

    /** Storage for the sublabels. */
    private Map sublabels;

    /** The sublabel font. */
    private Font sublabelFont;

    /** The sublabel paint. */
    private transient Paint sublabelPaint;

    /**
     * Creates a new axis.
     *
     * @param label  the axis label.
     */
    public ExtendedCategoryAxis(String label) {
        super(label);
        this.sublabels = new HashMap();
        this.sublabelFont = new Font("SansSerif", Font.PLAIN, 10);
        this.sublabelPaint = Color.BLACK;
    }

    /**
     * Returns the font for the sublabels.
     *
     * @return The font (never {@code null}).
     *
     * @see #setSubLabelFont(Font)
     */
    public Font getSubLabelFont() {
        return this.sublabelFont;
    }

    /**
     * Sets the font for the sublabels and sends an {@link AxisChangeEvent} to
     * all registered listeners.
     *
     * @param font  the font ({@code null} not permitted).
     *
     * @see #getSubLabelFont()
     */
    public void setSubLabelFont(Font font) {
        Args.nullNotPermitted(font, "font");
        this.sublabelFont = font;
        notifyListeners(new AxisChangeEvent(this));
    }

    /**
     * Returns the paint for the sublabels.
     *
     * @return The paint (never {@code null}).
     *
     * @see #setSubLabelPaint(Paint)
     */
    public Paint getSubLabelPaint() {
        return this.sublabelPaint;
    }

    /**
     * Sets the paint for the sublabels and sends an {@link AxisChangeEvent}
     * to all registered listeners.
     *
     * @param paint  the paint ({@code null} not permitted).
     *
     * @see #getSubLabelPaint()
     */
    public void setSubLabelPaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.sublabelPaint = paint;
        notifyListeners(new AxisChangeEvent(this));
    }

    /**
     * Adds a sublabel for a category.
     *
     * @param category  the category.
     * @param label  the label.
     */
    public void addSubLabel(Comparable category, String label) {
        this.sublabels.put(category, label);
    }

    /**
     * Overrides the default behaviour by adding the sublabel to the text
     * block that is used for the category label.
     *
     * @param category  the category.
     * @param width  the width (not used yet).
     * @param edge  the location of the axis.
     * @param g2  the graphics device.
     *
     * @return A label.
     */
    @Override
    protected TextBlock createLabel(Comparable category, float width,
                                    RectangleEdge edge, Graphics2D g2) {
        TextBlock label = super.createLabel(category, width, edge, g2);
        String s = (String) this.sublabels.get(category);
        if (s != null) {
            if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
                TextLine line = new TextLine(s, this.sublabelFont,
                        this.sublabelPaint);
                label.addLine(line);
            }
            else if (edge == RectangleEdge.LEFT
                    || edge == RectangleEdge.RIGHT) {
                TextLine line = label.getLastLine();
                if (line != null) {
                    line.addFragment(new TextFragment("  " + s,
                            this.sublabelFont, this.sublabelPaint));
                }
            }
        }
        return label;
    }

    /**
     * Tests this axis for equality with an arbitrary object.
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
        if (!(obj instanceof ExtendedCategoryAxis)) {
            return false;
        }
        ExtendedCategoryAxis that = (ExtendedCategoryAxis) obj;
        if (!this.sublabelFont.equals(that.sublabelFont)) {
            return false;
        }
        if (!PaintUtils.equal(this.sublabelPaint, that.sublabelPaint)) {
            return false;
        }
        if (!this.sublabels.equals(that.sublabels)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a clone of this axis.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if there is a problem cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ExtendedCategoryAxis clone = (ExtendedCategoryAxis) super.clone();
        clone.sublabels = new HashMap(this.sublabels);
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
        SerialUtils.writePaint(this.sublabelPaint, stream);
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
        this.sublabelPaint = SerialUtils.readPaint(stream);
    }

}
