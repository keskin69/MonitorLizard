package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.functions.Functions;
import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshUtil;

public class DelayReport extends AAddOn {
	public static void main(String[] args) {
		new DelayReport(
				"/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg");
	}

	public DelayReport(String confFile) {
		ConfigReader.file = confFile;
		ConfigReader conf = ConfigReader.getInstance();
		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);

		String cmdId = "NotifDelay";
		CommandConfig cmdConfig = xmlManager.findCommand(cmdId);
		ConnectionConfig conn = xmlManager.findConnection(cmdConfig.connectBy);
		String result = CommandManager.execute(conn, cmdConfig.cmd);

	}

	public void processRow(ResultSet rs) throws SQLException {
		// 24-NOV-14 10.12.03.668000000 AM
		String n = rs.getString(1);
		String t = rs.getString(2);

		System.out.println(n.substring(13));
		Date nn = KmshUtil.convertFullDate(n);
	}

	public void process() {

	}
}
