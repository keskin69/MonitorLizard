package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.com.telekom.kmsh.util.KmshUtil;

public class ReportConfig extends AConfig {
	public String id = null;
	public ArrayList<String> commands = null;
	public String useMail = null;
	public String useSms = null;
	public ArrayList<Condition> condition = null;
	public String note = null;
	public ArrayList<String> runAt = null;

	public ReportConfig() {
		commands = new ArrayList<String>();
		condition = new ArrayList<Condition>();
		runAt = new ArrayList<String>();
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			id = eElement.getAttribute("id");

			name = AConfigManager.readValue(eElement, "name");

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
				note = KmshUtil.strCheck(note);
			} catch (NullPointerException ex) {

			}

			NodeList node = eElement.getElementsByTagName("cmd");
			for (int i = 0; i < node.getLength(); i++) {
				commands.add(node.item(i).getTextContent());
			}

			node = eElement.getElementsByTagName("at");
			for (int i = 0; i < node.getLength(); i++) {
				Node n = node.item(i);
				Element e = (Element) n;

				runAt.add(e.getTextContent());
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
