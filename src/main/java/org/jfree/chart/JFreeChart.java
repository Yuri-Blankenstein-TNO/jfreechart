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
 * JFreeChart.java
 * ---------------
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Andrzej Porebski;
 *                   David Li;
 *                   Wolfgang Irler;
 *                   Christian W. Zuckschwerdt;
 *                   Klaus Rheinwald;
 *                   Nicolas Brodu;
 *                   Peter Kolb (patch 2603321);
 *                   Tracy Hiltbrand (equals/hashCode comply with EqualsVerifier);
 *
 * NOTE: The above list of contributors lists only the people that have
 * contributed to this source file (JFreeChart.java) - for a list of ALL
 * contributors to the project, please see the README.md file.
 *
 */

package org.jfree.chart;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.event.EventListenerList;

import org.jfree.chart.block.BlockParams;
import org.jfree.chart.block.EntityBlockResult;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.JFreeChartEntity;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.chart.event.TitleChangeListener;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.Align;
import org.jfree.chart.ui.Drawable;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.Size2D;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.chart.util.PaintUtils;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.SerialUtils;
import org.jfree.data.Range;

/**
 * A chart class implemented using the Java 2D APIs.  The current version
 * supports bar charts, line charts, pie charts and xy plots (including time
 * series data).
 * <P>
 * JFreeChart coordinates several objects to achieve its aim of being able to
 * draw a chart on a Java 2D graphics device: a list of {@link Title} objects
 * (which often includes the chart's legend), a {@link Plot} and a
 * {@link org.jfree.data.general.Dataset} (the plot in turn manages a
 * domain axis and a range axis).
 * <P>
 * You should use a {@link ChartPanel} to display a chart in a GUI.
 * <P>
 * The {@link ChartFactory} class contains static methods for creating
 * 'ready-made' charts.
 *
 * @see ChartPanel
 * @see ChartFactory
 * @see Title
 * @see Plot
 */
