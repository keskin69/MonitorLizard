package tr.com.telekom.kmsh.web;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tr.com.telekom.kmsh.config.KeyConfig;
import tr.com.telekom.kmsh.util.H2Manager;

public class PageMaker {
	// query db table, generate a page
	private KeyConfig keyConf = null;

	public PageMaker(String keyList, String name) {
		File fXmlFile = new File(keyList);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("keyList");
			for (int i = 0; i < nList.getLength(); i++) {
				KeyConfig keyConf = new KeyConfig();
				keyConf.parseXML(nList.item(i));

				if (keyConf.name.equals(name)) {
					this.keyConf = keyConf;
				}
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

	public String process() {
		String out = "";
		String values = H2Manager.readDB(keyConf.keyList);

		out += "Rapor Zamanı: " + H2Manager.getCurrentTimeStamp();
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Zaman</TH><TH>Veri</TH><TH>Değer</TH></TR>";
		for (String row : values.split("\n")) {
			out += "<TR>";

			for (String col : row.split(";")) {
				out += "<TD>" + col + "</TD>";
			}

			out += "</TR>";
		}

		out += "</TABLE>";

		return out;
	}

	public static void main(String args[]) {
		String conf = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/keyList.xml";
		String name = "key1";

		if (args.length == 2) {
			conf = args[0];
			name = args[2];
		}

		PageMaker page = new PageMaker(conf, name);
		System.out.println(page.process());
	}

}
