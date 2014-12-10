package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.SQLUtil;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public class NotifDelayReport extends AAddOn {
	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new NotifDelayReport().process("NotifDelay");
	}

	public void processH2Row(ResultSet rs) throws SQLException {
	}

	public String process(String cmdId) {
		String out = null;

		Object obj = CommandManager.execute(cmdId);
		// KmshUtil.serialize("./log/notif_delay.srl", obj);
		// Object obj = KmshUtil.deserialize("./log/notif_delay.srl");

		if (obj instanceof Table) {
			Table tbl = (Table) obj;
			String n = null;
			String t = null;
			Date nn = null;
			Date tt = null;
			int total = tbl.size();
			String type = null;
			int not80 = 0;
			int not100 = 0;
			int totalDelay = 0;
			int min = Integer.MAX_VALUE;
			int max = 0;
			String type2 = "";
			int kmsh = 0;
			int fus = 0;
			String stat;
			int del0 = 0;
			int del1 = 0;
			int del6 = 0;

			for (int i = 1; i < tbl.size(); i++) {
				@SuppressWarnings("unchecked")
				ArrayList<String> row = tbl.get(i);
				n = row.get(0);
				t = row.get(1);
				type = row.get(2);
				type2 = row.get(3);
				stat = row.get(4);

				nn = KmshUtil.convertFullDate(n);
				tt = KmshUtil.convertFullDate(t);
				int delay = (int) ((nn.getTime() - tt.getTime()) / (60 * 1000));

				if (nn.getTime() > tt.getTime()) {
					if (delay < min) {
						min = delay;
					}

				}

				if (delay > max) {
					max = delay;
				}

				if (type.equals("YUZDE80")) {
					not80++;
				} else if (type.equals("YUZDE100")) {
					not100++;
				}

				if (type2.equals("KMSH")) {
					kmsh++;
				} else {
					fus++;
				}

				if (stat.equals("0")) {
					del0++;
				} else if (stat.equals("1")) {
					del1++;
				} else if (stat.equals("6")) {
					del6++;
				}

				totalDelay += delay;
			}

			SQLUtil.writeDB("ToplamBildirim", "Günlük Toplam Bildirim", "",
					new Integer(total).toString());
			SQLUtil.writeDB("MinBildirim",
					"En hızlı bildirim zamanı  (Dakika)", "",
					new Integer(min).toString());
			SQLUtil.writeDB("MaxBildirim", "En geç bildirim zamanı (Dakika)",
					"", new Integer(max).toString());
			SQLUtil.writeDB("AveBildirim",
					"Ortalama bildirim zamanı  (Dakika)", "", new Integer(
							totalDelay / total).toString());
			SQLUtil.writeDB("KMSH80", "%80 KMSH Bildirim adedi", "",
					new Integer(not80).toString());
			SQLUtil.writeDB("KMSH100", "%100 KMSH Bildirim adedi", "",
					new Integer(not100).toString());
			SQLUtil.writeDB("KMSH", "Toplam KMSH Bildirim adedi", "",
					new Integer(kmsh).toString());
			SQLUtil.writeDB("FUS", "Toplam FÜS Bildirim adedi", "",
					new Integer(fus).toString());

			SQLUtil.writeDB("DeliveryPending", "İletilmeyen Bildrim", "",
					new Integer(del0).toString());

			SQLUtil.writeDB("DeliveryCompleted", "İletilen Bildrim", "",
					new Integer(del1).toString());

			SQLUtil.writeDB("DeliveryCancelled",
					"İletimi İptal Edilen Bildrim", "",
					new Integer(del6).toString());

			out = new Long(totalDelay / total).toString();
		}

		return out;
	}
}
