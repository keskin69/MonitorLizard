package tr.com.telekom.kmsh.config;

public class Key {
	public String name = null;
	public String grep = null;
	public String delim = null;
	public String field = null;

	public Key(String name, String grep, String delim, String fld) {
		this.name = name;
		this.grep = grep;
		this.delim = delim;
		field = fld;
	}
}
