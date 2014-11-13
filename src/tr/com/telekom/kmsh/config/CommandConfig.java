package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CommandConfig extends AConfig {
	public String title = null;
	public String connectBy = null;
	public String cmd = null;
	public String name = null;

	public CommandConfig(Node node) {
		parseXML(node);
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			title = eElement.getAttribute("title");
			name = eElement.getAttribute("name");

			try {
				connectBy = eElement.getElementsByTagName("connectBy").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
			}

			try {
				cmd = eElement.getElementsByTagName("cmd").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
			}
		}
	}
}
