package tr.com.telekom.kmsh.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshLogger;

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

	public static String executeSQL(ConnectionConfig conf, String sql) {
		String output = null;
		KmshLogger.log("Executing SQL> " + sql);
		// creating PreparedStatement object to execute query
		Statement statement = null;
		ResultSet result = null;

		Connection conn = connect(conf);
		try {
			statement = conn.createStatement();
			result = statement.executeQuery(sql);
		} catch (SQLException e) {
			output = e.getMessage();

			return output;
		}

		try {
			ResultSetMetaData rsmd = null;
			int colNumber = 0;

			while (result.next()) {
				if (result.getRow() == 1) {
					rsmd = result.getMetaData();
					colNumber = rsmd.getColumnCount();
					String header = "";
					for (int i = 1; i <= colNumber; i++) {
						header += rsmd.getColumnName(i)
								+ ConfigReader.getInstance().getProperty(
										"DELIM");
					}

					output = header;
				}

				String row = "";
				for (int i = 1; i <= colNumber; i++) {
					row += result.getString(i)
							+ ConfigReader.getInstance().getProperty("DELIM");
				}

				output += "\n" + row;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KmshLogger.log("Executing sql completed");
		return output;
	}
}
