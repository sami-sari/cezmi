package com.samisari.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UEDateUtils {
	private static final Log logger = LogFactory.getLog(UEDateUtils.class);

	public static String getUEDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return sdf.format(date);
	}

	public static Date getJavaDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddhhmmssSSS".substring(0, date.length()));
		return sdf.parse(date);
	}

	public static String format(String uedate, String dateFormat)
			throws ParseException {
		Date date = getJavaDate(uedate);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static String date() {
		return getUEDate(new Date());
	}

	public static int getYear(String date) {
		return Integer.parseInt(date.substring(0, 4));
	}

	public static int getMonth(String date) {
		return Integer.parseInt(date.substring(4, 6));
	}

	public static int getDay(String date) {
		return Integer.parseInt(date.substring(6, 8));
	}

	public static int getHour(String date) {
		return Integer.parseInt(date.substring(8, 10));
	}

	public static int setMin(String date) {
		return Integer.parseInt(date.substring(10, 12));
	}

	public static int getSeconds(String date) {
		return Integer.parseInt(date.substring(12, 14));
	}

	public static String setYear(String date, int year) {
		return ParserUtils.zeroPad(year, 4) + date.substring(4);
	}

	public static String setMonth(String date, int month) {
		return date.substring(0, 4) + ParserUtils.zeroPad(month, 2)
				+ date.substring(6);
	}

	public static String setDay(String date, int day) {
		return date.substring(0, 6) + ParserUtils.zeroPad(day, 2)
				+ date.substring(8);
	}

	public static String setHour(String date, int hour) {
		return date.substring(0, 8) + ParserUtils.zeroPad(hour, 2)
				+ date.substring(10);
	}

	public static String setMin(String date, int min) {
		return date.substring(0, 10) + ParserUtils.zeroPad(min, 2)
				+ date.substring(12);
	}

	public static String setSeconds(String date, int sec) {
		return date.substring(0, 12) + ParserUtils.zeroPad(sec, 2)
				+ date.substring(14);
	}

	public static void main(String[] args) {
		String ued = UEDateUtils.date();
		System.out.println(ued);
		try {
			System.out.println(UEDateUtils.format(UEDateUtils.date(),
					"dd/MM/yyyy hh:mm:ss"));
		} catch (ParseException e) {
			logger.error(null, e);
		}
		ued = UEDateUtils.setYear(ued, 2000);
		System.out.println(ued);
		ued = UEDateUtils.setMonth(ued, 10);
		System.out.println(ued);
		ued = UEDateUtils.setDay(ued, 5);
		System.out.println(ued);
		ued = UEDateUtils.setHour(ued, 21);
		System.out.println(ued);
		ued = UEDateUtils.setMin(ued, 15);
		System.out.println(ued);
		ued = UEDateUtils.setSeconds(ued, 2);
		System.out.println(ued);
		try {
			System.out.println(UEDateUtils.getJavaDate(ued));
		} catch (ParseException e) {
			logger.error(null,e);
		}

	}
}
