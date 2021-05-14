package com.market.base.framework.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class DateTimeUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前日期是今年的第几周
     *
     * @return
     */
    public static int getNowWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(DateTimeUtil.getNowDate());
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取UTC时间
     *
     * @return
     */
    public static Date getUTCDate() {

        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        Calendar cal = Calendar.getInstance();
        // 取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date, String formatStr) {
        if (StringUtils.isBlank(formatStr)) {
            formatStr = DATE_TIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 获得当前时间对像
     *
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 返回指定格式的当前时间，默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNow(String format) {
        java.text.DateFormat insDateFormat = new SimpleDateFormat(format);
        return (String) insDateFormat.format(new Date());
    }

    /**
     * 获得现在的时间格式为:yyyy-MM-dd HH:mm:ss
     *
     * @return 返回标准的如：2013-01-03 12:09:00
     */
    public static String getNow() {
        java.text.DateFormat insDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return (String) insDateFormat.format(new Date());
    }

    /**
     * 计算开始时间和结束时间的差，返回单位为秒
     *
     * @param startTime 开始时间字符串
     * @param endTime 结束时间字符串
     * @return 返回时间差的秒级单位，保留3位小数点
     */
    public static String getTimeDifference(String startTime, String endTime) throws Exception {
        Date begin = string2Date(startTime, "");
        Date end = string2Date(endTime, "");
        Long between = (end.getTime() - begin.getTime());
        BigDecimal a = new BigDecimal(between);
        BigDecimal b = new BigDecimal(1000);
        b = (a.divide(b, 3, RoundingMode.HALF_UP)); //转换为秒，并保留3位小数
        return String.valueOf(b);
    }

    /**
     * 计算开始时间和结束时间的差，返回单位毫秒
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public static long getMillisecondDifference(String startTime, String endTime) throws Exception {
        Date begin = string2Date(startTime, "");
        Date end = string2Date(endTime, "");
        Long between = (end.getTime() - begin.getTime());
        return between;
    }

    /**
     * 开始时间小于结束时间
     *
     * @param startTime 开始时间字符串
     * @param endTime 结束时间字符串
     * @return true表示小于，false表示大于
     */
    public static boolean startTimeLessEndTime(String startTime, String endTime) throws Exception {
        Date begin = string2Date(startTime, "");
        Date end = string2Date(endTime, "");
        Long between = (end.getTime() - begin.getTime());
        if (between > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始时间大于结束时间
     *
     * @param startTime 开始时间字符串
     * @param endTime 结束时间字符串
     * @return true表示大于，false表示小于
     */
    public static boolean startTimeGreaterEndTime(String startTime, String endTime) throws Exception {
        Date begin = string2Date(startTime, "");
        Date end = string2Date(endTime, "");
        Long between = (begin.getTime() - end.getTime());
        if (between > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串转换为时间对像，格式自动判断
     *
     * @param str 要转换的字符串
     * @return
     * @throws Exception
     */
    public static Date string2Date(String str) throws Exception {
        String format = DATE_TIME_FORMAT;
        if (str.length() == 10) {
            format = "yyyy-MM-dd";
        } else if (str.length() == 16) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat dfs = new SimpleDateFormat(format);
        return dfs.parse(str);
    }

    /**
     * 字符串转换为指定格式的时间
     *
     * @param str 要转换的字符串
     * @param format 如果传空值则表示默认格式:yyyy-MM-dd HH:mm:ss
     * @return
     * @throws Exception
     */
    public static Date string2Date(String str, String format) throws Exception {
        if (StringUtils.isBlank(format)) {
            if (str.length() == 10) {
                format = "yyyy-MM-dd";
            } else if (str.length() == 16) {
                format = "yyyy-MM-dd HH:mm";
            } else {
                format = DATE_TIME_FORMAT;
            }
        }
        SimpleDateFormat dfs = new SimpleDateFormat(format);
        return dfs.parse(str);
    }

    /**
     * 日期加减操作
     *
     * @param nowTime 格式为 yyyy-MM-dd HH:mm:ss
     * @param addType Calendar.YEAR=1,Calendar.MONTH=2,Calendar.HOUR=10,Calendar.MINUTE=12,Calendar.DATE=5,Calendar.SECOND=13,Calendar.MILLISECOND=14
     * @param addNum 正数表增加,负数表示减
     * @throws Exception
     */
    public static Date addDateTime(String nowTime, int addType, int addNum) throws Exception {
        Date nowDate = string2Date(nowTime, DATE_TIME_FORMAT);
        return addDateTime(nowDate, addType, addNum); //返回加减后的最新日期
    }

    /**
     * 日期加减操作
     *
     * @param nowDate 日期对像
     * @param addType Calendar.YEAR=1,Calendar.MONTH=2,Calendar.HOUR=10,Calendar.MINUTE=12,Calendar.DATE=5,Calendar.MILLISECOND=14
     * @param addNum 正数表增加,负数表示减
     * @throws Exception
     */
    public static Date addDateTime(Date nowDate, int addType, int addNum) throws Exception {
        Calendar specialDate = Calendar.getInstance();
        specialDate.setTime(nowDate); //注意在此处将 specialDate 的值改为特定日期
        specialDate.add(addType, addNum);
        return specialDate.getTime(); //返回加减后的最新日期
    }

    /**
     * 日期整数转日期格式
     *
     * @param time
     * @param format
     * @return
     */
    public static String long2dateString(long time, String format) {
        if (time > 0l) {
            Date date = new Date(time);
            return formatDateTime(date, format);
        }
        return "";
    }

}
