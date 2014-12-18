package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SMSConfig extends AConfig {
	public ArrayList<String> number = null;

	public SMSConfig() {
		number = new ArrayList<String>();
	}

	@Override
	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			name = eElement.getAttribute("name");

			NodeList node = eElement.getElementsByTagName("number");
			for (int i = 0; i < node.getLength(); i++) {
				number.add(node.item(i).getTextContent());
			}
		}
	}
}
