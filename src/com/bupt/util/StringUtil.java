package com.bupt.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	public static long convertString2Long(String str) {
		return StringUtils.isNumeric(str) == true ? Long.parseLong(str) : 0L;
	}

}
