package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.manager.PeriodicManager;
import tr.com.telekom.kmsh.util.SQLUtil;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

public class PeriodicMonitor {

	public PeriodicMonitor(XMLManager conf) {
		for (GroupCommandConfig grpConf : conf.group) {
			// check last execution time for periodic commands
			if (grpConf.period != -1) {
				if (SQLUtil.getAge(grpConf.name) >= grpConf.period) {
					KmshLogger.log(0, "Processing CommandList " + grpConf.name);
					PeriodicManager keyMan = new PeriodicManager(grpConf);

					String content = keyMan.process(conf);

					// write content to report log
					KmshUtil.writeLog(grpConf.name + ".log", content);

					SQLUtil.writeTag(grpConf.name);
				} else {
					KmshLogger.log(0, "Skipping group commands for "
							+ grpConf.name);
				}
			} else {
				KmshLogger
						.log(0, "Ignoring group commands for " + grpConf.name);
			}
		}
	}
}
