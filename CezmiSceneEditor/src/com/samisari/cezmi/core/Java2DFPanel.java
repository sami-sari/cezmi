package com.samisari.cezmi.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Comparator;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class Java2DFPanel extends JPanel implements FocusListener {
	private static final Logger	logger				= Logger.getLogger(Java2DFPanel.class);
	private static final long	serialVersionUID	= 1L;

	public Java2DFPanel() {
		super();
		setBackground(Color.WHITE);
		setFocusable(true);
		addFocusListener(this);

	}

	public void paintComponent(Graphics g) {
		// call superclass's paintComponent
		super.paintComponent(g);
		g.setPaintMode();

		// Draw Console
		g.setColor(Color.white);
		g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);


		// Paint console objects
		paintConsoleObjects(g);

		// paintContextMenu(g);
		ConsolePropertyManager.getDefaultInstance().paint(g);

	}

	private void paintConsoleObjects(Graphics g) {
		AbstractCommand currentCommand = CommandManager.getDeaultInstance().getCommand();
		CommandManager.getDeaultInstance().getHistory().sort(new Comparator<AbstractCommand>() {

			@Override
			public int compare(AbstractCommand o1, AbstractCommand o2) {
				if (o1.getZ() > o2.getZ())
					return 1;
				else if (o1.getZ() < o2.getZ())
					return -1;
				return 0;
			}
		});
		for (AbstractCommand c : CommandManager.getDeaultInstance().getHistory()) {
			c.paint(g);
		}
		if (currentCommand != null)
			currentCommand.paint(g);

	}

	// private void paintContextMenu(Graphics g) {
	// AbstractCommand currentCommand =
	// CommandManager.getInstance().getCommand();
	// if (currentCommand != null)
	// currentCommand.paint(g);
	//
	// Iterator<AbstractCommand> iter =
	// CommandManager.getInstance().getHistory().iterator();
	// while (iter.hasNext()) {
	// AbstractCommand cmd = (AbstractCommand) iter.next();
	// if (cmd.getCurrentStatus() == Status.CONTEXT_MENU)
	// cmd.paintContextMenu(g);
	// }
	//
	// }

	@Override
	public void focusGained(FocusEvent e) {
		if (CommandManager.getDeaultInstance().getCommand() != null)
			logger.debug("Focus Gained: " + CommandManager.getDeaultInstance().getCommand().getClass().getSimpleName());
		else
			logger.debug("Focus Gained: ");
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (CommandManager.getDeaultInstance().getCommand() != null)
			logger.debug("Focus Lost: " + CommandManager.getDeaultInstance().getCommand().getClass().getSimpleName());
		else
			logger.debug("Focus Lost: ");
	}

}
