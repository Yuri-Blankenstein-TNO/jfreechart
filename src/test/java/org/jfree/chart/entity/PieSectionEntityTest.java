/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2022, by David Gilbert and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
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
 * PieSectionEntityTest.java
 * -------------------------
 * (C) Copyright 2004-2022, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;

import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PieSectionEntity} class.
 */
public class PieSectionEntityTest {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        PieSectionEntity<String> e1 = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), 
                new DefaultPieDataset<String>(), 1, 2, "Key", "ToolTip", "URL");
        PieSectionEntity<String> e2 = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), 
                new DefaultPieDataset<String>(), 1, 2, "Key", "ToolTip", "URL");
        assertTrue(e1.equals(e2));

        e1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertFalse(e1.equals(e2));
        e2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertTrue(e1.equals(e2));

        e1.setToolTipText("New ToolTip");
        assertFalse(e1.equals(e2));
        e2.setToolTipText("New ToolTip");
        assertTrue(e1.equals(e2));

        e1.setURLText("New URL");
        assertFalse(e1.equals(e2));
        e2.setURLText("New URL");
        assertTrue(e1.equals(e2));

        e1.setDataset(null);
        assertFalse(e1.equals(e2));
        e2.setDataset(null);
        assertTrue(e1.equals(e2));

        e1.setPieIndex(99);
        assertFalse(e1.equals(e2));
        e2.setPieIndex(99);
        assertTrue(e1.equals(e2));

        e1.setSectionIndex(66);
        assertFalse(e1.equals(e2));
        e2.setSectionIndex(66);
        assertTrue(e1.equals(e2));

        e1.setSectionKey("ABC");
        assertFalse(e1.equals(e2));
        e2.setSectionKey("ABC");
        assertTrue(e1.equals(e2));
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PieSectionEntity<String> e1 = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), 
                new DefaultPieDataset<String>(), 1, 2, "Key", "ToolTip", "URL");
        PieSectionEntity<String> e2 = CloneUtils.clone(e1);
        assertTrue(e1 != e2);
        assertTrue(e1.getClass() == e2.getClass());
        assertTrue(e1.equals(e2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        PieSectionEntity<String> e1 = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), 
                new DefaultPieDataset<String>(), 1, 2, "Key", "ToolTip", "URL");
        PieSectionEntity<String> e2 = TestUtils.serialised(e1);
        assertEquals(e1, e2);
    }

}
