package tr.com.telekom.kmsh.manager;

import tr.com.telekom.kmsh.config.ConfigManager;
import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.config.Key;
import tr.com.telekom.kmsh.config.KeyConfig;
import tr.com.telekom.kmsh.util.H2Manager;

public class KeyManager {
	private KeyConfig keyConf = null;

	public KeyManager(KeyConfig keyConf) {
		this.keyConf = keyConf;
	}

	public void process(ConfigManager conf) {
		// execute all commands
		ConnectionConfig connection = conf.findConnection(keyConf.connectBy);
		if (connection != null) {
			for (Key key : keyConf.keyList) {
				String command = keyConf.base + " " + key.grep + " | tail -1";
				if (key.delim != null) {
					command += "| cut -d\"" + key.delim + "\"" + " -f "
							+ key.field;
				}

				String result = SSHManager.executeCommand(connection, command);
				H2Manager.writeDB(key.name, result);
				// System.out.println(command + "\n" + result);
			}
		}
	}

}
