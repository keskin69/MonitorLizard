package tr.com.telekom.kmsh.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ConfigReader config = null;
	public static String file = null;

	public static ConfigReader getInstance() {
		if (config == null) {
			config = new ConfigReader();
		}

		return config;
	}

	public int getInt(String key) {
		return new Integer(getProperty(key)).intValue();
	}

	public String getProperty(String key) {
		String out = super.getProperty(key);

		if (out == null) {
			KmshLogger
					.log(4, key + " not defined in configuration file" + file);
		}

		return out;
	}

	private ConfigReader() {
		InputStream input = null;

		try {

			input = new FileInputStream(file);

			// load a properties file
			load(input);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
