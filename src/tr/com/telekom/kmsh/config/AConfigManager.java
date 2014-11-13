package tr.com.telekom.kmsh.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public abstract class AConfigManager {
	public final static String DELIM = ";";
	private static final String PASSWORD = "ARECA";

	protected final static String decrypt(String str) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(PASSWORD);

		return encryptor.decrypt(str);
	}

	protected static String encrypt(String in) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(PASSWORD);

		return encryptor.encrypt(in);
	}

	abstract void readConfig(String confFile);
	abstract void readConnections(String confFile);
	abstract void readCommands(String confFile);
}
