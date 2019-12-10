/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */

package com.qf.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期时间工具类
 * author Vareck
 * date 2016/11/23 0023 下午 14:53
 */
public class DateUtil {
    private static String ymdhms = "yyyy-MM-dd HH:mm:ss";
    private static String ymd    = "yyyy-MM-dd";
    private static String ym     = "yyyy_MM";
    private static String y      = "yyyy";
    private static String m      = "MM";
    private static String d      = "dd";
    public static SimpleDateFormat ymdSDF = new SimpleDateFormat(ymd);
    private static String year = "yyyy";
    private static String month = "MM";
    private static String day = "dd";
    public static SimpleDateFormat yyyyMMddHHmmss  = new SimpleDateFormat(ymdhms);
    public static SimpleDateFormat yyyyMM 		   = new SimpleDateFormat(ym);
    public static SimpleDateFormat yyyy   		   = new SimpleDateFormat(y);
    public static SimpleDateFormat MM    		   = new SimpleDateFormat(m);
    public static SimpleDateFormat dd    		   = new SimpleDateFormat(d);
    public static SimpleDateFormat yearSDF 		   = new SimpleDateFormat(year);
    public static SimpleDateFormat monthSDF 	   = new SimpleDateFormat(month);
    public static SimpleDateFormat daySDF 		   = new SimpleDateFormat(day);
    public static SimpleDateFormat yyyyMMddHHmm    = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat yyyyMMdd 	   = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat yyyyMMddHH_NOT_ = new SimpleDateFormat("yyyyMMdd");
    public static long DATEMM = 86400L;

    /**
     * 获得当前时间(字符串类型)
     * 格式：2014-12-02 10:38:53
     * @return String
     */
    public static String getCurrentTime() {
        return yyyyMMddHHmmss.format(new Date());
    }
    public static String getCurrentTime(Date date) {
        return yyyyMMddHHmmss.format(new Date());
    }
    /**
     * 获得当前时间(字符串类型)
     * 格式：2018-09-21 10:38:53
     * @return String
     */
    public static String getCurrentTime2() {
        return yyyyMMdd.format(new Date());
    }
    
    public static String getCurrentTime3() {
        Calendar calendar    = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time          = sdf.format(calendar.getTime());
        return time;
    }
    
    public static String getCurrentTime4() {
        return yyyyMM.format(new Date());
    }
    
    public static String getCurrentTime5() {
        return yyyy.format(new Date());
    }
    
    public static String getCurrentTime6() {
        return MM.format(new Date());
    }
    
    public static String getCurrentTime7() {
        return dd.format(new Date());
    }
    
    public static String getCurrentTimes() {
        Calendar calendar    = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String time          = sdf.format(calendar.getTime());
        return time;
    }

    /**
     * 获得当前时间(日期类型)
     * 格式：2014-12-02 10:38:53
     * @return String
     */
    public static Date getSysTime() {
        return DateUtil.stringToDate(getCurrentTime(), ymdhms);
    }

    /**
     * 获取年月日(日期类型)
     * 格式：2014-12-02
     * @return String
     */
    public static Date getSysDate() {
        return DateUtil.stringToDate(getCurrentTime(), ymd);
    }

    /**
     * 获取年月日(字符串类型)
     * 格式：2014-12-02
     * @return String
     */
    public static String getCurrentDate() {
        return ymdSDF.format(new Date());
    }

