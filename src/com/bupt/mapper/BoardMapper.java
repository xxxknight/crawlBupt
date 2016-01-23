package com.bupt.mapper;

import java.util.List;

import com.bupt.pojo.Board;

public interface BoardMapper {
	int doInsertBoard(Board board);
	
	List<Board> getBoardsBySectionId(int sectionId);

}
