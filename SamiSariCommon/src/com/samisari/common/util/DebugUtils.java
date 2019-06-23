package com.samisari.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;

import com.samisari.common.util.HttpClientUtil.HttpState;

public class DebugUtils {
	public static final String	DEBUG_PATH	= "NO_LOGGING";

	public static void dumpResponse(String filename, HttpState response) throws IOException {
		if (DEBUG_PATH.equals("NO_LOGGING")) {
			return;
		}
		String dir = filename.substring(filename.indexOf("http://") + "http://".length(),
				filename.indexOf("/", filename.indexOf("http://") + "http://".length() + 1)).replaceAll("[^a-z|^A-Z|^0-9]", "_");
		dir = DEBUG_PATH + dir;
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		getContentEncoding(response);

		String filepath = filename.substring(filename.indexOf("/", filename.indexOf("http://") + "http://".length() + 1));
		filepath = filename.substring(0, filename.indexOf("_")) + filepath.replaceAll("[^a-z|^A-Z|^0-9]", "_");
		filepath = (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date()) + "-"
				+ (filepath.length() > 50 ? filepath.substring(0, 50) : filepath);
		File f = new File(dirFile, filepath + ".html");
		File frequest = new File(dirFile, filepath + "_request.txt");

		BufferedOutputStream wr = new BufferedOutputStream(new FileOutputStream(f));
		wr.write(response.responseBytes);
		wr.flush();
		wr.close();

		BufferedWriter wrr = new BufferedWriter(new FileWriter(frequest));
		wrr.write(response.uri + "\n");
		wrr.write(response.responseStatusCode + "\n");
		wrr.write("--------Cookies----------------------\n");
		if (response.cookies != null)
			for (Cookie cookie : response.cookies) {
				wrr.write(cookie.getName() + "=" + cookie.getValue() + "\n");
			}
		wrr.write("--------Parameters----------------------\n");
		if (response.parameters != null)
			for (NameValuePair nvp : response.parameters) {
				wrr.write(nvp.getName() + "='" + nvp.getValue() + "'\n");
			}
		wrr.flush();
		wrr.close();
	}

	public static String getContentEncoding(HttpState response) {
		String source = new String(response.responseBytes);
		String encoding = "UTF-8";
		String pattern = ".*<meta\\s*http-equiv=\"Content-Type\"\\s*content=\"text/html; charset=([^\"]+)\">.*";
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE + Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		if (m.matches()) {
			encoding = m.group(1);
		}
		return encoding;
	}

}
