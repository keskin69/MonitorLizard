package tr.com.telekom.kmsh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;

import org.h2.Driver;

import tr.com.telekom.kmsh.config.PeriodicCommandConfig;

public class H2Util {
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int COMMAND = 2;
	public static final int DATE = 3;
	public static final int VALUE = 4;

	public static void unregisterDrivers() {
		Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = (Driver) drivers.nextElement();

			try {
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void init() {
		KmshLogger.log(0, "Initializing H2");
		execute("drop table tblKey;");
		execute("create table tblKey (id varchar(255), name varchar(255), command varchar(255), date varchar(19), value varchar(255));");
		execute("insert into tblKey values('sql1','','','2014-11-25 23:00:00', '');");
		execute("insert into tblKey values('class2.1','','','2014-11-25 23:00:00', '');");
	}

	public static long getAge(String id) {
		String date = readDB(id, "date");
		if (!date.equals("")) {
			Date dLast = KmshUtil.convertToDate(date);
			Date dNow = new Date();

			long diff = dNow.getTime() - dLast.getTime();
			long minutes = diff / (1000 * 60);
			return minutes;
		}

		return Long.MAX_VALUE;
	}

	public static String readDB(String id, String field) {
		ConfigReader conf = ConfigReader.getInstance();
		Connection conn = null;
		String out = "";

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();
			String sql = "select * from tblKey where id='" + id
					+ "' order by date desc";
			ResultSet rs = stat.executeQuery(sql);

			// read only one line
			if (rs.next()) {
				out = rs.getString(field);
			}

			conn.close();
			stat.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

	public static void writeDB(PeriodicCommandConfig cmd, String value) {
		writeDB(cmd.id, cmd.name, cmd.command, value);
	}

	public static void writeTag(String id) {
		writeDB(id, "", "", "");
	}

	public static void writeDB(String id, String name, String command,
			String value) {
		String date = KmshUtil.getCurrentTimeStamp();
		writeDB(id, name, command, value, date);
	}

	public static void writeDB(String id, String name, String command,
			String value, String date) {
		String sql = "insert into tblKey values ('" + id + "','" + name + "','"
				+ command + "','" + date + "','" + value + "')";

		execute(sql);
	}

	public static void execute(String sql) {
		Connection conn;
		ConfigReader conf = ConfigReader.getInstance();

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();
			stat.execute(sql);

			stat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void writeSummary(String id, String value) {
	// String date = KmshUtil.getCurrentTimeStamp();
	// String sql = "insert into tblSummary values('" + id + "','" + value
	// + "','" + date + "')";
	// write(sql);
	// }

	// public static void main(String... args) throws Exception {
	// Class.forName("org.h2.Driver");
	// Connection conn = DriverManager.getConnection("jdbc:h2:~/kmsh");
	// Statement stat = conn.createStatement();
	//
	// // this line would initialize the database
	// // from the SQL script file 'init.sql'
	// // stat.execute("runscript from 'init.sql'");
	//
	// stat.execute("create table tblKey (id varchar(255), name varchar(255), command varchar(255), date varchar(19), value varchar(255)");
	//
	// stat.close();
	// conn.close();
	// }

	// insert into tblreport values('sql1','','','2014-11-25 01:00:00', '');
	// create table tblSummary (id varchar(255), date varchar(19), value
	// varchar(255));
}
