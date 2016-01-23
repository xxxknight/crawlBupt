package com.bupt.pojo;

import java.util.List;

public class Section {
	private int id;
	private String name;
	private List<Board> boardList;
	
	public Section() {
		super();
	}

	public Section(int id, String name, List<Board> boardList) {
		super();
		this.id = id;
		this.name = name;
		this.boardList = boardList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Board> getBoardList() {
		return boardList;
	}

	public void setBoardList(List<Board> boardList) {
		this.boardList = boardList;
	}
	
	@Override
	public String toString() {
		return "Section [id=" + id + ", name=" + name + ", boardList="
				+ boardList + "]";
	}
	
	

}
