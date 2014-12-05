package tr.com.telekom.kmsh.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tr.com.telekom.kmsh.util.SQLUtil;

public class Condition {
	private String commandId = null;
	private String rule = null;

	public Condition(Node node) {
		commandId = ((Element) node).getAttribute("check");
		rule = node.getTextContent();
	}

	public boolean checkRule() {
		String value = SQLUtil.readDB(commandId, "value");
		Pattern r = Pattern.compile(rule);
		Matcher m = r.matcher(value);
		return m.find();
	}
}
