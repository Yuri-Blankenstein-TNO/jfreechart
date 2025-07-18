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
 * -----------------------------
 * MeanAndStandardDeviation.java
 * -----------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.statistics;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple data structure that holds a mean value and a standard deviation
 * value.  This is used in the
 * {@link org.jfree.data.statistics.DefaultStatisticalCategoryDataset} class.
 */
public class MeanAndStandardDeviation implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7413468697315721515L;

    /** The mean. */
    private Number mean;

    /** The standard deviation. */
    private Number standardDeviation;

    /**
     * Creates a new mean and standard deviation record.
     *
     * @param mean  the mean.
     * @param standardDeviation  the standard deviation.
     */
    public MeanAndStandardDeviation(double mean, double standardDeviation) {
        this(Double.valueOf(mean), Double.valueOf(standardDeviation));
    }

    /**
     * Creates a new mean and standard deviation record.
     *
     * @param mean  the mean ({@code null} permitted).
     * @param standardDeviation  the standard deviation ({@code null}
     *                           permitted.
     */
    public MeanAndStandardDeviation(Number mean, Number standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    /**
     * Returns the mean.
     *
     * @return The mean.
     */
    public Number getMean() {
        return this.mean;
    }

    /**
     * Returns the mean as a double primitive.  If the underlying mean is
     * {@code null}, this method will return {@code Double.NaN}.
     *
     * @return The mean.
     *
     * @see #getMean()
     */
    public double getMeanValue() {
        double result = Double.NaN;
        if (this.mean != null) {
            result = this.mean.doubleValue();
        }
        return result;
    }

    /**
     * Returns the standard deviation.
     *
     * @return The standard deviation.
     */
    public Number getStandardDeviation() {
        return this.standardDeviation;
    }

    /**
     * Returns the standard deviation as a double primitive.  If the underlying
     * standard deviation is {@code null}, this method will return
     * {@code Double.NaN}.
     *
     * @return The standard deviation.
     */
    public double getStandardDeviationValue() {
        double result = Double.NaN;
        if (this.standardDeviation != null) {
            result = this.standardDeviation.doubleValue();
        }
        return result;
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
        if (!(obj instanceof MeanAndStandardDeviation)) {
            return false;
        }
        MeanAndStandardDeviation that = (MeanAndStandardDeviation) obj;
        if (!Objects.equals(this.mean, that.mean)) {
            return false;
        }
        if (!Objects.equals(this.standardDeviation, that.standardDeviation)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representing this instance.
     *
     * @return A string.
     */
    @Override
    public String toString() {
        return "[" + this.mean + ", " + this.standardDeviation + "]";
    }

}