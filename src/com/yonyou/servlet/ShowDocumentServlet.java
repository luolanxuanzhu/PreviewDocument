package com.yonyou.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yonyou.util.FSUtil;

public class ShowDocumentServlet extends HttpServlet {

	private static final long serialVersionUID = -8158542472867091750L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

		// 读取目录信息
		String urlParameter = req.getParameter("url");
		String urlAttrubute = (String) req.getAttribute("url");
		String role = req.getParameter("role");
		String dirname = null;
		if (null != urlAttrubute) {
			dirname = getDirNameByURL(urlAttrubute);
		} else {
			dirname = getDirNameByURL(urlParameter);
		}
		File dir = new File(FSUtil.RESOURCE + File.separatorChar + dirname);

		File[] fiels = dir.listFiles();
		if (null == fiels) {
			Logger.getLogger(ShowDocumentServlet.class)
					.error("不存在该文件夹：" + FSUtil.RESOURCE + File.separatorChar + dirname);
			throw new RuntimeException();
		}
		List<HashMap<String, Object>> fileInfoList = new ArrayList<HashMap<String, Object>>();
		for (File file : fiels) {
			// 权限控制
			if (role.equals(FSUtil.FTPNOENCRYPTION)) {
				if (file.getName().indexOf(FSUtil.FILEENCRYPTION) != -1) {
					continue;
				}
			}
			HashMap<String, Object> m = new HashMap<String, Object>();
			String type = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
			if (FSUtil.isImage(type) || FSUtil.isPDF(type) || FSUtil.isOfficeFile(type)) {
				m.put("ispreview", true);
			} else {
				m.put("ispreview", false);
			}
			m.put("filename", file.getName());
			m.put("isfile", file.isFile() ? FSUtil.ISFILE : FSUtil.ISDIR);
			fileInfoList.add(m);
		}
		req.setAttribute("fileInfoList", fileInfoList);
		req.setAttribute("dirname", dirname);
		req.setAttribute("role", role);

		req.getRequestDispatcher("/ShowDocuments.jsp").forward(req, response);

	}

	private String getDirNameByURL(String url) {
		StringBuffer sb = new StringBuffer();
		String[] arr = url.substring(20).split("/");
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i < arr.length - 1) {
				sb.append("/");
			}
		}
		return sb.toString();
	}

}
