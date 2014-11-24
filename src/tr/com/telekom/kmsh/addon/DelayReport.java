package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import tr.com.telekom.kmsh.functions.Functions;
import tr.com.telekom.kmsh.util.KmshUtil;

public class DelayReport extends AAddOn {

	public DelayReport() {
		String today = Functions.today();
		String sql = "select notification_date, triggering_event_date from arc_abone_bildirim where notification_date like '"
				+ today + "%';";
		readAll(sql);
	}

	public void processRow(ResultSet rs) throws SQLException {
		// 24-NOV-14 10.12.03.668000000 AM
		String n = rs.getString(1);
		String t = rs.getString(2);

		System.out.println(n.substring(13));
		Date nn = KmshUtil.convertFullDate(n);
	}

	public void process() {

	}
}
