/**
 * Created on MAY 26, 2011
 */
package org.gov.sunayana.app;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anujit
 */
public class DailyReport {

	private static String SELECT_QUERY = " SELECT LEFT(entry_date,10) reportDate,device,COUNT(*) AS recordCount, " + " SUM(FIND_IN_SET(response,'y')) AS 'responseY', "
			+ " SUM(FIND_IN_SET(response,'300')) AS 'response300', " + " SUM(IF((FIND_IN_SET(response,'Y,300'))=0,1,0)) AS 'responseOther', "
			+ " ROUND(AVG(response_time),2)/1000 AS 'avgResTime' " + " FROM daily_report " + " WHERE LEFT(entry_date,10)=? " + " GROUP BY device ";

	private static String DELETE_REPORT_QUERY = " DELETE FROM daily_report where date(entry_date)=? ";

	private static String SELECT_UID_COUNT = " SELECT device,COUNT(DISTINCT uid) as uid FROM daily_report WHERE DATE(entry_date)=? GROUP BY device ";

	private String reportDate;
	private String uid;
	private String device;
	private String recordCount;
	private String responseY;
	private String response300;
	private String responseOther;
	private String avgResTime;

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getResponseY() {
		return responseY;
	}

	public void setResponseY(String responseY) {
		this.responseY = responseY;
	}

	public String getResponse300() {
		return response300;
	}

	public void setResponse300(String response300) {
		this.response300 = response300;
	}

	public String getResponseOther() {
		return responseOther;
	}

	public void setResponseOther(String responseOther) {
		this.responseOther = responseOther;
	}

	public String getAvgResTime() {
		return avgResTime;
	}

	public void setAvgResTime(String avgResTime) {
		this.avgResTime = avgResTime;
	}

