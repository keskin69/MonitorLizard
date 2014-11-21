package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;

public class PeriodicCommand {
	public String name = null;
	public String command = null;
	public String delim = null;
	public String field = null;
	public String id = null;

	public PeriodicCommand(String grp, Element e) {
		command = e.getTextContent();
		command = command.replaceAll("\n", "");
		command = command.replaceAll("\t", "");
		System.out.println(command);
		name = e.getAttribute("name").replaceAll("\n", "");
		if (name.equals("")) {
			name = command;
		}

		delim = e.getAttribute("delim");
		field = e.getAttribute("field");
		id = grp + "." + e.getAttribute("id");
	}
}
