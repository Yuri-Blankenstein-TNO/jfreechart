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
 * LegendItemEntity.java
 * ---------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.chart.entity;

import java.awt.Shape;
import java.io.Serializable;
import java.util.Objects;

import org.jfree.data.general.Dataset;

/**
 * An entity that represents an item within a legend.
 */
public class LegendItemEntity extends ChartEntity
                              implements Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -7435683933545666702L;

    /**
     * The dataset.
     */
    private Dataset dataset;

    /**
     * The series key.
     */
    private Comparable seriesKey;

    /**
     * Creates a legend item entity.
     *
     * @param area  the area.
     */
    public LegendItemEntity(Shape area) {
        super(area);
    }

    /**
     * Returns a reference to the dataset that this legend item is derived
     * from.
     *
     * @return The dataset.
     *
     * @see #setDataset(Dataset)
     */
    public Dataset getDataset() {
        return this.dataset;
    }

    /**
     * Sets a reference to the dataset that this legend item is derived from.
     *
     * @param dataset  the dataset.
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Returns the series key that identifies the legend item.
     *
     * @return The series key.
     *
     * @see #setSeriesKey(Comparable)
     */
    public Comparable getSeriesKey() {
        return this.seriesKey;
    }

    /**
     * Sets the key for the series.
     *
     * @param key  the key.
     *
     * @see #getSeriesKey()
     */
    public void setSeriesKey(Comparable key) {
        this.seriesKey = key;
    }

    /**
     * Tests this object for equality with an arbitrary object.
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
        if (!(obj instanceof LegendItemEntity)) {
            return false;
        }
        LegendItemEntity that = (LegendItemEntity) obj;

        // fix the "equals not symmetric" problem
        if (!that.canEqual(this)) {
            return false;
        }
        if (!Objects.equals(this.seriesKey, that.seriesKey)) {
            return false;
        }
        if (!Objects.equals(this.dataset, that.dataset)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Ensures symmetry between super/subclass implementations of equals. For
     * more detail, see http://jqno.nl/equalsverifier/manual/inheritance.
     *
     * @param other Object
     * 
     * @return true ONLY if the parameter is THIS class type
     */
    @Override
    public boolean canEqual(Object other) {
        // Solves Problem: equals not symmetric
        return (other instanceof LegendItemEntity);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode(); // equals calls superclass function, so hashCode must also
        hash = 97 * hash + Objects.hashCode(this.dataset);
        hash = 97 * hash + Objects.hashCode(this.seriesKey);
        return hash;
    }

    /**
     * Returns a clone of the entity.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if there is a problem cloning the
     *         object.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns a string representing this object (useful for debugging
     * purposes).
     *
     * @return A string (never {@code null}).
     */
    @Override
    public String toString() {
        return "LegendItemEntity: seriesKey=" + this.seriesKey
                + ", dataset=" + this.dataset;
    }

}
