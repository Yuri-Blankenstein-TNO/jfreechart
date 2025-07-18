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
 * LegendRenderingOrder.java
 * -------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  Angel;
 * Contributor(s):   David Gilbert;
 *
 */

package org.jfree.chart;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Represents the order for rendering legend items.
 */
public final class LegendRenderingOrder implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -3832486612685808616L;

    /** In order. */
    public static final LegendRenderingOrder STANDARD
            = new LegendRenderingOrder("LegendRenderingOrder.STANDARD");

    /** In reverse order. */
    public static final LegendRenderingOrder REVERSE
            = new LegendRenderingOrder("LegendRenderingOrder.REVERSE");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private LegendRenderingOrder(String name) {
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
        if (!(obj instanceof LegendRenderingOrder)) {
            return false;
        }
        LegendRenderingOrder order = (LegendRenderingOrder) obj;
        if (!this.name.equals(order.toString())) {
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
        if (this.equals(LegendRenderingOrder.STANDARD)) {
            return LegendRenderingOrder.STANDARD;
        }
        else if (this.equals(LegendRenderingOrder.REVERSE)) {
            return LegendRenderingOrder.REVERSE;
        }
        return null;
    }

}
