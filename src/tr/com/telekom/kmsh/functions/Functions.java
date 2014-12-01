package tr.com.telekom.kmsh.functions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tr.com.telekom.kmsh.util.KmshUtil;

public class Functions {
	public static String yesterday() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return dateFormat.format(cal.getTime()).toUpperCase();
	}

	public static String today() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);

		return dateFormat.format(cal.getTime()).toUpperCase();
	}

	public static String donem() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);

		return dateFormat.format(cal.getTime());
	}

	public static String prevDonem() {
		// Bir gun onceye ait donem bilgisi
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return KmshUtil.getDonem(cal.getTime());
	}
}