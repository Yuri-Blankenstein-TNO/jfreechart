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
 * CategoryLabelPosition.java
 * --------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.chart.axis;

import java.io.Serializable;
import java.util.Objects;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.Args;

/**
 * The attributes that control the position of the labels for the categories on
 * a {@link CategoryAxis}. Instances of this class are immutable and other
 * JFreeChart classes rely upon this.
 */
public class CategoryLabelPosition implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 5168681143844183864L;

    /** The category anchor point. */
    private final RectangleAnchor categoryAnchor;

    /** The text block anchor. */
    private final TextBlockAnchor labelAnchor;

    /** The rotation anchor. */
    private final TextAnchor rotationAnchor;

    /** The rotation angle (in radians). */
    private final double angle;

    /** The width calculation type. */
    private final CategoryLabelWidthType widthType;

    /**
     * The maximum label width as a percentage of the category space or the
     * range space.
     */
    private final float widthRatio;

    /**
     * Creates a new position record with default settings.
     */
    public CategoryLabelPosition() {
        this(RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER,
                TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
    }

    /**
     * Creates a new category label position record.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor,
                                 TextBlockAnchor labelAnchor) {
        // argument checking delegated...
        this(categoryAnchor, labelAnchor, TextAnchor.CENTER, 0.0,
                CategoryLabelWidthType.CATEGORY, 0.95f);
    }

    /**
     * Creates a new category label position record.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     * @param widthType  the width type ({@code null} not permitted).
     * @param widthRatio  the maximum label width as a percentage (of the
     *                    category space or the range space).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor,
            TextBlockAnchor labelAnchor, CategoryLabelWidthType widthType,
            float widthRatio) {
        // argument checking delegated...
        this(categoryAnchor, labelAnchor, TextAnchor.CENTER, 0.0, widthType,
                widthRatio);
    }

    /**
     * Creates a new position record.  The item label anchor is a point
     * relative to the data item (dot, bar or other visual item) on a chart.
     * The item label is aligned by aligning the text anchor with the item
     * label anchor.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     * @param rotationAnchor  the rotation anchor ({@code null} not
     *                        permitted).
     * @param angle  the rotation angle ({@code null} not permitted).
     * @param widthType  the width type ({@code null} not permitted).
     * @param widthRatio  the maximum label width as a percentage (of the
     *                    category space or the range space).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor,
            TextBlockAnchor labelAnchor, TextAnchor rotationAnchor, 
            double angle, CategoryLabelWidthType widthType, float widthRatio) {

        Args.nullNotPermitted(categoryAnchor, "categoryAnchor");
        Args.nullNotPermitted(labelAnchor, "labelAnchor");
        Args.nullNotPermitted(rotationAnchor, "rotationAnchor");
        Args.nullNotPermitted(widthType, "widthType");

        this.categoryAnchor = categoryAnchor;
        this.labelAnchor = labelAnchor;
        this.rotationAnchor = rotationAnchor;
        this.angle = angle;
        this.widthType = widthType;
        this.widthRatio = widthRatio;

    }

    /**
     * Returns the item label anchor.
     *
     * @return The item label anchor (never {@code null}).
     */
    public RectangleAnchor getCategoryAnchor() {
        return this.categoryAnchor;
    }

    /**
     * Returns the text block anchor.
     *
     * @return The text block anchor (never {@code null}).
     */
    public TextBlockAnchor getLabelAnchor() {
        return this.labelAnchor;
    }

    /**
     * Returns the rotation anchor point.
     *
     * @return The rotation anchor point (never {@code null}).
     */
    public TextAnchor getRotationAnchor() {
        return this.rotationAnchor;
    }

    /**
     * Returns the angle of rotation for the label.
     *
     * @return The angle (in radians).
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Returns the width calculation type.
     *
     * @return The width calculation type (never {@code null}).
     */
    public CategoryLabelWidthType getWidthType() {
        return this.widthType;
    }

    /**
     * Returns the ratio used to calculate the maximum category label width.
     *
     * @return The ratio.
     */
    public float getWidthRatio() {
        return this.widthRatio;
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
        if (!(obj instanceof CategoryLabelPosition)) {
            return false;
        }
        CategoryLabelPosition that = (CategoryLabelPosition) obj;
        if (Double.doubleToLongBits(this.angle) !=
            Double.doubleToLongBits(that.angle)) {
            return false;
        }
        if (Float.floatToIntBits(this.widthRatio) !=
            Float.floatToIntBits(that.widthRatio)) {
            return false;
        }
        if (!Objects.equals(this.categoryAnchor,that.categoryAnchor)) {
            return false;
        }
        if (!Objects.equals(this.labelAnchor, that.labelAnchor)) {
            return false;
        }
        if (!Objects.equals(this.rotationAnchor, that.rotationAnchor)) {
            return false;
        }
        if (!Objects.equals(this.widthType, that.widthType)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        int result = 19;
        result = 61 * result + Objects.hashCode(this.categoryAnchor);
        result = 61 * result + Objects.hashCode(this.labelAnchor);
        result = 61 * result + Objects.hashCode(this.rotationAnchor);
        result = 61 * result + (int) (Double.doubleToLongBits(this.angle) ^
                                     (Double.doubleToLongBits(this.angle) >>> 32));
        result = 61 * result + Objects.hashCode(this.widthType);
        result = 61 * result + Float.floatToIntBits(this.widthRatio);
        return result;
    }

}
