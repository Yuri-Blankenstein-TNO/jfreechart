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
 * -------------------
 * CategoryAnchor.java
 * -------------------
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
 * Used to indicate one of three positions within a category:
 * {@code START}, {@code MIDDLE} and {@code END}.
 */
public final class CategoryAnchor implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -2604142742210173810L;

    /** Start of period. */
    public static final CategoryAnchor START
        = new CategoryAnchor("CategoryAnchor.START");

    /** Middle of period. */
    public static final CategoryAnchor MIDDLE
        = new CategoryAnchor("CategoryAnchor.MIDDLE");

    /** End of period. */
    public static final CategoryAnchor END
        = new CategoryAnchor("CategoryAnchor.END");

    /** The name. */
    private final String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private CategoryAnchor(String name) {
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
        if (!(obj instanceof CategoryAnchor)) {
            return false;
        }
        CategoryAnchor position = (CategoryAnchor) obj;
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
        if (this.equals(CategoryAnchor.START)) {
            return CategoryAnchor.START;
        }
        else if (this.equals(CategoryAnchor.MIDDLE)) {
            return CategoryAnchor.MIDDLE;
        }
        else if (this.equals(CategoryAnchor.END)) {
            return CategoryAnchor.END;
        }
        return null;
    }

}
