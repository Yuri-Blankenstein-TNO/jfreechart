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
 * PieSectionEntity.java
 * ---------------------
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
import java.io.Serializable;
import java.util.Objects;

import org.jfree.chart.HashUtils;
import org.jfree.data.general.PieDataset;

/**
 * A chart entity that represents one section within a pie plot.
 */
public class PieSectionEntity extends ChartEntity
                              implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 9199892576531984162L;

    /** The dataset. */
    private PieDataset dataset;

    /** The pie index. */
    private int pieIndex;

    /** The section index. */
    private int sectionIndex;

    /** The section key. */
    private Comparable sectionKey;

    /**
     * Creates a new pie section entity.
     *
     * @param area  the area.
     * @param dataset  the pie dataset.
     * @param pieIndex  the pie index (zero-based).
     * @param sectionIndex  the section index (zero-based).
     * @param sectionKey  the section key.
     * @param toolTipText  the tool tip text.
     * @param urlText  the URL text for HTML image maps.
     */
    public PieSectionEntity(Shape area,
                            PieDataset dataset,
                            int pieIndex, int sectionIndex,
                            Comparable sectionKey,
                            String toolTipText, String urlText) {

        super(area, toolTipText, urlText);
        this.dataset = dataset;
        this.pieIndex = pieIndex;
        this.sectionIndex = sectionIndex;
        this.sectionKey = sectionKey;

    }

    /**
     * Returns the dataset this entity refers to.
     *
     * @return The dataset.
     *
     * @see #setDataset(PieDataset)
     */
    public PieDataset getDataset() {
        return this.dataset;
    }

    /**
     * Sets the dataset this entity refers to.
     *
     * @param dataset  the dataset.
     *
     * @see #getDataset()
     */
    public void setDataset(PieDataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Returns the pie index.  For a regular pie chart, the section index is 0.
     * For a pie chart containing multiple pie plots, the pie index is the row
     * or column index from which the pie data is extracted.
     *
     * @return The pie index.
     *
     * @see #setPieIndex(int)
     */
    public int getPieIndex() {
        return this.pieIndex;
    }

    /**
     * Sets the pie index.
     *
     * @param index  the new index value.
     *
     * @see #getPieIndex()
     */
    public void setPieIndex(int index) {
        this.pieIndex = index;
    }

    /**
     * Returns the section index.
     *
     * @return The section index.
     *
     * @see #setSectionIndex(int)
     */
    public int getSectionIndex() {
        return this.sectionIndex;
    }

    /**
     * Sets the section index.
     *
     * @param index  the section index.
     *
     * @see #getSectionIndex()
     */
    public void setSectionIndex(int index) {
        this.sectionIndex = index;
    }

    /**
     * Returns the section key.
     *
     * @return The section key.
     *
     * @see #setSectionKey(Comparable)
     */
    public Comparable getSectionKey() {
        return this.sectionKey;
    }

    /**
     * Sets the section key.
     *
     * @param key  the section key.
     *
     * @see #getSectionKey()
     */
    public void setSectionKey(Comparable key) {
        this.sectionKey = key;
    }

    /**
     * Tests this entity for equality with an arbitrary object.
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
        if (!(obj instanceof PieSectionEntity)) {
            return false;
        }
        PieSectionEntity that = (PieSectionEntity) obj;

        // fix the "equals not symmetric" problem
        if (!that.canEqual(this)) {
            return false;
        }
        if (!Objects.equals(this.dataset, that.dataset)) {
            return false;
        }
        if (this.pieIndex != that.pieIndex) {
            return false;
        }
        if (this.sectionIndex != that.sectionIndex) {
            return false;
        }
        if (!Objects.equals(this.sectionKey, that.sectionKey)) {
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
        return (other instanceof PieSectionEntity);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = HashUtils.hashCode(result, this.dataset);
        result = HashUtils.hashCode(result, this.pieIndex);
        result = HashUtils.hashCode(result, this.sectionIndex);
        result = HashUtils.hashCode(result, this.sectionKey);
        return result;
    }

    /**
     * Returns a string representing the entity.
     *
     * @return A string representing the entity.
     */
    @Override
    public String toString() {
        return "PieSection: " + this.pieIndex + ", " + this.sectionIndex + "("
                              + this.sectionKey.toString() + ")";
    }

}
