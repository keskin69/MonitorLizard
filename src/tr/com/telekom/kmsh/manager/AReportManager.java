package tr.com.telekom.kmsh.manager;

import java.util.ArrayList;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.manager.ReportManager.ContentPart;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;

public abstract class AReportManager {
	protected ReportConfig repConfig = null;
	protected ArrayList<ContentPart> content = null;

	public static enum ContentType {
		TEXT, TABLE
	};

	public AReportManager(ReportConfig repConfig) {
		this.repConfig = repConfig;
		content = new ArrayList<ContentPart>();
	}

	abstract public void addContent(ContentType type, String title, String body);

	public boolean process(XMLManager conf) {
		boolean condition = true;
		String result = null;

		String before = H2Util.readDB(repConfig.name, "value");

		for (String cmdName : repConfig.commands) {
			result = H2Util.readDB(cmdName, "value");
			if (result.equals("")) {
				result = execute(conf, cmdName);
			} else {
				String title = H2Util.readDB(cmdName, "name");
				addContent(ContentType.TEXT, title, result);
			}
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

	public String execute(XMLManager conf, String cmdName) {
		String result = null;
		CommandConfig command = conf.findCommand(cmdName);

		if (command != null) {
			// execute all commands
			ConnectionConfig connection = conf
					.findConnection(command.connectBy);

			// insert java methods
			String cmd = KmshUtil.insertFunctionValue(command.cmd);

			if (connection != null) {
				if (connection.type.equals("ssh")) {
					// execute a ssh command
					result = SSHManager.executeCommand(connection, cmd);
					addContent(ContentType.TEXT, command.name, result);
				} else if (connection.type.equals("sql")) {
					// execute a db command
					result = SQLManager.executeSQL(connection, cmd);
					addContent(ContentType.TABLE, command.name, result);
				}
			} else {
				// Java command
				result = cmd;
				addContent(ContentType.TEXT, command.name, result);
			}
		}

		if (repConfig.note != null) {
			addContent(ContentType.TEXT, "Not:", repConfig.note);
		}

		return result;
	}
}
