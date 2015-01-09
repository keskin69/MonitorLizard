package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConnectionConfig extends AConfig {
	public String type = null;
	public String user = null;
	public String password = null;
	public String host = null;
	public String driver = null;

	public ConnectionConfig() {
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			name = eElement.getAttribute("name");
			type = eElement.getAttribute("type");

			user = AConfigManager.readValue(eElement, "user");
			password = AConfigManager.readValue(eElement, "password");
			host = AConfigManager.readValue(eElement, "host");
			driver = AConfigManager.readValue(eElement, "driver");
		}
	}
}
