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
 * ----------
 * Range.java
 * ----------
 * (C) Copyright 2002-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Chuanhao Chiu;
 *                   Bill Kelemen;
 *                   Nicolas Brodu;
 *                   Sergei Ivanov;
 *                   Tracy Hiltbrand (equals complies with EqualsVerifier);
 *
 */

package org.jfree.data;

import java.io.Serializable;
import org.jfree.chart.util.Args;

/**
 * Represents an immutable range of values.
 */
public strictfp class Range implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -906333695431863380L;

    /** The lower bound of the range. */
    private final double lower;

    /** The upper bound of the range. */
    private final double upper;

    /**
     * Creates a new range.
     *
     * @param lower  the lower bound (must be &lt;= upper bound).
     * @param upper  the upper bound (must be &gt;= lower bound).
     */
    public Range(double lower, double upper) {
        if (lower > upper) {
            String msg = "Range(double, double): require lower (" + lower
                + ") <= upper (" + upper + ").";
            throw new IllegalArgumentException(msg);
        }
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns the lower bound for the range.
     *
     * @return The lower bound.
     */
    public double getLowerBound() {
        return this.lower;
    }

    /**
     * Returns the upper bound for the range.
     *
     * @return The upper bound.
     */
    public double getUpperBound() {
        return this.upper;
    }

    /**
     * Returns the length of the range.
     *
     * @return The length.
     */
    public double getLength() {
        return this.upper - this.lower;
    }

    /**
     * Returns the central value for the range.
     *
     * @return The central value.
     */
    public double getCentralValue() {
        return this.lower / 2.0 + this.upper / 2.0;
    }

    /**
     * Returns {@code true} if the range contains the specified value and
     * {@code false} otherwise.
     *
     * @param value  the value to lookup.
     *
     * @return {@code true} if the range contains the specified value.
     */
    public boolean contains(double value) {
        return (value >= this.lower && value <= this.upper);
    }

    /**
     * Returns {@code true} if the range contains the specified range and
     * {@code false} otherwise.
     *
     * @param range the range to lookup.
     *
     * @return {@code true} if the range contains the specified range.
     */
    public boolean contains(Range range) {
        return (range.lower >= this.lower && range.upper <= this.upper);
    }

    /**
     * Returns {@code true} if the range intersects with the specified
     * range, and {@code false} otherwise.
     *
     * @param b0  the lower bound (should be &lt;= b1).
     * @param b1  the upper bound (should be &gt;= b0).
     *
     * @return A boolean.
     */
    public boolean intersects(double b0, double b1) {
        if (b0 <= this.lower) {
            return (b1 > this.lower);
        }
        else {
            return (b0 < this.upper && b1 >= b0);
        }
    }

    /**
     * Returns {@code true} if the range intersects with the specified
     * range, and {@code false} otherwise.
     *
     * @param range  another range ({@code null} not permitted).
     *
     * @return A boolean.
     */
    public boolean intersects(Range range) {
        return intersects(range.getLowerBound(), range.getUpperBound());
    }

    /**
     * Returns the value within the range that is closest to the specified
     * value.
     *
     * @param value  the value.
     *
     * @return The constrained value.
     */
    public double constrain(double value) {
        if (contains(value)) {
            return value;
        }
        if (value > this.upper) {
            return this.upper;
        }
        if (value < this.lower) {
            return this.lower;
        }
        return value; // covers Double.NaN
    }

    /**
     * Creates a new range by combining two existing ranges.
     * <P>
     * Note that:
     * <ul>
     *   <li>either range can be {@code null}, in which case the other
     *       range is returned;</li>
     *   <li>if both ranges are {@code null} the return value is
     *       {@code null}.</li>
     * </ul>
     *
     * @param range1  the first range ({@code null} permitted).
     * @param range2  the second range ({@code null} permitted).
     *
     * @return A new range (possibly {@code null}).
     */
    public static Range combine(Range range1, Range range2) {
        if (range1 == null) {
            return range2;
        }
        if (range2 == null) {
            return range1;
        }
        double l = Math.min(range1.getLowerBound(), range2.getLowerBound());
        double u = Math.max(range1.getUpperBound(), range2.getUpperBound());
        return new Range(l, u);
    }

    /**
     * Returns a new range that spans both {@code range1} and
     * {@code range2}.  This method has a special handling to ignore
     * Double.NaN values.
     *
     * @param range1  the first range ({@code null} permitted).
     * @param range2  the second range ({@code null} permitted).
     *
     * @return A new range (possibly {@code null}).
     */
    public static Range combineIgnoringNaN(Range range1, Range range2) {
        if (range1 == null) {
            if (range2 != null && range2.isNaNRange()) {
                return null;
            }
            return range2;
        }
        if (range2 == null) {
            if (range1.isNaNRange()) {
                return null;
            }
            return range1;
        }
        double l = min(range1.getLowerBound(), range2.getLowerBound());
        double u = max(range1.getUpperBound(), range2.getUpperBound());
        if (Double.isNaN(l) && Double.isNaN(u)) {
            return null;
        }
        return new Range(l, u);
    }

    /**
     * Returns the minimum value.  If either value is NaN, the other value is
     * returned.  If both are NaN, NaN is returned.
     *
     * @param d1  value 1.
     * @param d2  value 2.
     *
     * @return The minimum of the two values.
     */
    private static double min(double d1, double d2) {
        if (Double.isNaN(d1)) {
            return d2;
        }
        if (Double.isNaN(d2)) {
            return d1;
        }
        return Math.min(d1, d2);
    }

    private static double max(double d1, double d2) {
        if (Double.isNaN(d1)) {
            return d2;
        }
        if (Double.isNaN(d2)) {
            return d1;
        }
        return Math.max(d1, d2);
    }

    /**
     * Returns a range that includes all the values in the specified
     * {@code range} AND the specified {@code value}.
     *
     * @param range  the range ({@code null} permitted).
     * @param value  the value that must be included.
     *
     * @return A range.
     */
    public static Range expandToInclude(Range range, double value) {
        if (range == null) {
            return new Range(value, value);
        }
        if (value < range.getLowerBound()) {
            return new Range(value, range.getUpperBound());
        }
        else if (value > range.getUpperBound()) {
            return new Range(range.getLowerBound(), value);
        }
        else {
            return range;
        }
    }

    /**
     * Creates a new range by adding margins to an existing range.
     *
     * @param range  the range ({@code null} not permitted).
     * @param lowerMargin  the lower margin (expressed as a percentage of the
     *                     range length).
     * @param upperMargin  the upper margin (expressed as a percentage of the
     *                     range length).
     *
     * @return The expanded range.
     */
    public static Range expand(Range range,
                               double lowerMargin, double upperMargin) {
        Args.nullNotPermitted(range, "range");
        double length = range.getLength();
        double lower = range.getLowerBound() - length * lowerMargin;
        double upper = range.getUpperBound() + length * upperMargin;
        if (lower > upper) {
            lower = lower / 2.0 + upper / 2.0;
            upper = lower;
        }
        return new Range(lower, upper);
    }

    /**
     * Shifts the range by the specified amount.
     *
     * @param base  the base range ({@code null} not permitted).
     * @param delta  the shift amount.
     *
     * @return A new range.
     */
    public static Range shift(Range base, double delta) {
        return shift(base, delta, false);
    }

    /**
     * Shifts the range by the specified amount.
     *
     * @param base  the base range ({@code null} not permitted).
     * @param delta  the shift amount.
     * @param allowZeroCrossing  a flag that determines whether the
     *                           bounds of the range are allowed to cross
     *                           zero after adjustment.
     *
     * @return A new range.
     */
    public static Range shift(Range base, double delta,
                              boolean allowZeroCrossing) {
        Args.nullNotPermitted(base, "base");
        if (allowZeroCrossing) {
            return new Range(base.getLowerBound() + delta,
                    base.getUpperBound() + delta);
        }
        else {
            return new Range(shiftWithNoZeroCrossing(base.getLowerBound(),
                    delta), shiftWithNoZeroCrossing(base.getUpperBound(),
                    delta));
        }
    }

    /**
     * Returns the given {@code value} adjusted by {@code delta} but
     * with a check to prevent the result from crossing {@code 0.0}.
     *
     * @param value  the value.
     * @param delta  the adjustment.
     *
     * @return The adjusted value.
     */
    private static double shiftWithNoZeroCrossing(double value, double delta) {
        if (value > 0.0) {
            return Math.max(value + delta, 0.0);
        }
        else if (value < 0.0) {
            return Math.min(value + delta, 0.0);
        }
        else {
            return value + delta;
        }
    }

    /**
     * Scales the range by the specified factor.
     *
     * @param base the base range ({@code null} not permitted).
     * @param factor the scaling factor (must be non-negative).
     *
     * @return A new range.
     */
    public static Range scale(Range base, double factor) {
        Args.nullNotPermitted(base, "base");
        if (factor < 0) {
            throw new IllegalArgumentException("Negative 'factor' argument.");
        }
        return new Range(base.getLowerBound() * factor,
                base.getUpperBound() * factor);
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
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range) obj;
        if (Double.doubleToLongBits(this.lower) !=
            Double.doubleToLongBits(range.lower)) {
            return false;
        }
        if (Double.doubleToLongBits(this.upper) !=
            Double.doubleToLongBits(range.upper)) {
            return false;
        }
        return true;
    }

    /**
     * Returns {@code true} if both the lower and upper bounds are
     * {@code Double.NaN}, and {@code false} otherwise.
     *
     * @return A boolean.
     */
    public boolean isNaNRange() {
        return Double.isNaN(this.lower) && Double.isNaN(this.upper);
    }

    /**
     * Returns a hash code.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        int result = Double.hashCode(this.lower);
        return 29 * result + Double.hashCode(this.upper);
    }

    /**
     * Returns a string representation of this Range.
     *
     * @return A String "Range[lower,upper]" where lower=lower range and
     *         upper=upper range.
     */
    @Override
    public String toString() {
        return ("Range[" + this.lower + "," + this.upper + "]");
    }

}
