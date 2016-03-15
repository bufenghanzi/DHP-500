package com.mingseal.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(System.currentTimeMillis());
		return currentTime;
	}

	/**
	 * 获得当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		String format = "yyyy-MM-dd HH:mm:ss:SSS";
		return getCurrentTime(format);
	}

	/**
	 * 数据库保存的时间，不需要精确到秒
	 * @Title getCurrentDate
	 * @Description 获取当前时间
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getCurrentDate() {
		String format = "yyyy-MM-dd HH:mm";
		return getCurrentTime(format);
	}

}
