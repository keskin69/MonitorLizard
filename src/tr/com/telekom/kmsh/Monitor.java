package tr.com.telekom.kmsh;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.Table;

public class Monitor {
	static Logger logger = Logger.getLogger(Monitor.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure(new NullAppender());

		String confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";
		String type = "-t";
		String name = "KMSHStopped";

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
		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);

		// TODO
		//H2Util.writeTag("sql1");

		if (type.equals("-t")) {
			new PeriodicMonitor(xmlManager);
		} else if (type.equals("-r")) {
			new Repgen(xmlManager, name);
		} else if (type.equals("-win")) {
			new TerminalWindow(confFile);
		} else if (type.equals("-term")) {
			// TODO terminal without a window
			new TerminalWindow(confFile);
		} else if (type.equals("-c")) {
			Object result = CommandManager.execute(name);
			if (result instanceof String) {
				KmshLogger.log((String) result);
			} else {
				KmshLogger.log(((Table) result).getString());
			}
		}

		KmshLogger.log("Operations completed");
	}
}
