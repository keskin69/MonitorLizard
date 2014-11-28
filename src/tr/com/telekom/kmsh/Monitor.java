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

		KmshLogger.log(1, "Executing " + type + " " + name);

		ConfigReader.file = confFile;
		ConfigReader conf = ConfigReader.getInstance();
		String xmlFiles = conf.getProperty("base")
				+ conf.getProperty("xmlFiles");
		XMLManager xmlManager = new XMLManager();
		xmlManager.readConfig(xmlFiles);

		// TODO
		// H2Util.writeDB("sql1", "", "", "2014-11-25 23:00:00", "");

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
				KmshLogger.log(1, (String) result);
			} else {
				KmshLogger.log(1, ((Table) result).getString());
			}
		} else if (type.equals("-init")) {
			H2Util.init();
		} else {
			KmshLogger.log(4, "Unknown operation type" + type);
		}

		KmshLogger.log(1, "Operations completed");
	}
}
