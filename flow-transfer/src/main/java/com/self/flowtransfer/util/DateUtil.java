package com.self.flowtransfer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    /**
     * 精确地3位毫秒
     */
    public static final String format_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    /**
     * 精确到秒
     */
    public static final String format_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * 精确到天
     */
    public static final String format_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 精确到天
     */
    public static final String format_yyyyMMdd = "yyyyMMdd";

    /**
     * 精确到月
     */
    public static final String format_yyyyMM = "yyyyMM";

    /**
     * 精确到年
     */
    public static final String format_yyyy = "yyyy";

    /**
     * 精确地3位毫秒
     */
    public static final String format_yyyy_MM_dd_HHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 精确地0位毫秒
     */
    public static final String format_yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确地0位毫秒
     */
    public static final String format_yyyy_MM_ddTHHmmss = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * yyyy/MM/dd的时间路径
     */
    public static final String format_yyyy_MM_dd_path = "yyyy/MM/dd";

    /**
     * 时区东八区
     */
    public static final String format_GMT_8 = "GMT+8";

    /**
     * 格式化日期对象
     * @param date 日期对象
     * @param format 格式化字符串
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            return null;
        } else {
            result = sdf.format(date);
        }

        return result;
    }

    /**
     * 获取当前的时间yyyyMMddHHmmssSSS
     */
    public static String getCurrentTime_yyyyMMddHHmmssSSS() {
        return formatDate(new Date(), format_yyyyMMddHHmmssSSS);
    }

    /**
     * 获取当前的时间format_yyyy_MM_ddTHHmmss
     */
    public static String getCurrentTime_format_yyyy_MM_ddTHHmmss() {
        return formatDate(new Date(), format_yyyy_MM_ddTHHmmss);
    }

    /**
     * 格式化日期对象
     * @param date 日期对象
     * @param format 格式化字符串
     */
    public static Date parseDate(String date, String format) {
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null && !("".equals(date))) {
            try {
                result = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取时间差
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static String diffTime(Date startTime, Date endTime) {
        return diffTime(endTime.getTime(), startTime.getTime());
    }

    /**
     * 获取时间差
     * @param startTimeStamp 开始时间戳
     * @param endTimeStamp 结束时间戳
     */
    public static String diffTime(long startTimeStamp, long endTimeStamp) {
        String result = "";
        long diff = endTimeStamp - startTimeStamp;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = diff / (1000 * 60 * 60) - days * 24;
        long mins = diff / (1000 * 60) - hours * 60;
        long seconds = diff / (1000) - mins * 60;
        if (days > 0) {
            result += days + "天";
        }
        if (hours > 0) {
            result += hours + "小时";
        }
        if (mins > 0) {
            result += mins + "分";
        }
        if (seconds > 0) {
            result += seconds + "秒";
        }
        if (StringUtil.isEmpty(result)) {
            result = diff + "毫秒";
        }

        return result;
    }

    /**
     * 获取时间毫秒差
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间差
     */
    public static Long getTimes(Date startTime, Date endTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 获取两个时间之间的天数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 相差天数
     */
    public static long diffDays(Date startTime, Date endTime) {
        long result = 0;
        long diff = endTime.getTime() - startTime.getTime();
        result = diff / (1000 * 60 * 60 * 24);
        return result;
    }

    /**
     * 日期月份计算
     * @param startDate 基础日期
     * @param monthAmount 增加或减去的月份数
     * @return 返回增加或减去指定月份数后的日期
     */
    public static Date addMonth(Date startDate, int monthAmount) {
        return addTime(startDate, monthAmount, Calendar.MONTH);
    }

    /**
     * 日期月份计算
     * @param startDate 基础日期
     * @param monthAmount 增加或减去的月份数
     * @return 返回增加或减去指定月份数后的日期
     */
    public static Date addDays(Date startDate, int monthAmount) {
        return addTime(startDate, monthAmount, Calendar.DATE);
    }

    /**
     * 日期计算
     * @param date 基础日期
     * @param amount 天数或月数或年数...
     * @param field 需要计算的值:Calendar.MONTH,Calendar.YEAR,Calendar.DATE,Calendar.WEDNESDAY
     * @return 返回计算后的日期
     */
    public static Date addTime(Date date, int amount, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

}
