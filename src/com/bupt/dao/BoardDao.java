package com.bupt.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.bupt.db.DBAccess;
import com.bupt.mapper.BoardMapper;
import com.bupt.pojo.Board;

public class BoardDao {

	public int insertBoard(Board board) {
		SqlSession sqlSession = null;
		int num = 0;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
			num = boardMapper.doInsertBoard(board);
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

	public List<Board> getBoardsBySectionId(int sectionId) {
		SqlSession sqlSession = null;
		List<Board> res = null;
		try {
			sqlSession = DBAccess.getSqlSessionFactory().openSession();
			// 通过sqlSession执行SQL语句
			BoardMapper boardMapper = sqlSession.getMapper(BoardMapper.class);
			res = boardMapper.getBoardsBySectionId(sectionId);
			sqlSession.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return res;
	}

}
