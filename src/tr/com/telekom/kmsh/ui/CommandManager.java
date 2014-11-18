package tr.com.telekom.kmsh.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;

public class CommandManager {
	private ConnectionConfig conn = null;
	private ArrayList<ConnectionConfig> connectionList = null;
	private String commandClass = null;
	private Map<String, String> variables = null;

	public CommandManager(String configFile) {
		XMLManager conf = new XMLManager();
		conf.readConfig(configFile);
		commandClass = conf.reportList.get(0).commandClass;
		connectionList = conf.connectionList;

		variables = new HashMap<String, String>();
	}

	protected String parse(String cmd, String arg) {
		String out = "\n";

		if (cmd.equals("connect")) {
			// make a new connection or display connections
			out += cmdConnect(arg);
		} else if (cmd.equals("exec")) {
			// execute a command
			out += cmdExecute(arg);
		} else if (cmd.equals("set")) {
			// define or modify a new variable
			out += cmdSet(arg);
		} else if (cmd.equals("exit")) {
			// exit system
			System.exit(0);
		} else if (cmd.equals("print")) {
			// print a variable
			out += variables.get(arg);
		} else if (!cmd.equals("")) {
			out += "Unkown command";
		}

		return out;
	}

	private String cmdConnect(String arg) {
		String result = "";

		if (arg == null) {
			int ctr = 0;
			for (ConnectionConfig c : connectionList) {
				ctr++;
				result += c.name + "(" + c.host + ")";
				if (conn != null) {
					if (conn.name.equals(c.name)) {
						result += " *";
					}
				}

				if (ctr < connectionList.size() - 1) {
					result += "\n";
				}
			}
		} else {
			for (ConnectionConfig c : connectionList) {
				if (c.name.equals(arg)) {
					result += "Connection set to " + c.name + "(" + c.host
							+ ")";
					conn = c;
				}
			}
		}

		return result;
	}

	private String cmdSet(String arg) {
		String result = "";

		if (arg != null) {
			if (arg.startsWith("#")) {
				String str[] = arg.split(" ", 2);
				String varName = str[0];
				String value = str[1];

				value = insertVariables(value);

				variables.put(varName, value);
			} else {
				result = "Variable names should start with a #";
			}
		} else {
			result = "Wrong command";
		}

		return result;
	}

	private String cmdExecute(String arg) {
		String result = null;

		if (arg != null) {
			if (conn == null) {
				result = "No active connection";
			} else {
				arg = insertVariables(arg);
				result = conn.execute(arg);
			}
		} else {
			result = "Wrong command";
		}

		return result;
	}

	private String insertVariables(String str) {
		while (str.contains("#")) {
			int i = str.indexOf("#");
			int j = i + 1;

			while (true) {
				if (j == str.length()) {
					break;
				}

				if (!str.substring(j, j + 1).matches("[a-zA-Z0-9_]")) {
					break;
				}

				j++;
			}

			String func = str.substring(i + 1, j);
			String value = null;

			// first check in local variables
			if (variables.get(func) != null) {
				value = variables.get(func);
				str = str.replace("#" + func, value);
			} else {
				// check defined java methods
				value = executeMethod(func);

				if (value != null) {
					str = str.replace("#" + func, value);
				} else {
					str = str.replace("#" + func, "?");
				}
			}
		}

		return str;
	}

	private String executeMethod(String funcName) {

		try {
			for (Method method : Class.forName(commandClass)
					.getDeclaredMethods()) {
				if (method.getName().equals(funcName)) {
					return (String) method.invoke(this, null);
				}
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
