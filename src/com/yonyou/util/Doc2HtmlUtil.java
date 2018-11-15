package com.yonyou.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

public class Doc2HtmlUtil {

	/**
	 * 转换文件成pdf
	 */
	public static String file2pdf(InputStream fromFileInputStream, String toFilePath, String type, String filename)
			throws Exception {
		String timesuffix = filename;
		String docFileName = null;
		String htmFileName = null;
		if ("doc".equals(type)) {
			docFileName = timesuffix.concat(".doc");
			htmFileName = timesuffix.concat(".pdf");
		} else if ("xls".equals(type)) {
			docFileName = timesuffix.concat(".xls");
			htmFileName = timesuffix.concat(".pdf");
		} else if ("ppt".equals(type)) {
			docFileName = timesuffix.concat(".ppt");
			htmFileName = timesuffix.concat(".pdf");
		} else if ("txt".equals(type)) {
			docFileName = timesuffix.concat(".txt");
			htmFileName = timesuffix.concat(".pdf");
		} else {
			return null;
		}

		File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
		File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
		if (htmlOutputFile.exists()) {
			htmlOutputFile.delete();
		}

		htmlOutputFile.createNewFile();

		docInputFile.createNewFile();

		int bytesRead = 0;
		byte[] buffer = new byte[1024 * 8];
		OutputStream os = new FileOutputStream(docInputFile);
		while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		fromFileInputStream.close();

		OpenOfficeConnection connection = new SocketOpenOfficeConnection(FSUtil.FS_FILE_CONVERT_HOST, 8100);
		connection.connect();
		// convert
		DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
		converter.convert(docInputFile, htmlOutputFile);
		connection.disconnect();
		// 转换完之后删除word文件
		docInputFile.delete();
		return htmFileName;
	}

	/**
	 * 文件转换成Html
	 */
	public static String file2Html(InputStream fromFileInputStream, String toFilePath, String type, String filename)
			throws Exception {
		String timesuffix = filename;
		String docFileName = null;
		String htmFileName = null;
		if ("doc".equals(type)) {
			docFileName = timesuffix.concat(".doc");
			htmFileName = timesuffix.concat(".html");
		} else if ("xls".equals(type)) {
			docFileName = timesuffix.concat(".xls");
			htmFileName = timesuffix.concat(".html");
		} else if ("ppt".equals(type)) {
			docFileName = timesuffix.concat(".ppt");
			htmFileName = timesuffix.concat(".html");
		} else if ("txt".equals(type)) {
			docFileName = timesuffix.concat(".txt");
			htmFileName = timesuffix.concat(".html");
		} else if ("pdf".equals(type)) {
			docFileName = timesuffix.concat(".pdf");
			htmFileName = timesuffix.concat(".html");
		} else {
			return null;
		}
		File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
		File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
		if (htmlOutputFile.exists()) {
			htmlOutputFile.delete();
		}
		htmlOutputFile.createNewFile();
		docInputFile.createNewFile();

		int bytesRead = 0;
		byte[] buffer = new byte[1024 * 8];
		OutputStream os = new FileOutputStream(docInputFile);
		while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		fromFileInputStream.close();
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(FSUtil.FS_FILE_CONVERT_HOST, 8100);
		connection.connect();
		// convert
		DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
		converter.convert(docInputFile, htmlOutputFile);
		connection.disconnect();
		// 转换完之后删除word文件
		docInputFile.delete();
		return htmFileName;
	}

}
