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
 * TickUnitSource.java
 * -------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

/**
 * An interface used by the {@link DateAxis} and {@link NumberAxis} classes to
 * obtain a suitable {@link TickUnit}.
 */
public interface TickUnitSource {

    /**
     * Returns the smallest tick unit available in the source that is larger
     * than {@code unit} or, if there is no larger unit, returns {@code unit}.
     *
     * @param unit  the unit ({@code null} not permitted).
     *
     * @return A tick unit that is larger than the supplied unit.
     */
    TickUnit getLargerTickUnit(TickUnit unit);

    /**
     * Returns the tick unit in the collection that is greater than or equal
     * to (in size) the specified unit.
     *
     * @param unit  the unit.
     *
     * @return A unit from the collection.
     */
    TickUnit getCeilingTickUnit(TickUnit unit);

    /**
     * Returns the smallest tick unit available in the source that is greater 
     * than or equal to the specified size.  If there is no such tick unit,
     * the method should return the largest available tick in the source.
     *
     * @param size  the size.
     *
     * @return A unit from the collection (never {@code null}).
     */
    TickUnit getCeilingTickUnit(double size);

}
