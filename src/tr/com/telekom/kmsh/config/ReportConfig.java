package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReportConfig extends AConfig {
	public String id = null;
	public ArrayList<String> commands = null;
	public String useMail = null;
	public String name = null;
	public String useSms = null;
	public ArrayList<Condition> condition = null;
	public String note = null;
	public int period = -1;

	public ReportConfig() {
		commands = new ArrayList<String>();
		condition = new ArrayList<Condition>();

	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			id = eElement.getAttribute("id");

			try {
				name = eElement.getElementsByTagName("name").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
				name = "";
			}

			try {
				useMail = eElement.getAttribute("mail");
			} catch (NullPointerException ex) {

			}

			try {
				period = new Integer(eElement.getAttribute("period"))
						.intValue();
			} catch (NumberFormatException ex) {

			}

			try {
				useSms = eElement.getAttribute("sms");
			} catch (NullPointerException ex) {

			}

			try {
				NodeList node = eElement.getElementsByTagName("condition");
				for (int i = 0; i < node.getLength(); i++) {
					condition.add(new Condition(node.item(i)));
				}
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

	public boolean checkCondition() {
		for (Condition con : condition) {
			if (con.checkRule()) {
				return true;
			}
		}

		return false;
	}
}
