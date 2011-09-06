/**
 * Created on July 06, 2011
 */
package org.gov.sunayana.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Shylesh
 */
public class LoadProperties {

	private static String schema = null;
	private static String driver = null;
	private static String url = null;
	private static String username = null;
	private static String passwd = null;
	private static String logFilePath = null;

	private static String memoryCheck = "0";
	private static String eodReportFilePath = null;

	private static final String CONF_FILE = "conf/config.properties";

	/**
	 * This static block loads all properties from the configuration file.
	 */
	static {

		FileInputStream in = null;
		Properties props = null;

		if (new File(CONF_FILE).isFile()) {
			try {
				in = new FileInputStream(CONF_FILE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				props = new Properties();
				props.load(in);
				in.close();

				driver = props.getProperty("sunayana.db.driver");
				schema = props.getProperty("sunayana.db.schema");
				url = props.getProperty("sunayana.db.url");
				username = props.getProperty("sunayana.db.username");
				passwd = props.getProperty("sunayana.db.password");
				logFilePath = props.getProperty("sunayana.log.file.path");
				eodReportFilePath = props.getProperty("sunayana.report.file.path");
				memoryCheck = props.getProperty("sunayana.system.memcheck");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getDriver() {
		return driver;
	}

	public static String getSchema() {
		return schema;
	}

	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return passwd;
	}

	public static String getLogfilePath() {
		return logFilePath;
	}

	public static String getEodReportFilePath() {
		return eodReportFilePath;
	}

	public static String getMemoryCheck() {
		return memoryCheck;
	}
}
