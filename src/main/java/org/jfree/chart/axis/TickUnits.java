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
 * TickUnits.java
 * --------------
 * (C) Copyright 2001-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A collection of tick units, used by the {@link DateAxis} and
 * {@link NumberAxis} classes.
 */
public class TickUnits implements TickUnitSource, Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 1134174035901467545L;

    /** Storage for the tick units. */
    private List tickUnits;

    /**
     * Constructs a new collection of tick units.
     */
    public TickUnits() {
        this.tickUnits = new ArrayList();
    }

    /**
     * Adds a tick unit to the collection.  The tick units are maintained in
     * ascending order.
     *
     * @param unit  the tick unit to add ({@code null} not permitted).
     */
    public void add(TickUnit unit) {
        if (unit == null) {
            throw new NullPointerException("Null 'unit' argument.");
        }
        this.tickUnits.add(unit);
        Collections.sort(this.tickUnits);
    }

    /**
     * Returns the number of tick units in this collection.
     * <P>
     * This method is required for the XML writer.
     *
     * @return The number of units in this collection.
     */
    public int size() {
        return this.tickUnits.size();
    }

    /**
     * Returns the tickunit on the given position.
     * <P>
     * This method is required for the XML writer.
     *
     * @param pos the position in the list.
     *
     * @return The tickunit.
     */
    public TickUnit get(int pos) {
        return (TickUnit) this.tickUnits.get(pos);
    }

    /**
     * Returns a tick unit that is larger than the supplied unit.
     *
     * @param unit   the unit.
     *
     * @return A tick unit that is larger than the supplied unit.
     */
    @Override
    public TickUnit getLargerTickUnit(TickUnit unit) {
        int index = Collections.binarySearch(this.tickUnits, unit);
        if (index >= 0) {
            index = index + 1;
        }
        else {
            index = -index;
        }

        return (TickUnit) this.tickUnits.get(Math.min(index,
                this.tickUnits.size() - 1));
    }

    /**
     * Returns the tick unit in the collection that is greater than or equal
     * to (in size) the specified unit.
     *
     * @param unit  the unit.
     *
     * @return A unit from the collection.
     */
    @Override
    public TickUnit getCeilingTickUnit(TickUnit unit) {
        int index = Collections.binarySearch(this.tickUnits, unit);
        if (index >= 0) {
            return (TickUnit) this.tickUnits.get(index);
        }
        else {
            index = -(index + 1);
            return (TickUnit) this.tickUnits.get(Math.min(index,
                    this.tickUnits.size() - 1));
        }
    }

    /**
     * Returns the tick unit in the collection that is greater than or equal
     * to the specified size.
     *
     * @param size  the size.
     *
     * @return A unit from the collection.
     */
    @Override
    public TickUnit getCeilingTickUnit(double size) {
        return getCeilingTickUnit(new NumberTickUnit(size,
                NumberFormat.getInstance()));
    }

    /**
     * Returns a clone of the collection.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if an item in the collection does not
     *         support cloning.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        TickUnits clone = (TickUnits) super.clone();
        clone.tickUnits = new java.util.ArrayList(this.tickUnits);
        return clone;
    }

    /**
     * Tests an object for equality with this instance.
     *
     * @param obj  the object to test ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TickUnits)) {
            return false;
        }
        TickUnits that = (TickUnits) obj;
        return that.tickUnits.equals(this.tickUnits);
    }

}
