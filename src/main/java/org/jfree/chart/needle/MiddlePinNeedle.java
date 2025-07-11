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
 * --------------------
 * MiddlePinNeedle.java
 * --------------------
 * (C) Copyright 2002-present, by the Australian Antarctic Division and
 *                          Contributors.
 *
 * Original Author:  Bryan Scott (for the Australian Antarctic Division);
 * Contributor(s):   David Gilbert;
 *
 */

package org.jfree.chart.needle;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * A needle that is drawn as a pin shape.
 */
public class MiddlePinNeedle extends MeterNeedle implements Cloneable, 
        Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 6237073996403125310L;

    /**
     * Creates a new instance.
     */
    public MiddlePinNeedle() {
        super();
    }

    /**
     * Draws the needle.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     * @param rotate  the rotation point.
     * @param angle  the angle.
     */
    @Override
    protected void drawNeedle(Graphics2D g2, Rectangle2D plotArea,
            Point2D rotate, double angle) {

        Area shape;
        GeneralPath pointer = new GeneralPath();

        int minY = (int) (plotArea.getMinY());
        //int maxX = (int) (plotArea.getMaxX());
        int maxY = (int) (plotArea.getMaxY());
        int midY = ((maxY - minY) / 2) + minY;

        int midX = (int) (plotArea.getMinX() + (plotArea.getWidth() / 2));
        //int midY = (int) (plotArea.getMinY() + (plotArea.getHeight() / 2));
        int lenX = (int) (plotArea.getWidth() / 10);
        if (lenX < 2) {
            lenX = 2;
        }

        pointer.moveTo(midX - lenX, midY - lenX);
        pointer.lineTo(midX + lenX, midY - lenX);
        pointer.lineTo(midX, minY);
        pointer.closePath();

        lenX = 4 * lenX;
        Ellipse2D circle = new Ellipse2D.Double(midX - lenX / 2.0,
                                                midY - lenX, lenX, lenX);

        shape = new Area(circle);
        shape.add(new Area(pointer));
        if ((rotate != null) && (angle != 0)) {
            /// we have rotation
            getTransform().setToRotation(angle, rotate.getX(), rotate.getY());
            shape.transform(getTransform());
        }

        defaultDisplay(g2, shape);

    }

    /**
     * Tests another object for equality with this object.
     *
     * @param object  the object to test.
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (super.equals(object) && object instanceof MiddlePinNeedle) {
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns a clone of this needle.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if the {@code MiddlePinNeedle}
     *     cannot be cloned (in theory, this should not happen).
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
