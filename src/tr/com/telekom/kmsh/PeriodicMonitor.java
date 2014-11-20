package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.manager.KeyManager;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

public class PeriodicMonitor {

	public PeriodicMonitor(XMLManager conf) {
		for (GroupCommandConfig grpConf : conf.group) {
			// check last execution time for periodic commands
			if (H2Util.getAge(grpConf.name) >= grpConf.period) {
				KmshLogger.log("Processing CommandList " + grpConf.name);
				KeyManager keyMan = new KeyManager(grpConf);

				String content = keyMan.process(conf);

				// write content to report log
				KmshUtil.writeLog("log/" + grpConf.name + ".log", content);

				H2Util.writeTag(grpConf.name);
			} else {
				KmshLogger.log("Skipping group commands for " + grpConf.name);
			}
		}
	}

}
