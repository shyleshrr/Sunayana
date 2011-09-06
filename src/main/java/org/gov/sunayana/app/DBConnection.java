/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gov.sunayana.app;

import java.sql.*;

public class DBConnection {

	public static Connection getDBConnection() {
		Connection con = null;
		try {
			Class.forName(LoadProperties.getDriver()).newInstance();
			con = DriverManager.getConnection(LoadProperties.getUrl() + LoadProperties.getSchema(), LoadProperties.getUsername(), LoadProperties.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
