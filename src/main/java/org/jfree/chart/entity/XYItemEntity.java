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
 * -----------------
 * XYItemEntity.java
 * -----------------
 * (C) Copyright 2002-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Richard Atkinson;
 *                   Christian W. Zuckschwerdt;
 *                   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.chart.entity;

import java.awt.Shape;

import org.jfree.data.xy.XYDataset;

/**
 * A chart entity that represents one item within an
 * {@link org.jfree.chart.plot.XYPlot}.
 */
public class XYItemEntity extends ChartEntity {

    /** For serialization. */
    private static final long serialVersionUID = -3870862224880283771L;

    /** The dataset. */
    private transient XYDataset dataset;

    /** The series. */
    private int series;

    /** The item. */
    private int item;

    /**
     * Creates a new entity.
     *
     * @param area  the area.
     * @param dataset  the dataset.
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     * @param toolTipText  the tool tip text.
     * @param urlText  the URL text for HTML image maps.
     */
    public XYItemEntity(Shape area,
                        XYDataset dataset, int series, int item,
                        String toolTipText, String urlText) {
        super(area, toolTipText, urlText);
        this.dataset = dataset;
        this.series = series;
        this.item = item;
    }

    /**
     * Returns the dataset this entity refers to.
     *
     * @return The dataset.
     */
    public XYDataset getDataset() {
        return this.dataset;
    }

    /**
     * Sets the dataset this entity refers to.
     *
     * @param dataset  the dataset.
     */
    public void setDataset(XYDataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Returns the series index.
     *
     * @return The series index.
     */
    public int getSeriesIndex() {
        return this.series;
    }

    /**
     * Sets the series index.
     *
     * @param series the series index (zero-based).
     */
    public void setSeriesIndex(int series) {
        this.series = series;
    }

    /**
     * Returns the item index.
     *
     * @return The item index.
     */
    public int getItem() {
        return this.item;
    }

    /**
     * Sets the item index.
     *
     * @param item the item index (zero-based).
     */
    public void setItem(int item) {
        this.item = item;
    }

    /**
     * Tests the entity for equality with an arbitrary object.
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
        if (!(obj instanceof XYItemEntity)) {
            return false;
        }
        XYItemEntity that = (XYItemEntity) obj;

        // fix the "equals not symmetric" problem
        if (!that.canEqual(this)) {
            return false;
        }
        // compare fields in this class
        if (this.series != that.series) {
            return false;
        }
        if (this.item != that.item) {
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
        return (other instanceof XYItemEntity);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode(); // equals calls superclass function, so hashCode must also
        hash = 37 * hash + this.series;
        hash = 37 * hash + this.item;
        return hash;
    }

    /**
     * Returns a string representation of this instance, useful for debugging
     * purposes.
     *
     * @return A string.
     */
    @Override
    public String toString() {
        return "XYItemEntity: series = " + getSeriesIndex() + ", item = "
            + getItem() + ", dataset = " + getDataset();
    }

}
