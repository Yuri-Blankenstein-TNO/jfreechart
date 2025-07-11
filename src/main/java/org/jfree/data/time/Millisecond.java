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
 * ----------------
 * Millisecond.java
 * ----------------
 * (C) Copyright 2001-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import org.jfree.chart.util.Args;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Represents a millisecond.  This class is immutable, which is a requirement
 * for all {@link RegularTimePeriod} subclasses.
 */
public class Millisecond extends RegularTimePeriod implements Serializable {

    /** For serialization. */
    static final long serialVersionUID = -5316836467277638485L;

    /** A constant for the first millisecond in a second. */
    public static final int FIRST_MILLISECOND_IN_SECOND = 0;

    /** A constant for the last millisecond in a second. */
    public static final int LAST_MILLISECOND_IN_SECOND = 999;

    /** The day. */
    private Day day;

    /** The hour in the day. */
    private byte hour;

    /** The minute. */
    private byte minute;

    /** The second. */
    private byte second;

    /** The millisecond. */
    private int millisecond;

    /**
     * The pegged millisecond.
     */
    private long firstMillisecond;

    /**
     * Constructs a millisecond based on the current system time.
     * The time zone and locale are determined by the calendar
     * returned by {@link RegularTimePeriod#getCalendarInstance()}.
     */
    public Millisecond() {
        this(new Date());
    }

    /**
     * Constructs a millisecond.
     * The time zone and locale are determined by the calendar
     * returned by {@link RegularTimePeriod#getCalendarInstance()}.
     *
     * @param millisecond  the millisecond (0-999).
     * @param second  the second ({@code null} not permitted).
     */
    public Millisecond(int millisecond, Second second) {
        Args.nullNotPermitted(second, "second");
        this.millisecond = millisecond;
        this.second = (byte) second.getSecond();
        this.minute = (byte) second.getMinute().getMinute();
        this.hour = (byte) second.getMinute().getHourValue();
        this.day = second.getMinute().getDay();
        peg(getCalendarInstance());
    }

    /**
     * Creates a new millisecond.
     * The time zone and locale are determined by the calendar
     * returned by {@link RegularTimePeriod#getCalendarInstance()}.
     *
     * @param millisecond  the millisecond (0-999).
     * @param second  the second (0-59).
     * @param minute  the minute (0-59).
     * @param hour  the hour (0-23).
     * @param day  the day (1-31).
     * @param month  the month (1-12).
     * @param year  the year (1900-9999).
     */
    public Millisecond(int millisecond, int second, int minute, int hour,
                       int day, int month, int year) {

        this(millisecond, new Second(second, minute, hour, day, month, year));

    }

    /**
     * Constructs a new millisecond.
     * The time zone and locale are determined by the calendar
     * returned by {@link RegularTimePeriod#getCalendarInstance()}.
     *
     * @param time  the time.
     *
     * @see #Millisecond(Date, TimeZone, Locale)
     */
    public Millisecond(Date time) {
        this(time, getCalendarInstance());
    }

    /**
     * Creates a millisecond.
     *
     * @param time  the date-time ({@code null} not permitted).
     * @param zone  the time zone ({@code null} not permitted).
     * @param locale  the locale ({@code null} not permitted).
     */
    public Millisecond(Date time, TimeZone zone, Locale locale) {
        Args.nullNotPermitted(time, "time");
        Args.nullNotPermitted(zone, "zone");
        Args.nullNotPermitted(locale, "locale");
        Calendar calendar = Calendar.getInstance(zone, locale);
        calendar.setTime(time);
        this.millisecond = calendar.get(Calendar.MILLISECOND);
        this.second = (byte) calendar.get(Calendar.SECOND);
        this.minute = (byte) calendar.get(Calendar.MINUTE);
        this.hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        this.day = new Day(time, zone, locale);
        peg(calendar);
    }

    /**
     * Constructs a new instance, based on a particular date/time.
     * The time zone and locale are determined by the {@code calendar}
     * parameter.
     *
     * @param time the date/time ({@code null} not permitted).
     * @param calendar the calendar to use for calculations ({@code null} not permitted).
     */
    public Millisecond(Date time, Calendar calendar) {
        Args.nullNotPermitted(time, "time");
        Args.nullNotPermitted(calendar, "calendar");
        calendar.setTime(time);
        this.millisecond = calendar.get(Calendar.MILLISECOND);
        this.second = (byte) calendar.get(Calendar.SECOND);
        this.minute = (byte) calendar.get(Calendar.MINUTE);
        this.hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        this.day = new Day(time, calendar);
        peg(calendar);
    }

    /**
     * Returns the second.
     *
     * @return The second.
     */
    public Second getSecond() {
        return new Second(this.second, this.minute, this.hour,
                this.day.getDayOfMonth(), this.day.getMonth(),
                this.day.getYear());
    }

    /**
     * Returns the millisecond.
     *
     * @return The millisecond.
     */
    public long getMillisecond() {
        return this.millisecond;
    }

    /**
     * Returns the first millisecond of the second.  This will be determined
     * relative to the time zone specified in the constructor, or in the
     * calendar instance passed in the most recent call to the
     * {@link #peg(Calendar)} method.
     *
     * @return The first millisecond of the second.
     *
     * @see #getLastMillisecond()
     */
    @Override
    public long getFirstMillisecond() {
        return this.firstMillisecond;
    }

    /**
     * Returns the last millisecond of the second.  This will be
     * determined relative to the time zone specified in the constructor, or
     * in the calendar instance passed in the most recent call to the
     * {@link #peg(Calendar)} method.
     *
     * @return The last millisecond of the second.
     *
     * @see #getFirstMillisecond()
     */
    @Override
    public long getLastMillisecond() {
        return this.firstMillisecond;
    }

