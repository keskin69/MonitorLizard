package tr.com.telekom.kmsh.config;

import org.w3c.dom.Node;

public abstract class AConfig {
	public boolean valide(String str) {
		return false;
	}

	public abstract void parseXML(Node node);
}
