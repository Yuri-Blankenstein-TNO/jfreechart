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
 * ---------------
 * OHLCSeries.java
 * ---------------
 * (C) Copyright 2006-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time.ohlc;

import org.jfree.chart.util.Args;
import org.jfree.data.ComparableObjectItem;
import org.jfree.data.ComparableObjectSeries;
import org.jfree.data.time.RegularTimePeriod;

/**
 * A list of ({@link RegularTimePeriod}, open, high, low, close) data items.
 *
 * @see OHLCSeriesCollection
 */
public class OHLCSeries extends ComparableObjectSeries {

    /**
     * Creates a new empty series.  By default, items added to the series will
     * be sorted into ascending order by period, and duplicate periods will
     * not be allowed.
     *
     * @param key  the series key ({@code null} not permitted).
     */
    public OHLCSeries(Comparable key) {
        super(key, true, false);
    }

    /**
     * Returns the time period for the specified item.
     *
     * @param index  the item index.
     *
     * @return The time period.
     */
    public RegularTimePeriod getPeriod(int index) {
        OHLCItem item = (OHLCItem) getDataItem(index);
        return item.getPeriod();
    }

    /**
     * Returns the data item at the specified index.
     *
     * @param index  the item index.
     *
     * @return The data item.
     */
    @Override
    public ComparableObjectItem getDataItem(int index) {
        return super.getDataItem(index);
    }

    /**
     * Adds a data item to the series.
     *
     * @param period  the period.
     * @param open  the open-value.
     * @param high  the high-value.
     * @param low  the low-value.
     * @param close  the close-value.
     */
    public void add(RegularTimePeriod period, double open, double high,
            double low, double close) {
        if (getItemCount() > 0) {
            OHLCItem item0 = (OHLCItem) this.getDataItem(0);
            if (!period.getClass().equals(item0.getPeriod().getClass())) {
                throw new IllegalArgumentException(
                        "Can't mix RegularTimePeriod class types.");
            }
        }
        super.add(new OHLCItem(period, open, high, low, close), true);
    }
    
    /**
     * Adds a data item to the series.  The values from the item passed to
     * this method will be copied into a new object.
     * 
     * @param item  the item ({@code null} not permitted).
     */
    public void add(OHLCItem item) {
        Args.nullNotPermitted(item, "item");
        add(item.getPeriod(), item.getOpenValue(), item.getHighValue(),
                item.getLowValue(), item.getCloseValue());
    }

    /**
     * Removes the item with the specified index.
     *
     * @param index  the item index.
     * 
     * @return The item removed.
     */
    @Override
    public ComparableObjectItem remove(int index) {
        return super.remove(index);
    }

}
