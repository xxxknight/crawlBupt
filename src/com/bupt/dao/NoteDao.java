package com.bupt.dao;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import com.bupt.db.DBAccess;
import com.bupt.mapper.NoteMapper;
import com.bupt.pojo.Note;

public class NoteDao {

	public int insertNote(Note note) {
		SqlSession sqlSession = null;
		int num = 0;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			NoteMapper noteMapper = sqlSession.getMapper(NoteMapper.class);
			num = noteMapper.doInsertNote(note);
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

	public void insertNote2(Note note) {
		SqlSession sqlSession = null;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			NoteMapper noteMapper = sqlSession.getMapper(NoteMapper.class);
			noteMapper.doInsertNote2(note);
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
