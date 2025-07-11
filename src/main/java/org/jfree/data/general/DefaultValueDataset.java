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
 * DefaultValueDataset.java
 * ------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.general;

import java.io.Serializable;
import java.util.Objects;
import org.jfree.chart.util.PublicCloneable;

/**
 * A dataset that stores a single value (that is possibly {@code null}).
 * This class provides a default implementation of the {@link ValueDataset}
 * interface.
 */
public class DefaultValueDataset extends AbstractDataset
        implements ValueDataset, Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 8137521217249294891L;

    /** The value. */
    private Number value;

    /**
     * Constructs a new dataset, initially empty.
     */
    public DefaultValueDataset() {
        this(null);
    }

    /**
     * Creates a new dataset with the specified value.
     *
     * @param value  the value.
     */
    public DefaultValueDataset(double value) {
        this(Double.valueOf(value));
    }

    /**
     * Creates a new dataset with the specified value.
     *
     * @param value  the initial value ({@code null} permitted).
     */
    public DefaultValueDataset(Number value) {
        super();
        this.value = value;
    }

    /**
     * Returns the value.
     *
     * @return The value (possibly {@code null}).
     */
    @Override
    public Number getValue() {
        return this.value;
    }

    /**
     * Sets the value and sends a {@link DatasetChangeEvent} to all registered
     * listeners.
     *
     * @param value  the new value ({@code null} permitted).
     */
    public void setValue(Number value) {
        this.value = value;
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * Tests this dataset for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ValueDataset) {
            ValueDataset vd = (ValueDataset) obj;
            return Objects.equals(this.value, vd.getValue());
        }
        return false;
    }

    /**
     * Returns a hash code.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        return (this.value != null ? this.value.hashCode() : 0);
    }

}
