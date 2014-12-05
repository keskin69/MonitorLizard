package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.addon.IAddOn;
import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.SQLUtil;
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

		Object obj = null;

		if (cmdConfig != null) {
			ConnectionConfig conn = xmlManager
					.findConnection(cmdConfig.connectBy);
			obj = execute(conn, cmdConfig.cmd, cmdConfig.id, null);

		} else {
			KmshLogger.log(4, "Command with " + id
					+ " cannot found in the config files.");
		}

		return obj;
	}

	public static Object execute(ConnectionConfig conStr, String cmd,
			String id, String rule) {
		KmshLogger.log(1, "Processing command " + id);

		if (conStr.type.equals("ssh")) {
			// execute an SSH command
			String result = SSHManager.executeCommand(conStr, cmd);
			return result.trim();
		} else if (conStr.type.equals("sql")) {
			// execute an SQL command
			Table result = SQLManager.executeSQL(conStr, cmd);
			return result;
		} else if (conStr.type.equals("java")) {
			// execute a java class
			try {
				IAddOn addOn = (IAddOn) Class.forName(cmd).newInstance();
				String result = addOn.process(rule);
				SQLUtil.writeTag(id);

				return result;
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
