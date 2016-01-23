package com.bupt.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bupt.dao.BoardDao;
import com.bupt.pojo.Board;
import com.bupt.util.Constants;
import com.bupt.util.StringUtil;

public class CrawlSection {

	public static final int SEC_NUM = 10;

	public static int start_board_id = 100;

	public static final String HOST = "http://bbs.byr.cn";

	public static HashMap<Integer, List<Board>> map = new HashMap<Integer, List<Board>>();

	private BoardDao boardDao;

	public CrawlSection() {
		boardDao = new BoardDao();
	}

	public HashMap<Integer, List<Board>> crawl()
			throws ClientProtocolException, IOException {
		String html = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		for (int i = 0; i < SEC_NUM; i++) {
			String urlStr = HOST + "/section/" + i;
			HttpGet httpget = new HttpGet(urlStr);
			httpget.addHeader("X-Requested-With", "XMLHttpRequest");
			httpget.addHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				int resStatu = response.getStatusLine().getStatusCode();
				if (resStatu == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						html = EntityUtils.toString(entity);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
			saveJson(i, html);
		}
		return map;
	}

	public List<Board> saveJson(int sectionId, String html)
			throws ClientProtocolException, IOException {
		String secHtml = null;
		List<Board> res = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements elements = document
				.select("div.b-content table.board-list tbody tr");
		getBoards(res, sectionId, html);
		for (Element ele : elements) {
			String flagStr = ele.select("td.title_2").first().text().trim();
			if (flagStr.contains("二级目录")) {
				CloseableHttpClient httpclient = HttpClients.createDefault();
				String link = ele.select("td.title_1 a").first().attr("href");
				String url = HOST + link;
				System.out.println(url);
				HttpGet httpget = new HttpGet(url);
				httpget.addHeader("X-Requested-With", "XMLHttpRequest");
				httpget.addHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
				CloseableHttpResponse response = httpclient.execute(httpget);
				try {
					int resStatu = response.getStatusLine().getStatusCode();//
					if (resStatu == HttpStatus.SC_OK) {
						// 获得相应实体
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							secHtml = EntityUtils.toString(entity);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					response.close();
				}
				getBoards(res, sectionId, secHtml);
			}
		}
		map.put(sectionId, res);
		return res;

	}

	private void getBoards(List<Board> res, int sectionId, String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document
				.select("div.b-content table.board-list tbody tr");
		for (Element ele : elements) {
			String flagStr = ele.select("td.title_2").first().text().trim();
			if (!flagStr.contains("二级目录")) {
				Board board = new Board();
				String link = ele.select("td.title_1 a").first().attr("href");
				String title = ele.select("td.title_1 a").first().text().trim();
				StringBuilder adminBuilder = new StringBuilder();
				Elements adminElements = ele.select("td.title_2 a");
				for (Element adminElement : adminElements) {
					adminBuilder.append(adminElement.text() + " ");
				}
				String admin = adminBuilder.toString().trim();
				String totalNoteStr = ele.select("td.title_6").text();
				long totalNote = StringUtil.convertString2Long(totalNoteStr);
				long totalPage = (long) Math.ceil(totalNote
						/ Constants.PER_BOARD_NOTE.getState());
				String totalArticleStr = ele.select("td.title_7").text();
				long totalArticle = StringUtil
						.convertString2Long(totalArticleStr);

				String name = getBoardNameFromUrl(link);

				board.setBoardId(start_board_id++);
				board.setName(name);
				board.setSectionId(sectionId);
				board.setTitle(title);
				board.setLink(link);
				board.setAdmin(admin);
				board.setTotalNote(totalNote);
				board.setTotalArticle(totalArticle);
				board.setTotalPage(totalPage);
				boardDao.insertBoard(board);
				res.add(board);
			}
		}
	}

	private String getBoardNameFromUrl(String link) {
		if (null != link && link.length() > 0) {
			String[] strs = link.split("/");
			String name = strs[strs.length - 1];
			return name;
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			new CrawlSection().crawl();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