	/**
	 * 
	 * @param date
	 * @param device
	 * @param extractor
	 * @param vendor
	 * @param response
	 * @param response_time
	 * @return
	 */
	public int insertDailyReport(String date, String uid, String device, String extractor, String vendor, String response, int response_time) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBConnection.getDBConnection();
			if (con == null) {
				System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.insertDailyReport() : No Database Connection");
				return 0;
			}
			pstmt = con.prepareStatement(" INSERT INTO daily_report VALUES(?,?,?,?,?,?,?) ");
			pstmt.setString(1, date);
			pstmt.setString(2, uid);
			pstmt.setString(3, device);
			pstmt.setString(4, extractor);
			pstmt.setString(5, vendor);
			pstmt.setString(6, response);
			pstmt.setInt(7, response_time);
			returnValue = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.insertDailyReport() : " + e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
					pstmt = null;
				} catch (Exception e) {
				}
			}
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (Exception e) {
				}
			}
		}
		return returnValue;
	}

	/**
	 * 
	 * @return
	 */
	public List<DailyReport> getDailyReport(String date) {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<DailyReport> dailyReportList = null;

		try {
			if (con == null) {
				System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.getDailyReport() : No Database Connection");
			} else {
				dailyReportList = new ArrayList<DailyReport>();
				psmt = con.prepareStatement(SELECT_QUERY);
				psmt.setString(1, date);
//				System.out.println("Daily Report: " + psmt.toString());
				rs = psmt.executeQuery();
				while (rs.next()) {
					DailyReport dailyReport = new DailyReport();
					dailyReport.setReportDate(rs.getString("reportDate"));
					dailyReport.setDevice(rs.getString("device"));
					dailyReport.setRecordCount(rs.getString("recordCount"));
					dailyReport.setResponseY(rs.getString("responseY"));
					dailyReport.setResponse300(rs.getString("response300"));
					dailyReport.setResponseOther(rs.getString("responseOther"));
					dailyReport.setAvgResTime(rs.getString("avgResTime"));
					dailyReportList.add(dailyReport);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.getDailyReport() : " + e.getMessage());
		} finally {
			if (psmt != null) {
				try {
					psmt.close();
					psmt = null;
				} catch (Exception e) {
				}
			}
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (Exception e) {
				}
			}
		}
		return dailyReportList;
	}

	/**
	 * 
	 * @return
	 */
	public List<DailyReport> getDistinctUid(String date) {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<DailyReport> dailyReportList = null;

		try {
			if (con == null) {
				System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.getDistinctUid() : No Database Connection");
			} else {
				dailyReportList = new ArrayList<DailyReport>();
				psmt = con.prepareStatement(SELECT_UID_COUNT);
				psmt.setString(1, date);
				rs = psmt.executeQuery();
				while (rs.next()) {
					DailyReport dailyReport = new DailyReport();
					dailyReport.setDevice(rs.getString("device"));
					dailyReport.setUid(rs.getString("uid"));
					dailyReportList.add(dailyReport);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR : in.gov.uidai.auth.app.DailyReport.getDistinctUid() : " + e.getMessage());
		} finally {
			if (psmt != null) {
				try {
					psmt.close();
					psmt = null;
				} catch (Exception e) {
				}
			}
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (Exception e) {
				}
			}
		}
		return dailyReportList;
	}

	public void generateCsvFile(String dt, String mcid, String loc) {

		String reportFile = LoadProperties.getEodReportFilePath() + "/EODReport-" + System.currentTimeMillis() + ".csv";

		if ((mcid != null && mcid.length() > 0) && (loc != null && loc.length() > 0)) {
			reportFile = LoadProperties.getEodReportFilePath() + "/" + mcid + "_" + loc + "_" + "EODReport-" + System.currentTimeMillis() + ".csv";
		} else {
			reportFile = LoadProperties.getEodReportFilePath() + "/EODReport_" + System.currentTimeMillis() + ".csv";
		}

		try {
			File logDir = new File(LoadProperties.getEodReportFilePath());
			if (logDir.exists()) {
				File logFile = new File(reportFile);
				if (logFile.exists() == false) {
					logFile.createNewFile();
				}
			} else {
				logDir.mkdirs();
				File logFile = new File(reportFile);
				if (logFile.exists() == false) {
					logFile.createNewFile();
				}
			}
		} catch (Exception e) {
		}

		List<DailyReport> dailyReportList = getDailyReport(dt);
		try {
			FileWriter fw = new FileWriter(reportFile);

			fw.append("EOD Report for the date " + dt);
			fw.append('\n');
			fw.append('\n');
			fw.append("Date");
			fw.append(',');
			fw.append("Sensor");
			fw.append(',');
			fw.append("Total # Of Sessions");
			fw.append(',');
			fw.append("Total # Of 'Y's");
			fw.append(',');
			fw.append("Total # Of '300's");
			fw.append(',');
			fw.append("Total # Of Others");
			fw.append(',');
			fw.append("Average Response Time (seconds)");
			fw.append('\n');

			if (dailyReportList != null && !dailyReportList.isEmpty()) {
				for (DailyReport dr : dailyReportList) {
					fw.append("\""+dr.getReportDate()+"\"");
					fw.append(',');
					fw.append(dr.getDevice());
					fw.append(',');
					fw.append(dr.getRecordCount());
					fw.append(',');
					fw.append(dr.getResponseY());
					fw.append(',');
					fw.append(dr.getResponse300());
					fw.append(',');
					fw.append(dr.getResponseOther());
					fw.append(',');
					fw.append(dr.getAvgResTime());
					fw.append('\n');
				}

				List<DailyReport> uidList = getDistinctUid(dt);

				fw.append('\n');
				fw.append('\n');
				fw.append("Serial No");
				fw.append(',');
				fw.append("Sensor");
				fw.append(',');
				fw.append("Total # Of Unique UIDs");
				fw.append('\n');

				if (uidList != null && !uidList.isEmpty()) {
					for (int i = 0; i < uidList.size(); i++) {
						fw.append(String.valueOf(i + 1));
						fw.append(',');
						fw.append(uidList.get(i).getDevice());
						fw.append(',');
						fw.append(uidList.get(i).getUid());
						fw.append('\n');
					}
				}
				fw.flush();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will delete database entry for a particular date.
	 * 
	 * @param date
	 * @return
	 */

	public int deleteDailyReport(String date) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBConnection.getDBConnection();
			if (con == null) {
				System.out.println("ERROR : No Database Connection");
				return 0;
			}
			pstmt = con.prepareStatement(DELETE_REPORT_QUERY);
			pstmt.setString(1, date);
			returnValue = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
					pstmt = null;
				} catch (Exception e) {
				}
			}
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (Exception e) {
				}
			}
		}
		return returnValue;
	}
}
