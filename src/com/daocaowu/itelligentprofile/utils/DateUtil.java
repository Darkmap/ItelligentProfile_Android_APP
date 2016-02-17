package com.daocaowu.itelligentprofile.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取加减天数之后的日期
	 * 
	 * @param addDay
	 *            要加减的天数
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getDateByAdd(int addDay) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, addDay);
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort(String time) {
		if (!time.equals("")) {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date currentTime = strToDateLong(time);
			String dateString = formatter.format(currentTime);
			return dateString;
		} else
			return "";
	}

	/**
	 * 获取时间 小时:分 HH:mm
	 * 
	 * @return
	 */
	public static String getTimeJustMin(String time) {
		if (!time.equals("")) {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date currentTime = strToDateLong(time);
			String dateString = formatter.format(currentTime);
			return dateString;
		} else
			return "";
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDateStr(String time) {
		if (!time.equals("")) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date currentTime = strToDateLong(time);
			String dateString = formatter.format(currentTime);
			return dateString;
		} else
			return "";
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static String dateToStringLong(java.util.Date dateDate, int mm) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateDate.setHours(dateDate.getHours() + mm);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param Calendar
	 * @return
	 * */
	public static String dateToStrLong(java.util.Calendar dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate.getTime());
		return dateString;

	}

	/**
	 * 计算给定时间加固定几小时后的时间
	 * 
	 * @param dateDate
	 *            给定时间
	 * @param mm
	 *            要加减的时间数
	 * @return 返回计算后的时间
	 */
	public static String dateToStringLong(java.util.Calendar dateDate, int mm) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateDate.add(Calendar.HOUR_OF_DAY, mm);
		String dateString = formatter.format(dateDate.getTime());
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * 提取一个月中的最后一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return 字符串 yyyyMMdd HHmmss
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 得到现在小时
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * 得到现在分钟
	 * 
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	 * 
	 * @param sformat
	 *            yyyyMMddhhmmss
	 * @return
	 */
	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}


	/**
	 * 判断给定日期是否为当天，
	 * 
	 * 距离当前时间七天之内的日期，和七天之外的日期
	 * 
	 * @param dt
	 * @param type
	 *            0--当天 1--7天之内的 2--7天之外的
	 * @return
	 */
	public static boolean getDayDiffFromToday(Date dt, int type) {
		Date today = new Date();
		today.setHours(23);
		today.setMinutes(59);
		today.setSeconds(59);

		long diff = today.getTime() - dt.getTime();
		if (diff < 0)
			diff = 0;
		long days = diff / (1000 * 60 * 60 * 24);

		if (type == 0 && days == 0)
			return true;
		if (type == 1 && days > 0 && days <= 7)
			return true;
		if (type == 2 && days > 7)
			return true;

		return false;
	}

	/**
	 * 判断给定日期是否为当天，
	 * 
	 * 距离当前时间七天之内的日期，和七天之外的日期
	 * 
	 * @param dt
	 * @param type
	 *            0--当天 1--7天之内的 2--7天之外的
	 * @return
	 */
	public static boolean getDayDiffFromToday(String dt, int type) {
		Date date = new Date(dt);

		Date today = new Date();
		today.setHours(23);
		today.setMinutes(59);
		today.setSeconds(59);

		long diff = today.getTime() - date.getTime();
		if (diff < 0)
			diff = 0;
		long days = diff / (1000 * 60 * 60 * 24);

		if (type == 0 && days == 0)
			return true;
		if (type == 1 && days > 0 && days <= 7)
			return true;
		if (type == 2 && days > 7)
			return true;

		return false;
	}

	/**
	 * 给定时间与今天比较，如果是今天的话只截取时间
	 * 
	 * @param dt
	 * @return
	 */
	public static String getTimeDependToday(String dt) {

		String result = dt != null ? dt : "";
		if (!"".equals(dt)) {
			try {
				Date date = strToDateLong(dt);
				Date today = new Date();
				today.setHours(23);
				today.setMinutes(59);
				today.setSeconds(59);

				long diff = today.getTime() - date.getTime();
				if (diff < 0)
					diff = 0;
				long days = diff / (1000 * 60 * 60 * 24);

				if (days == 0) {
					result = getTimeJustMin(result);
				} else if (days == 1) {
					result = "昨天";
				} else if (days == 2) {
					result = "前天";
				} else {
					result = strToDateStr(result);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}
	
	
	
	
	
	/**给 时：分 返回一个"HH:MM"这样的String
	 * @param h 小时
	 * @param m 分钟
	 * @return
	 */
	public static String getStringbyHourandMinute(int h, int m){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    	Date dt = new Date();
    	dt.setHours(h);
    	dt.setMinutes(m);
    	return sdf.format(dt);
	}
	
	/**
	 * 返回当前时间，ex："HH:mm"
	 */
	public static String getHHmmString(){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    	return sdf.format(new java.util.Date());
	}
	
	
	/**使用一个"HH:mm"的String，获取今天日期下该时间的毫秒数
	 * @param time "HH:mm"的String
	 * @return
	 */
	public static long getmillisecond(String time){
		StringBuffer date = new StringBuffer(getStringDateShort());
		date.append(" "+time+":00");
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt;
		
		try {
			dt = sdf.parse(date.toString());
			return dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 从一个HH:mm格式的时间中提取出Hours
	 * @param time HH:mm
	 * @return
	 */
	public static int parseHoursFromHHMMTime(String time){
		StringBuffer date = new StringBuffer(getStringDateShort());
		date.append(" "+time+":00");
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt;
		
		try {
			dt = sdf.parse(date.toString());
			return dt.getHours();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 从一个HH:mm格式的时间中提取出Minutes
	 * @param time HH:mm
	 * @return
	 */
	public static int parseMinutesFromHHMMTime(String time){
		StringBuffer date = new StringBuffer(getStringDateShort());
		date.append(" "+time+":00");
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt;
		
		try {
			dt = sdf.parse(date.toString());
			return dt.getMinutes();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
    private static SimpleDateFormat dateFormat; 


    /** 
     * 将字符串类型的日期转换为timestamp（时间戳记java.sql.Timestamp 
     * 
     * @param dateString 
     *          转换为timestamp的字符串 
     *@return string2TimeStamp 
     */ 
    public final static java.sql.Timestamp string2TimeStamp(String dateString) { 
       try { 
           if(dateString.length()>12) 
           { 
              dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", 
                     Locale.CHINA);// 设定格式 
           }else{ 
              dateFormat = new SimpleDateFormat("yyyy-MM-dd", 
                     Locale.CHINA);// 设定格式 
           } 
           dateFormat.setLenient(false); 
           java.util.Date timeDate = dateFormat.parse(dateString);// util类型 
           java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate 
                  .getTime());// Timestamp类型,timeDate.getTime() 
           return dateTime; 
       } catch (ParseException pe) { 
           return null; 
       } 
    } 


    /** 
     *method 将日期Timestamp转换为字符串类型 
     * 
     * @param timeStamp 
     *           转换为String的字符串 
     *@return timestamp2String 
     */ 
    public final static String timestamp2String(java.sql.Timestamp timeStamp){ 
       String dateTime=""; 
       if(timeStamp.toString().indexOf("00:00:00")!=-1){ 
           dateTime=timeStamp.toString().substring(0,timeStamp.toString().indexOf(" ")); 
       }else{ 
           dateTime=timeStamp.toString().substring(0,timeStamp.toString().lastIndexOf(".")); 
       } 
       return dateTime; 
    } 


    /** 
     * 字符串转换为yyyy-MM-dd格式 
     * 
     * @param date 
     *            字符 
     * @return Date 
     */ 
    public static Date getDate(String date) { 
       try { 
           dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
           if ("".equals(date) || null == date) 
              return null; 
           return dateFormat.parse(date); 
       } catch (ParseException pe) { 
           return null; 
       } 
    } 


    /** 
     * yyyy-MM-dd转换为字符串格式 
     * 
     * @param date 
     *            Date类型 
     * @return 字符 
     */ 
    public static String getDate(Date date) { 
       dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
       return dateFormat.format(date); 
    } 


    /** 
     * 字符串转换为yyyy-MM-dd HH:mm:ss格式 
     * 
     * @param date 
     *            字符 
     * @return Date 
     */ 
    public static Date getLongDate(String date) { 
       try { 
           dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
           return dateFormat.parse(date); 
       } catch (ParseException pe) { 
           return null; 
       } 
    } 


    /** 
     * yyyy-MM-dd HH:mm:ss转换为字符串格式 
     * 
     * @param date 
     *            Date类型 
     * @return 字符 
     */ 
    public static String getLongDate(Date date) { 
       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
       return dateFormat.format(date); 
    } 


    /** 
     * 毫秒转换为yyyy-MM-dd HH:mm:ss日期格式 
     * 
     * @param timeMillis 
     *            毫秒 
     * @return Date 
     * @since Aug 25, 2009 
     */ 
    public static Date getLongDate(long timeMillis) { 
       try { 
           dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
           return dateFormat.parse(String.valueOf(timeMillis)); 
       } catch (Exception e) { 
           return null; 
       } 


    }
	
	
	
}
