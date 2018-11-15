package com.yonyou.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.star.uno.RuntimeException;
import com.yonyou.util.Doc2HtmlUtil;
import com.yonyou.util.FSUtil;

public class ConverServlet extends HttpServlet {

	private static final long serialVersionUID = -355046345960299468L;

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 读取目录信息
		String dirname = req.getParameter("dirname");
		String filename = req.getParameter("filename");

		if (!new File(FSUtil.RESOURCE + File.separatorChar + dirname + File.separatorChar + filename).isFile()) {
			// 转发
			req.setAttribute("url", "ftp://192.168.10.85/" + dirname + "/" + filename);
			req.getRequestDispatcher("showDocument").forward(req, resp);
			return;
		}

		String realFilePath = FSUtil.RESOURCE + File.separatorChar + dirname + File.separatorChar + filename;
		String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

		if (FSUtil.isImage(type) || FSUtil.isPDF(type)) {
			// 图片、pdf格式直接预览
			toBroswer(realFilePath, req, resp);
		} else if (FSUtil.isOfficeFile(type)) {
			// 微软格式文件转换pfd后预览
			FileInputStream fromFileInputStream = new FileInputStream(new File(realFilePath));
			String fileName = convertToPDF(req, dirname, filename, fromFileInputStream);
			if (null == fileName) {
				throw new RuntimeException("文件转换失败");
			}
			String PdfFilePath = req.getRealPath("/") + dirname + File.separatorChar + fileName;
			toBroswer(PdfFilePath, req, resp);
			fromFileInputStream.close();
		} else {
			// 不支持预览根式，直接下载
			// FXIME 不支持下载
			download(filename, realFilePath, req, resp);
		}
	}

	private void download(String filename, String realFilePath, HttpServletRequest req, HttpServletResponse resp) {

		// 文件名乱码问题
		String agent = req.getHeader("user-agent");
		String tempFileName = filename;
		filename = FSUtil.encodeFileNameByDownload(agent, filename);
		if (null == filename) {
			Logger.getLogger(ConverServlet.class).error("下载文件名转码失败！filename=" + tempFileName + ",agent=" + agent);
			throw new RuntimeException("下载文件名转码失败！filename=" + tempFileName + ",agent=" + agent);
		}

		resp.setHeader("Content-Disposition", "attachment;filename=" + filename);
		String mimeType = getServletContext().getMimeType(tempFileName);
		resp.setContentType(mimeType);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(new File(realFilePath));
			os = resp.getOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
			}
		} catch (IOException e) {
			Logger.getLogger(ConverServlet.class).error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					Logger.getLogger(ConverServlet.class).error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					Logger.getLogger(ConverServlet.class).error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private void toBroswer(String path, HttpServletRequest req, HttpServletResponse resp) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(new File(path));
			os = resp.getOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
			}
		} catch (IOException e) {
			Logger.getLogger(ConverServlet.class).error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					Logger.getLogger(ConverServlet.class).error(e.getMessage());
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					Logger.getLogger(ConverServlet.class).error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private String convertToPDF(HttpServletRequest request, String dirname, String fileName,
			InputStream fromFileInputStream) {
		String convertFileSavePath = request.getRealPath("/") + dirname;
		File file = new File(convertFileSavePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		try {
			if (type.endsWith("x")) {
				/*
				 * return Doc2HtmlUtil.file2Html(fromFileInputStream, convertFileSavePath,
				 * type.substring(0, type.length() - 1), fileName);
				 */
				return Doc2HtmlUtil.file2pdf(fromFileInputStream, convertFileSavePath,
						type.substring(0, type.length() - 1), fileName);
			} else {
				// return Doc2HtmlUtil.file2Html(fromFileInputStream, convertFileSavePath, type,
				// fileName);
				return Doc2HtmlUtil.file2pdf(fromFileInputStream, convertFileSavePath, type, fileName);
			}
		} catch (Exception e) {
			Logger.getLogger(ConverServlet.class).error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

}
