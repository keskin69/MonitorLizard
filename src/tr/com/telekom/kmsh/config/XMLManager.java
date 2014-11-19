package tr.com.telekom.kmsh.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLManager extends AConfigManager {
	public ArrayList<MailConfig> mailList = null;
	public ArrayList<ConnectionConfig> connectionList = null;
	public ArrayList<CommandConfig> commandList = null;
	public ArrayList<ReportConfig> reportList = null;
	public ArrayList<SMSConfig> smsList = null;
	public ArrayList<KeyConfig> group = null;

	public XMLManager() {
		mailList = new ArrayList<MailConfig>();
		connectionList = new ArrayList<ConnectionConfig>();
		reportList = new ArrayList<ReportConfig>();
		commandList = new ArrayList<CommandConfig>();
		smsList = new ArrayList<SMSConfig>();
		group = new ArrayList<KeyConfig>();
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

			// reports
			nList = doc.getElementsByTagName("report");
			for (int i = 0; i < nList.getLength(); i++) {
				ReportConfig rep = new ReportConfig();
				rep.parseXML(nList.item(i));
				reportList.add(rep);
			}

			// ssh group list
			nList = doc.getElementsByTagName("group");
			for (int i = 0; i < nList.getLength(); i++) {
				KeyConfig key = new KeyConfig();
				key.parseXML(nList.item(i));
				group.add(key);
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
