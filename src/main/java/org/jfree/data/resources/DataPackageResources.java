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
 * DataPackageResources.java
 * -------------------------
 * (C) Copyright 2002-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.resources;

import java.util.ListResourceBundle;

/**
 * A resource bundle that stores all the items that might need localisation.
 */
public class DataPackageResources extends ListResourceBundle {

    /**
     * Creates a new instance.
     */
    public DataPackageResources() {
        super();
    }

    /**
     * Returns the array of strings in the resource bundle.
     *
     * @return The localised resources.
     */
    @Override
    public Object[][] getContents() {
        return CONTENTS;
    }

    /** The resources to be localised. */
    private static final Object[][] CONTENTS = {

        {"series.default-prefix",     "Series"},
        {"categories.default-prefix", "Category"},

    };

}
