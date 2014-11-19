package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.Key;
import tr.com.telekom.kmsh.config.KeyConfig;
import tr.com.telekom.kmsh.util.H2Util;

public class KeyManager {
	private KeyConfig keyConf = null;

	public KeyManager(KeyConfig keyConf) {
		this.keyConf = keyConf;
	}

	public String process(XMLManager conf) {
		String out = "";

		// execute all commands
		ConnectionConfig connection = conf.findConnection(keyConf.connectBy);
		if (connection != null) {
			for (Key key : keyConf.keyList) {
				String command = "";

				if (keyConf.base == null) {
					command = key.command;
				} else {
					command = keyConf.base + " \"" + key.command + "\" | tail -1";
				}

				if (!key.delim.equals("")) {
					command += "| cut -d\"" + key.delim + "\"" + " -f "
							+ key.field;
				}

				String result = SSHManager.executeCommand(connection, command);
				result = result.trim();

				if (!result.equals("")) {
					H2Util.writeDB(key.name, result);
					out += key.command + "=" + result + ";";
				}
			}
		}

		return out;
	}
}
