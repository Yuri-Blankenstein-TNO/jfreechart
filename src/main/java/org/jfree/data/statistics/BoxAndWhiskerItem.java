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
 * ----------------------
 * BoxAndWhiskerItem.java
 * ----------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.statistics;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents one data item within a box-and-whisker dataset.  Instances of
 * this class are immutable.
 */
public class BoxAndWhiskerItem implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7329649623148167423L;

    /** The mean. */
    private final Number mean;

    /** The median. */
    private final Number median;

    /** The first quarter. */
    private final Number q1;

    /** The third quarter. */
    private final Number q3;

    /** The minimum regular value. */
    private final Number minRegularValue;

    /** The maximum regular value. */
    private final Number maxRegularValue;

    /** The minimum outlier. */
    private final Number minOutlier;

    /** The maximum outlier. */
    private final Number maxOutlier;

    /** The outliers. */
    private final List<? extends Number> outliers;

    /**
     * Creates a new box-and-whisker item.
     *
     * @param mean  the mean ({@code null} permitted).
     * @param median  the median ({@code null} permitted).
     * @param q1  the first quartile ({@code null} permitted).
     * @param q3  the third quartile ({@code null} permitted).
     * @param minRegularValue  the minimum regular value ({@code null}
     *                         permitted).
     * @param maxRegularValue  the maximum regular value ({@code null}
     *                         permitted).
     * @param minOutlier  the minimum outlier ({@code null} permitted).
     * @param maxOutlier  the maximum outlier ({@code null} permitted).
     * @param outliers  the outliers ({@code null} permitted).
     */
    public BoxAndWhiskerItem(Number mean, Number median, Number q1, Number q3,
            Number minRegularValue, Number maxRegularValue, Number minOutlier,
            Number maxOutlier, List<? extends Number> outliers) {

        this.mean = mean;
        this.median = median;
        this.q1 = q1;
        this.q3 = q3;
        this.minRegularValue = minRegularValue;
        this.maxRegularValue = maxRegularValue;
        this.minOutlier = minOutlier;
        this.maxOutlier = maxOutlier;
        this.outliers = outliers;

    }

    /**
     * Creates a new box-and-whisker item.
     *
     * @param mean  the mean.
     * @param median  the median
     * @param q1  the first quartile.
     * @param q3  the third quartile.
     * @param minRegularValue  the minimum regular value.
     * @param maxRegularValue  the maximum regular value.
     * @param minOutlier  the minimum outlier value.
     * @param maxOutlier  the maximum outlier value.
     * @param outliers  a list of the outliers.
     */
    public BoxAndWhiskerItem(double mean, double median, double q1, double q3,
            double minRegularValue, double maxRegularValue, double minOutlier,
            double maxOutlier, List<? extends Number> outliers) {

        // pass values to other constructor
        this(Double.valueOf(mean), Double.valueOf(median), Double.valueOf(q1),
                Double.valueOf(q3), Double.valueOf(minRegularValue),
                Double.valueOf(maxRegularValue), Double.valueOf(minOutlier),
                Double.valueOf(maxOutlier), outliers);

    }

    /**
     * Returns the mean.
     *
     * @return The mean (possibly {@code null}).
     */
    public Number getMean() {
        return this.mean;
    }

    /**
     * Returns the median.
     *
     * @return The median (possibly {@code null}).
     */
    public Number getMedian() {
        return this.median;
    }

    /**
     * Returns the first quartile.
     *
     * @return The first quartile (possibly {@code null}).
     */
    public Number getQ1() {
        return this.q1;
    }

    /**
     * Returns the third quartile.
     *
     * @return The third quartile (possibly {@code null}).
     */
    public Number getQ3() {
        return this.q3;
    }

    /**
     * Returns the minimum regular value.
     *
     * @return The minimum regular value (possibly {@code null}).
     */
    public Number getMinRegularValue() {
        return this.minRegularValue;
    }

    /**
     * Returns the maximum regular value.
     *
     * @return The maximum regular value (possibly {@code null}).
     */
    public Number getMaxRegularValue() {
        return this.maxRegularValue;
    }

    /**
     * Returns the minimum outlier.
     *
     * @return The minimum outlier (possibly {@code null}).
     */
    public Number getMinOutlier() {
        return this.minOutlier;
    }

    /**
     * Returns the maximum outlier.
     *
     * @return The maximum outlier (possibly {@code null}).
     */
    public Number getMaxOutlier() {
        return this.maxOutlier;
    }

    /**
     * Returns a list of outliers.
     *
     * @return A list of outliers (possibly {@code null}).
     */
    public List<Number> getOutliers() {
        if (this.outliers == null) {
            return null;
        }
        return Collections.unmodifiableList(this.outliers);
    }

    /**
     * Returns a string representation of this instance, primarily for
     * debugging purposes.
     *
     * @return A string representation of this instance.
     */
    @Override
    public String toString() {
        return super.toString() + "[mean=" + this.mean + ",median="
                + this.median + ",q1=" + this.q1 + ",q3=" + this.q3 + "]";
    }

    /**
     * Tests this object for equality with an arbitrary object.
     *
     * @param obj  the object to test against ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BoxAndWhiskerItem)) {
            return false;
        }
        BoxAndWhiskerItem that = (BoxAndWhiskerItem) obj;
        if (!Objects.equals(this.mean, that.mean)) {
            return false;
        }
        if (!Objects.equals(this.median, that.median)) {
            return false;
        }
        if (!Objects.equals(this.q1, that.q1)) {
            return false;
        }
        if (!Objects.equals(this.q3, that.q3)) {
            return false;
        }
        if (!Objects.equals(this.minRegularValue,
                that.minRegularValue)) {
            return false;
        }
        if (!Objects.equals(this.maxRegularValue,
                that.maxRegularValue)) {
            return false;
        }
        if (!Objects.equals(this.minOutlier, that.minOutlier)) {
            return false;
        }
        if (!Objects.equals(this.maxOutlier, that.maxOutlier)) {
            return false;
        }
        if (!Objects.equals(this.outliers, that.outliers)) {
            return false;
        }
        return true;
    }

}
