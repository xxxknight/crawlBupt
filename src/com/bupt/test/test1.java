package com.bupt.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class test1 {
	public static void main(String[] args) throws IOException {
		String url = "http://book.douban.com/subject/26671358/";
		Document doc = Jsoup
				.connect(url)
				.header("Accept", "*/*")
				.header("Accept-Encoding", "gzip, deflate, sdch")
				.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
				.header("Connection", "keep-alive")
				.header("Host", "bbs.byr.cn")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36")
				.timeout(0)
				.get();
		String dir = "d:\\test";
		String filename = "1.txt";
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(dir + "\\" + filename);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(doc.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fw = null;
			}
		}
		System.out.println(doc);
		
		
	}

}
