package tr.com.telekom.kmsh.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tr.com.telekom.kmsh.addon.IAddOn;
import tr.com.telekom.kmsh.manager.SQLManager;
import tr.com.telekom.kmsh.manager.SSHManager;
import tr.com.telekom.kmsh.util.KmshLogger;

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

			try {
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
			} catch (NullPointerException ex) {

			}
		}
	}

	public String execute(String cmd) {
		String result = "";

		if (type.equals("ssh")) {
			// execute an ssh command
			result = SSHManager.executeCommand(this, cmd);
			result = result.trim();
		} else if (type.equals("sql")) {
			// execute an sql command
			result = SQLManager.executeSQL(this, cmd).getString();
		} else if (type.equals("java")) {
			// execute a java class
			KmshLogger.log("Executing Java Class> " + cmd);
			try {
				IAddOn addOn = (IAddOn) Class.forName(cmd).newInstance();
				addOn.process();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}
}
