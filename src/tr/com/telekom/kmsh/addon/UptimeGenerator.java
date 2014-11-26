package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;

public class UptimeGenerator extends AAddOn {
	private long uptime = 0L;
	private long downtime = 0L;
	private Date prevTime = null;
	private Date curTime = null;

	public void processRow(ResultSet rs) throws SQLException {
		String d = rs.getString(1);
		curTime = KmshUtil.convertToDate(d);
		if (prevTime == null) {
			prevTime = curTime;
		}

		String val = rs.getString(2);
		if (val.contains("running")) {
			uptime += (curTime.getTime() - prevTime.getTime()) / (60 * 1000);
		} else {
			downtime += (curTime.getTime() - prevTime.getTime()) / (60 * 1000);
		}

		prevTime = curTime;
	}

	public void process(String cmdId) {
		cmdId = conf.getProperty("statusCmdId");
		String sql = "select date, value from tblKey where id='" + cmdId
				+ "' order by date asc";
		readAll(sql);

		// write results to db
		double upRatio = (uptime * 100D) / (uptime + downtime + 0D);
		String value = KmshUtil.DecimalFmt.format(upRatio);
		H2Util.writeDB("UpTime", "UpTime%", "", value);

		double downRatio = (downtime * 100D) / (uptime + downtime + 0D);
		value = KmshUtil.DecimalFmt.format(downRatio);
		H2Util.writeDB("DownTime", "DownTime%", "", value);

		value = KmshUtil.DecimalFmt.format(downtime);
		H2Util.writeDB("TotalDownTime", "Total Down Time", "", value);

		value = KmshUtil.DecimalFmt.format(uptime);
		H2Util.writeDB("TotalUpTime", "Total Up Time", "", value);
	}
}