    /**
     * 可以获取昨天的日期
     * 格式：2014-12-01
     *
     * @return String
     */
    public static String getYesterdayYYYYMMDD() {
        Date date = new Date(System.currentTimeMillis() - DATEMM * 1000L);
        String str = yyyyMMdd.format(date);
        try {
            date = yyyyMMddHHmmss.parse(str + " 00:00:00");
            return yyyyMMdd.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 可以获取后退N天的日期
     * 格式：传入2 得到2014-11-30
     * @param backDay
     * @return String
     */
    public String getStrDate(String backDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt("-" + backDay));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String back = sdf.format(calendar.getTime());
        return back;
    }

    /**
     * 获取当前的年、月、日
     * @return String
     */
    public static String getCurrentYear() {
        return yearSDF.format(new Date());
    }

    public static String getCurrentMonth() {
        return monthSDF.format(new Date());
    }

    public static String getCurrentDay() {
        return daySDF.format(new Date());
    }

    /**
     * 获取今天0点开始的秒数
     * @return long
     */
    public static long getTimeNumberToday() {
        Date date  = new Date();
        String str = yyyyMMdd.format(date);
        try {
            date = yyyyMMdd.parse(str);
            return date.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取今天的日期
     * 格式：20141202
     * @return String
     */
    public static String getTodateString() {
        String str = yyyyMMddHH_NOT_.format(new Date());
        return str;
    }

    /**
     * 获取昨天的日期
     * 格式：20141201
     * @return String
     */
    public static String getYesterdayString() {
        Date date = new Date(System.currentTimeMillis() - DATEMM * 1000L);
        String str = yyyyMMddHH_NOT_.format(date);
        return str;
    }

    /**
     * 获得昨天零点
     * @return Date
     */
    public static Date getYesterDayZeroHour() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        return cal.getTime();
    }

    /**
     * 把long型日期转String ；---OK
     * @param date   long型日期；
     * @param format 日期格式；
     * @return
     */
    public static String longToString(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        java.util.Date dt2 = new Date(date * 1000L);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    /**
     * 获得今天零点
     * @return Date
     */
    public static Date getTodayZeroHour() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        return cal.getTime();
    }

    /**
     * 获得昨天23时59分59秒
     * @return
     */
    public static Date getYesterDay24Hour() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR, 23);
        return cal.getTime();
    }

