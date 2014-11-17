package tr.com.telekom.kmsh.util;

public class KmshLogger {

	public static void log(String str) {

		System.out.println(KmshUtil.getCurrentTimeStamp() + ": " + str);
	}
}
