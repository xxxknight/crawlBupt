package com.bupt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	public static long convertString2Long(String str) {
		return StringUtils.isNumeric(str) == true ? Long.parseLong(str) : 0L;
	}

	public static String getNowTimeString() {
		Calendar cal = Calendar.getInstance();
		Date time = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}

}
