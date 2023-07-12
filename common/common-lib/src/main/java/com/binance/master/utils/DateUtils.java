package com.binance.master.utils;

import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String SIMPLE_PATTERN = "yyyy-MM-dd";

    public static final String SIMPLE_NUMBER_PATTERN = "yyyyMMdd";

    public static final String DETAILED_NUMBER_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String SIMPLE_PATTERN_HOUR = "yyyy/MM/dd HH:mm";

    public static final String EMAIL_TITLE_UTC = "yyyy-MM-dd HH:mm:ss'(UTC)'";

    public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date dateTmp = null;
        if (date != null) {
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        return dateTmp;
    }

    public static Long getNewTimeMillis() {
        return getNewDate().getTime();
    }

    public static Long getNewUTCTimeMillis() {
        return getNewUTCDate().getTime();
    }

    public static Date getNewDate() {
        return getNewCalendar().getTime();
    }

    public static Date getNewUTCDate() {
        return changeTimeZone(getNewDate(), TimeZone.getDefault(), TimeZone.getTimeZone("UTC"));
    }

    public static String getNewDateUTC() {
        return formatterUTC(getNewUTCDate(), SIMPLE_PATTERN_HOUR);
    }

    public static Calendar getNewCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 在当前时间上加上指定的天数返回
     * 
     * @param day
     * @return
     */
    public static Date getNewUTCDateAddDay(int day) {
        Calendar calendar = getNewCalendar();
        calendar.setTime(getNewUTCDate());
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 在当前时间上加上指定的小时数返回
     * 
     * @return
     */
    public static Date getNewUTCDateAddHour(int hour) {
        Calendar calendar = getNewCalendar();
        calendar.setTime(getNewUTCDate());
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 在当前时间上加上指定的分钟数返回
     * 
     * @param minute
     * @return
     */
    public static Date getNewUTCDateAddMinute(int minute) {
        Calendar calendar = getNewCalendar();
        calendar.setTime(getNewUTCDate());
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 在当前时间上加上指定的天数返回
     * 
     * @param day
     * @return
     */
    public static Date getNewDateAddDay(int day) {
        Calendar calendar = getNewCalendar();
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 在当前时间上加上指定的小时数返回
     * 
     * @return
     */
    public static Date getNewDateAddHour(int hour) {
        Calendar calendar = getNewCalendar();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 在当前时间上加上指定的分钟数返回
     * 
     * @param minute
     * @return
     */
    public static Date getNewDateAddMinute(int minute) {
        Calendar calendar = getNewCalendar();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date add(Date data, int unit, int num) {
        Calendar calendar = getNewCalendar();
        calendar.setTime(data);
        calendar.add(unit, num);
        return calendar.getTime();
    }

    public static String formatter(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatterUTC(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static Date formatter(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    public static Date formatterUTC(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(date);
    }

    /**
     * 把秒精度的转换成时间
     * 
     * @param seconds
     * @return
     */
    public static Date createDateSeconds(Long seconds) {
        Calendar calendar = getNewCalendar();
        calendar.setTimeInMillis(seconds * 1000L);;
        return calendar.getTime();
    }

    /**
     * 获取零点的时刻
     */
    public static Date getDateBegin(Date date) {
        Assert.notNull(date, "date cannot be null");
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 获取 23：59：59的时间
     */
    public static Date getDateEnd(Date date) {
        Assert.notNull(date, "date cannot be null");
        date = org.apache.commons.lang3.time.DateUtils.setHours(date, 23);
        date = org.apache.commons.lang3.time.DateUtils.setMinutes(date, 59);
        date = org.apache.commons.lang3.time.DateUtils.setSeconds(date, 59);
        date = org.apache.commons.lang3.time.DateUtils.setMilliseconds(date, 999);
        return date;
    }

    public static Date getTodayBegin() {
        return getDateBegin(new Date());
    }

    public static Date getTodayEnd() {
        return getDateEnd(new Date());
    }
}
