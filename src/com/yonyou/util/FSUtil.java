package com.yonyou.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

public class FSUtil {

	public static final String FS_FILE_CONVERT_HOST = "127.0.0.1";

	public static final String RESOURCE = "D:\\ftp";

	public static final int ISDIR = 1;
	public static final int ISFILE = 2;

	public static final String FTPNOENCRYPTION = "FTP_001";
	public static final String FTPENCRYPTION = "FTP_002";

	public static final String FILEENCRYPTION = "加密";

	public static boolean isOfficeFile(String type) {
		if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx") || type.equalsIgnoreCase("xls")
				|| type.equalsIgnoreCase("xlsx") || type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx")) {
			return true;
		}
		return false;
	}

	public static boolean isImage(String type) {
		if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpge") || type.equalsIgnoreCase("png")
				|| type.equalsIgnoreCase("bmp")) {
			return true;
		}
		return false;
	}

	public static boolean isPDF(String type) {
		if (type.equalsIgnoreCase("pdf")) {
			return true;
		}
		return false;
	}

	public static String encodeFileNameByDownload(String agent, String filename) {
		try {
			if (agent.contains("Firefox")) {
				// 采用BASE64编码
				filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
			} else {
				// 其它浏览器 IE 、google 采用URL编码
				filename = URLEncoder.encode(filename, "utf-8");
				filename = filename.replace("+", " ");
			}
			return filename;
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(FSUtil.class).error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
