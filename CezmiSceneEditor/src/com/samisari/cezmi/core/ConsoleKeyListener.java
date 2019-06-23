package com.samisari.cezmi.core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;

import com.samisari.cezmi.input.KeyInputListener;
import com.samisari.cezmi.interpreter.CezmiDialog;

public class ConsoleKeyListener implements KeyInputListener {
	private static final Logger	logger	= Logger.getLogger(ConsoleKeyListener.class);

	private int					x;
	private int					y;

	@Override
	public void keyPressed(KeyEvent e) {
		AbstractCommand pivotCommand = CommandManager.getDeaultInstance().getCommand();
		if (pivotCommand != null) {
			logger.info("Pivot Command:" + pivotCommand.getBounds().toString());
			morphCommand(e, pivotCommand);
		} else {
			menu(e);
		}
	}

	private void menu(KeyEvent e) {

		if ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == KeyEvent.ALT_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_S) {
			new CezmiDialog();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			try {
				AbstractCommand cmd = (AbstractCommand) Class.forName("com.samisari.graphics.commands.CmdSODelete").newInstance();
				cmd.run();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
				logger.error(e);
			}
		}

	}

	private void morphCommand(KeyEvent e, AbstractCommand pivotCommand) {
		Operation operation = ConsolePropertyManager.getDefaultInstance().getActiveOp();
		if (operation != null) {
			// means the command already existed and user manipulating
			switch (operation) {
			case LEFT:
				x = pivotCommand.getX();
				y = pivotCommand.getY() + pivotCommand.getHeight() / 2;
				break;
			case RIGHT:
				x = pivotCommand.getX() + pivotCommand.getWidth();
				y = pivotCommand.getY() + pivotCommand.getHeight() / 2;
				break;
			case TOP:
				x = pivotCommand.getX() + pivotCommand.getWidth() / 2;
				y = pivotCommand.getY();
				break;
			case BOTTOM:
				x = pivotCommand.getX() + pivotCommand.getWidth() / 2;
				y = pivotCommand.getY() + pivotCommand.getHeight();
				break;
			case TOP_LEFT:
				x = pivotCommand.getX();
				y = pivotCommand.getY();
				break;
			case TOP_RIGHT:
				x = pivotCommand.getX() + pivotCommand.getWidth();
				y = pivotCommand.getY();
				break;
			case BOTTOM_LEFT:
				x = pivotCommand.getX();
				y = pivotCommand.getY() + pivotCommand.getHeight();
				break;
			case BOTTOM_RIGHT:
				x = pivotCommand.getX() + pivotCommand.getWidth();
				y = pivotCommand.getY() + pivotCommand.getHeight();
				break;
			case MOVE:
				x = pivotCommand.getX() + pivotCommand.getWidth() / 2;
				y = pivotCommand.getY() + pivotCommand.getHeight() / 2;
				break;
			default:
				break;
			}
		}
		logger.debug("Operation Point:(" + x + "," + y + ")");

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyUpPressed();
			break;
		case KeyEvent.VK_DOWN:
			keyDownPressed();
			break;
		case KeyEvent.VK_LEFT:
			keyLeftPressed();
			break;
		case KeyEvent.VK_RIGHT:
			keyRightPressed();
			break;
		case KeyEvent.VK_ENTER:
			keyCommitPressed();
			break;
		case KeyEvent.VK_DELETE:
			try {
				AbstractCommand cmd = (AbstractCommand) Class.forName("CmdSoDelete").newInstance();
				cmd.run();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
				logger.error(e1);
			}
			break;
		default:
			if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				ConsolePropertyManager.getDefaultInstance().setStepping(e.getKeyCode() - KeyEvent.VK_0 + 1);
			}
			if (e.getKeyCode() >= KeyEvent.VK_NUMPAD0 && e.getKeyCode() <= KeyEvent.VK_NUMPAD9) {
				ConsolePropertyManager.getDefaultInstance().setStepping(e.getKeyCode() - KeyEvent.VK_NUMPAD0 + 1);
			}
		}
	}

	@Override
	public void keyUpPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x -= offset.x;
			y -= offset.y;
			y -= ConsolePropertyManager.getDefaultInstance().getStepping();
			MouseEvent me = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(me);
		}

	}

	@Override
	public void keyCommitPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x -= offset.x;
			y -= offset.y;
			MouseEvent evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
			evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
			evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
			//			mousePressed(evt);
			//			mouseReleased(evt);
			//			mouseClicked(evt);
		}

	}

	@Override
	public void keyDownPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y++;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x -= offset.x;
			y -= offset.y;
			y += ConsolePropertyManager.getDefaultInstance().getStepping();
			MouseEvent evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
		}

	}

	@Override
	public void keyLeftPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			x--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x -= offset.x;
			y -= offset.y;
			x -= ConsolePropertyManager.getDefaultInstance().getStepping();
			MouseEvent evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
		}

	}

	@Override
	public void keyRightPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			x++;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			x -= offset.x;
			y -= offset.y;
			x += ConsolePropertyManager.getDefaultInstance().getStepping();
			MouseEvent evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().dispatchEvent(evt);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing

	}
}
