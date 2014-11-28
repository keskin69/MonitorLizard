package tr.com.telekom.kmsh.util;

public class KmshLogger {
	private static final int LEVEL = 0;

	public static void log(int level, String str) {
		if (level >= LEVEL) {
			System.out.println(KmshUtil.getCurrentTimeStamp(0) + ": " + str);
		}
	}
}
