package tr.com.telekom.kmsh.addon;

import java.util.Date;

import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Reader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;

public class UptimeGenerator implements IAddOn {
	private long uptime = 0;
	private long downtime = 0;

	public UptimeGenerator() {
		ConfigReader conf = ConfigReader.getInstance();
		String cmd = conf.getProperty("statusCmdId");

		Date prevTime = null;
		Date curTime = null;

		String values = H2Reader.readAll(cmd);
		for (String row : values.split("\n")) {
			String col[] = row.split(conf.getProperty("DELIM"));
			String d = col[0];
			curTime = KmshUtil.convertToDate(d);
			if (prevTime == null) {
				prevTime = curTime;
			}

			String val = col[1];
			if (val.contains("running")) {
				uptime += (curTime.getTime() - prevTime.getTime())
						/ (60 * 1000);
			} else {
				downtime += (curTime.getTime() - prevTime.getTime())
						/ (60 * 1000);
			}

			prevTime = curTime;
		}
	}

	public void process() {
		// write results to db
		double upRatio = (uptime * 100D) / (uptime + downtime + 0D);
		H2Util.writeDB("UpTime", "UpTime%", "",
				KmshUtil.DecimalFmt.format(upRatio));

		double downRatio = (downtime * 100D) / (uptime + downtime + 0D);
		H2Util.writeDB("DownTime", "DownTime%", "",
				KmshUtil.DecimalFmt.format(downRatio));

		H2Util.writeDB("TotalDownTime", "Total Down Time ", "",
				KmshUtil.DecimalFmt.format(downtime));

		H2Util.writeDB("TotalUpTime", "Total Up Time ", "",
				KmshUtil.DecimalFmt.format(uptime));
	}
}
