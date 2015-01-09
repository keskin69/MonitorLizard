package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MailConfig extends AConfig {
	public String password = null;
	public String sender = null;
	public ArrayList<String> receipents = null;

	public MailConfig() {
		receipents = new ArrayList<String>();
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			name = eElement.getAttribute("name");
			password = AConfigManager.readValue(eElement, "password");
			sender = AConfigManager.readValue(eElement, "sender");

			NodeList node = eElement.getElementsByTagName("receipent");
			for (int i = 0; i < node.getLength(); i++) {
				receipents.add(node.item(i).getTextContent());
			}

		}
	}
}
