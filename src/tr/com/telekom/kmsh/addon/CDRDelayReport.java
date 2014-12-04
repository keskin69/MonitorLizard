package tr.com.telekom.kmsh.addon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.manager.SQLManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshLogger;

public class CDRDelayReport extends AAddOn {
	long totalDelay = 0;
	int total = 0;
	long min = Long.MAX_VALUE;
	long max = 0;

	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new CDRDelayReport().process("CDRDelay");
	}

	public void processH2Row(ResultSet rs) throws SQLException {
	}

	public String process(String cmdId) {
		String out = null;

		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);
		CommandConfig cmdConfig = xmlManager.findCommand(cmdId);
		ConnectionConfig cfg = xmlManager.findConnection(cmdConfig.connectBy);
		Connection conn = SQLManager.connect(cfg);

		try {
			Statement statement = conn.createStatement();

			KmshLogger.log(1, "Executing " + cmdConfig.cmd);
			ResultSet rs = statement.executeQuery(cmdConfig.cmd);

			KmshLogger.log(1, "Processing data");

			while (rs.next()) {
				total++;

				Date call = rs.getDate("arama_zamani");
				Date process = rs.getDate("process_date");

				long delay = (process.getTime() - call.getTime()) / (60 * 1000);

				if (delay < min) {
					min = delay;
				}

				if (delay > max) {
					max = delay;
				}

				totalDelay += delay;
			}
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
