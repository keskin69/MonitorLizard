package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.addon.IAddOn;
import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.Table;

public class CommandManager {
	public static Object execute(String id) {
		ConfigReader conf = ConfigReader.getInstance();
		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);
		CommandConfig cmdConfig = xmlManager.findCommand(id);

		ConnectionConfig conn = xmlManager.findConnection(cmdConfig.connectBy);
		Object obj = execute(conn, cmdConfig.cmd, cmdConfig.id);

		return obj;
	}

	public static Object execute(ConnectionConfig conStr, String cmd, String id) {
		KmshLogger.log(1, "Processing command " + id);

		if (conStr.type.equals("ssh")) {
			// execute an ssh command
			String result = SSHManager.executeCommand(conStr, cmd);
			return result.trim();
		} else if (conStr.type.equals("sql")) {
			// execute an sql command
			Table result = SQLManager.executeSQL(conStr, cmd);
			return result;
		} else if (conStr.type.equals("java")) {
			// execute a java class
			try {
				IAddOn addOn = (IAddOn) Class.forName(cmd).newInstance();
				addOn.process(id);
				H2Util.writeTag(id);
				return "";
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
}
