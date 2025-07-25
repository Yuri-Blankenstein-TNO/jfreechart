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
 * ImageMapUtils.java
 * ------------------
 * (C) Copyright 2004-present, by Richard Atkinson and Contributors.
 *
 * Original Author:  Richard Atkinson;
 * Contributor(s):   David Gilbert;
 *                   Fawad Halim - bug 2690293;
 *
 */

package org.jfree.chart.imagemap;

import java.io.IOException;
import java.io.PrintWriter;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.StringUtils;

/**
 * Collection of utility methods related to producing image maps.
 * Functionality was originally in {@link org.jfree.chart.ChartUtils}.
 */
public class ImageMapUtils {

    private ImageMapUtils() {
        // no requirement to instantiate
    }

    /**
     * Writes an image map to an output stream.
     *
     * @param writer  the writer ({@code null} not permitted).
     * @param name  the map name ({@code null} not permitted).
     * @param info  the chart rendering info ({@code null} not permitted).
     *
     * @throws java.io.IOException if there are any I/O errors.
     */
    public static void writeImageMap(PrintWriter writer, String name,
            ChartRenderingInfo info) throws IOException {

        // defer argument checking...
        writeImageMap(writer, name, info,
                new StandardToolTipTagFragmentGenerator(),
                new StandardURLTagFragmentGenerator());

    }

    /**
     * Writes an image map to an output stream.
     *
     * @param writer  the writer ({@code null} not permitted).
     * @param name  the map name ({@code null} not permitted).
     * @param info  the chart rendering info ({@code null} not permitted).
     * @param useOverLibForToolTips  whether to use OverLIB for tooltips
     *                               (http://www.bosrup.com/web/overlib/).
     *
     * @throws java.io.IOException if there are any I/O errors.
     */
    public static void writeImageMap(PrintWriter writer,
            String name, ChartRenderingInfo info,
            boolean useOverLibForToolTips) throws IOException {

        ToolTipTagFragmentGenerator toolTipTagFragmentGenerator;
        if (useOverLibForToolTips) {
            toolTipTagFragmentGenerator
                    = new OverLIBToolTipTagFragmentGenerator();
        }
        else {
            toolTipTagFragmentGenerator
                    = new StandardToolTipTagFragmentGenerator();
        }
        writeImageMap(writer, name, info,
                toolTipTagFragmentGenerator,
                new StandardURLTagFragmentGenerator());

    }

    /**
     * Writes an image map to an output stream.
     *
     * @param writer  the writer ({@code null} not permitted).
     * @param name  the map name ({@code null} not permitted).
     * @param info  the chart rendering info ({@code null} not permitted).
     * @param toolTipTagFragmentGenerator  a generator for the HTML fragment
     *     that will contain the tooltip text ({@code null} not permitted
     *     if {@code info} contains tooltip information).
     * @param urlTagFragmentGenerator  a generator for the HTML fragment that
     *     will contain the URL reference ({@code null} not permitted if
     *     {@code info} contains URLs).
     *
     * @throws java.io.IOException if there are any I/O errors.
     */
    public static void writeImageMap(PrintWriter writer, String name,
            ChartRenderingInfo info,
            ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,
            URLTagFragmentGenerator urlTagFragmentGenerator)
        throws IOException {

        writer.println(ImageMapUtils.getImageMap(name, info,
                toolTipTagFragmentGenerator, urlTagFragmentGenerator));
    }

    /**
     * Creates an image map element that complies with the XHTML 1.0
     * specification.
     *
     * @param name  the map name ({@code null} not permitted).
     * @param info  the chart rendering info ({@code null} not permitted).
     *
     * @return The map element.
     */
    public static String getImageMap(String name, ChartRenderingInfo info) {
        return ImageMapUtils.getImageMap(name, info,
                new StandardToolTipTagFragmentGenerator(),
                new StandardURLTagFragmentGenerator());
    }

    /**
     * Creates an image map element that complies with the XHTML 1.0
     * specification.
     *
     * @param name  the map name ({@code null} not permitted).
     * @param info  the chart rendering info ({@code null} not permitted).
     * @param toolTipTagFragmentGenerator  a generator for the HTML fragment
     *     that will contain the tooltip text ({@code null} not permitted
     *     if {@code info} contains tooltip information).
     * @param urlTagFragmentGenerator  a generator for the HTML fragment that
     *     will contain the URL reference ({@code null} not permitted if
     *     {@code info} contains URLs).
     *
     * @return The map tag.
     */
    public static String getImageMap(String name, ChartRenderingInfo info,
            ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,
            URLTagFragmentGenerator urlTagFragmentGenerator) {

        StringBuilder sb = new StringBuilder();
        sb.append("<map id=\"").append(htmlEscape(name));
        sb.append("\" name=\"").append(htmlEscape(name)).append("\">");
        sb.append(StringUtils.getLineSeparator());
        EntityCollection entities = info.getEntityCollection();
        if (entities != null) {
            int count = entities.getEntityCount();
            for (int i = count - 1; i >= 0; i--) {
                ChartEntity entity = entities.getEntity(i);
                if (entity.getToolTipText() != null
                        || entity.getURLText() != null) {
                    String area = entity.getImageMapAreaTag(
                            toolTipTagFragmentGenerator,
                            urlTagFragmentGenerator);
                    if (area.length() > 0) {
                        sb.append(area);
                        sb.append(StringUtils.getLineSeparator());
                    }
                }
            }
        }
        sb.append("</map>");
        return sb.toString();

    }

    /**
     * Returns a string that is equivalent to the input string, but with
     * special characters converted to HTML escape sequences.
     *
     * @param input  the string to escape ({@code null} not permitted).
     *
     * @return A string with characters escaped.
     */
    public static String htmlEscape(String input) {
        Args.nullNotPermitted(input, "input");
        StringBuilder result = new StringBuilder();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == '&') {
                result.append("&amp;");
            }
            else if (c == '\"') {
                result.append("&quot;");
            }
            else if (c == '<') {
                result.append("&lt;");
            }
            else if (c == '>') {
                result.append("&gt;");
            }
            else if (c == '\'') {
                result.append("&#39;");
            }
            else if (c == '\\') {
                result.append("&#092;");
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Returns a string that is equivalent to the input string, but with
     * special characters converted to JavaScript escape sequences.
     *
     * @param input  the string to escape ({@code null} not permitted).
     *
     * @return A string with characters escaped.
     */
    public static String javascriptEscape(String input) {
        Args.nullNotPermitted(input, "input");
        StringBuilder result = new StringBuilder();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == '\"') {
                result.append("\\\"");
            }
            else if (c == '\'') {
                result.append("\\'");
            }
            else if (c == '\\') {
                result.append("\\\\");
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
