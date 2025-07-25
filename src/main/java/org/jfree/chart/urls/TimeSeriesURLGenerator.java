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
 * ---------------------------
 * TimeSeriesURLGenerator.java
 * ---------------------------
 * (C) Copyright 2002-present, by Richard Atkinson and Contributors.
 *
 * Original Author:  Richard Atkinson;
 * Contributors:     David Gilbert;
 *
 */

package org.jfree.chart.urls;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

import org.jfree.chart.util.Args;
import org.jfree.data.xy.XYDataset;

/**
 * A URL generator for time series charts.
 */
public class TimeSeriesURLGenerator implements XYURLGenerator, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -9122773175671182445L;

    /** A formatter for the date. */
    private DateFormat dateFormat = DateFormat.getInstance();

    /** Prefix to the URL */
    private String prefix = "index.html";

    /** Name to use to identify the series */
    private String seriesParameterName = "series";

    /** Name to use to identify the item */
    private String itemParameterName = "item";

    /**
     * Default constructor.
     */
    public TimeSeriesURLGenerator() {
        super();
    }

    /**
     * Construct TimeSeriesURLGenerator overriding defaults.
     *
     * @param dateFormat  a formatter for the date ({@code null} not
     *         permitted).
     * @param prefix  the prefix of the URL ({@code null} not permitted).
     * @param seriesParameterName  the name of the series parameter in the URL
     *         ({@code null} not permitted).
     * @param itemParameterName  the name of the item parameter in the URL
     *         ({@code null} not permitted).
     */
    public TimeSeriesURLGenerator(DateFormat dateFormat, String prefix,
            String seriesParameterName, String itemParameterName) {

        Args.nullNotPermitted(dateFormat, "dateFormat");
        Args.nullNotPermitted(prefix, "prefix");
        Args.nullNotPermitted(seriesParameterName, "seriesParameterName");
        Args.nullNotPermitted(itemParameterName, "itemParameterName");
        this.dateFormat = (DateFormat) dateFormat.clone();
        this.prefix = prefix;
        this.seriesParameterName = seriesParameterName;
        this.itemParameterName = itemParameterName;
    }

    /**
     * Returns a clone of the date format assigned to this URL generator.
     *
     * @return The date format (never {@code null}).
     */
    public DateFormat getDateFormat() {
        return (DateFormat) this.dateFormat.clone();
    }

    /**
     * Returns the prefix string.
     *
     * @return The prefix string (never {@code null}).
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Returns the series parameter name.
     *
     * @return The series parameter name (never {@code null}).
     */
    public String getSeriesParameterName() {
        return this.seriesParameterName;
    }

    /**
     * Returns the item parameter name.
     *
     * @return The item parameter name (never {@code null}).
     */
    public String getItemParameterName() {
        return this.itemParameterName;
    }

    /**
     * Generates a URL for a particular item within a series.
     *
     * @param dataset  the dataset ({@code null} not permitted).
     * @param series  the series number (zero-based index).
     * @param item  the item number (zero-based index).
     *
     * @return The generated URL.
     */
    @Override
    public String generateURL(XYDataset dataset, int series, int item) {
        String result = this.prefix;
        boolean firstParameter = !result.contains("?");
        Comparable seriesKey = dataset.getSeriesKey(series);
        if (seriesKey != null) {
            result += firstParameter ? "?" : "&amp;";
            try {
                result += this.seriesParameterName + "=" + URLEncoder.encode(
                        seriesKey.toString(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            firstParameter = false;
        }

        long x = (long) dataset.getXValue(series, item);
        String xValue = this.dateFormat.format(new Date(x));
        result += firstParameter ? "?" : "&amp;";
        try {
            result += this.itemParameterName + "=" + URLEncoder.encode(xValue,
                    "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    /**
     * Tests this generator for equality with an arbitrary object.
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
        if (!(obj instanceof TimeSeriesURLGenerator)) {
            return false;
        }
        TimeSeriesURLGenerator that = (TimeSeriesURLGenerator) obj;
        if (!this.dateFormat.equals(that.dateFormat)) {
            return false;
        }
        if (!this.itemParameterName.equals(that.itemParameterName)) {
            return false;
        }
        if (!this.prefix.equals(that.prefix)) {
            return false;
        }
        if (!this.seriesParameterName.equals(that.seriesParameterName)) {
            return false;
        }
        return true;
    }

}
