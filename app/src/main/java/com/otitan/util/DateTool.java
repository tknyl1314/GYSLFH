package com.otitan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Timestamp;

public class DateTool {

	private static SimpleDateFormat dateFormat = null;

	static {
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 设置lenient为false.
		// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
		dateFormat.setLenient(false);
	}

	/**
	 *
	 * 函数名称 : isValidDate 功能描述 : 参数及返回值说明：
	 *
	 * @param s
	 * @return
	 *         判断一个字符串是否符合时间格式
	 */
	public boolean isValidDate(String s) {
		try {
			dateFormat.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public String getDateStr() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTimeStr = sdf.format(date).toString();
		return currentTimeStr;
	}

	/**
	 *
	 * 函数名称 : formatDate 功能描述 : 参数及返回值说明：
	 *
	 * @param d
	 *            时间
	 * @param dateFormat
	 *            时间格式
	 * @return
	 *
	 *         修改记录： 日期：2013-3-27 上午10:09:48 修改人：adminstrator 描述 ：
	 *         将一个日期按照你指定的格式输出
	 *
	 */
	public String formatDate(Date d, String dateFormat) {
		SimpleDateFormat Format = new SimpleDateFormat(dateFormat);
		return Format.format(d);
	}

	/**
	 *
	 * 函数名称 : dateCompare 功能描述 : 参数及返回值说明：
	 *
	 * @param date1
	 *            时间格式的字符串 eg:2013-03-27
	 * @param date2
	 *            时间格式的字符串
	 * @return 0 ：相等 1 ：大于 2：小于
	 *         修改记录： 日期：2013-3-27 上午10:15:23 修改人：adminstrator 描述 ：比较断两个时间
	 *
	 */
	public int dateCompare(String date1, String date2) {

		Date a = Date.valueOf(date1);
		Date b = Date.valueOf(date2);

		if (a.equals(b)) {
			return 0;
		} else if (a.after(b)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 *
	 * 函数名称 : timeCompare 功能描述 : 参数及返回值说明：
	 * @param a
	 *            时间字符串 eg:2013-03-27 15:48:24
	 * @param b
	 *            时间字符串
	 * @return 0 ：相等 1 ：大于 2：小于
	 *
	 *         修改记录： 日期：2013-3-27 上午10:24:08 修改人：adminstrator 描述 ：
	 *         比较两个时间格式的字符串的前后
	 *
	 */
	public int timeCompare(String a, String b) {
		Timestamp time1 = Timestamp.valueOf(a);
		Timestamp time2 = Timestamp.valueOf(b);

		if (time1.equals(time2)) {
			return 0;
		} else if (time1.after(time2)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 *
	 * 函数名称 : twoDateInterval 功能描述 : 参数及返回值说明：
	 *
	 * @param aa
	 *            时间格式的字符串 eg：2013-03-02
	 * @param bb
	 * @return 两个时间格式的字符串之间的时间间隔的天数
	 *
	 *         修改记录： 日期：2013-3-27 上午10:27:52 修改人：adminstrator 描述
	 *         ：两个时间格式的字符串之间的时间间隔的天数
	 *
	 */
	public int twoDateInterval(String aa, String bb) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		long a = 0;
		long b = 0;
		try {
			a = sdf.parse(aa).getTime();
			b = sdf.parse(bb).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int julianDay = 0;
		if (a > b) {
			julianDay = (int) ((a - b) / (1000 * 60 * 60 * 24));
		} else {
			julianDay = (int) ((b - a) / (1000 * 60 * 60 * 24));
		}

		System.out.println(aa + " - " + bb + " = " + julianDay + " days");

		return julianDay;
	}

	/**
	 *
	 * 函数名称 : twoTimeInterval 功能描述 : 参数及返回值说明：
	 *
	 * @param a
	 *            eg:"2013-03-25 11:30:09"
	 * @param b
	 * @return 天：时：分：秒 修改记录： 日期：2013-3-27 上午10:34:55 修改人：adminstrator 描述
	 *         ：计算两个时间间隔的时分秒
	 *
	 */
	public String twoTimeInterval(String a, String b) {
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date begin = dfs.parse(a);
			java.util.Date end = dfs.parse(b);

			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

			System.out.println("相差的时间：" + between);

			long day = between / (24 * 3600);
			long hour = between % (24 * 3600) / 3600;
			long minute = between % 3600 / 60;
			long second = (between % 60);
			System.out.println("" + day + "天" + hour + "小时" + minute + "分"
					+ second + "秒");
			return day + ":" + hour + ":" + minute + ":" + second;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 *
	 * 函数名称 : afterdate 功能描述 : 参数及返回值说明：
	 *
	 * @param str_date
	 *            时间 eg:2013-03-27
	 * @param days
	 *            间隔天数
	 * @return
	 *
	 *         修改记录： 日期：2013-3-27 上午10:40:41 修改人：adminstrator 描述 ：计算间隔days天的日期
	 *
	 */
	public String afterdate(String str_date, int days) {
		try {

			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = dfs.parse(str_date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, days);
			// 运算之后的日期
			String str_return = (new SimpleDateFormat("yyyy-MM-dd")).format(cal
					.getTime());
			return str_return;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 *
	 * 函数名称 : afterTime 功能描述 : 参数及返回值说明：
	 *
	 * @param str_date
	 *            日期 2013-03-23 11:40:09
	 * @param minute
	 *            间隔分钟
	 * @return 计算之后的日期
	 *
	 *         修改记录： 日期：2013-3-27 上午10:50:52 修改人：adminstrator 描述 ：间隔minute之后的日期
	 *
	 */
	public String afterTime(String str_date, int minute) {
		try {

			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = dfs.parse(str_date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minute);
			// 运算之后的时间
			String str_return = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
					.format(cal.getTime());
			return str_return;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 *
	 * 函数名称 : withinRange 功能描述 : 参数及返回值说明：
	 *
	 * @param str_date
	 *            日期
	 * @param uncheck
	 *            待验证的日期
	 * @param befer
	 *            之前的几分钟
	 * @param after
	 *            之后的几分钟
	 * @return
	 *
	 *         修改记录： 日期：2013-3-27 上午10:56:24 修改人：adminstrator 描述
	 *         ：判断uncheck这个日期是在str_date之前的before分钟和after分钟之后范围内
	 *
	 */
	public boolean withinRange(String str_date, String uncheck, int before,
							   int after) {
		try {

			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = dfs.parse(str_date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, (-before));

			// 开始时间
			String str_before = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
					.format(cal.getTime());
			System.out.println("str_before" + str_before);
			Timestamp time_before = Timestamp.valueOf(str_before);

			// 结束时间
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date);
			cal2.add(Calendar.MINUTE, after);
			String str_after = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
					.format(cal2.getTime());
			System.out.println("str_after" + str_after);
			Timestamp time_after = Timestamp.valueOf(str_after);

			// 待验证的时间
			Timestamp time_unckeck = Timestamp.valueOf(uncheck);

			// 判断是否在这两个时间段之间
			if (time_unckeck.after(time_before)
					&& time_unckeck.before(time_after)
					|| time_unckeck.equals(time_before)
					|| time_unckeck.equals(time_after)) {
				return true;
			} else {
				System.out.print("!=");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * 函数名称 : WhatDayIsToday 功能描述 : 参数及返回值说明：
	 *
	 * @param century
	 *            （当前世纪-1）例如2013年是21世纪，所以century为20
	 * @param year
	 *            年：例如2013，那么year就是13
	 * @param monthOfYear
	 *            月份（一年中的月份）
	 * @param dayOfMonth
	 *            日期（一月中的日期）
	 *
	 *            修改记录： 日期：2013-4-8 上午10:32:33 修改人：adminstrator 描述
	 *            ：计算某个时间是一周之中的星期几 计算规则：w=y+[y/4]+[c/4]-2c+[26(m+1)/10]+d-1
	 *            公式中的符号含义如下
	 *            ，w：星期；c：世纪-1；y：年（两位数）；m：月（m大于等于3，小于等于14，即在蔡勒公式中，某年的1、
	 *            2月要看作上一年的13、14月来计算， 比如2003年1月1日要看作2002年的13月1日来计算）；d：日；[
	 *            ]代表取整，即只要整数部分。(C是世纪数减一，y是年份后两位，M是月份，d是日数。 1月和2月要按上一年的13月和
	 *            14月来算，这时C和y均按上一年取值。)算出来的W除以7，余数是几就是星期几。如果余数是0，则为星期日。
	 */
	public void WhatDayIsToday(int century, int year, int monthOfYear,
							   int dayOfMonth) {
		int c = century;
		int y = year;
		int m = 0;

		if (monthOfYear < 3) {
			y -= 1;
			m = monthOfYear + 12;
		} else {
			m = monthOfYear;
		}

		int d = dayOfMonth;
		int w = (y + (y / 4) + (c / 4) - (2 * c) + (26 * (m + 1) / 10) + d - 1) % 7;
		System.out.println("w = " + w);

		if (w < 0) {
			w = w + 7;
		}

		String myWeek = null;
		switch (w) {
			case 1:
				myWeek = "一";
				break;
			case 2:
				myWeek = "二";
				break;
			case 3:
				myWeek = "三";
				break;
			case 4:
				myWeek = "四";
				break;
			case 5:
				myWeek = "五";
				break;
			case 6:
				myWeek = "六";
				break;
			case 0:
				myWeek = "天";
				break;

			default:
				break;

		}

		System.out.println(year + "年" + (monthOfYear) + "月" + dayOfMonth + "日"
				+ "周" + myWeek);
	}

	/**
	 *
	 * 函数名称 : dateDiff 功能描述 : 参数及返回值说明：
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @param str
	 *
	 *            修改记录：计算两个时间相差（天、小时、分钟、秒）
	 *
	 */
	@SuppressWarnings("unused")
	public Long dateDiff(String startTime, String endTime, String format,
						 String str) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		// 获得两个时间的毫秒时间差异
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd;// 计算差多少天
			hour = diff % nd / nh + day * 24;// 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
			sec = diff % nd % nh % nm / ns;// 计算差多少秒
			// 输出结果
			// System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
			// + (min - day * 24 * 60) + "分钟" + sec + "秒。");
			// System.out.println("hour=" + hour + ",min=" + min);

			if (str.equalsIgnoreCase("h")) {
				return hour;
			} else {
				return min;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * 函数名称 : weekTime 功能描述 : 参数及返回值说明：
	 *
	 * 修改记录： 日期：2013-6-21 下午05:59:03 修改人：郭齐源 描述 ：计算本周的时间段
	 *
	 */
	public HashMap<String, Object> weekTime() {
		Calendar cal = Calendar.getInstance();
		String today = String.format("%tF", cal.getTime());

		System.out.println("今天的日期: " + today);

		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		cal.add(Calendar.DATE, -day_of_week);
		String startDate = String.format("%tF", cal.getTime());
		System.out.println("本周第一天: " + startDate);

		// cal.add(Calendar.DATE, 7);
		cal.add(Calendar.DATE, 7);
		String endDate = String.format("%tF", cal.getTime());
		System.out.println("本周末: " + endDate);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("today", today);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	/**
	 *
	 * 函数名称 : differSecond 功能描述 : 参数及返回值说明：
	 *
	 * @param a
	 * @param b
	 * @return
	 *
	 *         修改记录： 日期：2013-6-25 下午09:59:35 修改人：adminstrator 描述 ：相差的秒数
	 *
	 */
	public long differSecond(String a, String b) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin;
		long between = 0;

		try {
			begin = dfs.parse(a);
			java.util.Date end = dfs.parse(b);
			between = (end.getTime() - begin.getTime()) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return between;
	}
}
