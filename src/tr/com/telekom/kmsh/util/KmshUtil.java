package tr.com.telekom.kmsh.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KmshUtil {
	public static final DecimalFormat DecimalFmt = new DecimalFormat("#.##");
	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void serialize(String fileName, Object obj) {
		// serialize the List
		try {
			OutputStream file = new FileOutputStream(fileName);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			output.writeObject(obj);
			output.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Object deserialize(String fileName) {
		Object obj = null;

		try {
			InputStream file = new FileInputStream(fileName);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			obj = input.readObject();
			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return obj;
	}

	public static void writeLog(String logFile, String content) {
		ConfigReader conf = ConfigReader.getInstance();
		String base = conf.getProperty("base");
		File f = new File(base + "/log");
		if (!f.exists()) {
			f.mkdir();
		}

		File file = new File(base + "/log/" + logFile);

		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			KmshLogger.log(0, "Writing log file " + file.getAbsolutePath());
			bw.write(content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String strCheck(String str) {
		String out = str.replaceAll("\n", " ");
		out = out.replaceAll("\t", " ");

		out = out.replaceAll("([ ]+)$", "");

		out = out.replaceAll("[ ]+", " ");

		return out;
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

		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static Date convertFullDate(String str) {
		str = str.substring(0, 19);
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
