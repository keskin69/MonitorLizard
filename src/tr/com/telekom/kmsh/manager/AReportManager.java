package tr.com.telekom.kmsh.manager;

import java.util.ArrayList;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.manager.ReportManager.ContentPart;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public abstract class AReportManager {
	protected ReportConfig repConfig = null;
	protected ArrayList<ContentPart> content = null;

	public AReportManager(ReportConfig repConfig) {
		this.repConfig = repConfig;
		content = new ArrayList<ContentPart>();
	}

	abstract public void addContent(String title, Object body);

	public boolean process(XMLManager conf) {
		boolean condition = true;
		String result = null;

		String before = H2Util.readDB(repConfig.name, "value");

		for (String cmdId : repConfig.commands) {
			result = H2Util.readDB(cmdId, "value");
			if (result.equals("")) {
				execute(conf, cmdId);

				if (repConfig.note != null) {
					addContent("Not", repConfig.note);
				}
			} else {
				String title = H2Util.readDB(cmdId, "name");
				addContent(title, result);
			}
		}

		if (repConfig.condition.size() == 0) {
			return true;
		}

		condition = repConfig.checkCondition();

		if (condition) {
			if (before.equals("REP")) {
				// be sure it is not yet reported
				condition = false;
			}

			H2Util.writeDB(repConfig.name, "", "", "REP");
		} else {
			H2Util.writeDB(repConfig.name, "", "", "");
		}

		return condition;
	}

	public void execute(XMLManager conf, String cmdId) {
		String result = null;
		CommandConfig command = conf.findCommand(cmdId);

		if (command != null) {
			// execute all commands
			ConnectionConfig connection = conf
					.findConnection(command.connectBy);

			if (connection != null) {
				if (connection.type.equals("ssh")) {
					// execute a ssh command
					result = SSHManager.executeCommand(connection, command.cmd);
					addContent(command.name, result);
				} else if (connection.type.equals("sql")) {
					// execute a db command
					Table tbl = SQLManager.executeSQL(connection, command.cmd);
					addContent(command.name, tbl);
				} else {
					// Java command
					result = command.cmd;
					addContent(command.name, result);
				}
			}
		}
	}
}
