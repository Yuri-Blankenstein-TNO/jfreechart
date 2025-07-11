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
 * -----------------------------
 * LegendItemBlockContainer.java
 * -----------------------------
 * (C) Copyright 2006-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.title;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.EntityBlockResult;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.general.Dataset;

/**
 * A container that holds all the pieces of a single legend item.
 */
public class LegendItemBlockContainer extends BlockContainer {

    /**
     * The dataset.
     */
    private Dataset dataset;

    /**
     * The series key.
     */
    private Comparable seriesKey;

    /** The dataset index. */
    private int datasetIndex;

    /** The series index. */
    private int series;

    /** The tool tip text (can be {@code null}). */
    private String toolTipText;

    /** The URL text (can be {@code null}). */
    private String urlText;

    /**
     * Creates a new legend item block.
     *
     * @param arrangement  the arrangement.
     * @param dataset  the dataset.
     * @param seriesKey  the series key.
     */
    public LegendItemBlockContainer(Arrangement arrangement, Dataset dataset,
            Comparable seriesKey) {
        super(arrangement);
        this.dataset = dataset;
        this.seriesKey = seriesKey;
    }

    /**
     * Returns a reference to the dataset for the associated legend item.
     *
     * @return A dataset reference.
     */
    public Dataset getDataset() {
        return this.dataset;
    }

    /**
     * Returns the series key.
     *
     * @return The series key.
     */
    public Comparable getSeriesKey() {
        return this.seriesKey;
    }

    /**
     * Returns the series index.
     *
     * @return The series index.
     */
    public int getSeriesIndex() {
        return this.series;
    }

    /**
     * Returns the tool tip text.
     *
     * @return The tool tip text (possibly {@code null}).
     */
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Sets the tool tip text.
     *
     * @param text  the text ({@code null} permitted).
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
    }

    /**
     * Returns the URL text.
     *
     * @return The URL text (possibly {@code null}).
     */
    public String getURLText() {
        return this.urlText;
    }

    /**
     * Sets the URL text.
     *
     * @param text  the text ({@code null} permitted).
     */
    public void setURLText(String text) {
        this.urlText = text;
    }

    /**
     * Draws the block within the specified area.
     *
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  passed on to blocks within the container
     *                ({@code null} permitted).
     *
     * @return An instance of {@link EntityBlockResult}, or {@code null}.
     */
    @Override
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        // draw the block without collecting entities
        super.draw(g2, area, null);
        EntityBlockParams ebp;
        BlockResult r = new BlockResult();
        if (params instanceof EntityBlockParams) {
            ebp = (EntityBlockParams) params;
            if (ebp.getGenerateEntities()) {
                EntityCollection ec = new StandardEntityCollection();
                LegendItemEntity entity = new LegendItemEntity(
                        (Shape) area.clone());
                entity.setSeriesKey(this.seriesKey);
                entity.setDataset(this.dataset);
                entity.setToolTipText(getToolTipText());
                entity.setURLText(getURLText());
                ec.add(entity);
                r.setEntityCollection(ec);
            }
        }
        return r;
    }
}
