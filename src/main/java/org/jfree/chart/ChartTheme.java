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
 * ChartTheme.java
 * ---------------
 * (C) Copyright 2008-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart;

/**
 * A {@link ChartTheme} a class that can apply a style or 'theme' to a chart.
 * It can be implemented in an arbitrary manner, with the styling applied to
 * the chart via the {@code apply(JFreeChart)} method.  We provide one
 * implementation ({@link StandardChartTheme}) that just mimics the manual
 * process of calling methods to set various chart parameters.
 */
public interface ChartTheme {

    /**
     * Applies this theme to the supplied chart.
     *
     * @param chart  the chart ({@code null} not permitted).
     */
    void apply(JFreeChart chart);

}
