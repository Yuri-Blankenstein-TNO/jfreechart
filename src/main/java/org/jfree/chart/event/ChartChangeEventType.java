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
 * ChartChangeEventType.java
 * -------------------------
 * (C) Copyright 2005-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.event;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Defines tokens used to indicate an event type.
 */
public final class ChartChangeEventType implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 5481917022435735602L;

    /** GENERAL. */
    public static final ChartChangeEventType GENERAL
            = new ChartChangeEventType("ChartChangeEventType.GENERAL");

    /** NEW_DATASET. */
    public static final ChartChangeEventType NEW_DATASET
            = new ChartChangeEventType("ChartChangeEventType.NEW_DATASET");

    /** DATASET_UPDATED. */
    public static final ChartChangeEventType DATASET_UPDATED
            = new ChartChangeEventType("ChartChangeEventType.DATASET_UPDATED");

    /** The name. */
    private final String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private ChartChangeEventType(String name) {
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
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChartChangeEventType)) {
            return false;
        }
        ChartChangeEventType that = (ChartChangeEventType) obj;
        if (!this.name.equals(that.toString())) {
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
        if (this.equals(ChartChangeEventType.GENERAL)) {
            return ChartChangeEventType.GENERAL;
        }
        else if (this.equals(ChartChangeEventType.NEW_DATASET)) {
            return ChartChangeEventType.NEW_DATASET;
        }
        else if (this.equals(ChartChangeEventType.DATASET_UPDATED)) {
            return ChartChangeEventType.DATASET_UPDATED;
        }
        return null;
    }

}
