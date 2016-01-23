package com.bupt.service;

import com.bupt.dao.DictoryDao;
import com.bupt.pojo.Dictory;
import com.bupt.util.StringUtil;

public class DictoryService {
	private DictoryDao dictoryDao;

	public DictoryService() {
		this.dictoryDao = new DictoryDao();
	}
	
	public int updateStartTime(){
		String nowTime = StringUtil.getNowTimeString();
		Dictory dictory = new Dictory("start_time" , nowTime);
		System.out.println(nowTime);
		return this.dictoryDao.updateDictory(dictory);
	}
	
	public int updateEndTime(){
		String nowTime = StringUtil.getNowTimeString();
		Dictory dictory = new Dictory("end_time" , nowTime);
		System.out.println(nowTime);
		return this.dictoryDao.updateDictory(dictory);
	}
	
	

}
