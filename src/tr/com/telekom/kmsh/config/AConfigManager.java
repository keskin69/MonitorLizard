package tr.com.telekom.kmsh.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.w3c.dom.Element;

public abstract class AConfigManager {
	private static final String PASSWORD = "ARECA";

	public final static String decrypt(String str) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(PASSWORD);

		return encryptor.decrypt(str);
	}

	protected static String encrypt(String in) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(PASSWORD);

		return encryptor.encrypt(in);
	}

	public static String readValue(Element e, String field) {
		String value = null;
		try {
			value = e.getElementsByTagName(field).item(0).getTextContent();

			if (value.startsWith("ENC(")) {
				value = decrypt(value.substring(4, value.length() - 1));
			}
		} catch (NullPointerException ex) {
			value = "";
		}

		return value;
	}

	abstract void readConfig(String confFile);

	abstract void readConnections(String confFile);

	abstract void readCommands(String confFile);
}
