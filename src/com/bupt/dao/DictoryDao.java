package com.bupt.dao;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import com.bupt.db.DBAccess;
import com.bupt.mapper.DictoryMapper;
import com.bupt.pojo.Dictory;

public class DictoryDao {

	public int updateDictory(Dictory dictory) {
		SqlSession sqlSession = null;
		int num = 0;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			DictoryMapper dictoryMapper = sqlSession
					.getMapper(DictoryMapper.class);
			num = dictoryMapper.updateDictByKey(dictory);
			sqlSession.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return num;
	}

}
