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
import com.yonyou.util.FSUtil;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 5461676756311393742L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 读取目录信息
		String dirname = req.getParameter("dirname");
		String filename = req.getParameter("filename");
		String realFilePath = FSUtil.RESOURCE + File.separatorChar + dirname + File.separatorChar + filename;
		download(filename, realFilePath, req, resp);
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

}
