package com.xlongwei.archetypes.dubbo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 常见日期类型处理：字符串、日期Date、长整数new Date(long)，parse(time).getTime()
 * @author hongwei
 */
public class DateUtil {
	public static final SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");//10
	public static final SimpleDateFormat dayRead = new SimpleDateFormat("yyyy年MM月dd日");//11
	public static final SimpleDateFormat dayTimeRead = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");//18
	public static final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//19
	public static final SimpleDateFormat dayShortTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");//16
	public static final SimpleDateFormat simpleDay = new SimpleDateFormat("yyyyMMdd");//8
	public static final SimpleDateFormat seconds = new SimpleDateFormat("yyyyMMddHHmmss");//14
	public static final SimpleDateFormat microSeconds = new SimpleDateFormat("yyyyMMddHHmmssSSS");//17
	public static final SimpleDateFormat httpHeader = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);//29
	public static final Map<String, SimpleDateFormat> dateFormats = new HashMap<>();

	static {
		httpHeader.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static enum FormatType {
		/** yyyy-MM-dd, curdate() */
		DAY,
		/** yyyy年MM月dd日 */
		DAYREAD,
		/** yyyy年MM月dd日 HH点mm分 */
		DAYTIMEREAD,
		/** yyyy-MM-dd HH:mm:ss, now() */
		DAYTIME,
		/** yyyy-MM-dd HH:mm */
		DAYSHORTTIME,
		/** yyyyMMdd */
		SIMPLEDAY,
		/** yyyyMMddHHmmss, 14位秒级时间序列号 */
		SECONDS,
		/** yyyyMMddHHmmssSSS, 17位毫秒时间序列号 */
		MICROSECONDS,
		/** EEE, dd MMM yyyy HH:mm:ss zzz */
		HTTPHEADER,
		/** 1406167122870, System.currentTimeInMillis() */
		JAVA,
		/** 1406166160, unix_timestamp(now( )) */
		MYSQL
	};

	/**
	 * @param time 支持格式：<li>yyyy-MM-dd, mysql: curdate()</li>
	 * <li>yyyy-MM-dd HH:mm:ss, mysql: now()</li>
	 * <li>1406167122870，java: System.currentTimeInMillis()</li>
	 * <li>1406166160，mysql: unix_timestamp(now())</li>
	 */
	public static Date parse(String time) {
		if(time==null || time.isEmpty()) return null;
		try {
			int length = time.length();//length=8|10|14|17|19
			switch(length) {
			case 8: return simpleDay.parse(time);
			case 10: return day.parse(time);
			case 11: return dayRead.parse(time);
			case 13: return new Date(Long.valueOf(time));
			case 14: return seconds.parse(time);
			case 16: return dayShortTime.parse(time);
			case 17: return microSeconds.parse(time);
			case 18: return dayTimeRead.parse(time);
			case 19: return dayTime.parse(time);
			case 29: return httpHeader.parse(time);
			}
		}catch(Exception ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}

	/**
	 * @param type 格式化日期<li>DAY yyyy-MM-dd, mysql: curdate()</li>
	 * <li>DAYTIME yyyy-MM-dd HH:mm:ss, mysql: now()</li>
	 * <li>JAVA 1406167122870，java: System.currentTimeInMillis()</li>
	 * <li>MYSQL 1406166160，mysql: unix_timestamp(now())</li>
	 */
	public static String format(Date date, FormatType type) {
		if(date == null) return null;
		if(type == null) type = FormatType.DAYTIME;
		switch(type) {
		case DAY: return day.format(date);
		case DAYREAD: return dayRead.format(date);
		case DAYTIME: return dayTime.format(date);
		case DAYTIMEREAD: return dayTimeRead.format(date);
		case DAYSHORTTIME: return dayShortTime.format(date);
		case SIMPLEDAY: return simpleDay.format(date);
		case SECONDS: return seconds.format(date);
		case MICROSECONDS: return microSeconds.format(date);
		case HTTPHEADER: return httpHeader.format(date);
		case JAVA: return String.valueOf(date.getTime());
		case MYSQL: return String.valueOf(date.getTime()/1000);
		}
		return null;
	}
	
	public static String format(Date date, String format) {
		if(date==null || format==null) return null;
		SimpleDateFormat dateFormat = dateFormats.get(format);
		if(dateFormat==null) {
			try {
				dateFormat = new SimpleDateFormat(format);
				dateFormats.put(format, dateFormat);
			}catch(Exception e) {
				return null;
			}
		}
		return dateFormat.format(date);
	}

	/** 判断是否同一天，用于同一天多次登录判断时间 */
	public static boolean isSameDay(Date date1, Date date2) {
		if(date1!=null && date2!=null) {
			String format1 = format(date1, FormatType.DAY);
			String format2 = format(date2, FormatType.DAY);
			return format1.equals(format2);
		}
		return false;
	}

	/** 某天的结束时间点 */
	public static String dayEnd(Date date) {
		return format(date, FormatType.DAY) + " 23:59:59";
	}
}
