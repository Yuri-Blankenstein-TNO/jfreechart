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
 * --------------
 * DateRange.java
 * --------------
 * (C) Copyright 2002-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Bill Kelemen;
 *
 */

package org.jfree.data.time;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.jfree.data.Range;

/**
 * A range specified in terms of two {@code java.util.Date} objects.
 * Instances of this class are immutable.
 */
public class DateRange extends Range implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4705682568375418157L;

    /** The lower bound for the range. */
    private final long lowerDate;

    /** The upper bound for the range. */
    private final long upperDate;

    /**
     * Default constructor.
     */
    public DateRange() {
        this(new Date(0), new Date(1));
    }

    /**
     * Constructs a new range.
     *
     * @param lower  the lower bound ({@code null} not permitted).
     * @param upper  the upper bound ({@code null} not permitted).
     */
    public DateRange(Date lower, Date upper) {
        super(lower.getTime(), upper.getTime());
        this.lowerDate = lower.getTime();
        this.upperDate = upper.getTime();
    }

    /**
     * Constructs a new range using two values that will be interpreted as
     * "milliseconds since midnight GMT, 1-Jan-1970".
     *
     * @param lower  the lower (oldest) date.
     * @param upper  the upper (most recent) date.
     */
    public DateRange(double lower, double upper) {
        super(lower, upper);
        this.lowerDate = (long) lower;
        this.upperDate = (long) upper;
    }

    /**
     * Constructs a new range that is based on another {@link Range}.  The
     * other range does not have to be a {@link DateRange}.  If it is not, the
     * upper and lower bounds are evaluated as milliseconds since midnight
     * GMT, 1-Jan-1970.
     *
     * @param other  the other range ({@code null} not permitted).
     */
    public DateRange(Range other) {
        this(other.getLowerBound(), other.getUpperBound());
    }

    /**
     * Returns the lower (earlier) date for the range.
     *
     * @return The lower date for the range.
     *
     * @see #getLowerMillis()
     */
    public Date getLowerDate() {
        return new Date(this.lowerDate);
    }

    /**
     * Returns the lower bound of the range in milliseconds.
     *
     * @return The lower bound.
     *
     * @see #getLowerDate()
     */
    public long getLowerMillis() {
        return this.lowerDate;
    }

    /**
     * Returns the upper (later) date for the range.
     *
     * @return The upper date for the range.
     *
     * @see #getUpperMillis()
     */
    public Date getUpperDate() {
        return new Date(this.upperDate);
    }

    /**
     * Returns the upper bound of the range in milliseconds.
     *
     * @return The upper bound.
     *
     * @see #getUpperDate()
     */
    public long getUpperMillis() {
        return this.upperDate;
    }

    /**
     * Returns a string representing the date range (useful for debugging).
     *
     * @return A string representing the date range.
     */
    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ISO_DATE_TIME;
        return "[" + df.format(getLowerDate().toInstant()) + " --> "
                + df.format(getUpperDate().toInstant()) + "]";
    }

}
