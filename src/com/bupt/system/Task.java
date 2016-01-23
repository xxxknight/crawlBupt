package com.bupt.system;

import java.util.TimerTask;

public class Task extends TimerTask {

	@Override
	public void run() {
		Start start = new Start();
		start.exe();
	}

}