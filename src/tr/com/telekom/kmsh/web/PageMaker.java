package tr.com.telekom.kmsh.web;

import java.io.File;

import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.KmshUtil;

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

		out += "Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp();
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Zaman</TH><TH>Veri</TH><TH>Değer</TH><TH>Detay</TH></TR>";

		for (String row : values.split("\n")) {
			out += "<TR>";

			int cnt = 0;
			String key = "";
			for (String col : row.split(";")) {
				if (cnt == 1) {
					key = col;
				}

				out += "<TD>" + col + "</TD>\n";
				cnt++;
			}

			out += "<TD><CENTER><INPUT TYPE=\"BUTTON\" VALUE=\"?\" ONCLICK=\"detail('"
					+ key + "')\"><CENTER></TD>";
			out += "</TR>";
		}

		out += "</TABLE>";

		return out;
	}

	public static String getDetail(String key) {
		String out = "";
		String values = H2Manager.readAll(key);

		out += "Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp();
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Zaman</TH><TH>Değer</TH></TR>";

		for (String row : values.split("\n")) {
			out += "<TR>";

			int cnt = 0;
			for (String col : row.split(";")) {
				if (cnt != 1) {
					out += "<TD>" + col + "</TD>\n";
				}
				cnt++;
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
			name = args[1];
		}

		PageMaker page = new PageMaker(conf, name);
		KmshLogger.log(page.process());
	}

}
