package com.bupt.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TESTdATE {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(calendar.getTime());
		System.out.println(date);
	}

}
