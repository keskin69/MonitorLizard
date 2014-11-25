package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public class DelayReport extends AAddOn {
	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new DelayReport().process("NotifDelay");
	}

	public void processRow(ResultSet rs) throws SQLException {
		// 24-NOV-14 10.12.03.668000000 AM
		String n = rs.getString(1);
		String t = rs.getString(2);

		System.out.println(n.substring(13));
		Date nn = KmshUtil.convertFullDate(n);
	}

	public void process(String cmdId) {
		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);
		CommandConfig cmdConfig = xmlManager.findCommand(cmdId);
		ConnectionConfig conn = xmlManager.findConnection(cmdConfig.connectBy);
		Object obj = CommandManager.execute(conn, cmdConfig.cmd, cmdConfig.id);

		if (obj instanceof Table) {
			Table tbl = (Table) obj;

			for (int i = 1; i < tbl.size(); i++) {
				@SuppressWarnings("unchecked")
				ArrayList<String> row = tbl.get(i);
				String n = row.get(0);
				System.out.print(n);
			}

		}
	}
}
