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
 * TaskSeriesCollection.java
 * -------------------------
 * (C) Copyright 2002-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Thomas Schuster;
 *                   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 */

package org.jfree.data.gantt;

import org.jfree.chart.util.Args;
import org.jfree.chart.util.ObjectUtils;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.general.AbstractSeriesDataset;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.time.TimePeriod;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A collection of {@link TaskSeries} objects.  This class provides one
 * implementation of the {@link GanttCategoryDataset} interface.
 */
public class TaskSeriesCollection extends AbstractSeriesDataset
        implements GanttCategoryDataset, Cloneable, PublicCloneable,
                   Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -2065799050738449903L;

    /**
     * Storage for aggregate task keys (the task description is used as the
     * key).
     */
    private List keys;

    /** Storage for the series. */
    private List data;

    /**
     * Default constructor.
     */
    public TaskSeriesCollection() {
        this.keys = new java.util.ArrayList();
        this.data = new java.util.ArrayList();
    }

    /**
     * Returns a series from the collection.
     *
     * @param key  the series key ({@code null} not permitted).
     *
     * @return The series.
     */
    public TaskSeries getSeries(Comparable key) {
        if (key == null) {
            throw new NullPointerException("Null 'key' argument.");
        }
        TaskSeries result = null;
        int index = getRowIndex(key);
        if (index >= 0) {
            result = getSeries(index);
        }
        return result;
    }

    /**
     * Returns a series from the collection.
     *
     * @param series  the series index (zero-based).
     *
     * @return The series.
     */
    public TaskSeries getSeries(int series) {
        if ((series < 0) || (series >= getSeriesCount())) {
            throw new IllegalArgumentException("Series index out of bounds");
        }
        return (TaskSeries) this.data.get(series);
    }

    /**
     * Returns the number of series in the collection.
     *
     * @return The series count.
     */
    @Override
    public int getSeriesCount() {
        return getRowCount();
    }

    /**
     * Returns the name of a series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The name of a series.
     */
    @Override
    public Comparable getSeriesKey(int series) {
        TaskSeries ts = (TaskSeries) this.data.get(series);
        return ts.getKey();
    }

    /**
     * Returns the number of rows (series) in the collection.
     *
     * @return The series count.
     */
    @Override
    public int getRowCount() {
        return this.data.size();
    }

    /**
     * Returns the row keys.  In this case, each series is a key.
     *
     * @return The row keys.
     */
    @Override
    public List getRowKeys() {
        return this.data;
    }

    /**
     * Returns the number of column in the dataset.
     *
     * @return The column count.
     */
    @Override
    public int getColumnCount() {
        return this.keys.size();
    }

    /**
     * Returns a list of the column keys in the dataset.
     *
     * @return The category list.
     */
    @Override
    public List getColumnKeys() {
        return this.keys;
    }

    /**
     * Returns a column key.
     *
     * @param index  the column index.
     *
     * @return The column key.
     */
    @Override
    public Comparable getColumnKey(int index) {
        return (Comparable) this.keys.get(index);
    }

    /**
     * Returns the column index for a column key.
     *
     * @param columnKey  the column key ({@code null} not permitted).
     *
     * @return The column index.
     */
    @Override
    public int getColumnIndex(Comparable columnKey) {
        Args.nullNotPermitted(columnKey, "columnKey");
        return this.keys.indexOf(columnKey);
    }

    /**
     * Returns the row index for the given row key.
     *
     * @param rowKey  the row key.
     *
     * @return The index.
     */
    @Override
    public int getRowIndex(Comparable rowKey) {
        int result = -1;
        int count = this.data.size();
        for (int i = 0; i < count; i++) {
            TaskSeries s = (TaskSeries) this.data.get(i);
            if (s.getKey().equals(rowKey)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Returns the key for a row.
     *
     * @param index  the row index (zero-based).
     *
     * @return The key.
     */
    @Override
    public Comparable getRowKey(int index) {
        TaskSeries series = (TaskSeries) this.data.get(index);
        return series.getKey();
    }

    /**
     * Adds a series to the dataset and sends a
     * {@link org.jfree.data.general.DatasetChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series ({@code null} not permitted).
     */
    public void add(TaskSeries series) {
        Args.nullNotPermitted(series, "series");
        this.data.add(series);
        series.addChangeListener(this);

        // look for any keys that we don't already know about...
        for (Object o : series.getTasks()) {
            Task task = (Task) o;
            String key = task.getDescription();
            int index = this.keys.indexOf(key);
            if (index < 0) {
                this.keys.add(key);
            }
        }
        fireDatasetChanged();
    }

    /**
     * Removes a series from the collection and sends
     * a {@link org.jfree.data.general.DatasetChangeEvent}
     * to all registered listeners.
     *
     * @param series  the series.
     */
    public void remove(TaskSeries series) {
        Args.nullNotPermitted(series, "series");
        if (this.data.contains(series)) {
            series.removeChangeListener(this);
            this.data.remove(series);
            fireDatasetChanged();
        }
    }

    /**
     * Removes a series from the collection and sends
     * a {@link org.jfree.data.general.DatasetChangeEvent}
     * to all registered listeners.
     *
     * @param series  the series (zero based index).
     */
    public void remove(int series) {
        if ((series < 0) || (series >= getSeriesCount())) {
            throw new IllegalArgumentException(
                "TaskSeriesCollection.remove(): index outside valid range.");
        }

        // fetch the series, remove the change listener, then remove the series.
        TaskSeries ts = (TaskSeries) this.data.get(series);
        ts.removeChangeListener(this);
        this.data.remove(series);
        fireDatasetChanged();

    }

    /**
     * Removes all the series from the collection and sends
     * a {@link org.jfree.data.general.DatasetChangeEvent}
     * to all registered listeners.
     */
    public void removeAll() {

        // deregister the collection as a change listener to each series in
        // the collection.
        for (Object item : this.data) {
            TaskSeries series = (TaskSeries) item;
            series.removeChangeListener(this);
        }

        // remove all the series from the collection and notify listeners.
        this.data.clear();
        fireDatasetChanged();

    }

    /**
     * Returns the value for an item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     *
     * @return The item value.
     */
    @Override
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        return getStartValue(rowKey, columnKey);
    }

    /**
     * Returns the value for a task.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The start value.
     */
    @Override
    public Number getValue(int row, int column) {
        return getStartValue(row, column);
    }

    /**
     * Returns the start value for a task.  This is a date/time value, measured
     * in milliseconds since 1-Jan-1970.
     *
     * @param rowKey  the series.
     * @param columnKey  the category.
     *
     * @return The start value (possibly {@code null}).
     */
    @Override
    public Number getStartValue(Comparable rowKey, Comparable columnKey) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            TimePeriod duration = task.getDuration();
            if (duration != null) {
                result = duration.getStart().getTime();
            }
        }
        return result;
    }

    /**
     * Returns the start value for a task.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The start value.
     */
    @Override
    public Number getStartValue(int row, int column) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getStartValue(rowKey, columnKey);
    }

    /**
     * Returns the end value for a task.  This is a date/time value, measured
     * in milliseconds since 1-Jan-1970.
     *
     * @param rowKey  the series.
     * @param columnKey  the category.
     *
     * @return The end value (possibly {@code null}).
     */
    @Override
    public Number getEndValue(Comparable rowKey, Comparable columnKey) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            TimePeriod duration = task.getDuration();
            if (duration != null) {
                result = duration.getEnd().getTime();
            }
        }
        return result;
    }

    /**
     * Returns the end value for a task.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The end value.
     */
    @Override
    public Number getEndValue(int row, int column) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getEndValue(rowKey, columnKey);
    }

    /**
     * Returns the percent complete for a given item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The percent complete (possibly {@code null}).
     */
    @Override
    public Number getPercentComplete(int row, int column) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getPercentComplete(rowKey, columnKey);
    }

    /**
     * Returns the percent complete for a given item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     *
     * @return The percent complete.
     */
    @Override
    public Number getPercentComplete(Comparable rowKey, Comparable columnKey) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            result = task.getPercentComplete();
        }
        return result;
    }

    /**
     * Returns the number of sub-intervals for a given item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The sub-interval count.
     */
    @Override
    public int getSubIntervalCount(int row, int column) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getSubIntervalCount(rowKey, columnKey);
    }

    /**
     * Returns the number of sub-intervals for a given item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     *
     * @return The sub-interval count.
     */
    @Override
    public int getSubIntervalCount(Comparable rowKey, Comparable columnKey) {
        int result = 0;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            result = task.getSubtaskCount();
        }
        return result;
    }

    /**
     * Returns the start value of a sub-interval for a given item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param subinterval  the sub-interval index (zero-based).
     *
     * @return The start value (possibly {@code null}).
     */
    @Override
    public Number getStartValue(int row, int column, int subinterval) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getStartValue(rowKey, columnKey, subinterval);
    }

    /**
     * Returns the start value of a sub-interval for a given item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param subinterval  the subinterval.
     *
     * @return The start value (possibly {@code null}).
     */
    @Override
    public Number getStartValue(Comparable rowKey, Comparable columnKey,
                                int subinterval) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            Task sub = task.getSubtask(subinterval);
            if (sub != null) {
                TimePeriod duration = sub.getDuration();
                result = duration.getStart().getTime();
            }
        }
        return result;
    }

    /**
     * Returns the end value of a sub-interval for a given item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param subinterval  the subinterval.
     *
     * @return The end value (possibly {@code null}).
     */
    @Override
    public Number getEndValue(int row, int column, int subinterval) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getEndValue(rowKey, columnKey, subinterval);
    }

    /**
     * Returns the end value of a sub-interval for a given item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param subinterval  the subinterval.
     *
     * @return The end value (possibly {@code null}).
     */
    @Override
    public Number getEndValue(Comparable rowKey, Comparable columnKey,
                              int subinterval) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            Task sub = task.getSubtask(subinterval);
            if (sub != null) {
                TimePeriod duration = sub.getDuration();
                result = duration.getEnd().getTime();
            }
        }
        return result;
    }

    /**
     * Returns the percentage complete value of a sub-interval for a given item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param subinterval  the sub-interval.
     *
     * @return The percent complete value (possibly {@code null}).
     */
    @Override
    public Number getPercentComplete(int row, int column, int subinterval) {
        Comparable rowKey = getRowKey(row);
        Comparable columnKey = getColumnKey(column);
        return getPercentComplete(rowKey, columnKey, subinterval);
    }

    /**
     * Returns the percentage complete value of a sub-interval for a given item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param subinterval  the sub-interval.
     *
     * @return The percent complete value (possibly {@code null}).
     */
    @Override
    public Number getPercentComplete(Comparable rowKey, Comparable columnKey,
                                     int subinterval) {
        Number result = null;
        int row = getRowIndex(rowKey);
        TaskSeries series = (TaskSeries) this.data.get(row);
        Task task = series.get(columnKey.toString());
        if (task != null) {
            Task sub = task.getSubtask(subinterval);
            if (sub != null) {
                result = sub.getPercentComplete();
            }
        }
        return result;
    }

    /**
     * Called when a series belonging to the dataset changes.
     *
     * @param event  information about the change.
     */
    @Override
    public void seriesChanged(SeriesChangeEvent event) {
        refreshKeys();
        fireDatasetChanged();
    }

    /**
     * Refreshes the keys.
     */
    private void refreshKeys() {

        this.keys.clear();
        for (int i = 0; i < getSeriesCount(); i++) {
            TaskSeries series = (TaskSeries) this.data.get(i);
            // look for any keys that we don't already know about...
            for (Object o : series.getTasks()) {
                Task task = (Task) o;
                String key = task.getDescription();
                int index = this.keys.indexOf(key);
                if (index < 0) {
                    this.keys.add(key);
                }
            }
        }

    }

    /**
     * Tests this instance for equality with an arbitrary object.
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
        if (!(obj instanceof TaskSeriesCollection)) {
            return false;
        }
        TaskSeriesCollection that = (TaskSeriesCollection) obj;
        if (!Objects.equals(this.data, that.data)) {
            return false;
        }
        if (!Objects.equals(this.keys, that.keys)) {
            return false;
        }
        if (!that.canEqual(this)) {
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
        // fix the "equals not symmetric" problem
        return (other instanceof TaskSeriesCollection);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode(); // equals calls superclass, hashCode must also
        hash = 79 * hash + Objects.hashCode(this.data);
        hash = 79 * hash + Objects.hashCode(this.keys);
        return hash;
    }

    /**
     * Returns an independent copy of this dataset.
     *
     * @return A clone of the dataset.
     *
     * @throws CloneNotSupportedException if there is some problem cloning
     *     the dataset.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        TaskSeriesCollection clone = (TaskSeriesCollection) super.clone();
        clone.data = (List) ObjectUtils.deepClone(this.data);
        clone.keys = new java.util.ArrayList(this.keys);
        return clone;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        for (Object item : this.data) {
            TaskSeries series = (TaskSeries) item;
            series.addChangeListener(this);
        }
    }
}
