package com.bupt.test;

import org.apache.ibatis.session.SqlSession;

import com.bupt.db.DBAccess;

public class testDB {
	public static void main(String[] args) {
		SqlSession sqlSession = null;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			
			// 通过sqlSession执行SQL语句
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sqlSession != null) {
				sqlSession.close();
			}
		}
	}

}
