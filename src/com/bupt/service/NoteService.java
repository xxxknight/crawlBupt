package com.bupt.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bupt.dao.NoteDetailDao;
import com.bupt.pojo.Note;
import com.bupt.pojo.NoteDetail;
import com.bupt.util.StringUtil;

public class NoteService {
	public static final String DIRECTORY = "D:\\byr\\note";

	public static final String HOST = "http://bbs.byr.cn";

	
	private NoteDetailDao noteDetailDao;

	public NoteService() {
		this.noteDetailDao = new NoteDetailDao();
	}
	
	public void CrawlNoteList(List<Note> list) throws IOException{
		if (list == null || list.size() <=0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			crawlPageHtml(list.get(i));
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
			} finally {
				response.close();
			}
//			FileUtil.saveFile(DIRECTORY + "\\" + noteId, noteId + "-" + (i + 1)
//					+ ".txt", html);
			crawlPerPage(urlBuilder.toString(), html, note, (i + 1));
		}
	}

	private void crawlPerPage(String pageUrl, String html,Note note,
			int pageNum) {
		Document document = Jsoup.parse(html);
		
		Elements contentElements = document
				.select("table.article tbody tr.a-body td.a-content div.a-content-wrap");
		StringBuilder contentbBuilder = new StringBuilder();
		for (Element contentElement : contentElements) {
			contentbBuilder.append(contentElement.text());
		}
		NoteDetail detail = new NoteDetail();

		detail.setDetailId(StringUtil.convertString2Long("" + note.getNoteId() + "0"
				+ pageNum));
		detail.setTitle(note.getTitle());
		detail.setPageNum(pageNum);
		detail.setUrl(pageUrl);
		detail.setNoteId(note.getNoteId());
		detail.setContent(contentbBuilder.toString());
		this.noteDetailDao.insertNoteDetail(detail);
	}

}
