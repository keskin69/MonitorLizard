package tr.com.telekom.kmsh.config;

import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Node;

import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.SQLUtil;

public abstract class AConfig {
	public String name = null;
	public int period = -1;

	public boolean valide(String str) {
		return false;
	}

	public abstract void parseXML(Node node);

	public boolean canExecute(ArrayList<String> runAt) {
		if (period != -1) {
			// period specified => Check age
			if (SQLUtil.getAge(name) >= period) {
				return true;
			} else {
				KmshLogger.log(0, "Skipping group command execution for "
						+ name);
			}
		} else {
			// period not specified check for execution time
			if (runAt.size() > 0) {
				Calendar cal = Calendar.getInstance();
				int n = cal.get(Calendar.HOUR_OF_DAY) * 60
						+ cal.get(Calendar.MINUTE);

				for (String s : runAt) {
					String str[] = s.split(":");
					int time = new Integer(str[0]).intValue() * 60
							+ new Integer(str[1]).intValue();

					if (Math.abs(time - n) <= 5) {
						return true;
					}
				}

				KmshLogger.log(0, "Skipping group command execution for "
						+ name);
			} else {
				KmshLogger.log(0, "Ignoring group command execution for "
						+ name);
			}
		}

		return false;
	}
}
