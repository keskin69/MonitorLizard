package tr.com.telekom.kmsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.Table;

public class Monitor {
	static Logger logger = Logger.getLogger(Monitor.class);
	final static String version = "V0.1";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting repgen " + version + " application");
		// check if another instance exists
		if (!checkInstance()) {
			return;
		}

		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure(new NullAppender());

		String confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";
		String type = "-t";
		String name = "";

		if (args.length == 2) {
			confFile = args[0];
			type = args[1];
		} else if (args.length == 3) {
			confFile = args[0];
			type = args[1];
			name = args[2];
		}

		KmshLogger.log(0, "Executing " + type + " " + name);

		ConfigReader.file = confFile;
		XMLManager xmlManager = new XMLManager();

		// TODO
		// H2Util.writeDB("sql1", "", "", "2014-11-29 01:00:00", "");

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
			// Executes commands in on_demand_command list
			Object result = CommandManager.execute(name);
			if (result instanceof String) {
				KmshLogger.log(1, (String) result);
			} else if (result instanceof Table) {
				KmshLogger.log(1, ((Table) result).getString());
			}
		} else if (type.equals("-init")) {
			H2Util.init();
		} else {
			KmshLogger.log(3, "Unknown operation type" + type);
		}

		KmshLogger.log(1, "Operations completed");
	}

	private static boolean checkInstance() {
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("ps -ef | grep repgen.jar");
			p.waitFor();

			BufferedReader b = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
				System.out.println("Application already running");
				if (!line.contains("grep")) {
					return false;
				}
			}

			b.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
