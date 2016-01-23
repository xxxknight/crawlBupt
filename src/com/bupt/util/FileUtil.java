package com.bupt.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class FileUtil {

	public static void saveFile(String dir, String filename, String content) {
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(dir + "\\" + filename);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(content);
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
	}
	
	public static void saveJsonFile(String dir, String filename, Object object){
		Gson gson = new Gson();
		String json = gson.toJson(object);
		saveFile(dir, filename, json);
	}

}
