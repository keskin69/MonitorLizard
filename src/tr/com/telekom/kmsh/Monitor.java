package tr.com.telekom.kmsh;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import tr.com.telekom.kmsh.config.ConfigManager;
import tr.com.telekom.kmsh.util.KmshLogger;

public class Monitor {
	static Logger logger = Logger.getLogger(Monitor.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure(new NullAppender());

		String confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/config.xml";
		String type = "-t";
		String name = "key1";

		if (args.length == 2) {
			confFile = args[0];
			type = args[1];
		} else if (args.length == 3) {
			confFile = args[0];
			type = args[1];
			name = args[2];
		}

		ConfigManager conf = new ConfigManager();
		conf.readConfig(confFile);

		if (type.equals("-t")) {
			new Keygen(conf, name);
		} else if (type.equals("-r")) {
			new Repgen(conf, name);
		} else if (type.equals("-win")) {
			new Terminal(confFile);
		}else if (type.equals("-term")) {
			new Terminal(confFile);
		}

		KmshLogger.log("Operations completed");
	}
}
