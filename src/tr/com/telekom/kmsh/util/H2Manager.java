package tr.com.telekom.kmsh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import org.h2.Driver;
import org.h2.jdbc.JdbcSQLException;

import tr.com.telekom.kmsh.config.Key;

public class H2Manager {
	public static String readDB(ArrayList<Key> keyList) {
		Connection conn = null;
		String out = "";

		try {
			Class.forName("org.h2.Driver");

			conn = DriverManager.getConnection(
					"jdbc:h2:tcp://localhost/~/kmsh", "sa", "");
			Statement stat = conn.createStatement();

			for (Key key : keyList) {
				String sql = "select * from tblKey where key='" + key.name
						+ "' order by date desc";

				ResultSet rs = stat.executeQuery(sql);

				// read only one line
				rs.next();
				try {
					out += rs.getString("date") + ";";
				} catch (JdbcSQLException ex) {
					out += "-;";
				}

				out += key.name + ";";

				try {
					out += rs.getString("value");
				} catch (JdbcSQLException ex) {
					out += "-;";
				}

				if (!out.endsWith("\n")) {
					out += "\n";
				}
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

	public static void writeDB(String key, String value) {
		Connection conn;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(
					"jdbc:h2:tcp://localhost/~/kmsh", "sa", "");

			String date = getCurrentTimeStamp();
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

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
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
