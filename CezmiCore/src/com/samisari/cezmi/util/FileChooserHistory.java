package com.samisari.cezmi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class FileChooserHistory {
	private static final Logger logger = Logger
			.getLogger(FileChooserHistory.class);
	private static String pathHistoryLocation;
	private static HashMap<String, String> pathHistory = new HashMap<String, String>();

	public static String get(String cmd) {
		return pathHistory.get(cmd);
	}

	public static void put(String cmd, String path) {
		pathHistory.put(cmd, path);
	}

	static {
		boolean fileCanRead = true;
		Properties pathProps = new Properties();
		pathHistoryLocation = (String) (System.getProperties().get("user.home"))
				+ "/Cezmi/paths.properties";
		File pathHistoryFile = new File(pathHistoryLocation);
		try {
			if (!pathHistoryFile.exists()) {
				if (!pathHistoryFile.getParentFile().exists()) {
					pathHistoryFile.getParentFile().mkdir();
				}
				pathHistoryFile.createNewFile();
			}
			pathProps.load(new FileInputStream(pathHistoryLocation));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Dosya erişim listesi yaratılamıyor!");
		} 
		if (fileCanRead) {
			Set<String> keys = pathProps.stringPropertyNames();
			for (String key : keys) {
				pathHistory.put(key, (String) pathProps.get(key));
			}
		} else {

		}

	}

	public static void save() {
		Properties pathProps = new Properties();
		Set<String> keys = pathHistory.keySet();
		for (String key : keys) {
			pathProps.put(key, pathHistory.get(key));
		}
		try {
			pathProps.store(new FileOutputStream(pathHistoryLocation),
					"Should change the path");
		} catch (FileNotFoundException e) {
			logger.debug("Dosya tarihçe dosyası bulunamadı. Yenisi yaratılıyor.");
			try {
				pathProps.store(new FileOutputStream(pathHistoryLocation),
						"Should change the path");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			logger.error("Error unexpected when retreiving application properties", e);
		}

	}
}
