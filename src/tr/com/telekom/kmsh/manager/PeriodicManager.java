package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.PeriodicCommandConfig;
import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.Table;

public class PeriodicManager {
	private GroupCommandConfig groupConf = null;

	public PeriodicManager(GroupCommandConfig keyConf) {
		this.groupConf = keyConf;
	}

	public String process(XMLManager conf) {
		String out = "";

		// execute all commands in the specified group
		ConnectionConfig connection = conf.findConnection(groupConf.connectBy);
		if (connection != null) {
			for (PeriodicCommandConfig cmd : groupConf.commandList) {
				String command = "";

				if (groupConf.base.equals("")) {
					command = cmd.command;
				} else {
					command = groupConf.base + " \"" + cmd.command
							+ "\" | tail -1";
				}

				if (!cmd.delim.equals("")) {
					command += "| cut -d\"" + cmd.delim + "\"" + " -f "
							+ cmd.field;
				}

				Object obj = CommandManager.execute(connection, command,
						cmd.id, cmd.rule);
				String result = null;
				if (obj instanceof String) {
					result = (String) obj;
				} else {
					result = ((Table) obj).getString();
				}

				if (!result.equals("")) {
					H2Util.writeDB(cmd, result);
					out += cmd.command + "=" + result + ";";
				}
			}
		}

		return out;
	}
}
