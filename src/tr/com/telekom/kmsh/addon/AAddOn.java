package tr.com.telekom.kmsh.addon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tr.com.telekom.kmsh.util.ConfigReader;

public abstract class AAddOn implements IAddOn {
	public void readAll(String sql) {
		Connection conn = null;

		ConfigReader conf = ConfigReader.getInstance();

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				processRow(rs);
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
	}
}
