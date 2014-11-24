package tr.com.telekom.kmsh.config;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.com.telekom.kmsh.util.KmshUtil;

public class GroupCommandConfig extends AConfig {

	public String name = null;
	public String base = null;
	public String connectBy = null;
	public ArrayList<PeriodicCommand> commandList = null;
	public int period = 0;

	public GroupCommandConfig() {
		commandList = new ArrayList<PeriodicCommand>();
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
				base = base.replace("\t", "");
				base = base.replace("\n", "");
			} catch (NullPointerException ex) {

			}

			NodeList node = eElement.getElementsByTagName("cmd");
			for (int i = 0; i < node.getLength(); i++) {
				Node n = node.item(i);
				Element e = (Element) n;

				commandList.add(new PeriodicCommand(name, e));
			}
		}
	}
}
