package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public class CDRDelayReport extends AAddOn {
	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new CDRDelayReport().process("CDRDelay");
	}

	public void processRow(ResultSet rs) throws SQLException {
	}

	public String process(String cmdId) {
		String out = null;

		Object obj = CommandManager.execute(cmdId);
		KmshUtil.serialize("cdr_delay.srl", obj);
		// Object obj = KmshUtil.deserialize("out.srl");
		long totalDelay = 0;
		int total = 0;
		long min = Long.MAX_VALUE;
		long max = 0;

		if (obj instanceof Table) {
			Table tbl = (Table) obj;
			String n = null;
			String t = null;
			Date nn = null;
			Date tt = null;
			total = tbl.size();

			for (int i = 1; i < tbl.size(); i++) {
				@SuppressWarnings("unchecked")
				ArrayList<String> row = tbl.get(i);
				n = row.get(0);
				t = row.get(1);

				nn = KmshUtil.convertFullDate(n);
				tt = KmshUtil.convertFullDate(t);
				long delay = (nn.getTime() - tt.getTime()) / (60 * 1000);

				if (delay < min) {
					min = delay;
				}

				if (delay > max) {
					max = delay;
				}

				totalDelay += delay;
			}

			H2Util.writeDB("ToplamBildirim", "Günlük Toplam Bildirim", "",
					new Integer(total).toString());
			H2Util.writeDB("MinBildirim", "En hızlı bildirim zamanı", "",
					new Long(min).toString());
			H2Util.writeDB("MaxBildirim", "En geç bildirim zamanı", "",
					new Long(max).toString());
			H2Util.writeDB("AveBildirim", "Ortalama bildirim zamanı", "",
					new Long(totalDelay / total).toString());

			out = "Ave. notif:" + new Long(totalDelay / total).toString();
		}

		return out;
	}
}
