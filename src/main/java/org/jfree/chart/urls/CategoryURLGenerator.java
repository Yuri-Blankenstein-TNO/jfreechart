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
 * CategoryURLGenerator.java
 * -------------------------
 * (C) Copyright 2002-present, by Richard Atkinson and Contributors.
 *
 * Original Author:  Richard Atkinson;
 * Contributors:     David Gilbert;
 *
 */

package org.jfree.chart.urls;

import org.jfree.data.category.CategoryDataset;

/**
 * A URL generator for items in a {@link CategoryDataset}.
 */
public interface CategoryURLGenerator {

    /**
     * Returns a URL for one item in a dataset. As a guideline, the URL
     * should be valid within the context of an XHTML 1.0 document.  Classes
     * that implement this interface are responsible for correctly escaping
     * any text that is derived from the dataset, as this may be user-specified
     * and could pose a security risk.
     *
     * @param dataset  the dataset.
     * @param series  the series (zero-based index).
     * @param category  the category.
     *
     * @return A string containing the URL.
     */
    String generateURL(CategoryDataset dataset, int series,
                       int category);

}
