package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReportConfig extends AConfig {
	public String name = null;
	public ArrayList<String> commands = null;
	public String useMail = null;
	public String title = null;
	public String useSms = null;
	public String preCondition = null;
	public String postCondition = null;
	public String note = null;

	public ReportConfig() {
		commands = new ArrayList<String>();
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			name = eElement.getAttribute("name");

			try {
				title = eElement.getElementsByTagName("title").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
				title = "";
			}

			try {
				useMail = eElement.getAttribute("mail");
			} catch (NullPointerException ex) {

			}

			try {
				useSms = eElement.getAttribute("sms");
			} catch (NullPointerException ex) {

			}
			try {
				preCondition = eElement.getElementsByTagName("precondition")
						.item(0).getTextContent();
			} catch (NullPointerException ex) {

			}
			try {
				postCondition = eElement.getElementsByTagName("postcondition")
						.item(0).getTextContent();
			} catch (NullPointerException ex) {

			}

			try {
				note = eElement.getElementsByTagName("note").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {

			}

			NodeList node = eElement.getElementsByTagName("cmd");
			for (int i = 0; i < node.getLength(); i++) {
				commands.add(node.item(i).getTextContent());
			}
		}
	}
}
