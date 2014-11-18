package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tr.com.telekom.kmsh.manager.DataManager;
import tr.com.telekom.kmsh.manager.SSHManager;

public class ConnectionConfig extends AConfig {
	public String name = null;
	public String type = null;
	public String user = null;
	public String password = null;
	public String host = null;

	public ConnectionConfig() {
	}

	public void parseXML(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			name = eElement.getAttribute("name");
			type = eElement.getAttribute("type");

			password = eElement.getElementsByTagName("password").item(0)
					.getTextContent();

			if (password.startsWith("ENC(")) {
				password = XMLManager.decrypt(password.substring(4,
						password.length() - 1));
			}

			user = eElement.getElementsByTagName("user").item(0)
					.getTextContent();
			host = eElement.getElementsByTagName("host").item(0)
					.getTextContent();
		}
	}

	public String execute(String cmd) {
		String result = "";

		if (type.equals("ssh")) {
			// execute an ssh command
			result = SSHManager.executeCommand(this, cmd);
		} else if (type.equals("sql")) {
			// execute an sql command
			result = DataManager.executeSQL(this, cmd);
		}

		return result;
	}
}
