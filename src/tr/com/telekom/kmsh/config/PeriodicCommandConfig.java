package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;

import tr.com.telekom.kmsh.util.KmshUtil;

public class PeriodicCommandConfig {
	public String name = null;
	public String command = null;
	public String delim = null;
	public String field = null;
	public String id = null;

	public PeriodicCommandConfig(String grpId, Element e) {
		command = e.getTextContent();
		command = KmshUtil.insertFunctionValue(command);
		command = KmshUtil.strCheck(command);

		name = e.getAttribute("name");
		if (name.equals("")) {
			name = command;
		}

		delim = e.getAttribute("delim");
		field = e.getAttribute("field");
		id = grpId + "." + e.getAttribute("id");
	}
}
