package com.bupt.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimeCrawl {

	public static void main(String[] args) {
		Timer timer = new Timer();
		Task task = new Task();
		Calendar cal = Calendar.getInstance();
		Date time = cal.getTime();
		timer.schedule(task, time, 259200000);
	}
}
