package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.addon.IAddOn;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.Table;

public class CommandManager {
	public static Object execute(ConnectionConfig config, String cmd, String id) {
		if (config.type.equals("ssh")) {
			// execute an ssh command
			String result = SSHManager.executeCommand(config, cmd);
			return result.trim();
		} else if (config.type.equals("sql")) {
			// execute an sql command
			Table result = SQLManager.executeSQL(config, cmd);
			return result;
		} else if (config.type.equals("java")) {
			// execute a java class
			KmshLogger.log("Executing Java Class> " + cmd);
			try {
				IAddOn addOn = (IAddOn) Class.forName(cmd).newInstance();
				addOn.process(id);

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
