package com.samisari.cezmi.core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.apache.log4j.Logger;

import com.samisari.cezmi.input.KeyInputListener;

public class ConsoleMouseMotionListener implements MouseMotionListener, KeyInputListener {
	private static final Logger	logger	= Logger.getLogger(ConsoleMouseMotionListener.class);
	private int					x;
	private int					y;

	@Override
	public void mouseDragged(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			double zx = ConsolePropertyManager.getDefaultInstance().getScaleFactor();
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x = (int) (e.getX() / zx) + offset.x;
			y = (int) (e.getY() / zx) + offset.y;
			logger.debug("mouseMoved: " + x + ", " + y);
			command.mouseMoved(x, y);
		}
	}

	@Override
	public void keyUpPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			command.mouseMoved(x, y - 1);
		}

	}

	@Override
	public void keyDownPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			command.mouseMoved(x, y + 1);
		}

	}

	@Override
	public void keyLeftPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			command.mouseMoved(x + 1, y);
		}

	}

	@Override
	public void keyRightPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			command.mouseMoved(x - 1, y);
		}

	}
	
	@Override
	public void keyCommitPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			command.mouseClicked(x, y);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
		
	}

}
