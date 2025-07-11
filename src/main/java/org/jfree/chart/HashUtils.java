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
 * --------------
 * HashUtils.java
 * --------------
 * (C) Copyright 2006-present, by David Gilbert;
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import org.jfree.chart.util.BooleanList;
import org.jfree.chart.util.PaintList;
import org.jfree.chart.util.StrokeList;

/**
 * Some utility methods for calculating hash codes.
 */
public class HashUtils {

    private HashUtils() {
        // no requirement to instantiate
    }

    /**
     * Returns a hash code for a {@code Paint} instance.  If 
     * {@code p} is {@code null}, this method returns zero.
     * 
     * @param p  the paint ({@code null} permitted).
     * 
     * @return The hash code.
     */
    public static int hashCodeForPaint(Paint p) {
        if (p == null) {
            return 0;
        }
        int result;
        // handle GradientPaint as a special case
        if (p instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint) p;
            result = 193;
            result = 37 * result + gp.getColor1().hashCode();
            result = 37 * result + gp.getPoint1().hashCode();
            result = 37 * result + gp.getColor2().hashCode();
            result = 37 * result + gp.getPoint2().hashCode();
        }
        else {
            // we assume that all other Paint instances implement equals() and
            // hashCode()...of course that might not be true, but what can we
            // do about it?
            result = p.hashCode();
        }
        return result;
    }
    
    /**
     * Returns a hash code for a {@code double[]} instance.  If the array
     * is {@code null}, this method returns zero.
     * 
     * @param a  the array ({@code null} permitted).
     * 
     * @return The hash code.
     */
    public static int hashCodeForDoubleArray(double[] a) {
        if (a == null) { 
            return 0;
        }
        int result = 193;
        long temp;
        for (int i = 0; i < a.length; i++) {
            temp = Double.doubleToLongBits(a[i]);
            result = 29 * result + (int) (temp ^ (temp >>> 32));
        }
        return result;
    }
    
    /**
     * Returns a hash value based on a seed value and the value of a boolean
     * primitive.
     * 
     * @param pre  the seed value.
     * @param b  the boolean value.
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, boolean b) {
        return 37 * pre + (b ? 0 : 1);
    }
    
    /**
     * Returns a hash value based on a seed value and the value of an int
     * primitive.
     * 
     * @param pre  the seed value.
     * @param i  the int value.
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, int i) {
        return 37 * pre + i;
    }

    /**
     * Returns a hash value based on a seed value and the value of a double
     * primitive.
     * 
     * @param pre  the seed value.
     * @param d  the double value.
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, double d) {
        long l = Double.doubleToLongBits(d);
        return 37 * pre + (int) (l ^ (l >>> 32));
    }
    
    /**
     * Returns a hash value based on a seed value and a paint instance.
     * 
     * @param pre  the seed value.
     * @param p  the paint ({@code null} permitted).
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, Paint p) {
        return 37 * pre + hashCodeForPaint(p);
    }

    /**
     * Returns a hash value based on a seed value and a stroke instance.
     * 
     * @param pre  the seed value.
     * @param s  the stroke ({@code null} permitted).
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, Stroke s) {
        int h = (s != null ? s.hashCode() : 0);
        return 37 * pre + h;
    }

    /**
     * Returns a hash value based on a seed value and a string instance.
     * 
     * @param pre  the seed value.
     * @param s  the string ({@code null} permitted).
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, String s) {
        int h = (s != null ? s.hashCode() : 0);
        return 37 * pre + h;
    }

    /**
     * Returns a hash value based on a seed value and a {@code Comparable}
     * instance.
     * 
     * @param pre  the seed value.
     * @param c  the comparable ({@code null} permitted).
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, Comparable c) {
        int h = (c != null ? c.hashCode() : 0);
        return 37 * pre + h;
    }

    /**
     * Returns a hash value based on a seed value and an {@code Object}
     * instance.
     * 
     * @param pre  the seed value.
     * @param obj  the object ({@code null} permitted).
     * 
     * @return A hash value.
     */
    public static int hashCode(int pre, Object obj) {
        int h = (obj != null ? obj.hashCode() : 0);
        return 37 * pre + h;
    }
    
    /**
     * Computes a hash code for a {@link BooleanList}.  In the latest version
     * of JCommon, the {@link BooleanList} class should implement the hashCode()
     * method correctly, but we compute it here anyway so that we can work with 
     * older versions of JCommon (back to 1.0.0).
     * 
     * @param pre  the seed value.
     * @param list  the list ({@code null} permitted).
     * 
     * @return The hash code.
     */
    public static int hashCode(int pre, BooleanList list) {
        if (list == null) {
            return pre;
        }
        int result = 127;
        int size = list.size();
        result = HashUtils.hashCode(result, size);
        
        // for efficiency, we just use the first, last and middle items to
        // compute a hashCode...
        if (size > 0) {
            result = HashUtils.hashCode(result, list.getBoolean(0));
            if (size > 1) {
                result = HashUtils.hashCode(result, 
                        list.getBoolean(size - 1));
                if (size > 2) {
                    result = HashUtils.hashCode(result, 
                            list.getBoolean(size / 2));
                }
            }
        }
        return 37 * pre + result;
    }

    /**
     * Computes a hash code for a {@link PaintList}.  In the latest version
     * of JCommon, the {@link PaintList} class should implement the hashCode()
     * method correctly, but we compute it here anyway so that we can work with 
     * older versions of JCommon (back to 1.0.0).
     * 
     * @param pre  the seed value.
     * @param list  the list ({@code null} permitted).
     * 
     * @return The hash code.
     */
    public static int hashCode(int pre, PaintList list) {
        if (list == null) {
            return pre;
        }
        int result = 127;
        int size = list.size();
        result = HashUtils.hashCode(result, size);
        
        // for efficiency, we just use the first, last and middle items to
        // compute a hashCode...
        if (size > 0) {
            result = HashUtils.hashCode(result, list.getPaint(0));
            if (size > 1) {
                result = HashUtils.hashCode(result, 
                        list.getPaint(size - 1));
                if (size > 2) {
                    result = HashUtils.hashCode(result, 
                            list.getPaint(size / 2));
                }
            }
        }
        return 37 * pre + result;
    }

    /**
     * Computes a hash code for a {@link StrokeList}.  In the latest version
     * of JCommon, the {@link StrokeList} class should implement the hashCode()
     * method correctly, but we compute it here anyway so that we can work with 
     * older versions of JCommon (back to 1.0.0).
     * 
     * @param pre  the seed value.
     * @param list  the list ({@code null} permitted).
     * 
     * @return The hash code.
     */
    public static int hashCode(int pre, StrokeList list) {
        if (list == null) {
            return pre;
        }
        int result = 127;
        int size = list.size();
        result = HashUtils.hashCode(result, size);
        
        // for efficiency, we just use the first, last and middle items to
        // compute a hashCode...
        if (size > 0) {
            result = HashUtils.hashCode(result, list.getStroke(0));
            if (size > 1) {
                result = HashUtils.hashCode(result, 
                        list.getStroke(size - 1));
                if (size > 2) {
                    result = HashUtils.hashCode(result, 
                            list.getStroke(size / 2));
                }
            }
        }
        return 37 * pre + result;
    }
}
