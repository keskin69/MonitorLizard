package tr.com.telekom.kmsh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import org.h2.Driver;

public class H2Util {
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

	public static long getAge(String key) {
		String date = readDB("_" + key, "date");
		if (!date.equals("")) {
			Date dLast = KmshUtil.convertToDate(date);
			Date dNow = new Date();

			long diff = dNow.getTime() - dLast.getTime();
			long minutes = diff / (1000 * 60);
			return minutes;
		}

		return 0;
	}

	public static String readDB(String key, String field) {
		ConfigReader conf = ConfigReader.getInstance();
		Connection conn = null;
		String out = "";

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();
			String sql = "select * from tblKey where key='" + key
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

	public static void writeDB(String key, String value) {
		Connection conn;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(
					"jdbc:h2:tcp://localhost/~/kmsh", "sa", "");

			String date = KmshUtil.getCurrentTimeStamp();
			String sql = "insert into tblKey values ('" + date + "','" + key
					+ "','" + value + "')";
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

	// public static void main(String... args) throws Exception {
	// Class.forName("org.h2.Driver");
	// Connection conn = DriverManager.getConnection("jdbc:h2:~/kmsh");
	// Statement stat = conn.createStatement();
	//
	// // this line would initialize the database
	// // from the SQL script file 'init.sql'
	// // stat.execute("runscript from 'init.sql'");
	//
	// stat.execute("create table tblKey (date varchar(255), key varchar(255), value varchar(255))");
	//
	// stat.close();
	// conn.close();
	// }
}
