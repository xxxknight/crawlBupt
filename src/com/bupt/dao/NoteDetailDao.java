package com.bupt.dao;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import com.bupt.db.DBAccess;
import com.bupt.mapper.NoteDetailMapper;
import com.bupt.pojo.NoteDetail;

public class NoteDetailDao {

	public int insertNoteDetail(NoteDetail nDetail) {
		SqlSession sqlSession = null;
		int num = 0;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			NoteDetailMapper noteDetailMapper = sqlSession
					.getMapper(NoteDetailMapper.class);
			num = noteDetailMapper.doInsertNoteDetail(nDetail);
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
	
	public void insertNoteDetail2(NoteDetail nDetail) {
		SqlSession sqlSession = null;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			NoteDetailMapper noteDetailMapper = sqlSession
					.getMapper(NoteDetailMapper.class);
			noteDetailMapper.doInsertNoteDetail2(nDetail);
			sqlSession.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}
}
