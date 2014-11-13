package tr.com.telekom.kmsh.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CommandArea extends TextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandArea(int x, int y) {
		super(x, y);

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (pos > getCaretPosition()) {
					e.consume();
					setCaretPosition(getText().length());
					return;
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					String line = getLastCommand();

					if (!line.equals("")) {
						// execute and put the result
						int i = line.indexOf(" ");
						String arg = null;
						String cmd = null;

						if (i > 0) {
							cmd = line.substring(0, i).trim();
							arg = line.substring(i).trim();
						} else {
							cmd = line;
						}

						append(commander.parse(cmd.toLowerCase(), arg));

						addHistory(line);
						curHistory = lastHistPos;
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					e.consume();
					setHistory(-1);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.consume();
					setHistory(1);
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					append(">");
					pos = getText().length();
				}
			}

			public void keyTyped(KeyEvent e) {
				if (pos > getCaretPosition()) {
					e.consume();
				}
			}
		});
	}

	protected void addHistory(String cmd) {
		lastHistPos++;

		if (lastHistPos == HIST_SIZE) {
			lastHistPos = 0;
		}

		history[lastHistPos] = cmd;
	}

	protected void setHistory(int offSet) {
		if ((curHistory + offSet >= 0) && (curHistory + offSet <= lastHistPos)) {
			curHistory += offSet;

			// erase current line
			replaceRange("", pos, getText().length());

			// add history line
			append(history[curHistory]);
		}
	}

}
