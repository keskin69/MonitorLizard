package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.ConfigManager;
import tr.com.telekom.kmsh.config.KeyConfig;
import tr.com.telekom.kmsh.manager.KeyManager;

public class Keygen {

	public Keygen(ConfigManager conf, String name) {
		boolean found = false;

		for (KeyConfig keyConf : conf.keyList) {
			if (keyConf.name.equals(name)) {
				KeyManager keyMan = new KeyManager(keyConf);

				keyMan.process(conf);
				found = true;
			}
		}

		if (!found) {
			System.out.println("Cannot find key list definition for " + name);
		}
	}

}
