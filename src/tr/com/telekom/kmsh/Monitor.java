package tr.com.telekom.kmsh;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshLogger;

public class Monitor {
	static Logger logger = Logger.getLogger(Monitor.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure(new NullAppender());

		String confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";
		String type = "-r";
		String name = "Test";

		if (args.length == 2) {
			confFile = args[0];
			type = args[1];
		} else if (args.length == 3) {
			confFile = args[0];
			type = args[1];
			name = args[2];
		}

		ConfigReader.file = confFile;
		ConfigReader conf = ConfigReader.getInstance();
		String xmlFiles = conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);

		if (type.equals("-t")) {
			new PeriodicMonitor(xmlManager, name);
		} else if (type.equals("-r")) {
			new Repgen(xmlManager, name);
		} else if (type.equals("-win")) {
			new Terminal(confFile);
		} else if (type.equals("-term")) {
			new Terminal(confFile);
		}

		KmshLogger.log("Operations completed");
	}
}
