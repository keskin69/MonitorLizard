package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.Table;

public class FileTransferDelay extends AAddOn {
	public static void main(String[] args) {
		ConfigReader.file = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		new FileTransferDelay().process("FileDelay");
	}

	@Override
	public String process(String cmdId) {
		String out = "";

		Object obj = CommandManager.execute(cmdId);
		// KmshUtil.serialize("./log/file_delay.srl", obj);
		// Object obj = KmshUtil.deserialize("./log/file_delay.srl");

		if (obj instanceof Table) {
			Table tbl = (Table) obj;

			String n = null;
			String t = null;
			Date nn = null;
			Date tt = null;
			int min = Integer.MAX_VALUE;
			int max = 0;
			int total = tbl.size();
			int totalDelay = 0;

			for (int i = 1; i < tbl.size(); i++) {
				@SuppressWarnings("unchecked")
				ArrayList<String> row = tbl.get(i);
				n = row.get(1);
				t = row.get(0);

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

				totalDelay += delay;
			}

			out = new Long(totalDelay / total).toString();

			H2Util.writeDB("ToplamDosya", "Günlük İşlenen Dosya", "",
					new Integer(total).toString());
			H2Util.writeDB("MinDosya", "En hızlı dosya işleme (Dakika)", "",
					new Integer(min).toString());
			H2Util.writeDB("MaxDosya",
					"En yavaş dosya işleme zamanı (Dakika)", "", new Integer(
							max).toString());
			H2Util.writeDB("AveDosya",
					"Ortalama dosya işleme zamanı (Dakika)", "", new Integer(
							totalDelay / total).toString());
		}

		return out;
	}

	@Override
	public void processH2Row(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
	}

}
