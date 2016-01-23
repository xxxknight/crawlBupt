package com.bupt.pojo;

public class Board {
	private int boardId;
	private String name;
	private String title;
	private String link;
	private String admin;
	private long totalNote;
	private long totalArticle;
	private int sectionId;
	private long totalPage;

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getTotalNote() {
		return totalNote;
	}

	public void setTotalNote(long totalNote) {
		this.totalNote = totalNote;
	}

	public long getTotalArticle() {
		return totalArticle;
	}

	public void setTotalArticle(long totalArticle) {
		this.totalArticle = totalArticle;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		return "Board [boardId=" + boardId + ", name=" + name + ", title="
				+ title + ", link=" + link + ", admin=" + admin
				+ ", totalNote=" + totalNote + ", totalArticle=" + totalArticle
				+ ", sectionId=" + sectionId + ", totalPage=" + totalPage + "]";
	}

}
