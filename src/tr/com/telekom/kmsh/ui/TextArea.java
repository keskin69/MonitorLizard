package tr.com.telekom.kmsh.ui;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class TextArea extends JTextArea {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CommandManager commander = null;
	protected int pos = 1;
	protected static final int HIST_SIZE = 100;
	protected int curHistory = 0;
	protected String history[] = null;
	protected int lastHistPos = -1;

	protected TextArea(int x, int y) {
		super(x, y);
		setBackground(Color.BLACK);
		setForeground(Color.GREEN);
		setLineWrap(true);
		setWrapStyleWord(true);
		setText(">");
		setCaretColor(Color.BLUE);

		history = new String[HIST_SIZE];
	}

	public void setCommander(String file) {
		commander = new CommandManager(file);
	}

	public String getLastCommand() {
		String line = "";
		try {
			line = getText(pos, getText().length() - pos).trim();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return line;
	}
}
