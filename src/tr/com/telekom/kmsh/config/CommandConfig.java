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

		try {
			connectBy = eElement.getElementsByTagName("connectBy").item(0)
					.getTextContent();
		} catch (NullPointerException ex) {
		}

		try {
			cmd = eElement.getElementsByTagName("cmd").item(0).getTextContent();
			cmd = KmshUtil.insertFunctionValue(cmd);
			cmd = KmshUtil.strCheck(cmd);
		} catch (NullPointerException ex) {
		}

	}
}
