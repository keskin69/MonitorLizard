package tr.com.telekom.kmsh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tr.com.telekom.kmsh.config.PeriodicCommand;
import tr.com.telekom.kmsh.util.ConfigReader;

public class H2Reader {

	public static String readDB(ArrayList<PeriodicCommand> commandList) {
		Connection conn = null;
		String out = "";
		ConfigReader conf = ConfigReader.getInstance();
		String DELIM = conf.getProperty("DELIM");

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();

			for (PeriodicCommand cmd : commandList) {
				String sql = "select * from tblKey where id='" + cmd.id
						+ "' order by date desc";

				ResultSet rs = stat.executeQuery(sql);

				// read only one line
				if (rs.next()) {
					String d = rs.getString("date");
					String v = rs.getString("value");
					v = v.replaceAll("\n", conf.getProperty("NL"));
					v = v.replaceAll(DELIM, conf.getProperty("FS"));

					String line = d.trim() + DELIM + cmd.name + DELIM
							+ v.trim() + DELIM + cmd.id;
					out += line.replaceAll("\n", "") + "\n";
				} else {
					out += DELIM + cmd.name + DELIM + DELIM + DELIM + "\n";
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

	public static String readAll(String cmdId) {
		Connection conn = null;
		String out = "";
		ConfigReader conf = ConfigReader.getInstance();
		String DELIM = conf.getProperty("DELIM");

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();

			String sql = "select * from tblKey where id='" + cmdId
					+ "' order by date desc";

			ResultSet rs = stat.executeQuery(sql);
			int cnt = 0;
			while (rs.next() && cnt < conf.getInt("MAX_VALUE")) {
				cnt++;
				String d = rs.getString("date");
				String v = rs.getString("value");

				out += d.trim() + DELIM + v.replace("\n", "").trim() + "\n";
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
}
