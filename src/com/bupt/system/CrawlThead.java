package com.bupt.system;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.bupt.pojo.Board;
import com.bupt.service.BoardService;

public class CrawlThead extends Thread {
	private int sectionId;
	private List<Board> boards;

	public CrawlThead(int sectionId, List<Board> boards) {
		this.sectionId = sectionId;
		this.boards = boards;
	}

	@Override
	public void run() {
		System.out.println("start to crawl : " + sectionId);
		BoardService boardService = new BoardService();
		try {
			boardService.crawlBoardList(boards);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end crawl : " + sectionId);
	}
}
