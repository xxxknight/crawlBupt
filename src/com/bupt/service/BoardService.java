package com.bupt.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.bupt.dao.NoteDao;
import com.bupt.dao.NoteDetailDao;
import com.bupt.pojo.Board;
import com.bupt.pojo.Note;
import com.bupt.pojo.NoteDetail;
import com.bupt.util.Constants;
import com.bupt.util.StringUtil;

public class BoardService {
	public static final String HOST = "http://bbs.byr.cn";

	private NoteDetailDao noteDetailDao;

	private NoteDao noteDao;

	public BoardService() {
		this.noteDao = new NoteDao();
		this.noteDetailDao = new NoteDetailDao();
	}

	public void crawlBoardList(List<Board> list)
			throws ClientProtocolException, IOException {
		if (list == null || list.size() <= 0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			crawlBoard(list.get(i));
		}
	}

	private void crawlBoard(Board board) throws ClientProtocolException,
			IOException {
		String html = null;
		String url = HOST + board.getLink();
		for (int i = 0; i < board.getTotalPage(); i++) {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			StringBuilder urlBuilder = new StringBuilder(url);
			urlBuilder.append("?p=" + (i + 1));
			HttpGet httpget = new HttpGet(urlBuilder.toString());// 以get方式请求该URL
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
						html = EntityUtils.toString(entity);//
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				response.close();
			}
			extractNote(html, board);
		}
	}

	private void extractNote(String html, Board board) {
		Document document = Jsoup.parse(html);
		Elements elements = document
				.select("div.b-content table.board-list tbody tr");
		for (Element ele : elements) {
			Note note = new Note();
			String link = ele.select("td.title_8 a").attr("href");
			String title = ele.select("td.title_9 a").first().text().trim();
			String replyNumStr = ele.select("td.title_11").text().trim();
			String dateStr = ele.select("td.title_10").first().text().trim();
			int replyNum = Integer.parseInt(replyNumStr);
			int totalPage = (int) Math.ceil((replyNum + 1)
					/ Constants.PER_NOTE_REPLY.getState());
			String author = ele.select("td.title_12 a").first().text().trim();
			long noteId = getNoteIdFromUrl(link, board.getBoardId());
			note.setNoteId(noteId);
			note.setTitle(title);
			note.setAuthor(author);
			note.setBoardId(board.getBoardId());
			note.setBoard(board.getName());
			note.setLink(link);
			note.setReplyNum(replyNum);
			note.setTotalPage(totalPage);

			note.setDate(dateStr.length() == 10 ? dateStr : getNowDate());
			note.setIstop(0);
			note.setValidReplyNum(0);

			int num = this.noteDao.insertNote(note);
			//有新的帖子插入才执行
			if (num > 0) {
				try {
					crawlPageHtml(note);
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}

		}
	}

	private void crawlPageHtml(Note note) throws IOException {
		String html = null;
		int totalPage = note.getTotalPage();
		String url = HOST + note.getLink();

		for (int i = 0; i < totalPage; i++) {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			StringBuilder urlBuilder = new StringBuilder(url);
			urlBuilder.append("?p=" + (i + 1));
			HttpGet httpget = new HttpGet(urlBuilder.toString());// 以get方式请求该URL
			httpget.addHeader("X-Requested-With", "XMLHttpRequest");
			httpget.addHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				int resStatu = response.getStatusLine().getStatusCode();// 返回
				if (resStatu == HttpStatus.SC_OK) {// 200正常
					// 获得相应实体
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						html = EntityUtils.toString(entity);//
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				response.close();
			}
			crawlPerPage(urlBuilder.toString(), html, note, (i + 1));
		}
	}

	private void crawlPerPage(String pageUrl, String html, Note note,
			int pageNum) {
		Document document = Jsoup.parse(html);

		Elements contentElements = document
				.select("table.article tbody tr.a-body td.a-content div.a-content-wrap");
		StringBuilder contentbBuilder = new StringBuilder();
		for (Element contentElement : contentElements) {
			contentbBuilder.append(contentElement.text());
		}
		NoteDetail detail = new NoteDetail();

		detail.setDetailId(StringUtil.convertString2Long("" + note.getNoteId()
				+ "0" + pageNum));
		detail.setTitle(note.getTitle());
		detail.setPageNum(pageNum);
		detail.setUrl(pageUrl);
		detail.setNoteId(note.getNoteId());
		detail.setContent(contentbBuilder.toString());
		this.noteDetailDao.insertNoteDetail(detail);
	}

	private long getNoteIdFromUrl(String link, int boardId) {
		if (null != link && link.length() > 0) {
			String[] strs = link.split("/");
			long noteId = StringUtil.convertString2Long(boardId
					+ strs[strs.length - 1]);
			return noteId;
		}
		return 0L;
	}

	private String getNowDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(calendar.getTime());
		return date;
	}

}