public class JFreeChart implements Drawable, TitleChangeListener,
        PlotChangeListener, Serializable, Cloneable {

    /** For serialization. */
    private static final long serialVersionUID = -3470703747817429120L;

    /** The default font for titles. */
    public static final Font DEFAULT_TITLE_FONT
            = new Font("SansSerif", Font.BOLD, 18);

    /** The default background color. */
    public static final Paint DEFAULT_BACKGROUND_PAINT = Color.LIGHT_GRAY;

    /** The default background image. */
    public static final Image DEFAULT_BACKGROUND_IMAGE = null;

    /** The default background image alignment. */
    public static final int DEFAULT_BACKGROUND_IMAGE_ALIGNMENT = Align.FIT;

    /** The default background image alpha. */
    public static final float DEFAULT_BACKGROUND_IMAGE_ALPHA = 0.5f;

    /**
     * The key for a rendering hint that can suppress the generation of a 
     * shadow effect when drawing the chart.  The hint value must be a 
     * Boolean.
     */
    public static final RenderingHints.Key KEY_SUPPRESS_SHADOW_GENERATION
            = new RenderingHints.Key(0) {
        @Override
        public boolean isCompatibleValue(Object val) {
            return val instanceof Boolean;
        }
    };
    
    /**
     * Rendering hints that will be used for chart drawing.  This should never
     * be {@code null}.
     */
    private transient RenderingHints renderingHints;

    /** The chart id (optional, will be used by JFreeSVG export). */
    private String id;
    
    /** A flag that controls whether the chart border is drawn. */
    private boolean borderVisible;

    /** The stroke used to draw the chart border (if visible). */
    private transient Stroke borderStroke;

    /** The paint used to draw the chart border (if visible). */
    private transient Paint borderPaint;

    /** The padding between the chart border and the chart drawing area. */
    private RectangleInsets padding;

    /** The chart title (optional). */
    private TextTitle title;

    /**
     * The chart subtitles (zero, one or many).  This field should never be
     * {@code null}.
     */
    private List<Title> subtitles;

    /** Draws the visual representation of the data. */
    private Plot plot;

    /** Paint used to draw the background of the chart. */
    private transient Paint backgroundPaint;

    /** An optional background image for the chart. */
    private transient Image backgroundImage;  // todo: not serialized yet

    /** The alignment for the background image. */
    private int backgroundImageAlignment = Align.FIT;

    /** The alpha transparency for the background image. */
    private float backgroundImageAlpha = 0.5f;

    /** Storage for registered change listeners. */
    private transient EventListenerList changeListeners;

    /** Storage for registered progress listeners. */
    private transient EventListenerList progressListeners;

    /**
     * A flag that can be used to enable/disable notification of chart change
     * events.
     */
    private boolean notify;

    /** 
     * A flag that controls whether rendering hints that identify
     * chart element should be added during rendering.  This defaults to false
     * and it should only be enabled if the output target will use the hints.
     * JFreeSVG is one output target that supports these hints.
     */
    private boolean elementHinting;
    
    /**
     * Creates a new chart based on the supplied plot.  The chart will have
     * a legend added automatically, but no title (although you can easily add
     * one later).
     * <br><br>
     * Note that the  {@link ChartFactory} class contains a range
     * of static methods that will return ready-made charts, and often this
     * is a more convenient way to create charts than using this constructor.
     *
     * @param plot  the plot ({@code null} not permitted).
     */
    public JFreeChart(Plot plot) {
        this(null, null, plot, true);
    }

    /**
     * Creates a new chart with the given title and plot.  A default font
     * ({@link #DEFAULT_TITLE_FONT}) is used for the title, and the chart will
     * have a legend added automatically.
     * <br><br>
     * Note that the {@link ChartFactory} class contains a range
     * of static methods that will return ready-made charts, and often this
     * is a more convenient way to create charts than using this constructor.
     *
     * @param title  the chart title ({@code null} permitted).
     * @param plot  the plot ({@code null} not permitted).
     */
    public JFreeChart(String title, Plot plot) {
        this(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }

    /**
     * Creates a new chart with the given title and plot.  The
     * {@code createLegend} argument specifies whether a legend
     * should be added to the chart.
     * <br><br>
     * Note that the  {@link ChartFactory} class contains a range
     * of static methods that will return ready-made charts, and often this
     * is a more convenient way to create charts than using this constructor.
     *
     * @param title  the chart title ({@code null} permitted).
     * @param titleFont  the font for displaying the chart title
     *                   ({@code null} permitted).
     * @param plot  controller of the visual representation of the data
     *              ({@code null} not permitted).
     * @param createLegend  a flag indicating whether a legend should
     *                      be created for the chart.
     */
    public JFreeChart(String title, Font titleFont, Plot plot,
                      boolean createLegend) {

        Args.nullNotPermitted(plot, "plot");
        this.id = null;
        plot.setChart(this);
        
        // create storage for listeners...
        this.progressListeners = new EventListenerList();
        this.changeListeners = new EventListenerList();
        this.notify = true;  // default is to notify listeners when the
                             // chart changes

        this.renderingHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // added the following hint because of 
        // http://stackoverflow.com/questions/7785082/
        this.renderingHints.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        
        this.borderVisible = false;
        this.borderStroke = new BasicStroke(1.0f);
        this.borderPaint = Color.BLACK;

        this.padding = RectangleInsets.ZERO_INSETS;

        this.plot = plot;
        plot.addChangeListener(this);

        this.subtitles = new ArrayList<>();

        // create a legend, if requested...
        if (createLegend) {
            LegendTitle legend = new LegendTitle(this.plot);
            legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
            legend.setBackgroundPaint(Color.WHITE);
            legend.setPosition(RectangleEdge.BOTTOM);
            this.subtitles.add(legend);
            legend.addChangeListener(this);
        }

        // add the chart title, if one has been specified...
        if (title != null) {
            if (titleFont == null) {
                titleFont = DEFAULT_TITLE_FONT;
            }
            this.title = new TextTitle(title, titleFont);
            this.title.addChangeListener(this);
        }

        this.backgroundPaint = DEFAULT_BACKGROUND_PAINT;

        this.backgroundImage = DEFAULT_BACKGROUND_IMAGE;
        this.backgroundImageAlignment = DEFAULT_BACKGROUND_IMAGE_ALIGNMENT;
        this.backgroundImageAlpha = DEFAULT_BACKGROUND_IMAGE_ALPHA;
    }

    /**
     * Returns the ID for the chart.
     * 
     * @return The ID for the chart (possibly {@code null}).
     */
    public String getID() {
        return this.id;
    }
    
    /**
     * Sets the ID for the chart.
     * 
     * @param id  the id ({@code null} permitted).
     */
    public void setID(String id) {
        this.id = id;
    }
    
    /**
     * Returns the flag that controls whether rendering hints 
     * ({@link ChartHints#KEY_BEGIN_ELEMENT} and 
     * {@link ChartHints#KEY_END_ELEMENT}) that identify chart elements are 
     * added during rendering.  The default value is {@code false}.
     * 
     * @return A boolean.
     * 
     * @see #setElementHinting(boolean) 
     */
    public boolean getElementHinting() {
        return this.elementHinting;
    }
    
    /**
     * Sets the flag that controls whether rendering hints 
     * ({@link ChartHints#KEY_BEGIN_ELEMENT} and 
     * {@link ChartHints#KEY_END_ELEMENT}) that identify chart elements are 
     * added during rendering.
     * 
     * @param hinting  the new flag value.
     * 
     * @see #getElementHinting() 
     */
    public void setElementHinting(boolean hinting) {
        this.elementHinting = hinting;
    }
    
    /**
     * Returns the collection of rendering hints for the chart.
     *
     * @return The rendering hints for the chart (never {@code null}).
     *
     * @see #setRenderingHints(RenderingHints)
     */
    public RenderingHints getRenderingHints() {
        return this.renderingHints;
    }

    /**
     * Sets the rendering hints for the chart.  These will be added (using the
     * {@code Graphics2D.addRenderingHints()} method) near the start of the
     * {@code JFreeChart.draw()} method.
     *
     * @param renderingHints  the rendering hints ({@code null} not permitted).
     *
     * @see #getRenderingHints()
     */
    public void setRenderingHints(RenderingHints renderingHints) {
        Args.nullNotPermitted(renderingHints, "renderingHints");
        this.renderingHints = renderingHints;
        fireChartChanged();
    }

    /**
     * Returns a flag that controls whether a border is drawn around the
     * outside of the chart.
     *
     * @return A boolean.
     *
     * @see #setBorderVisible(boolean)
     */
    public boolean isBorderVisible() {
        return this.borderVisible;
    }

    /**
     * Sets a flag that controls whether a border is drawn around the
     * outside of the chart.
     *
     * @param visible  the flag.
     *
     * @see #isBorderVisible()
     */
    public void setBorderVisible(boolean visible) {
        this.borderVisible = visible;
        fireChartChanged();
    }

    /**
     * Returns the stroke used to draw the chart border (if visible).
     *
     * @return The border stroke.
     *
     * @see #setBorderStroke(Stroke)
     */
    public Stroke getBorderStroke() {
        return this.borderStroke;
    }

    /**
     * Sets the stroke used to draw the chart border (if visible).
     *
     * @param stroke  the stroke.
     *
     * @see #getBorderStroke()
     */
    public void setBorderStroke(Stroke stroke) {
        this.borderStroke = stroke;
        fireChartChanged();
    }

    /**
     * Returns the paint used to draw the chart border (if visible).
     *
     * @return The border paint.
     *
     * @see #setBorderPaint(Paint)
     */
    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    /**
     * Sets the paint used to draw the chart border (if visible).
     *
     * @param paint  the paint.
     *
     * @see #getBorderPaint()
     */
    public void setBorderPaint(Paint paint) {
        this.borderPaint = paint;
        fireChartChanged();
    }

    /**
     * Returns the padding between the chart border and the chart drawing area.
     *
     * @return The padding (never {@code null}).
     *
     * @see #setPadding(RectangleInsets)
     */
    public RectangleInsets getPadding() {
        return this.padding;
    }

    /**
     * Sets the padding between the chart border and the chart drawing area,
     * and sends a {@link ChartChangeEvent} to all registered listeners.
     *
     * @param padding  the padding ({@code null} not permitted).
     *
     * @see #getPadding()
     */
    public void setPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.padding = padding;
        notifyListeners(new ChartChangeEvent(this));
    }

    /**
     * Returns the main chart title.  Very often a chart will have just one
     * title, so we make this case simple by providing accessor methods for
     * the main title.  However, multiple titles are supported - see the
     * {@link #addSubtitle(Title)} method.
     *
     * @return The chart title (possibly {@code null}).
     *
     * @see #setTitle(TextTitle)
     */
    public TextTitle getTitle() {
        return this.title;
    }

    /**
     * Sets the main title for the chart and sends a {@link ChartChangeEvent}
     * to all registered listeners.  If you do not want a title for the
     * chart, set it to {@code null}.  If you want more than one title on
     * a chart, use the {@link #addSubtitle(Title)} method.
     *
     * @param title  the title ({@code null} permitted).
     *
     * @see #getTitle()
     */
    public void setTitle(TextTitle title) {
        if (this.title != null) {
            this.title.removeChangeListener(this);
        }
        this.title = title;
        if (title != null) {
            title.addChangeListener(this);
        }
        fireChartChanged();
    }

    /**
     * Sets the chart title and sends a {@link ChartChangeEvent} to all
     * registered listeners.  This is a convenience method that ends up calling
     * the {@link #setTitle(TextTitle)} method.  If there is an existing title,
     * its text is updated, otherwise a new title using the default font is
     * added to the chart.  If {@code text} is {@code null} the chart
     * title is set to {@code null}.
     *
     * @param text  the title text ({@code null} permitted).
     *
     * @see #getTitle()
     */
    public void setTitle(String text) {
        if (text != null) {
            if (this.title == null) {
                setTitle(new TextTitle(text, JFreeChart.DEFAULT_TITLE_FONT));
            } else {
                this.title.setText(text);
            }
        }
        else {
            setTitle((TextTitle) null);
        }
    }

    /**
     * Adds a legend to the plot and sends a {@link ChartChangeEvent} to all
     * registered listeners.
     *
     * @param legend  the legend ({@code null} not permitted).
     *
     * @see #removeLegend()
     */
    public void addLegend(LegendTitle legend) {
        addSubtitle(legend);
    }

    /**
     * Returns the legend for the chart, if there is one.  Note that a chart
     * can have more than one legend - this method returns the first.
     *
     * @return The legend (possibly {@code null}).
     *
     * @see #getLegend(int)
     */
    public LegendTitle getLegend() {
        return getLegend(0);
    }

    /**
     * Returns the nth legend for a chart, or {@code null}.
     *
     * @param index  the legend index (zero-based).
     *
     * @return The legend (possibly {@code null}).
     *
     * @see #addLegend(LegendTitle)
     */
    public LegendTitle getLegend(int index) {
        int seen = 0;
        for (Title subtitle : this.subtitles) {
            if (subtitle instanceof LegendTitle) {
                if (seen == index) {
                    return (LegendTitle) subtitle;
                } else {
                    seen++;
                }
            }
        }
        return null;
    }

    /**
     * Removes the first legend in the chart and sends a
     * {@link ChartChangeEvent} to all registered listeners.
     *
     * @see #getLegend()
     */
    public void removeLegend() {
        removeSubtitle(getLegend());
    }

    /**
     * Returns the list of subtitles for the chart.
     *
     * @return The subtitle list (possibly empty, but never {@code null}).
     *
     * @see #setSubtitles(List)
     */
    public List<Title> getSubtitles() {
        return new ArrayList<>(this.subtitles);
    }

    /**
     * Sets the title list for the chart (completely replaces any existing
     * titles) and sends a {@link ChartChangeEvent} to all registered
     * listeners.
     *
     * @param subtitles  the new list of subtitles ({@code null} not
     *                   permitted).
     *
     * @see #getSubtitles()
     */
    public void setSubtitles(List<Title> subtitles) {
        if (subtitles == null) {
            throw new NullPointerException("Null 'subtitles' argument.");
        }
        setNotify(false);
        clearSubtitles();
        for (Title t : subtitles) {
            if (t != null) {
                addSubtitle(t);
            }
        }
        setNotify(true);  // this fires a ChartChangeEvent
    }

    /**
     * Returns the number of titles for the chart.
     *
     * @return The number of titles for the chart.
     *
     * @see #getSubtitles()
     */
    public int getSubtitleCount() {
        return this.subtitles.size();
    }

    /**
     * Returns a chart subtitle.
     *
     * @param index  the index of the chart subtitle (zero based).
     *
     * @return A chart subtitle.
     *
     * @see #addSubtitle(Title)
     */
    public Title getSubtitle(int index) {
        if ((index < 0) || (index >= getSubtitleCount())) {
            throw new IllegalArgumentException("Index out of range.");
        }
        return this.subtitles.get(index);
    }

    /**
     * Adds a chart subtitle, and notifies registered listeners that the chart
     * has been modified.
     *
     * @param subtitle  the subtitle ({@code null} not permitted).
     *
     * @see #getSubtitle(int)
     */
    public void addSubtitle(Title subtitle) {
        Args.nullNotPermitted(subtitle, "subtitle");
        this.subtitles.add(subtitle);
        subtitle.addChangeListener(this);
        fireChartChanged();
    }

    /**
     * Adds a subtitle at a particular position in the subtitle list, and sends
     * a {@link ChartChangeEvent} to all registered listeners.
     *
     * @param index  the index (in the range 0 to {@link #getSubtitleCount()}).
     * @param subtitle  the subtitle to add ({@code null} not permitted).
     */
    public void addSubtitle(int index, Title subtitle) {
        if (index < 0 || index > getSubtitleCount()) {
            throw new IllegalArgumentException(
                    "The 'index' argument is out of range.");
        }
        Args.nullNotPermitted(subtitle, "subtitle");
        this.subtitles.add(index, subtitle);
        subtitle.addChangeListener(this);
        fireChartChanged();
    }

    /**
     * Clears all subtitles from the chart and sends a {@link ChartChangeEvent}
     * to all registered listeners.
     *
     * @see #addSubtitle(Title)
     */
    public void clearSubtitles() {
        for (Title t : this.subtitles) {
            t.removeChangeListener(this);
        }
        this.subtitles.clear();
        fireChartChanged();
    }

    /**
     * Removes the specified subtitle and sends a {@link ChartChangeEvent} to
     * all registered listeners.
     *
     * @param title  the title.
     *
     * @see #addSubtitle(Title)
     */
    public void removeSubtitle(Title title) {
        this.subtitles.remove(title);
        fireChartChanged();
    }

    /**
     * Returns the plot for the chart.  The plot is a class responsible for
     * coordinating the visual representation of the data, including the axes
     * (if any).
     *
     * @return The plot.
     */
    public Plot getPlot() {
        return this.plot;
    }

    /**
     * Returns the plot cast as a {@link CategoryPlot}.
     * <p>
     * NOTE: if the plot is not an instance of {@link CategoryPlot}, then a
     * {@code ClassCastException} is thrown.
     *
     * @return The plot.
     *
     * @see #getPlot()
     */
    public CategoryPlot getCategoryPlot() {
        return (CategoryPlot) this.plot;
    }

    /**
     * Returns the plot cast as an {@link XYPlot}.
     * <p>
     * NOTE: if the plot is not an instance of {@link XYPlot}, then a
     * {@code ClassCastException} is thrown.
     *
     * @return The plot.
     *
     * @see #getPlot()
     */
    public XYPlot getXYPlot() {
        return (XYPlot) this.plot;
    }

    /**
     * Returns a flag that indicates whether antialiasing is used when
     * the chart is drawn.
     *
     * @return The flag.
     *
     * @see #setAntiAlias(boolean)
     */
    public boolean getAntiAlias() {
        Object val = this.renderingHints.get(RenderingHints.KEY_ANTIALIASING);
        return RenderingHints.VALUE_ANTIALIAS_ON.equals(val);
    }

    /**
     * Sets a flag that indicates whether antialiasing is used when the
     * chart is drawn.
     * <P>
     * Antialiasing usually improves the appearance of charts, but is slower.
     *
     * @param flag  the new value of the flag.
     *
     * @see #getAntiAlias()
     */
    public void setAntiAlias(boolean flag) {
        Object hint = flag ? RenderingHints.VALUE_ANTIALIAS_ON 
                : RenderingHints.VALUE_ANTIALIAS_OFF;
        this.renderingHints.put(RenderingHints.KEY_ANTIALIASING, hint);
        fireChartChanged();
    }

    /**
     * Returns the current value stored in the rendering hints table for
     * {@link RenderingHints#KEY_TEXT_ANTIALIASING}.
     *
     * @return The hint value (possibly {@code null}).
     *
     * @see #setTextAntiAlias(Object)
     */
    public Object getTextAntiAlias() {
        return this.renderingHints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
    }

    /**
     * Sets the value in the rendering hints table for
     * {@link RenderingHints#KEY_TEXT_ANTIALIASING} to either
     * {@link RenderingHints#VALUE_TEXT_ANTIALIAS_ON} or
     * {@link RenderingHints#VALUE_TEXT_ANTIALIAS_OFF}, then sends a
     * {@link ChartChangeEvent} to all registered listeners.
     *
     * @param flag  the new value of the flag.
     *
     * @see #getTextAntiAlias()
     * @see #setTextAntiAlias(Object)
     */
    public void setTextAntiAlias(boolean flag) {
        if (flag) {
            setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        else {
            setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }

    /**
     * Sets the value in the rendering hints table for
     * {@link RenderingHints#KEY_TEXT_ANTIALIASING} and sends a
     * {@link ChartChangeEvent} to all registered listeners.
     *
     * @param val  the new value ({@code null} permitted).
     *
     * @see #getTextAntiAlias()
     * @see #setTextAntiAlias(boolean)
     */
    public void setTextAntiAlias(Object val) {
        this.renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, val);
        notifyListeners(new ChartChangeEvent(this));
    }

    /**
     * Returns the paint used for the chart background.
     *
     * @return The paint (possibly {@code null}).
     *
     * @see #setBackgroundPaint(Paint)
     */
    public Paint getBackgroundPaint() {
        return this.backgroundPaint;
    }

    /**
     * Sets the paint used to fill the chart background and sends a
     * {@link ChartChangeEvent} to all registered listeners.
     *
     * @param paint  the paint ({@code null} permitted).
     *
     * @see #getBackgroundPaint()
     */
    public void setBackgroundPaint(Paint paint) {

        if (this.backgroundPaint != null) {
            if (!this.backgroundPaint.equals(paint)) {
                this.backgroundPaint = paint;
                fireChartChanged();
            }
        }
        else {
            if (paint != null) {
                this.backgroundPaint = paint;
                fireChartChanged();
            }
        }

    }

    /**
     * Returns the background image for the chart, or {@code null} if
     * there is no image.
     *
     * @return The image (possibly {@code null}).
     *
     * @see #setBackgroundImage(Image)
     */
    public Image getBackgroundImage() {
        return this.backgroundImage;
    }

    /**
     * Sets the background image for the chart and sends a
     * {@link ChartChangeEvent} to all registered listeners.
     *
     * @param image  the image ({@code null} permitted).
     *
     * @see #getBackgroundImage()
     */
    public void setBackgroundImage(Image image) {

        if (this.backgroundImage != null) {
            if (!this.backgroundImage.equals(image)) {
                this.backgroundImage = image;
                fireChartChanged();
            }
        }
        else {
            if (image != null) {
                this.backgroundImage = image;
                fireChartChanged();
            }
        }

    }

    /**
     * Returns the background image alignment. Alignment constants are defined
     * in the {@link Align} class.
     *
     * @return The alignment.
     *
     * @see #setBackgroundImageAlignment(int)
     */
    public int getBackgroundImageAlignment() {
        return this.backgroundImageAlignment;
    }

    /**
     * Sets the background alignment.  Alignment options are defined by the
     * {@link org.jfree.chart.ui.Align} class.
     *
     * @param alignment  the alignment.
     *
     * @see #getBackgroundImageAlignment()
     */
    public void setBackgroundImageAlignment(int alignment) {
        if (this.backgroundImageAlignment != alignment) {
            this.backgroundImageAlignment = alignment;
            fireChartChanged();
        }
    }

    /**
     * Returns the alpha-transparency for the chart's background image.
     *
     * @return The alpha-transparency.
     *
     * @see #setBackgroundImageAlpha(float)
     */
    public float getBackgroundImageAlpha() {
        return this.backgroundImageAlpha;
    }

    /**
     * Sets the alpha-transparency for the chart's background image.
     * Registered listeners are notified that the chart has been changed.
     *
     * @param alpha  the alpha value.
     *
     * @see #getBackgroundImageAlpha()
     */
    public void setBackgroundImageAlpha(float alpha) {
        if (this.backgroundImageAlpha != alpha) {
            this.backgroundImageAlpha = alpha;
            fireChartChanged();
        }
    }

    /**
     * Returns a flag that controls whether change events are sent to
     * registered listeners.
     *
     * @return A boolean.
     *
     * @see #setNotify(boolean)
     */
    public boolean isNotify() {
        return this.notify;
    }

    /**
     * Sets a flag that controls whether listeners receive
     * {@link ChartChangeEvent} notifications.
     *
     * @param notify  a boolean.
     *
     * @see #isNotify()
     */
    public void setNotify(boolean notify) {
        this.notify = notify;
        // if the flag is being set to true, there may be queued up changes...
        if (notify) {
            notifyListeners(new ChartChangeEvent(this));
        }
    }

    /**
     * Draws the chart on a Java 2D graphics device (such as the screen or a
     * printer).
     * <P>
     * This method is the focus of the entire JFreeChart library.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the chart should be drawn.
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area) {
        draw(g2, area, null, null);
    }

    /**
     * Draws the chart on a Java 2D graphics device (such as the screen or a
     * printer).  This method is the focus of the entire JFreeChart library.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the chart should be drawn.
     * @param info  records info about the drawing (null means collect no info).
     */
    public void draw(Graphics2D g2, Rectangle2D area, ChartRenderingInfo info) {
        draw(g2, area, null, info);
    }

    /**
     * Draws the chart on a Java 2D graphics device (such as the screen or a
     * printer).
     * <P>
     * This method is the focus of the entire JFreeChart library.
     *
     * @param g2  the graphics device.
     * @param chartArea  the area within which the chart should be drawn.
     * @param anchor  the anchor point (in Java2D space) for the chart
     *                ({@code null} permitted).
     * @param info  records info about the drawing (null means collect no info).
     */
    public void draw(Graphics2D g2, Rectangle2D chartArea, Point2D anchor,
             ChartRenderingInfo info) {

        notifyListeners(new ChartProgressEvent(this, this,
                ChartProgressEvent.DRAWING_STARTED, 0));
        
        if (this.elementHinting) {
            Map<String, String> m = new HashMap<>();
            if (this.id != null) {
                m.put("id", this.id);
            }
            m.put("ref", "JFREECHART_TOP_LEVEL");            
            g2.setRenderingHint(ChartHints.KEY_BEGIN_ELEMENT, m);            
        }
        
        EntityCollection entities = null;
        // record the chart area, if info is requested...
        if (info != null) {
            info.clear();
            info.setChartArea(chartArea);
            entities = info.getEntityCollection();
        }
        if (entities != null) {
            entities.add(new JFreeChartEntity((Rectangle2D) chartArea.clone(),
                    this));
        }

        // ensure no drawing occurs outside chart area...
        Shape savedClip = g2.getClip();
        g2.clip(chartArea);

        g2.addRenderingHints(this.renderingHints);

        // draw the chart background...
        if (this.backgroundPaint != null) {
            g2.setPaint(this.backgroundPaint);
            g2.fill(chartArea);
        }

        if (this.backgroundImage != null) {
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    this.backgroundImageAlpha));
            Rectangle2D dest = new Rectangle2D.Double(0.0, 0.0,
                    this.backgroundImage.getWidth(null),
                    this.backgroundImage.getHeight(null));
            Align.align(dest, chartArea, this.backgroundImageAlignment);
            g2.drawImage(this.backgroundImage, (int) dest.getX(),
                    (int) dest.getY(), (int) dest.getWidth(),
                    (int) dest.getHeight(), null);
            g2.setComposite(originalComposite);
        }

        if (isBorderVisible()) {
            Paint paint = getBorderPaint();
            Stroke stroke = getBorderStroke();
            if (paint != null && stroke != null) {
                Rectangle2D borderArea = new Rectangle2D.Double(
                        chartArea.getX(), chartArea.getY(),
                        chartArea.getWidth() - 1.0, chartArea.getHeight()
                        - 1.0);
                g2.setPaint(paint);
                g2.setStroke(stroke);
                g2.draw(borderArea);
            }
        }

        // draw the title and subtitles...
        Rectangle2D nonTitleArea = new Rectangle2D.Double();
        nonTitleArea.setRect(chartArea);
        this.padding.trim(nonTitleArea);

        if (this.title != null && this.title.isVisible()) {
            EntityCollection e = drawTitle(this.title, g2, nonTitleArea,
                    (entities != null));
            if (e != null && entities != null) {
                entities.addAll(e);
            }
        }

        for (Title currentTitle : this.subtitles) {
            if (currentTitle.isVisible()) {
                EntityCollection e = drawTitle(currentTitle, g2, nonTitleArea,
                        (entities != null));
                if (e != null && entities != null) {
                    entities.addAll(e);
                }
            }
        }

        Rectangle2D plotArea = nonTitleArea;

        // draw the plot (axes and data visualisation)
        PlotRenderingInfo plotInfo = null;
        if (info != null) {
            plotInfo = info.getPlotInfo();
        }
        this.plot.draw(g2, plotArea, anchor, null, plotInfo);
        g2.setClip(savedClip);
        if (this.elementHinting) {         
            g2.setRenderingHint(ChartHints.KEY_END_ELEMENT, Boolean.TRUE);            
        }

        notifyListeners(new ChartProgressEvent(this, this,
                ChartProgressEvent.DRAWING_FINISHED, 100));
    }

    /**
     * Creates a rectangle that is aligned to the frame.
     *
     * @param dimensions  the dimensions for the rectangle.
     * @param frame  the frame to align to.
     * @param hAlign  the horizontal alignment.
     * @param vAlign  the vertical alignment.
     *
     * @return A rectangle.
     */
    private Rectangle2D createAlignedRectangle2D(Size2D dimensions,
            Rectangle2D frame, HorizontalAlignment hAlign,
            VerticalAlignment vAlign) {
        double x = Double.NaN;
        double y = Double.NaN;
        if (hAlign == HorizontalAlignment.LEFT) {
            x = frame.getX();
        }
        else if (hAlign == HorizontalAlignment.CENTER) {
            x = frame.getCenterX() - (dimensions.width / 2.0);
        }
        else if (hAlign == HorizontalAlignment.RIGHT) {
            x = frame.getMaxX() - dimensions.width;
        }
        if (vAlign == VerticalAlignment.TOP) {
            y = frame.getY();
        }
        else if (vAlign == VerticalAlignment.CENTER) {
            y = frame.getCenterY() - (dimensions.height / 2.0);
        }
        else if (vAlign == VerticalAlignment.BOTTOM) {
            y = frame.getMaxY() - dimensions.height;
        }

        return new Rectangle2D.Double(x, y, dimensions.width,
                dimensions.height);
    }

    /**
     * Draws a title.  The title should be drawn at the top, bottom, left or
     * right of the specified area, and the area should be updated to reflect
     * the amount of space used by the title.
     *
     * @param t  the title ({@code null} not permitted).
     * @param g2  the graphics device ({@code null} not permitted).
     * @param area  the chart area, excluding any existing titles
     *              ({@code null} not permitted).
     * @param entities  a flag that controls whether an entity
     *                  collection is returned for the title.
     *
     * @return An entity collection for the title (possibly {@code null}).
     */
    protected EntityCollection drawTitle(Title t, Graphics2D g2,
                                         Rectangle2D area, boolean entities) {

        Args.nullNotPermitted(t, "t");
        Args.nullNotPermitted(area, "area");
        Rectangle2D titleArea;
        RectangleEdge position = t.getPosition();
        double ww = area.getWidth();
        if (ww <= 0.0) {
            return null;
        }
        double hh = area.getHeight();
        if (hh <= 0.0) {
            return null;
        }
        RectangleConstraint constraint = new RectangleConstraint(ww,
                new Range(0.0, ww), LengthConstraintType.RANGE, hh,
                new Range(0.0, hh), LengthConstraintType.RANGE);
        Object retValue = null;
        BlockParams p = new BlockParams();
        p.setGenerateEntities(entities);
        if (position == RectangleEdge.TOP) {
            Size2D size = t.arrange(g2, constraint);
            titleArea = createAlignedRectangle2D(size, area,
                    t.getHorizontalAlignment(), VerticalAlignment.TOP);
            retValue = t.draw(g2, titleArea, p);
            area.setRect(area.getX(), Math.min(area.getY() + size.height,
                    area.getMaxY()), area.getWidth(), Math.max(area.getHeight()
                    - size.height, 0));
        } else if (position == RectangleEdge.BOTTOM) {
            Size2D size = t.arrange(g2, constraint);
            titleArea = createAlignedRectangle2D(size, area,
                    t.getHorizontalAlignment(), VerticalAlignment.BOTTOM);
            retValue = t.draw(g2, titleArea, p);
            area.setRect(area.getX(), area.getY(), area.getWidth(),
                    area.getHeight() - size.height);
        } else if (position == RectangleEdge.RIGHT) {
            Size2D size = t.arrange(g2, constraint);
            titleArea = createAlignedRectangle2D(size, area,
                    HorizontalAlignment.RIGHT, t.getVerticalAlignment());
            retValue = t.draw(g2, titleArea, p);
            area.setRect(area.getX(), area.getY(), area.getWidth()
                    - size.width, area.getHeight());
        } else if (position == RectangleEdge.LEFT) {
            Size2D size = t.arrange(g2, constraint);
            titleArea = createAlignedRectangle2D(size, area,
                    HorizontalAlignment.LEFT, t.getVerticalAlignment());
            retValue = t.draw(g2, titleArea, p);
            area.setRect(area.getX() + size.width, area.getY(), area.getWidth()
                    - size.width, area.getHeight());
        }
        else {
            throw new RuntimeException("Unrecognised title position.");
        }
        EntityCollection result = null;
        if (retValue instanceof EntityBlockResult) {
            EntityBlockResult ebr = (EntityBlockResult) retValue;
            result = ebr.getEntityCollection();
        }
        return result;
    }

    /**
     * Creates and returns a buffered image into which the chart has been drawn.
     *
     * @param width  the width.
     * @param height  the height.
     *
     * @return A buffered image.
     */
    public BufferedImage createBufferedImage(int width, int height) {
        return createBufferedImage(width, height, null);
    }

    /**
     * Creates and returns a buffered image into which the chart has been drawn.
     *
     * @param width  the width.
     * @param height  the height.
     * @param info  carries back chart state information ({@code null}
     *              permitted).
     *
     * @return A buffered image.
     */
    public BufferedImage createBufferedImage(int width, int height,
                                             ChartRenderingInfo info) {
        return createBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB,
                info);
    }

    /**
     * Creates and returns a buffered image into which the chart has been drawn.
     *
     * @param width  the width.
     * @param height  the height.
     * @param imageType  the image type.
     * @param info  carries back chart state information ({@code null}
     *              permitted).
     *
     * @return A buffered image.
     */
    public BufferedImage createBufferedImage(int width, int height,
            int imageType, ChartRenderingInfo info) {
        BufferedImage image = new BufferedImage(width, height, imageType);
        Graphics2D g2 = image.createGraphics();
        draw(g2, new Rectangle2D.Double(0, 0, width, height), null, info);
        g2.dispose();
        return image;
    }

    /**
     * Creates and returns a buffered image into which the chart has been drawn.
     *
     * @param imageWidth  the image width.
     * @param imageHeight  the image height.
     * @param drawWidth  the width for drawing the chart (will be scaled to
     *                   fit image).
     * @param drawHeight  the height for drawing the chart (will be scaled to
     *                    fit image).
     * @param info  optional object for collection chart dimension and entity
     *              information.
     *
     * @return A buffered image.
     */
    public BufferedImage createBufferedImage(int imageWidth,
                                             int imageHeight,
                                             double drawWidth,
                                             double drawHeight,
                                             ChartRenderingInfo info) {

        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        double scaleX = imageWidth / drawWidth;
        double scaleY = imageHeight / drawHeight;
        AffineTransform st = AffineTransform.getScaleInstance(scaleX, scaleY);
        g2.transform(st);
        draw(g2, new Rectangle2D.Double(0, 0, drawWidth, drawHeight), null,
                info);
        g2.dispose();
        return image;
    }

    /**
     * Handles a 'click' on the chart.  JFreeChart is not a UI component, so
     * some other object (for example, {@link ChartPanel}) needs to capture
     * the click event and pass it onto the JFreeChart object.
     * If you are not using JFreeChart in a client application, then this
     * method is not required.
     *
     * @param x  x-coordinate of the click (in Java2D space).
     * @param y  y-coordinate of the click (in Java2D space).
     * @param info  contains chart dimension and entity information
     *              ({@code null} not permitted).
     */
    public void handleClick(int x, int y, ChartRenderingInfo info) {
        // pass the click on to the plot...
        // rely on the plot to post a plot change event and redraw the chart...
        this.plot.handleClick(x, y, info.getPlotInfo());
    }

    /**
     * Registers an object for notification of changes to the chart.
     *
     * @param listener  the listener ({@code null} not permitted).
     *
     * @see #removeChangeListener(ChartChangeListener)
     */
    public void addChangeListener(ChartChangeListener listener) {
        Args.nullNotPermitted(listener, "listener");
        this.changeListeners.add(ChartChangeListener.class, listener);
    }

    /**
     * Deregisters an object for notification of changes to the chart.
     *
     * @param listener  the listener ({@code null} not permitted)
     *
     * @see #addChangeListener(ChartChangeListener)
     */
    public void removeChangeListener(ChartChangeListener listener) {
        Args.nullNotPermitted(listener, "listener");
        this.changeListeners.remove(ChartChangeListener.class, listener);
    }

    /**
     * Sends a default {@link ChartChangeEvent} to all registered listeners.
     * <P>
     * This method is for convenience only.
     */
    public void fireChartChanged() {
        ChartChangeEvent event = new ChartChangeEvent(this);
        notifyListeners(event);
    }

    /**
     * Sends a {@link ChartChangeEvent} to all registered listeners.
     *
     * @param event  information about the event that triggered the
     *               notification.
     */
    protected void notifyListeners(ChartChangeEvent event) {
        if (this.notify) {
            Object[] listeners = this.changeListeners.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ChartChangeListener.class) {
                    ((ChartChangeListener) listeners[i + 1]).chartChanged(
                            event);
                }
            }
        }
    }

    /**
     * Registers an object for notification of progress events relating to the
     * chart.
     *
     * @param listener  the object being registered.
     *
     * @see #removeProgressListener(ChartProgressListener)
     */
    public void addProgressListener(ChartProgressListener listener) {
        this.progressListeners.add(ChartProgressListener.class, listener);
    }

    /**
     * Deregisters an object for notification of changes to the chart.
     *
     * @param listener  the object being deregistered.
     *
     * @see #addProgressListener(ChartProgressListener)
     */
    public void removeProgressListener(ChartProgressListener listener) {
        this.progressListeners.remove(ChartProgressListener.class, listener);
    }

    /**
     * Sends a {@link ChartProgressEvent} to all registered listeners.
     *
     * @param event  information about the event that triggered the
     *               notification.
     */
    protected void notifyListeners(ChartProgressEvent event) {
        Object[] listeners = this.progressListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChartProgressListener.class) {
                ((ChartProgressListener) listeners[i + 1]).chartProgress(event);
            }
        }
    }

    /**
     * Receives notification that a chart title has changed, and passes this
     * on to registered listeners.
     *
     * @param event  information about the chart title change.
     */
    @Override
    public void titleChanged(TitleChangeEvent event) {
        event.setChart(this);
        notifyListeners(event);
    }

    /**
     * Receives notification that the plot has changed, and passes this on to
     * registered listeners.
     *
     * @param event  information about the plot change.
     */
    @Override
    public void plotChanged(PlotChangeEvent event) {
        event.setChart(this);
        notifyListeners(event);
    }

    /**
     * Tests this chart for equality with another object.
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
        if (!(obj instanceof JFreeChart)) {
            return false;
        }
        JFreeChart that = (JFreeChart) obj;
        if (!Objects.equals(this.renderingHints, that.renderingHints)) {
            return false;
        }
        if (this.borderVisible != that.borderVisible) {
            return false;
        }
        if (this.elementHinting != that.elementHinting) {
            return false;
        }
        if (!Objects.equals(this.borderStroke, that.borderStroke)) {
            return false;
        }
        if (!PaintUtils.equal(this.borderPaint, that.borderPaint)) {
            return false;
        }
        if (!Objects.equals(this.padding, that.padding)) {
            return false;
        }
        if (!Objects.equals(this.title, that.title)) {
            return false;
        }
        if (!Objects.equals(this.subtitles, that.subtitles)) {
            return false;
        }
        if (!Objects.equals(this.plot, that.plot)) {
            return false;
        }
        if (!PaintUtils.equal(this.backgroundPaint, that.backgroundPaint)) {
            return false;
        }
        if (!Objects.equals(this.backgroundImage, that.backgroundImage)) {
            return false;
        }
        if (this.backgroundImageAlignment != that.backgroundImageAlignment) {
            return false;
        }
        if (Float.floatToIntBits(this.backgroundImageAlpha) !=
            Float.floatToIntBits(that.backgroundImageAlpha)) {
            return false;
        }
        if (this.notify != that.notify) {
            return false;
        }
        if (!Objects.equals(this.id, that.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.renderingHints);
        hash = 43 * hash + Objects.hashCode(this.id);
        hash = 43 * hash + (this.borderVisible ? 1 : 0);
        hash = 43 * hash + Objects.hashCode(this.borderStroke);
        hash = 43 * hash + HashUtils.hashCodeForPaint(this.borderPaint);
        hash = 43 * hash + Objects.hashCode(this.padding);
        hash = 43 * hash + Objects.hashCode(this.title);
        hash = 43 * hash + Objects.hashCode(this.subtitles);
        hash = 43 * hash + Objects.hashCode(this.plot);
        hash = 43 * hash + HashUtils.hashCodeForPaint(this.backgroundPaint);
        hash = 43 * hash + Objects.hashCode(this.backgroundImage);
        hash = 43 * hash + this.backgroundImageAlignment;
        hash = 43 * hash + Float.floatToIntBits(this.backgroundImageAlpha);
        hash = 43 * hash + (this.notify ? 1 : 0);
        hash = 43 * hash + (this.elementHinting ? 1 : 0);
        return hash;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtils.writeStroke(this.borderStroke, stream);
        SerialUtils.writePaint(this.borderPaint, stream);
        SerialUtils.writePaint(this.backgroundPaint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.borderStroke = SerialUtils.readStroke(stream);
        this.borderPaint = SerialUtils.readPaint(stream);
        this.backgroundPaint = SerialUtils.readPaint(stream);
        this.progressListeners = new EventListenerList();
        this.changeListeners = new EventListenerList();
        this.renderingHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        this.renderingHints.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        
        // register as a listener with subcomponents...
        if (this.title != null) {
            this.title.addChangeListener(this);
        }

        for (int i = 0; i < getSubtitleCount(); i++) {
            getSubtitle(i).addChangeListener(this);
        }
        this.plot.addChangeListener(this);
    }

    /**
     * Clones the object, and takes care of listeners.
     * Note: caller shall register its own listeners on cloned graph.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if the chart is not cloneable.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        JFreeChart chart = (JFreeChart) super.clone();

        chart.renderingHints = (RenderingHints) this.renderingHints.clone();
        // private boolean borderVisible;
        // private transient Stroke borderStroke;
        // private transient Paint borderPaint;

        if (this.title != null) {
            chart.title = (TextTitle) this.title.clone();
            chart.title.addChangeListener(chart);
        }

        chart.subtitles = new ArrayList<>();
        for (int i = 0; i < getSubtitleCount(); i++) {
            Title subtitle = (Title) getSubtitle(i).clone();
            chart.subtitles.add(subtitle);
            subtitle.addChangeListener(chart);
        }

        if (this.plot != null) {
            chart.plot = (Plot) this.plot.clone();
            chart.plot.addChangeListener(chart);
        }

        chart.progressListeners = new EventListenerList();
        chart.changeListeners = new EventListenerList();
        return chart;
    }

}
