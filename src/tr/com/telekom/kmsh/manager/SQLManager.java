package tr.com.telekom.kmsh.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.Table;

public class SQLManager {
	private static Connection connect(ConnectionConfig conf) {
		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", conf.user);
		props.setProperty("password", conf.password);

		// creating connection to Oracle database using JDBC
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(conf.host, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public static Table executeSQL(ConnectionConfig conf, String sql) {
		Table result = null;

		KmshLogger.log(1, "Executing SQL> " + sql);

		Connection conn = connect(conf);
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			result = new Table(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		KmshLogger.log(1, "Executing sql completed");

		return result;
	}
}
