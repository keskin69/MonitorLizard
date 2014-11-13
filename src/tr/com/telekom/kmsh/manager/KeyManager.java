package tr.com.telekom.kmsh.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import tr.com.telekom.kmsh.config.ConfigManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.Key;
import tr.com.telekom.kmsh.config.KeyConfig;

public class KeyManager {
	private KeyConfig keyConf = null;

	public KeyManager(KeyConfig keyConf) {
		this.keyConf = keyConf;
	}

	public void process(ConfigManager conf) {
		// execute all commands
		ConnectionConfig connection = conf.findConnection(keyConf.connectBy);
		if (connection != null) {
			for (Key key : keyConf.keyList) {
				String command = keyConf.base + " " + key.grep + " | tail -1";
				if (key.delim != null) {
					command += "| cut -d\"" + key.delim + "\"" + " -f "
							+ key.field;
				}

				String result = SSHManager.executeCommand(connection, command);
				writeDB(key.name, result);
				// System.out.println(command + "\n" + result);
			}
		}

	}

	public void writeDB(String key, String value) {
		Connection conn;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/kmsh", "sa", "");

			String date = getCurrentTimeStamp();
			String sql = "insert into tblKey values ('" + date + "','" + key
					+ "','" + value + "')";
			Statement stm = conn.createStatement();
			stm.execute(sql);

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
