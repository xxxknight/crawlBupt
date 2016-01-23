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

import com.bupt.pojo.Note;
import com.bupt.util.FileUtil;

public class CrawlTopTen {
	public static final String HOST = "";
	public static final String REQUEST_URL = "http://bbs.byr.cn/default";
	public static final String SAVE_DIR = "D:\\byr\\default";
	public static final String SAVE_ORIGIN_FILENAME = "default.txt";
	public static final String SAVE_TOPTEN_FILENAME = "top.json";

	public static void crawl() throws ClientProtocolException, IOException {
		String url = REQUEST_URL + "?_uid=guest";
		String html = null;
		System.out
				.println("##################################################################################");
		System.out
				.println("##                    start crawl default...                                    ##");
		System.out
				.println("##################################################################################");
		long time1 = System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);// 以get方式请求该URL
		httpget.addHeader("X-Requested-With", "XMLHttpRequest");
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
		CloseableHttpResponse response = httpclient.execute(httpget);

		try {
			int resStatu = response.getStatusLine().getStatusCode();// 返回
			if (resStatu == HttpStatus.SC_OK) {// 200正常 其他就不
				// 获得相应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);// 获得html源代
				}
			}
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		} finally {
			response.close();
		}
		FileUtil.saveFile(SAVE_DIR, SAVE_ORIGIN_FILENAME, html);
		List<Note> res = extractOriginHtml2Top(html);
		System.out.println(res);
		//FileUtil.saveJsonFile(SAVE_DIR, SAVE_TOPTEN_FILENAME, res);
		crawlTopList(res);
		long time2 = System.currentTimeMillis();
		long diffTime = time2 - time1;
		System.out
				.println("##################################################################################");
		System.out.println("##                 end crawl default... (用时)  "
				+ diffTime + "  ms             ##");
		System.out
				.println("##################################################################################");
	}

	public static List<Note> extractOriginHtml2Top(String html) {
		List<Note> list = new ArrayList<Note>();
		Document document = Jsoup.parse(html);
		Elements eles = document.select("#topten .widget-content>ul>li");
		for (Element e : eles) {
			Note note = new Note();
			String title = e.attr("title");
			String link = e.select("a").attr("href");
			String validReplyNum = e.select("a>span").text();
			String[] strs = splitLink(link);

			note.setTitle(title);
			note.setNoteId(Long.parseLong(strs[3]));
			note.setBoard(strs[2]);
			note.setLink(link);
			note.setValidReplyNum((Integer.parseInt(validReplyNum)));
			list.add(note);
		}
		return list;
	}

	public static String[] splitLink(String link) {
		if (null != link || "".equals(link)) {
			String[] strs = link.split("/");
			if (strs != null) {
				return strs;
			}
		}
		return null;
	}
	
	public static void crawlTopList(List<Note> list) throws IOException{
		if (null == list || list.size() == 0) {
			return ;
		}
		for (int i = 0; i < list.size(); i++) {
			crawlNoteDetail(list.get(i));
		}
	}
	
	public static void crawlNoteDetail(Note note) throws IOException{
		 CrawlNoteDetail.crawl(note.getLink());
		
	}

	public static void main(String[] args) {
		try {
			crawl();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
