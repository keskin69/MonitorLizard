package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.KeyConfig;
import tr.com.telekom.kmsh.manager.KeyManager;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

public class Keygen {

	public Keygen(XMLManager conf, String name) {
		boolean found = false;

		for (KeyConfig keyConf : conf.keyList) {
			if (keyConf.name.equals(name)) {
				KmshLogger.log("Processing KeyList " + name);
				KeyManager keyMan = new KeyManager(keyConf);

				String content = keyMan.process(conf);
				found = true;

				// write content to report log
				KmshUtil.writeLog("log/" + name + ".log", content);
			}
		}

		if (!found) {
			KmshLogger.log("Cannot find key list definition for " + name);
		}
	}

}
