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
 * ------------------
 * KeyedValues2D.java
 * ------------------
 * (C) Copyright 2002-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data;

import java.util.List;

/**
 * An extension of the {@link Values2D} interface where a unique key is
 * associated with the row and column indices.
 */
public interface KeyedValues2D extends Values2D {

    /**
     * Returns the row key for a given index.
     *
     * @param row  the row index (zero-based).
     *
     * @return The row key.
     *
     * @throws IndexOutOfBoundsException if {@code row} is out of bounds.
     */
    Comparable getRowKey(int row);

    /**
     * Returns the row index for a given key.
     *
     * @param key  the row key.
     *
     * @return The row index, or {@code -1} if the key is unrecognised.
     */
    int getRowIndex(Comparable key);

    /**
     * Returns the row keys.
     *
     * @return The keys.
     */
    List getRowKeys();

    /**
     * Returns the column key for a given index.
     *
     * @param column  the column index (zero-based).
     *
     * @return The column key.
     *
     * @throws IndexOutOfBoundsException if {@code row} is out of bounds.
     */
    Comparable getColumnKey(int column);

    /**
     * Returns the column index for a given key.
     *
     * @param key  the column key.
     *
     * @return The column index, or {@code -1} if the key is unrecognised.
     */
    int getColumnIndex(Comparable key);

    /**
     * Returns the column keys.
     *
     * @return The keys.
     */
    List getColumnKeys();

    /**
     * Returns the value associated with the specified keys.
     *
     * @param rowKey  the row key ({@code null} not permitted).
     * @param columnKey  the column key ({@code null} not permitted).
     *
     * @return The value.
     *
     * @throws UnknownKeyException if either key is not recognised.
     */
    Number getValue(Comparable rowKey, Comparable columnKey);

}
