package com.bupt.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class test {
	public static void main(String[] args) {
		try {
			Document doc = Jsoup
					.connect("http://bbs.byr.cn/board/WWWTechnology?_uid=guest")
					.header("Accept", "*/*")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
					.header("Connection", "keep-alive")
					.header("Host", "bbs.byr.cn")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36")
					.header("X-Requested-With", "XMLHttpRequest").timeout(0)
					.get();
			System.out.println(doc.text());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
