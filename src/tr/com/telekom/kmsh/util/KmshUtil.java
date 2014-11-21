package tr.com.telekom.kmsh.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KmshUtil {
	public static final DecimalFormat DecimalFmt = new DecimalFormat("#.##");
	public static void writeLog(String logFile, String content) {
		File file = new File(logFile);

		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String insertFunctionValue(String str) {

		while (str.contains("#")) {
			int i = str.indexOf("#");
			int j = i + 1;

			while (true) {
				if (j == str.length()) {
					break;
				}

				if (!str.substring(j, j + 1).matches("[a-zA-Z0-9_]")) {
					break;
				}

				j++;
			}

			String func = str.substring(i + 1, j);

			String value = null;
			try {
				String commandClass = ConfigReader.getInstance().getProperty(
						"commandClass");
				for (Method method : Class.forName(commandClass)
						.getDeclaredMethods()) {
					if (method.getName().equals(func)) {
						value = (String) method.invoke(
								ConfigReader.getInstance(), null);
						str = str.replace("#" + func, value);
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
				break;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return str;
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static Date convertToDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static Date getCurrentTime() {
		return convertToDate(getCurrentTimeStamp());
	}
}
