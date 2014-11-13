package tr.com.telekom.kmsh.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigManager extends AConfigManager {
	public ArrayList<MailConfig> mailList = null;
	public ArrayList<ConnectionConfig> connectionList = null;
	public ArrayList<CommandConfig> commandList = null;
	public ArrayList<ReportConfig> reportList = null;
	public ArrayList<SMSConfig> smsList = null;
	public HashMap<String, String> aliasList = null;
	public ArrayList<KeyConfig> keyList = null;

	public ConfigManager() {
		mailList = new ArrayList<MailConfig>();
		connectionList = new ArrayList<ConnectionConfig>();
		reportList = new ArrayList<ReportConfig>();
		commandList = new ArrayList<CommandConfig>();
		smsList = new ArrayList<SMSConfig>();
		aliasList = new HashMap<String, String>();
		keyList = new ArrayList<KeyConfig>();
	}

	public void readConfig(String file) {
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("config");
			Element eElement = (Element) nList.item(0);
			String connectionsFile = eElement
					.getElementsByTagName("connectionsFile").item(0)
					.getTextContent();
			readConnections(connectionsFile);

			for (int i = 0; i < eElement.getElementsByTagName("commandsFile")
					.getLength(); i++) {
				String commandsFile = eElement
						.getElementsByTagName("commandsFile").item(i)
						.getTextContent();
				readCommands(commandsFile);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readConnections(String file) {
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			// mails
			NodeList nList = doc.getElementsByTagName("mail");
			for (int i = 0; i < nList.getLength(); i++) {
				MailConfig mail = new MailConfig();
				mail.parseXML(nList.item(i));
				mailList.add(mail);
			}

			// connections
			nList = doc.getElementsByTagName("connection");
			for (int i = 0; i < nList.getLength(); i++) {
				ConnectionConfig con = new ConnectionConfig();
				con.parseXML(nList.item(i));
				connectionList.add(con);
			}

			// sms
			nList = doc.getElementsByTagName("sms");
			for (int i = 0; i < nList.getLength(); i++) {
				SMSConfig con = new SMSConfig();
				con.parseXML(nList.item(i));
				smsList.add(con);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readCommands(String file) {
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			// commands
			NodeList nList = doc.getElementsByTagName("command");
			for (int i = 0; i < nList.getLength(); i++) {
				commandList.add(new CommandConfig(nList.item(i)));
			}

			// alias
			nList = doc.getElementsByTagName("alias");
			for (int i = 0; i < nList.getLength(); i++) {
				Element eElement = (Element) nList.item(i);
				aliasList.put(eElement.getAttribute("name"), nList.item(i)
						.getTextContent());
			}

			// reports
			nList = doc.getElementsByTagName("report");
			for (int i = 0; i < nList.getLength(); i++) {
				ReportConfig rep = new ReportConfig();
				rep.parseXML(nList.item(i));
				reportList.add(rep);
			}

			// keyList
			nList = doc.getElementsByTagName("keyList");
			for (int i = 0; i < nList.getLength(); i++) {
				KeyConfig key = new KeyConfig();
				key.parseXML(nList.item(i));
				keyList.add(key);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String insertAliases(String cmd) {
		for (String key : aliasList.keySet()) {
			cmd = cmd.replaceAll(key, aliasList.get(key));
		}

		cmd = cmd.replaceAll("\n", "");
		return cmd;
	}

	public ConnectionConfig findConnection(String conName) {
		for (ConnectionConfig con : connectionList) {
			if (con.name.equals(conName)) {
				return con;
			}
		}

		return null;
	}

	public SMSConfig findSMS(String sms) {
		for (SMSConfig s : smsList) {
			if (s.name.equals(sms)) {
				return s;
			}
		}

		return null;
	}

	public CommandConfig findCommand(String conName) {
		for (CommandConfig cmd : commandList) {
			if (cmd.name.equals(conName)) {
				return cmd;
			}
		}

		return null;
	}

	public MailConfig findMailConfig(String mail) {
		for (MailConfig m : mailList) {
			if (m.name.equals(mail)) {
				return m;
			}
		}

		return null;
	}

	public static void main(String[] args) {
		System.out.println(encrypt("peste123"));
		System.out.println(encrypt("areca1234"));
		System.out.println(encrypt("Gecici_123"));
		System.out.println(encrypt("Areca123!"));
	}
}
