package tr.com.telekom.kmsh;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import tr.com.telekom.kmsh.config.ConfigManager;

public class Monitor {
	static Logger logger = Logger.getLogger(Monitor.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure(new NullAppender());

		String arg = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/config.xml";
		String type = "-t";
		String name = "key1";

		if (args.length == 3) {
			arg = args[0];
			type = args[1];
			name = args[2];
		}

		ConfigManager conf = new ConfigManager();
		conf.readConfig(arg);

		if (type.equals("-t")) {
			new Keygen(conf, name);
		} else if (type.equals("-r")) {
			new Repgen(conf, name);
		}

	}
}
