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
 * ------------------------
 * RendererChangeEvent.java
 * ------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.event;

/**
 * An event that can be forwarded to any {@link RendererChangeListener} to
 * signal a change to a renderer.
 */
public class RendererChangeEvent extends ChartChangeEvent {

    /** The renderer that generated the event. */
    private Object renderer;

    /**
     * A flag that indicates whether this event relates to a change in the
     * series visibility.  If so, the receiver (if it is a plot) may want to
     * update the axis bounds.
     */
    private boolean seriesVisibilityChanged;

    /**
     * Creates a new event.
     *
     * @param renderer  the renderer that generated the event.
     */
    public RendererChangeEvent(Object renderer) {
        this(renderer, false);
    }

    /**
     * Creates a new event.
     *
     * @param renderer  the renderer that generated the event.
     * @param seriesVisibilityChanged  a flag that indicates whether
     *         the event relates to a change in the series visibility flags.
     */
    public RendererChangeEvent(Object renderer,
            boolean seriesVisibilityChanged) {
        super(renderer);
        this.renderer = renderer;
        this.seriesVisibilityChanged = seriesVisibilityChanged;
    }

    /**
     * Returns the renderer that generated the event.
     *
     * @return The renderer that generated the event.
     */
    public Object getRenderer() {
        return this.renderer;
    }

    /**
     * Returns the flag that indicates whether the event relates to
     * a change in series visibility.
     *
     * @return A boolean.
     */
    public boolean getSeriesVisibilityChanged() {
        return this.seriesVisibilityChanged;
    }

}
