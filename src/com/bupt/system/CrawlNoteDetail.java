package com.bupt.system;

import java.io.IOException;

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
import com.bupt.pojo.Note;
import com.bupt.pojo.NoteDetail;
import com.bupt.util.FileUtil;
import com.bupt.util.StringUtil;

public class CrawlNoteDetail {
	public static final String DIRECTORY = "D:\\byr\\note";

	public static final String HOST = "http://bbs.byr.cn";

	public static String crawl(String url) throws ClientProtocolException,
			IOException {
		String html = null;
		String[] strs = url.split("/");
		String noteId = strs[3];
		String board = strs[2].trim();
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
		crawlPageHtml(url, html, board, noteId);
		return html;
	}

	public static void crawlPageHtml(String url, String html, String board,
			String noteId) throws IOException {
		Document document = Jsoup.parse(html);

		Element notenum = document.select("li.page-pre i").first();
		Element authorElement = document.select(
				"table.article tbody tr.a-head td.a-left span.a-u-name a")
				.first();
		Element titleElement = document.getElementsByClass("n-left").first();
		String[] strs = titleElement.text().split(":");

		int replynum = Integer.parseInt(notenum.text());

		int totalPage = (int) Math.ceil(replynum / 10.0);
		String author = authorElement.text();
		String title = strs[1].trim();
		System.out.println("totalPage:" + totalPage);

		Note note = new Note();
		note.setLink(url);
		note.setNoteId(StringUtil.convertString2Long(noteId));
		note.setTotalPage(totalPage);
		note.setBoard(board);
		note.setReplyNum(replynum);
		note.setAuthor(author);
		note.setTitle(title);

		// 这两个字段是十大贴时才用到，默认为0
		note.setIstop(0);
		note.setValidReplyNum(0);
		NoteDao noteDao = new NoteDao();
		noteDao.insertNote(note);

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
			} finally {
				response.close();
			}
			FileUtil.saveFile(DIRECTORY + "\\" + noteId, noteId + "-" + (i + 1)
					+ ".txt", html);
			crawlPerPage(urlBuilder.toString(), html, noteId, (i + 1));
		}
	}

	public static void crawlPerPage(String pageUrl, String html, String noteId,
			int pageNum) {
		Document document = Jsoup.parse(html);
		Element titleElement = document.getElementsByClass("n-left").first();
		String[] strs = titleElement.text().split(":");
		String title = strs[1].trim();
		Elements contentElements = document
				.select("table.article tbody tr.a-body td.a-content div.a-content-wrap");
		StringBuilder contentbBuilder = new StringBuilder();
		for (Element contentElement : contentElements) {
			contentbBuilder.append(contentElement.text());
		}
		NoteDetail detail = new NoteDetail();

		detail.setDetailId(StringUtil.convertString2Long("" + noteId + "0" 
				+ pageNum));
		detail.setTitle(title);
		detail.setPageNum(pageNum);
		detail.setUrl(pageUrl);
		detail.setNoteId(StringUtil.convertString2Long(noteId));
		detail.setContent(contentbBuilder.toString());
		NoteDetailDao noteDetailDao = new NoteDetailDao();
		noteDetailDao.insertNoteDetail(detail);
	}

	public static void main(String[] args) {
		String url = "/article/Talking/5776850";
		try {
			crawl(url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
