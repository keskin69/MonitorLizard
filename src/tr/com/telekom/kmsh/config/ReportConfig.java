package tr.com.telekom.kmsh.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReportConfig extends AConfig {
	public String name = null;
	public String commandClass = null;
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

	public String insertFunctionValue(String str) {

		while (str.contains("#")) {
			int i = str.indexOf("#");
			int j = i + 1;

			while (true) {
				if (j == str.length()) {
					break;
				}

				if (!str.substring(j, j + 1).matches("[a-zA-Z0-9_]")) {
					break;
				}

				j++;
			}

			String func = str.substring(i + 1, j);

			String value = null;
			try {
				for (Method method : Class.forName(commandClass)
						.getDeclaredMethods()) {
					if (method.getName().equals(func)) {
						value = (String) method.invoke(this, null);
						str = str.replace("#" + func, value);
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
				break;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return str;
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
				commandClass = eElement.getElementsByTagName("commandClass")
						.item(0).getTextContent();
			} catch (NullPointerException ex) {
				commandClass = "tr.com.telekom.kmsh.functions.Functions";
			}

			NodeList node = eElement.getElementsByTagName("cmd");
			for (int i = 0; i < node.getLength(); i++) {
				commands.add(node.item(i).getTextContent());
			}

			try {
				note = eElement.getElementsByTagName("note").item(0)
						.getTextContent();
			} catch (NullPointerException ex) {
			}
		}
	}
}
