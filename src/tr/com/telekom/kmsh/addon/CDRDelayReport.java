package tr.com.telekom.kmsh.addon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.SQLUtil;

public class CDRDelayReport extends AAddOn {
	long totalDelay = 0;
	int total = 0;
	long min = Long.MAX_VALUE;
	long max = 0;
	int pageSize = 1000;

	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new CDRDelayReport().process("CDRDelay");
	}

	public void processH2Row(ResultSet rs) throws SQLException {
	}

	public String process(String cmdId) {
		String out = null;

		XMLManager xmlManager = new XMLManager();

		CommandConfig cmdConfig = xmlManager.findCommand(cmdId);
		ConnectionConfig cfg = xmlManager.findConnection(cmdConfig.connectBy);
		Connection conn = SQLUtil.connect(cfg);

		try {
			ResultSet rs = null;

			Statement statement = conn.createStatement();
			statement.setFetchDirection(ResultSet.FETCH_FORWARD);

			KmshLogger.log(1, "Executing " + cmdConfig.cmd);

			rs = statement.executeQuery(cmdConfig.cmd);

			KmshLogger.log(1, "Processing data");
			Date call = null;
			Date process = null;
			long delay = 0;

			while (rs.next()) {
				call = rs.getDate("arama_zamani");
				process = rs.getDate("process_date");

				delay = (process.getTime() - call.getTime()) / (60 * 1000);

				if (delay < min && delay > 0) {
					min = delay;
				}

				if (delay > max) {
					max = delay;
				}

				totalDelay += delay;
			}

			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		H2Util.writeDB("ToplamCDRIsleme", "Günlük Toplam İşlenen CDR", "",
				new Integer(total).toString());
		H2Util.writeDB("MinCDRIsleme", "En hızlı CDR İşleme zamanı (Dakika)",
				"", new Long(min).toString());
		H2Util.writeDB("MaxCDRIsleme", "En yavaş CDR işleme zamanı (Dakika)",
				"", new Long(max).toString());
		H2Util.writeDB("AveCDRIsleme", "Ortalama CDR işleme zamanı (Dakika)",
				"", new Long(totalDelay / total).toString());

		out = "Ave. notif:" + new Long(totalDelay / total).toString();

		KmshLogger.log(1, "Completed:" + cmdConfig.cmd);

		return out;
	}
}
