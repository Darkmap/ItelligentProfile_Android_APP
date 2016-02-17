package com.daocaowu.itelligentprofile.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ������ yyyy-MM-dd HH:mm:ss
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
	 * ��ȡ����ʱ��
	 * 
	 * @return�����ַ�����ʽ yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ��ȡ�Ӽ�����֮�������
	 * 
	 * @param addDay
	 *            Ҫ�Ӽ�������
	 * 
	 * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd
	 */
	public static String getDateByAdd(int addDay) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, addDay);
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	/**
	 * ��ȡʱ�� Сʱ:��;�� HH:mm:ss
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
	 * ��ȡʱ�� Сʱ:��;�� HH:mm:ss
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
	 * ��ȡʱ�� Сʱ:�� HH:mm
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
	 * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd HH:mm:ss
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
	 * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd HH:mm:ss
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
	 * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd HH:mm:ss
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
	 * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd HH:mm:ss
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
	 * �������ʱ��ӹ̶���Сʱ���ʱ��
	 * 
	 * @param dateDate
	 *            ����ʱ��
	 * @param mm
	 *            Ҫ�Ӽ���ʱ����
	 * @return ���ؼ�����ʱ��
	 */
	public static String dateToStringLong(java.util.Calendar dateDate, int mm) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateDate.add(Calendar.HOUR_OF_DAY, mm);
		String dateString = formatter.format(dateDate.getTime());
		return dateString;
	}

	/**
	 * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd
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
	 * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd
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
	 * �õ�����ʱ��
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * ��ȡһ�����е����һ��
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
	 * �õ�����ʱ��
	 * 
	 * @return �ַ��� yyyyMMdd HHmmss
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * �õ�����Сʱ
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
	 * �õ����ڷ���
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
	 * �����û������ʱ���ʾ��ʽ�����ص�ǰʱ��ĸ�ʽ �����yyyyMMdd��ע����ĸy���ܴ�д��
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
	 * �жϸ��������Ƿ�Ϊ���죬
	 * 
	 * ���뵱ǰʱ������֮�ڵ����ڣ�������֮�������
	 * 
	 * @param dt
	 * @param type
	 *            0--���� 1--7��֮�ڵ� 2--7��֮���
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
	 * �жϸ��������Ƿ�Ϊ���죬
	 * 
	 * ���뵱ǰʱ������֮�ڵ����ڣ�������֮�������
	 * 
	 * @param dt
	 * @param type
	 *            0--���� 1--7��֮�ڵ� 2--7��֮���
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
	 * ����ʱ�������Ƚϣ�����ǽ���Ļ�ֻ��ȡʱ��
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
					result = "����";
				} else if (days == 2) {
					result = "ǰ��";
				} else {
					result = strToDateStr(result);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}
	
	
	
	
	
	/**�� ʱ���� ����һ��"HH:MM"������String
	 * @param h Сʱ
	 * @param m ����
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
	 * ���ص�ǰʱ�䣬ex��"HH:mm"
	 */
	public static String getHHmmString(){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    	return sdf.format(new java.util.Date());
	}
	
	
	/**ʹ��һ��"HH:mm"��String����ȡ���������¸�ʱ��ĺ�����
	 * @param time "HH:mm"��String
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
	 * ��һ��HH:mm��ʽ��ʱ������ȡ��Hours
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
	 * ��һ��HH:mm��ʽ��ʱ������ȡ��Minutes
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
     * ���ַ������͵�����ת��Ϊtimestamp��ʱ�����java.sql.Timestamp 
     * 
     * @param dateString 
     *          ת��Ϊtimestamp���ַ��� 
     *@return string2TimeStamp 
     */ 
    public final static java.sql.Timestamp string2TimeStamp(String dateString) { 
       try { 
           if(dateString.length()>12) 
           { 
              dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", 
                     Locale.CHINA);// �趨��ʽ 
           }else{ 
              dateFormat = new SimpleDateFormat("yyyy-MM-dd", 
                     Locale.CHINA);// �趨��ʽ 
           } 
           dateFormat.setLenient(false); 
           java.util.Date timeDate = dateFormat.parse(dateString);// util���� 
           java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate 
                  .getTime());// Timestamp����,timeDate.getTime() 
           return dateTime; 
       } catch (ParseException pe) { 
           return null; 
       } 
    } 


    /** 
     *method ������Timestampת��Ϊ�ַ������� 
     * 
     * @param timeStamp 
     *           ת��ΪString���ַ��� 
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
     * �ַ���ת��Ϊyyyy-MM-dd��ʽ 
     * 
     * @param date 
     *            �ַ� 
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
     * yyyy-MM-ddת��Ϊ�ַ�����ʽ 
     * 
     * @param date 
     *            Date���� 
     * @return �ַ� 
     */ 
    public static String getDate(Date date) { 
       dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
       return dateFormat.format(date); 
    } 


    /** 
     * �ַ���ת��Ϊyyyy-MM-dd HH:mm:ss��ʽ 
     * 
     * @param date 
     *            �ַ� 
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
     * yyyy-MM-dd HH:mm:ssת��Ϊ�ַ�����ʽ 
     * 
     * @param date 
     *            Date���� 
     * @return �ַ� 
     */ 
    public static String getLongDate(Date date) { 
       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
       return dateFormat.format(date); 
    } 


    /** 
     * ����ת��Ϊyyyy-MM-dd HH:mm:ss���ڸ�ʽ 
     * 
     * @param timeMillis 
     *            ���� 
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
