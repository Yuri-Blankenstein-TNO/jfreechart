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
 * AbstractXYAnnotation.java
 * -------------------------
 * (C) Copyright 2004-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Peter Kolb (patch 2809117);
 *                   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *                   Yuri Blankenstein;
 *
 */

package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYAnnotationEntity;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;

/**
 * The interface that must be supported by annotations that are to be added to
 * an {@link XYPlot}.
 */
public abstract class AbstractXYAnnotation extends AbstractAnnotation
        implements XYAnnotation {

    /** The tool tip text. */
    private String toolTipText;

    /** The URL. */
    private String url;

    /**
     * Creates a new instance that has no tool tip or URL specified.
     */
    protected AbstractXYAnnotation() {
        super();
        this.toolTipText = null;
        this.url = null;
    }

    /**
     * Returns the tool tip text for the annotation.  This will be displayed in
     * a {@link org.jfree.chart.ChartPanel} when the mouse pointer hovers over
     * the annotation.
     *
     * @return The tool tip text (possibly {@code null}).
     *
     * @see #setToolTipText(String)
     */
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Sets the tool tip text for the annotation.
     *
     * @param text  the tool tip text ({@code null} permitted).
     *
     * @see #getToolTipText()
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
    }

    /**
     * Returns the URL for the annotation.  This URL will be used to provide
     * hyperlinks when an HTML image map is created for the chart.
     *
     * @return The URL (possibly {@code null}).
     *
     * @see #setURL(String)
     */
    public String getURL() {
        return this.url;
    }

    /**
     * Sets the URL for the annotation.
     *
     * @param url  the URL ({@code null} permitted).
     *
     * @see #getURL()
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Draws the annotation.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param rendererIndex  the renderer index.
     * @param info  if supplied, this info object will be populated with
     *              entity information.
     */
    @Override
    public abstract void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
                              ValueAxis domainAxis, ValueAxis rangeAxis,
                              int rendererIndex,
                              PlotRenderingInfo info);

    /**
     * A utility method for adding an {@link XYAnnotationEntity} to
     * a {@link PlotRenderingInfo} instance.
     *
     * @param info  the plot rendering info ({@code null} permitted).
     * @param hotspot  the hotspot area.
     * @param rendererIndex  the renderer index.
     * @param toolTipText  the tool tip text.
     * @param urlText  the URL text.
     * @see #createEntity(Shape, int, String, String)
     */
    protected void addEntity(PlotRenderingInfo info,
                             Shape hotspot, int rendererIndex,
                             String toolTipText, String urlText) {
        if (info == null) {
            return;
        }
        EntityCollection entities = info.getOwner().getEntityCollection();
        if (entities == null) {
            return;
        }
        XYAnnotationEntity entity = createEntity(hotspot, rendererIndex,
                toolTipText, urlText);
        entities.add(entity);
    }
    
    /**
     * A factory method for creating an {@link XYAnnotationEntity} for this
     * annotation.
     * 
     * @param hotspot       the hotspot area.
     * @param rendererIndex the renderer index.
     * @param toolTipText   the tool tip text.
     * @param urlText       the URL text.
     * @return the created entity
     */
    protected XYAnnotationEntity createEntity(Shape hotspot, int rendererIndex,
            String toolTipText, String urlText) {
        return new XYAnnotationEntity(hotspot, this, rendererIndex, toolTipText,
                urlText);
    }

    /**
     * Tests this annotation for equality with an arbitrary object.
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
        if (!(obj instanceof AbstractXYAnnotation)) {
            return false;
        }
        AbstractXYAnnotation that = (AbstractXYAnnotation) obj;
        if (!Objects.equals(this.toolTipText, that.toolTipText)) {
            return false;
        }
        if (!Objects.equals(this.url, that.url)) {
            return false;
        }

        // fix the "equals not symmetric" problem
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
        return (other instanceof AbstractXYAnnotation);
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode(); // equals calls superclass, hashCode must also
        result = 37 * result + Objects.hashCode(this.toolTipText);
        result = 37 * result + Objects.hashCode(this.url);
        return result;
    }

}
