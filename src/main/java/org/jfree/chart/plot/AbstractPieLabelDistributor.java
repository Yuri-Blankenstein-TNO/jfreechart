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
 * --------------------------------
 * AbstractPieLabelDistributor.java
 * --------------------------------
 * (C) Copyright 2007-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 * 
 */

package org.jfree.chart.plot;

import java.io.Serializable;
import java.util.List;
import org.jfree.chart.util.Args;

/**
 * A base class for handling the distribution of pie section labels.  Create
 * your own subclass and set it using the
 * {@link PiePlot#setLabelDistributor(AbstractPieLabelDistributor)} method
 * if you want to customise the label distribution.
 */
public abstract class AbstractPieLabelDistributor implements Serializable {

    /** The label records. */
    protected List labels;

    /**
     * Creates a new instance.
     */
    public AbstractPieLabelDistributor() {
        this.labels = new java.util.ArrayList();
    }

    /**
     * Returns a label record from the list.
     *
     * @param index  the index.
     *
     * @return The label record.
     */
    public PieLabelRecord getPieLabelRecord(int index) {
        return (PieLabelRecord) this.labels.get(index);
    }

    /**
     * Adds a label record.
     *
     * @param record  the label record ({@code null} not permitted).
     */
    public void addPieLabelRecord(PieLabelRecord record) {
        Args.nullNotPermitted(record, "record");
        this.labels.add(record);
    }

    /**
     * Returns the number of items in the list.
     *
     * @return The item count.
     */
    public int getItemCount() {
        return this.labels.size();
    }

    /**
     * Clears the list of labels.
     */
    public void clear() {
        this.labels.clear();
    }

    /**
     * Called by the {@link PiePlot} class.  Implementations should distribute
     * the labels in this.labels then return.
     *
     * @param minY  the y-coordinate for the top of the label area.
     * @param height  the height of the label area.
     */
    public abstract void distributeLabels(double minY, double height);

}
