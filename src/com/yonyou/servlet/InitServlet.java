package com.yonyou.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 2116425161663967427L;

	public void init() throws ServletException {
		super.init();
		initLogProperty();
		initOpenOfficeService();
	}

	public void destroy() {
		closeOpenOfficeService();
		super.destroy();
	}

	private void initOpenOfficeService() {
		Runtime rn = Runtime.getRuntime();
		try {
			InputStream is = getServletContext().getResourceAsStream("config.properties");
			Properties p = new Properties();
			try {
				p.load(is);
				is.close();
			} catch (IOException e) {
				Logger.getLogger(InitServlet.class).error(e.getMessage());
				e.printStackTrace();
			}

			File file = new File(p.getProperty("openoffice_bat"));
			if (!file.exists()) {
				FileWriter writer = new FileWriter(p.getProperty("openoffice_bat"));
				writer.write("@echo   off ");
				writer.write("\r\n ");
				writer.write("C:");
				writer.write("\r\n ");

				writer.write("cd " + p.getProperty("openoffice_location"));
				writer.write("\r\n ");
				writer.write("soffice -headless -accept=socket,host=127.0.0.1,port=8100;urp; -nofirststartwizard");
				writer.write("\r\n ");
				writer.write("@echo   on ");
				writer.close();
			}
			rn.exec("cmd.exe /C " + p.getProperty("openoffice_bat"));
			System.out.println("openoffice正常打开");
			Logger.getLogger(InitServlet.class).info("openoffice正常打开");
		} catch (Exception e) {
			Logger.getLogger(InitServlet.class).error(e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void closeOpenOfficeService() {
		try {
			Process process = Runtime.getRuntime().exec("tasklist");
			Scanner in = new Scanner(process.getInputStream());
			while (in.hasNextLine()) {
				String processString = in.nextLine();
				if (processString.contains("soffice.exe")) {
					String cmd = "taskkill /f /im soffice.exe";
					process = Runtime.getRuntime().exec(cmd);
					System.out.println("openoffice正常关闭");
					Logger.getLogger(InitServlet.class).info("openoffice正常关闭");
				}
			}
		} catch (IOException e) {
			Logger.getLogger(InitServlet.class).error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void initLogProperty() {
		InputStream is = getServletContext().getResourceAsStream("log4j.properties");
		Properties p = new Properties();
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			Logger.getLogger(InitServlet.class).error(e.getMessage());
			e.printStackTrace();
		}
		PropertyConfigurator.configure(p);
		System.out.println("log4j配置已被加载");
		Logger.getLogger(InitServlet.class).info("log4j配置已被加载");
	}
}