    /**
     * Recalculates the start date/time and end date/time for this time period
     * relative to the supplied calendar (which incorporates a time zone).
     *
     * @param calendar  the calendar ({@code null} not permitted).
     */
    @Override
    public void peg(Calendar calendar) {
        this.firstMillisecond = getFirstMillisecond(calendar);
    }

    /**
     * Returns the millisecond preceding this one.
     * No matter what time zone and locale this instance was created with,
     * the returned instance will use the default calendar for time
     * calculations, obtained with {@link RegularTimePeriod#getCalendarInstance()}.
     *
     * @return The millisecond preceding this one.
     */
    @Override
    public RegularTimePeriod previous() {
        RegularTimePeriod result = null;
        if (this.millisecond != FIRST_MILLISECOND_IN_SECOND) {
            result = new Millisecond(this.millisecond - 1, getSecond());
        }
        else {
            Second previous = (Second) getSecond().previous();
            if (previous != null) {
                result = new Millisecond(LAST_MILLISECOND_IN_SECOND, previous);
            }
        }
        return result;
    }

    /**
     * Returns the millisecond following this one.
     * No matter what time zone and locale this instance was created with,
     * the returned instance will use the default calendar for time
     * calculations, obtained with {@link RegularTimePeriod#getCalendarInstance()}.
     *
     * @return The millisecond following this one.
     */
    @Override
    public RegularTimePeriod next() {
        RegularTimePeriod result = null;
        if (this.millisecond != LAST_MILLISECOND_IN_SECOND) {
            result = new Millisecond(this.millisecond + 1, getSecond());
        }
        else {
            Second next = (Second) getSecond().next();
            if (next != null) {
                result = new Millisecond(FIRST_MILLISECOND_IN_SECOND, next);
            }
        }
        return result;
    }

    /**
     * Returns a serial index number for the millisecond.
     *
     * @return The serial index number.
     */
    @Override
    public long getSerialIndex() {
        long hourIndex = this.day.getSerialIndex() * 24L + this.hour;
        long minuteIndex = hourIndex * 60L + this.minute;
        long secondIndex = minuteIndex * 60L + this.second;
        return secondIndex * 1000L + this.millisecond;
    }

    /**
     * Tests the equality of this object against an arbitrary Object.
     * <P>
     * This method will return true ONLY if the object is a Millisecond object
     * representing the same millisecond as this instance.
     *
     * @param obj  the object to compare
     *
     * @return {@code true} if milliseconds and seconds of this and object
     *      are the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Millisecond)) {
            return false;
        }
        Millisecond that = (Millisecond) obj;
        if (this.millisecond != that.millisecond) {
            return false;
        }
        if (this.second != that.second) {
            return false;
        }
        if (this.minute != that.minute) {
            return false;
        }
        if (this.hour != that.hour) {
            return false;
        }
        if (!this.day.equals(that.day)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this object instance.  The approach described by
     * Joshua Bloch in "Effective Java" has been used here:
     * <p>
     * {@code http://developer.java.sun.com/developer/Books/effectivejava
     * /Chapter3.pdf}
     *
     * @return A hashcode.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.millisecond;
        result = 37 * result + getSecond().hashCode();
        return result;
    }

    /**
     * Returns an integer indicating the order of this Millisecond object
     * relative to the specified object:
     * <p>
     * negative == before, zero == same, positive == after.
     *
     * @param obj  the object to compare
     *
     * @return negative == before, zero == same, positive == after.
     */
    @Override
    public int compareTo(Object obj) {
        int result;
        long difference;

        // CASE 1 : Comparing to another Second object
        // -------------------------------------------
        if (obj instanceof Millisecond) {
            Millisecond ms = (Millisecond) obj;
            difference = getFirstMillisecond() - ms.getFirstMillisecond();
            if (difference > 0) {
                result = 1;
            }
            else {
                if (difference < 0) {
                    result = -1;
                }
                else {
                    result = 0;
                }
            }
        }

        // CASE 2 : Comparing to another TimePeriod object
        // -----------------------------------------------
        else if (obj instanceof RegularTimePeriod) {
            RegularTimePeriod rtp = (RegularTimePeriod) obj;
            final long thisVal = this.getFirstMillisecond();
            final long anotherVal = rtp.getFirstMillisecond();
            result = (thisVal < anotherVal ? -1
                    : (thisVal == anotherVal ? 0 : 1));
        }

        // CASE 3 : Comparing to a non-TimePeriod object
        // ---------------------------------------------
        else {
            // consider time periods to be ordered after general objects
            result = 1;
        }

        return result;
    }

    /**
     * Returns the first millisecond of the time period.
     *
     * @param calendar  the calendar ({@code null} not permitted).
     *
     * @return The first millisecond of the time period.
     *
     * @throws NullPointerException if {@code calendar} is
     *     {@code null}.
     */
    @Override
    public long getFirstMillisecond(Calendar calendar) {
        int year = this.day.getYear();
        int month = this.day.getMonth() - 1;
        int d = this.day.getDayOfMonth();
        calendar.clear();
        calendar.set(year, month, d, this.hour, this.minute, this.second);
        calendar.set(Calendar.MILLISECOND, this.millisecond);
        return calendar.getTimeInMillis();
    }

    /**
     * Returns the last millisecond of the time period.
     *
     * @param calendar  the calendar ({@code null} not permitted).
     *
     * @return The last millisecond of the time period.
     *
     * @throws NullPointerException if {@code calendar} is
     *     {@code null}.
     */
    @Override
    public long getLastMillisecond(Calendar calendar) {
        return getFirstMillisecond(calendar);
    }

}
