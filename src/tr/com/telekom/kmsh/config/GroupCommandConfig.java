package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.com.telekom.kmsh.util.KmshUtil;

public class GroupCommandConfig extends AConfig {
	public String base = null;
	public String connectBy = null;
	public ArrayList<PeriodicCommandConfig> commandList = null;
	public ArrayList<String> runAt = null;

	public GroupCommandConfig() {
		commandList = new ArrayList<PeriodicCommandConfig>();
		runAt = new ArrayList<String>();
	}

	public CommandConfig findCommand(String id) {
		CommandConfig cf = null;
		for (PeriodicCommandConfig cmd : commandList) {
			if (cmd.id.equals(id)) {
				cf = new CommandConfig();
				cf.cmd = cmd.command;
				cf.id = cmd.id;
				cf.connectBy = connectBy;
				cf.name = cmd.name;
			}
		}

		return cf;
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			name = eElement.getAttribute("name");
			String p = eElement.getAttribute("period");
			if (KmshUtil.isNumeric(p)) {
				period = new Integer(p).intValue();
			}

			try {
				connectBy = eElement.getElementsByTagName("connectBy").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
				connectBy = "";
			}

			try {
				base = eElement.getElementsByTagName("base").item(0)
						.getTextContent();
				base = KmshUtil.strCheck(base);
			} catch (NullPointerException ex) {

			}

			NodeList node = eElement.getElementsByTagName("cmd");
			for (int i = 0; i < node.getLength(); i++) {
				Node n = node.item(i);
				Element e = (Element) n;

				commandList.add(new PeriodicCommandConfig(name, e));
			}

			node = eElement.getElementsByTagName("at");
			for (int i = 0; i < node.getLength(); i++) {
				Node n = node.item(i);
				Element e = (Element) n;

				runAt.add(e.getTextContent());
			}
		}
	}
}
