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
 * ---------------------
 * TimePeriodAnchor.java
 * ---------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Used to indicate one of three positions in a time period:
 * {@code START}, {@code MIDDLE} and {@code END}.
 */
public final class TimePeriodAnchor implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 2011955697457548862L;

    /** Start of period. */
    public static final TimePeriodAnchor START
        = new TimePeriodAnchor("TimePeriodAnchor.START");

    /** Middle of period. */
    public static final TimePeriodAnchor MIDDLE
        = new TimePeriodAnchor("TimePeriodAnchor.MIDDLE");

    /** End of period. */
    public static final TimePeriodAnchor END
        = new TimePeriodAnchor("TimePeriodAnchor.END");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private TimePeriodAnchor(String name) {
        this.name = name;
    }

    /**
     * Returns a string representing the object.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns {@code true} if this object is equal to the specified
     * object, and {@code false} otherwise.
     *
     * @param obj  the other object.
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimePeriodAnchor)) {
            return false;
        }

        TimePeriodAnchor position = (TimePeriodAnchor) obj;
        if (!this.name.equals(position.name)) {
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
        return this.name.hashCode();
    }

    /**
     * Ensures that serialization returns the unique instances.
     *
     * @return The object.
     *
     * @throws ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        if (this.equals(TimePeriodAnchor.START)) {
            return TimePeriodAnchor.START;
        }
        else if (this.equals(TimePeriodAnchor.MIDDLE)) {
            return TimePeriodAnchor.MIDDLE;
        }
        else if (this.equals(TimePeriodAnchor.END)) {
            return TimePeriodAnchor.END;
        }
        return null;
    }

}