    /**
     * String To Date ---OK
     * @param date   待转换的字符串型日期；
     * @param format 转化的日期格式
     * @return 返回该字符串的日期型数据；
     */
    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获得指定日期所在的自然周的第一天，即周日
     * @param date 日期
     * @return 自然周的第一天
     */
    public static Date getStartDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期所在的自然周的最后一天，即周六
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 7);
        date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期所在当月第一天
     * @param date
     * @return
     */
    public static Date getStartDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期所在当月最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期的下一个月的第一天
     * @param date
     * @return
     */
    public static Date getStartDayOfNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期的下一个月的最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, 2);
        c.add(Calendar.DATE, -1);
        date = c.getTime();
        return date;
    }

    /**
     * 求某一个时间向前多少秒的时间(currentTimeToBefer)---OK
     * @param givedTime        给定的时间
     * @param interval         间隔时间的毫秒数；计算方式 ：n(天)*24(小时)*60(分钟)*60(秒)(类型)
     * @param format_Date_Sign 输出日期的格式；如yyyy-MM-dd、yyyyMMdd等；
     */
    public static String givedTimeToBefer(String givedTime, long interval, String format_Date_Sign) {
        String tomorrow = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format_Date_Sign);
            Date gDate = sdf.parse(givedTime);
            long current = gDate.getTime(); // 将Calendar表示的时间转换成毫秒
            long beforeOrAfter = current - interval * 1000L; // 将Calendar表示的时间转换成毫秒
            Date date = new Date(beforeOrAfter); // 用timeTwo作参数构造date2
            tomorrow = new SimpleDateFormat(format_Date_Sign).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tomorrow;
    }

    /**
     * 把String 日期转换成long型日期；---OK
     *
     * @param date   String 型日期；
     * @param format 日期格式；
     * @return
     */
    public static long stringToLong(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt2 = null;
        long lTime = 0;
        try {
            dt2 = sdf.parse(date);
            // 继续转换得到秒数的long型
            lTime = dt2.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lTime;
    }

    /**
     * 得到二个日期间的间隔日期；
     *
     * @param endTime   结束时间
     * @param beginTime 开始时间
     * @param isEndTime 是否包含结束日期；
     * @return
     */
    public static Map<String, String> getTwoDay(String endTime,
                                                String beginTime, boolean isEndTime) {
        Map<String, String> result = new HashMap<String, String>();
        if ((endTime == null || endTime.equals("") || (beginTime == null || beginTime
                .equals(""))))
            return null;
        try {
            java.util.Date date = ymdSDF.parse(endTime);
            endTime = ymdSDF.format(date);
            java.util.Date mydate = ymdSDF.parse(beginTime);
            long day = (date.getTime() - mydate.getTime())
                    / (24 * 60 * 60 * 1000);
            result = getDate(endTime, Integer.parseInt(day + ""), isEndTime);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 得到二个日期间的间隔日期；
     *
     * @param endTime   结束时间
     * @param beginTime 开始时间
     * @param isEndTime 是否包含结束日期；
     * @return
     */
    public static Integer getTwoDayInterval(String endTime, String beginTime,
                                            boolean isEndTime) {
        if ((endTime == null || endTime.equals("") || (beginTime == null || beginTime
                .equals(""))))
            return 0;
        long day = 0l;
        try {
            java.util.Date date = ymdSDF.parse(endTime);
            endTime = ymdSDF.format(date);
            java.util.Date mydate = ymdSDF.parse(beginTime);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 0;
        }
        return Integer.parseInt(day + "");
    }

    /**
     * 根据结束时间以及间隔差值，求符合要求的日期集合；
     *
     * @param endTime
     * @param interval
     * @param isEndTime
     * @return
     */
    public static Map<String, String> getDate(String endTime, Integer interval,
                                              boolean isEndTime) {
        Map<String, String> result = new HashMap<String, String>();
        if (interval == 0 || isEndTime) {
            if (isEndTime)
                result.put(endTime, endTime);
        }
        if (interval > 0) {
            int begin = 0;
            for (int i = begin; i < interval; i++) {
                endTime = givedTimeToBefer(endTime, DATEMM, ymd);
                result.put(endTime, endTime);
            }
        }
        return result;
    }
    
    
    
    /* 
     * 将时间转换为时间戳
     */    
    public static long dateToStamp(String s, String k) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(k);
        Date date = simpleDateFormat.parse(s);
        long ts   = date.getTime()/1000;
        return ts;
    }

    /* 
     * 将时间转换为时间戳
     */    
    public static Integer getTimes() throws ParseException{
    	Integer ts = (int) new Date().getTime();
        return ts;
    }
    
    
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    //获取某月总天数
	public static int getDaysOfMonth( String date ) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date datek           = sdf.parse(date);
		Calendar calendar    = Calendar.getInstance();
		calendar.setTime(datek);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	//获取当前年份所有月份的天数
	public static Integer[] getDaysOfMonthDays() throws ParseException {
		Integer[] days = new Integer[12];
		days[0]  = 31;
		days[1]  = getDaysOfMonth(yyyy.format(new Date())+"-02");
		days[2]  = 31;
		days[3]  = 30;
		days[4]  = 31;
		days[5]  = 30;
		days[6]  = 31;
		days[7]  = 31;
		days[8]  = 30;
		days[9]  = 31;
		days[10] = 30;
		days[11] = 31;
        return days;
	}
    
	//某年月日向前n个数字
	public static String getBeforeMonth( Integer year , Integer month , Integer day , int y , int m , int d ) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year,month-1,day,0,0,0);
		calendar.add(Calendar.YEAR, -y);        // 年份减n  
		calendar.add(Calendar.MONTH, -m);       // 月份减n  
		calendar.add(Calendar.DATE, -d);        // 日期减n 
		Date resultDate = calendar.getTime();   // 结果  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return sdf.format(resultDate);
	}
	 //改成时间戳
    public static long getTimestamp(String sTime) throws ParseException {  
    	   
        Timestamp ts = Timestamp.valueOf(sTime);  
        Date date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")   
        .parse("1970/01/01 08:00:00");  
        long l = ts.getTime() - date2.getTime() > 0 ? ts.getTime()   
                - date2.getTime() : date2.getTime() - ts.getTime();   
         long rand = (int)(l/1000);  
           
         return rand;  
     }
    public static String timestampToString(Integer time){
		//int转long时，先进行转型再进行计算，否则会是计算结束后在转型
		long temp = (long)time*1000;
		Timestamp ts = new Timestamp(temp);  
        String tsStr = "";  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            //方法一  
            tsStr = dateFormat.format(ts);  
           
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return tsStr;  
    }
}