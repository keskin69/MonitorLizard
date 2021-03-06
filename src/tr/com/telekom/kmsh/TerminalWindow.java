package tr.com.telekom.kmsh;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.ScrollPaneConstants;

import tr.com.telekom.kmsh.ui.CommandArea;

public class TerminalWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7515622399486840967L;

	private CommandArea txaEditor = null;

	public TerminalWindow(String configFile) {
		super("M# Commander V0.1");

		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);

		JPanel pnlEditor = new JPanel();
		getContentPane().add(pnlEditor, BorderLayout.CENTER);

		txaEditor = new CommandArea(20, 50);
		txaEditor.setCommander(configFile);

		JScrollPane scrollPane = new JScrollPane(txaEditor);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		pack();
		this.setVisible(true);
	}
}
