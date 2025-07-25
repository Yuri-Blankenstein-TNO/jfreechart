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
 * --------------------
 * TimePeriodValue.java
 * --------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import java.io.Serializable;
import java.util.Objects;

import org.jfree.chart.util.Args;

/**
 * Represents a time period and an associated value.
 */
public class TimePeriodValue implements Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 3390443360845711275L;

    /** The time period. */
    private TimePeriod period;

    /** The value associated with the time period. */
    private Number value;

    /**
     * Constructs a new data item.
     *
     * @param period  the time period ({@code null} not permitted).
     * @param value  the value associated with the time period.
     *
     * @throws IllegalArgumentException if {@code period} is {@code null}.
     */
    public TimePeriodValue(TimePeriod period, Number value) {
        Args.nullNotPermitted(period, "period");
        this.period = period;
        this.value = value;
    }

    /**
     * Constructs a new data item.
     *
     * @param period  the time period ({@code null} not permitted).
     * @param value  the value associated with the time period.
     *
     * @throws IllegalArgumentException if {@code period} is {@code null}.
     */
    public TimePeriodValue(TimePeriod period, double value) {
        this(period, Double.valueOf(value));
    }

    /**
     * Returns the time period.
     *
     * @return The time period (never {@code null}).
     */
    public TimePeriod getPeriod() {
        return this.period;
    }

    /**
     * Returns the value.
     *
     * @return The value (possibly {@code null}).
     *
     * @see #setValue(Number)
     */
    public Number getValue() {
        return this.value;
    }

    /**
     * Sets the value for this data item.
     *
     * @param value  the new value ({@code null} permitted).
     *
     * @see #getValue()
     */
    public void setValue(Number value) {
        this.value = value;
    }

    /**
     * Tests this object for equality with the target object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimePeriodValue)) {
            return false;
        }
        TimePeriodValue timePeriodValue = (TimePeriodValue) obj;
        if (!Objects.equals(this.period, timePeriodValue.period)) {
            return false;
        }
        if (!Objects.equals(this.value, timePeriodValue.value)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        int result;
        result = (this.period != null ? this.period.hashCode() : 0);
        result = 29 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    /**
     * Clones the object.
     * <P>
     * Note: no need to clone the period or value since they are immutable
     * classes.
     *
     * @return A clone.
     */
    @Override
    public Object clone() {
        Object clone;
        try {
            clone = super.clone();
        }
        catch (CloneNotSupportedException e) { // won't get here...
            throw new RuntimeException(e);
        }
        return clone;
    }

    /**
     * Returns a string representing this instance, primarily for use in
     * debugging.
     *
     * @return A string.
     */
    @Override
    public String toString() {
        return "TimePeriodValue[" + getPeriod() + "," + getValue() + "]";
    }

}
