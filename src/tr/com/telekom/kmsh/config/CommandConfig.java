package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tr.com.telekom.kmsh.util.KmshUtil;

public class CommandConfig extends AConfig {
	public String id = null;
	public String connectBy = null;
	public String cmd = null;

	public CommandConfig(Node node) {
		parseXML(node);
	}

	public CommandConfig() {
	}

	public void parseXML(Node nNode) {

		Element eElement = (Element) nNode;

		id = eElement.getAttribute("id");
		name = eElement.getAttribute("name");

		connectBy = AConfigManager.readValue(eElement, "connectBy");

		try {
			cmd = AConfigManager.readValue(eElement, "cmd");
			cmd = KmshUtil.insertFunctionValue(cmd);
			cmd = KmshUtil.strCheck(cmd);
		} catch (NullPointerException ex) {
		}
	}
}
