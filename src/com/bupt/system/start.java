package com.bupt.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.bupt.pojo.Board;
import com.bupt.service.BoardService;
import com.bupt.service.DictoryService;
import com.bupt.service.SectionService;

public class Start {
	private static final int THREAD_NUM = 10;

	public void exe() {
		DictoryService dictoryService = new DictoryService();
		dictoryService.updateStartTime();
		
		System.out.println("start to crawl boards");
		SectionService sectionService = new SectionService();
		HashMap<Integer, List<Board>> map = null;
		try {
			map = sectionService.crawl();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end to crawl boards");

		// CrawlThead[] crawlTheads = new CrawlThead[THREAD_NUM];
		//
		// for (int i = 0; i < THREAD_NUM; i++) {
		// crawlTheads[i] = new CrawlThead(i, map.get(i));
		// crawlTheads[i].setName("seciton-" + i + "-thread");
		// }
		//
		// for (int i = 0; i < THREAD_NUM; i++) {
		// crawlTheads[i].start();
		// }

		for (int i = 0; i < THREAD_NUM; i++) {
			System.out.println("start to crawl : " + i);
			BoardService boardService = new BoardService();
			try {
				boardService.crawlBoardList(map.get(i));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("end crawl : " + i);
		}
		dictoryService.updateEndTime();
	}
	
	public static void main(String[] args) {
		Start start = new Start();
		start.exe();
	}
}
