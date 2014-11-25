package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.addon.IAddOn;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.KmshLogger;

public class CommandManager {
	public static String execute(ConnectionConfig config, String cmd) {
		String result = "";

		if (config.type.equals("ssh")) {
			// execute an ssh command
			result = SSHManager.executeCommand(config, cmd);
			result = result.trim();
		} else if (config.type.equals("sql")) {
			// execute an sql command
			result = SQLManager.executeSQL(config, cmd).getString();
		} else if (config.type.equals("java")) {
			// execute a java class
			KmshLogger.log("Executing Java Class> " + cmd);
			try {
				IAddOn addOn = (IAddOn) Class.forName(cmd).newInstance();
				addOn.process();
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

		return result;
	}
}
