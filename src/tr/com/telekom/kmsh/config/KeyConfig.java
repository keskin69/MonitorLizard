package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KeyConfig extends AConfig {

	public String name = null;
	public String base = null;
	public String connectBy = null;
	public ArrayList<Key> keyList = null;

	public KeyConfig() {
		keyList = new ArrayList<Key>();
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			name = eElement.getAttribute("name");

			try {
				connectBy = eElement.getElementsByTagName("connectBy").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}

			try {
				base = eElement.getElementsByTagName("base").item(0)
						.getTextContent();
				base = base.replace("\t", "");
				base = base.replace("\n", "");
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}

			NodeList node = eElement.getElementsByTagName("key");
			for (int i = 0; i < node.getLength(); i++) {
				Node n = node.item(i);
				Element e = (Element) n;
				String delim = null;
				String field = null;
				String name = null;
				String grep = e.getTextContent();

				name = e.getAttribute("name");
				if (name.equals("")) {
					name = grep;
				}

				try {
					delim = e.getAttribute("delim");
				} catch (NullPointerException ex) {

				}

				try {
					field = e.getAttribute("field");
				} catch (NullPointerException ex) {

				}

				keyList.add(new Key(name, grep, delim, field));
			}
		}
	}

}
