package tr.com.telekom.kmsh.manager;

import java.util.ArrayList;

import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.util.Table;

public class ReportManager extends AReportManager {
	public ReportManager(ReportConfig repConfig) {
		super(repConfig);
	}

	public final void addContent(String title, Object body) {
		content.add(new ContentPart(title, body));
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

			if (p.body instanceof String) {
				output += p.title + ":";
				output += p.body + "\n";
			} else {
				output += "\n" + p.title + "\n";
				Table tbl = (Table) p.body;
				output += tbl.getString();
			}
		}

		return output;
	}

	public String getHTMLContent() {
		String output = repConfig.title + "<BR>";

		for (ContentPart p : content) {
			if (p.body instanceof String) {
				output += p.title + "<BR>" + p.body + "<BR>";
			} else {
				output += p.title + "<BR><TABLE>\n";

				Table tbl = (Table) p.body;
				for (int i = 0; i < tbl.size(); i++) {
					@SuppressWarnings("unchecked")
					ArrayList<String> row = tbl.get(i);
					output += "<TR>";
					for (int j = 0; j < row.size(); j++) {
						output += "<TD>" + row.get(i) + "</TD>\n";
					}
					output += "</TR>\n";
				}

				output += "</TABLE><BR>";
			}
		}

		return output;
	}

	public class ContentPart {
		String title;
		Object body;

		public ContentPart(String title, Object body) {
			this.title = title;
			this.body = body;
		}
	}
}
