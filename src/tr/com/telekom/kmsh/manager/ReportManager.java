package tr.com.telekom.kmsh.manager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tr.com.telekom.kmsh.config.CommandConfig;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.util.H2Util;

public class ReportManager {
	private ReportConfig repConfig = null;
	private ArrayList<ContentPart> content = null;

	public static enum ContentType {
		TEXT, TABLE
	};

	public ReportManager(ReportConfig repConfig) {
		this.repConfig = repConfig;
		content = new ArrayList<ContentPart>();
	}

	public boolean process(XMLManager conf) {
		String result = null;
		boolean condition = true;

		for (String cmdName : repConfig.commands) {
			CommandConfig command = conf.findCommand(cmdName);
			if (command != null) {
				String cmd = "";
				if (command.cmd != null) {
					// insert java methods
					cmd = repConfig.insertFunctionValue(command.cmd);

					// execute all commands
					ConnectionConfig connection = conf
							.findConnection(command.connectBy);

					String preValue = H2Util.readDB(command.name);

					if (repConfig.preCondition != null) {
						Pattern r = Pattern.compile(repConfig.preCondition);
						Matcher m = r.matcher(preValue);
						if (!m.find()) {
							condition = false;
						}
					}

					if (connection != null) {
						if (connection.type.equals("ssh")) {
							// execute a ssh command
							result = SSHManager.executeCommand(connection, cmd);
							addContent(ContentType.TEXT, command.title, result);
						} else if (connection.type.equals("sql")) {
							// execute a db command
							result = SQLManager.executeSQL(connection, cmd);
							addContent(ContentType.TABLE, command.title, result);
						}
					} else {
						// Java command
						result = cmd;
						addContent(ContentType.TEXT, command.title, result);
					}

					H2Util.writeDB(command.name, result);

					if (repConfig.postCondition != null) {
						Pattern r = Pattern.compile(repConfig.postCondition);
						Matcher m = r.matcher(result);
						if (!m.find()) {
							condition = false;
						}
					}
				}
			}
		}

		return condition;
	}

	public final void addContent(ContentType type, String title, String body) {
		if (!body.endsWith("\n")) {
			body += "\n";
		}

		content.add(new ContentPart(type, title, body));
	}

	public String getContent() {
		return getTextContent();
	}

	private String getTextContent() {
		String output = repConfig.title + "\n\n";

		for (ContentPart p : content) {

			if (p.body == null) {
				output += p.title;
				continue;
			}

			if (p.type == ContentType.TEXT) {
				output += p.title;
				output += p.body;
			} else {
				output += "\n" + p.title + "\n";
				String lines[] = p.body.split("\n");

				for (String str : lines) {
					String col[] = str.split(XMLManager.DELIM);

					for (String c : col) {
						output += c + ";";
					}

					output += "\n";
				}
			}
		}

		if (repConfig.note != null) {
			output += repConfig.note;
		}

		return output;
	}

	public String getHTMLContent() {
		String output = repConfig.title + "<BR>";

		for (ContentPart p : content) {
			if (p.type == ContentType.TEXT) {
				output += p.title + "<BR>" + p.body + "<BR>";
			} else {
				String lines[] = p.body.split("\n");
				output += p.title + "<BR><TABLE>\n";

				for (String str : lines) {
					String col[] = str.split(XMLManager.DELIM);

					output += "<TR>";
					for (String c : col) {
						output += "<TD>" + c + "</TD>";
					}
					output += "</TR>\n";
				}

				output += "</TABLE>";
			}
		}

		return output;
	}

	public class ContentPart {
		ContentType type;
		String title;
		String body;

		public ContentPart(ContentType type, String title, String body) {
			this.type = type;
			this.title = title;
			this.body = body;
		}
	}
}
