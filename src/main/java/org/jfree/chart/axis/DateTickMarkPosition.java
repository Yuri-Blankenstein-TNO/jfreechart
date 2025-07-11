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
 * DateTickMarkPosition.java
 * -------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Used to indicate the required position of tick marks on a date axis relative
 * to the underlying time period.
 */
public final class DateTickMarkPosition implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 2540750672764537240L;

    /** Start of period. */
    public static final DateTickMarkPosition START
        = new DateTickMarkPosition("DateTickMarkPosition.START");

    /** Middle of period. */
    public static final DateTickMarkPosition MIDDLE
        = new DateTickMarkPosition("DateTickMarkPosition.MIDDLE");

    /** End of period. */
    public static final DateTickMarkPosition END
        = new DateTickMarkPosition("DateTickMarkPosition.END");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private DateTickMarkPosition(String name) {
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
        if (!(obj instanceof DateTickMarkPosition)) {
            return false;
        }
        DateTickMarkPosition position = (DateTickMarkPosition) obj;
        if (!this.name.equals(position.toString())) {
            return false;
        }
        return true;

    }

    /**
     * Ensures that serialization returns the unique instances.
     *
     * @return The object.
     *
     * @throws ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        if (this.equals(DateTickMarkPosition.START)) {
            return DateTickMarkPosition.START;
        }
        else if (this.equals(DateTickMarkPosition.MIDDLE)) {
            return DateTickMarkPosition.MIDDLE;
        }
        else if (this.equals(DateTickMarkPosition.END)) {
            return DateTickMarkPosition.END;
        }
        return null;
    }


}
