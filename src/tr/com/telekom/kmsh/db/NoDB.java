package tr.com.telekom.kmsh.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NoDB {
	private static Map<String, String> content = null;
	private static final String FS = "Œ";
	private static final String NL = "Ï";

	// a text based persistance data handler
	public static void set(String key, String value) {
		content = new HashMap<String, String>();
		read();
		content.put(key, value);
		write();
	}

	public static String get(String key) {
		content = new HashMap<String, String>();
		read();
		String s = content.get(key);

		if (s == null) {
			s = "";
		}

		return s;
	}

	private static void write() {
		File file = new File("log/areca.db");

		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (String key : content.keySet()) {
				bw.write(key + FS + content.get(key) + NL);
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void read() {
		BufferedReader br = null;
		String s = "";

		try {
			br = new BufferedReader(new FileReader("log/areca.db"));
			String line = null;

			while ((line = br.readLine()) != null) {
				s += line;
			}
		} catch (Exception e) {
		}

		String lines[] = s.split(NL);
		for (String line : lines) {
			String fields[] = line.split(FS);
			if (fields.length == 2) {
				content.put(fields[0], fields[1]);
			}
		}

		try {
			br.close();
		} catch (Exception e) {

		}
	}
}
