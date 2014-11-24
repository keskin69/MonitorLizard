package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.util.ConfigReader;

public class ReportManager extends AReportManager {
	public ReportManager(ReportConfig repConfig) {
		super(repConfig);
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
					String col[] = str.split(ConfigReader.getInstance()
							.getProperty("DELIM"));

					for (String c : col) {
						output += c + ";";
					}

					output += "\n";
				}
			}
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
					String col[] = str.split(ConfigReader.getInstance()
							.getProperty("DELIM"));

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
