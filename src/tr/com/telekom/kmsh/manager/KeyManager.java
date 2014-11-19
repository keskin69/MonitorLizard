package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.Command;
import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;

public class KeyManager {
	private GroupCommandConfig keyConf = null;

	public KeyManager(GroupCommandConfig keyConf) {
		this.keyConf = keyConf;
	}

	public String process(XMLManager conf) {
		String out = "";

		// execute all commands in the specified group
		ConnectionConfig connection = conf.findConnection(keyConf.connectBy);
		if (connection != null) {
			for (Command cmd : keyConf.commandList) {
				String command = "";

				cmd.command = KmshUtil.insertFunctionValue(cmd.command);
				if (keyConf.base == null) {
					command = cmd.command;
				} else {
					command = keyConf.base + " \"" + cmd.command
							+ "\" | tail -1";
				}

				if (!cmd.delim.equals("")) {
					command += "| cut -d\"" + cmd.delim + "\"" + " -f "
							+ cmd.field;
				}

				String result = null;
				if (connection.type.equals("ssh")) {
					// execute an ssh command
					result = SSHManager.executeCommand(connection, command);
					result = result.trim();
				} else if (connection.type.equals("sql")) {
					// execute a db command
					result = SQLManager.executeSQL(connection, command);
				}

				if (!result.equals("")) {
					H2Util.writeDB(cmd.name, result, cmd.id);
					out += cmd.command + "=" + result + ";";
				}
			}
		}

		return out;
	}
}
