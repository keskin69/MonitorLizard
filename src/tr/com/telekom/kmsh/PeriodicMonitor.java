package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.manager.KeyManager;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

public class PeriodicMonitor {

	public PeriodicMonitor(XMLManager conf, String name) {
		boolean found = false;

		for (GroupCommandConfig keyConf : conf.group) {
			if (keyConf.name.equals(name) || (name.equals(""))) {
				// check last execution time for periodic commands
				if (H2Util.getAge(keyConf.name) >= keyConf.period) {
					KmshLogger.log("Processing KeyList " + name);
					KeyManager keyMan = new KeyManager(keyConf);

					String content = keyMan.process(conf);
					found = true;

					// write content to report log
					KmshUtil.writeLog("log/" + name + ".log", content);

					H2Util.writeDB("", "", keyConf.name);
				}
			}
		}

		if (!found) {
			KmshLogger.log("Cannot find key list definition for " + name);
		}
	}

}
