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
 * KeyedValueComparator.java
 * -------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data;

import java.io.Serializable;
import java.util.Comparator;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.SortOrder;

/**
 * A utility class that can compare and order two {@link KeyedValue} instances
 * and sort them into ascending or descending order by key or by value.
 */
public class KeyedValueComparator implements Comparator, Serializable {

    /** The comparator type. */
    private KeyedValueComparatorType type;

    /** The sort order. */
    private SortOrder order;

    /**
     * Creates a new comparator.
     *
     * @param type  the type ({@code BY_KEY} or {@code BY_VALUE},
     *              {@code null} not permitted).
     * @param order  the order ({@code null} not permitted).
     */
    public KeyedValueComparator(KeyedValueComparatorType type,
                                SortOrder order) {
        Args.nullNotPermitted(type, "type");
        Args.nullNotPermitted(order, "order");
        this.type = type;
        this.order = order;
    }

    /**
     * Returns the type.
     *
     * @return The type (never {@code null}).
     */
    public KeyedValueComparatorType getType() {
        return this.type;
    }

    /**
     * Returns the sort order.
     *
     * @return The sort order (never {@code null}).
     */
    public SortOrder getOrder() {
        return this.order;
    }

    /**
     * Compares two {@link KeyedValue} instances and returns an
     * {@code int} that indicates the relative order of the two objects.
     *
     * @param o1  object 1.
     * @param o2  object 2.
     *
     * @return An int indicating the relative order of the objects.
     */
    @Override
    public int compare(Object o1, Object o2) {

        if (o2 == null) {
            return -1;
        }
        if (o1 == null) {
            return 1;
        }

        int result;

        KeyedValue kv1 = (KeyedValue) o1;
        KeyedValue kv2 = (KeyedValue) o2;

        if (this.type == KeyedValueComparatorType.BY_KEY) {
            if (this.order.equals(SortOrder.ASCENDING)) {
                result = kv1.getKey().compareTo(kv2.getKey());
            }
            else if (this.order.equals(SortOrder.DESCENDING)) {
                result = kv2.getKey().compareTo(kv1.getKey());
            }
            else {
                throw new IllegalArgumentException("Unrecognised sort order.");
            }
        }
        else if (this.type == KeyedValueComparatorType.BY_VALUE) {
            Number n1 = kv1.getValue();
            Number n2 = kv2.getValue();
            if (n2 == null) {
                return -1;
            }
            if (n1 == null) {
                return 1;
            }
            double d1 = n1.doubleValue();
            double d2 = n2.doubleValue();
            if (this.order.equals(SortOrder.ASCENDING)) {
                if (d1 > d2) {
                    result = 1;
                }
                else if (d1 < d2) {
                    result = -1;
                }
                else {
                    result = 0;
                }
            }
            else if (this.order.equals(SortOrder.DESCENDING)) {
                if (d1 > d2) {
                    result = -1;
                }
                else if (d1 < d2) {
                    result = 1;
                }
                else {
                    result = 0;
                }
            }
            else {
                throw new IllegalArgumentException("Unrecognised sort order.");
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognised type.");
        }

        return result;
    }

}
