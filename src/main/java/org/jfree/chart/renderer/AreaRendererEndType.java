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
 * ------------------------
 * AreaRendererEndType.java
 * ------------------------
 * (C) Copyright 2004-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * An enumeration of the 'end types' for an area renderer.
 */
public final class AreaRendererEndType implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -1774146392916359839L;

    /**
     * The area tapers from the first or last value down to zero.
     */
    public static final AreaRendererEndType TAPER = new AreaRendererEndType(
            "AreaRendererEndType.TAPER");

    /**
     * The area is truncated at the first or last value.
     */
    public static final AreaRendererEndType TRUNCATE = new AreaRendererEndType(
            "AreaRendererEndType.TRUNCATE");

    /**
     * The area is levelled at the first or last value.
     */
    public static final AreaRendererEndType LEVEL = new AreaRendererEndType(
            "AreaRendererEndType.LEVEL");

    /** The name. */
    private final String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private AreaRendererEndType(String name) {
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
        if (!(obj instanceof AreaRendererEndType)) {
            return false;
        }
        AreaRendererEndType that = (AreaRendererEndType) obj;
        if (!this.name.equals(that.toString())) {
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
        Object result = null;
        if (this.equals(AreaRendererEndType.LEVEL)) {
            result = AreaRendererEndType.LEVEL;
        }
        else if (this.equals(AreaRendererEndType.TAPER)) {
            result = AreaRendererEndType.TAPER;
        }
        else if (this.equals(AreaRendererEndType.TRUNCATE)) {
            result = AreaRendererEndType.TRUNCATE;
        }
        return result;
    }

}
