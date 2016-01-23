package com.bupt.system;

import java.io.IOException;
import java.util.ArrayList;
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
import com.bupt.pojo.Note;
import com.bupt.util.StringUtil;

public class CrawlBoard {
	public static final String HOST = "http://bbs.byr.cn";

	public static String crawl(String url, String board)
			throws ClientProtocolException, IOException {
		String html = null;
		url = HOST + url;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);// 以get方式请求该URL
		httpget.addHeader("X-Requested-With", "XMLHttpRequest");
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			int resStatu = response.getStatusLine().getStatusCode();
			if (resStatu == HttpStatus.SC_OK) {
				// 获得相应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);//
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
		extractNote(html, board);
		return html;

	}

	public static List<Note> extractNote(String html, String board) {
		List<Note> res = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements elements = document
				.select("div.b-content table.board-list tbody tr");
		System.out.println(elements);
		System.out.println(elements.size());
		for (Element ele : elements) {
			Note note = new Note();
			String link = ele.select("td.title_8 a").attr("href");
			String title = ele.select("td.title_9 a").first().text().trim();
			String replyNumStr = ele.select("td.title_11").text().trim();
			int replyNum = Integer.parseInt(replyNumStr);
			int totalPage = (int) Math.ceil((replyNum + 1) / 10.0);
			String author = ele.select("td.title_12 a").first().text().trim();
			long noteId = getNoteIdFromUrl(link);
			note.setNoteId(noteId);
			note.setTitle(title);
			note.setAuthor(author);
			note.setBoard(board);
			note.setLink(link);
			note.setReplyNum(replyNum);
			note.setTotalPage(totalPage);
			
			note.setIstop(0);
			note.setValidReplyNum(0);
			NoteDao noteDao = new NoteDao();
			noteDao.insertNote(note);

		}
		return res;
	}

	public static long getNoteIdFromUrl(String link) {
		if (null != link && link.length() > 0) {
			String[] strs = link.split("/");
			long noteId = StringUtil.convertString2Long(strs[strs.length - 1]);
			return noteId;
		}
		return 0L;
	}

	public static void main(String[] args) {
		String url = "/board/Talking";
		String board = "Talking";
		try {
			crawl(url, board);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
