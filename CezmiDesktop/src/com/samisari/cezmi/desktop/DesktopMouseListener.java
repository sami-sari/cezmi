package com.samisari.cezmi.desktop;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.ContextMenuItem;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;
import com.samisari.cezmi.input.KeyInputListener;


public class DesktopMouseListener implements MouseListener, MouseMotionListener, KeyInputListener, MouseWheelListener {
	private static final Logger		logger				= Logger.getLogger(DesktopMouseListener.class);
	private Operation				operation			= Operation.NONE;
	private List<AbstractCommand>	selectedCommands	= new ArrayList<AbstractCommand>();
	private Point					click;
	private AbstractCommand			pivotCommand;
	private String					instance;
	private int						x;
	private int						y;

	public DesktopMouseListener(String key) {
		super();
		this.instance = key;
	}

	public DesktopMouseListener() {
		this("DEFAULT");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		logger.debug(e);
		double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
		Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
		int x = (int) (e.getX() / zx) + offset.x;
		int y = (int) (e.getY() / zx) + offset.y;
		click = new Point(x, y);
		AbstractCommand clickedCmd = null;

		// Bu kod haddini aşıyor
		for (KeyListener l : ConsolePropertyManager.getDefaultInstance().getConsolePanel().getKeyListeners()) {
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().removeKeyListener(l);
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().setFocusable(true);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().grabFocus();

		AbstractCommand command = CommandManager.getInstance(instance).getCommand();
		if (command != null) {
			command.mouseClicked(x, y);
			logger.debug("Command is " + command.getClass().getSimpleName());
		} else {
			selectedCommands = CommandManager.getInstance(instance).getSelectedCommands();
			if (contextMenuClicked(selectedCommands, x, y)) {
				return;
			}
			clickedCmd = CommandManager.getInstance(instance).getSmallestAreaCommandInRange(x, y, ConsolePropertyManager.SELECTION_RADIUS);
			if (operation == Operation.NONE) {
				if (clickedCmd != null) {
					// Clicked on a command and selected command list is not
					// empty check for operations
					for (AbstractCommand cmd : selectedCommands) {
						Rectangle bounds = cmd.getBounds();
						if (cmd.isSelected() && bounds != null) {
							Operation tmp = cmd.getOperation(click);
							logger.debug(tmp);
							if (tmp != null) {
								pivotCommand = cmd;
								operation = tmp;
							}
						}
					}
				}
			} else {
				operation = Operation.NONE;

			}
			if (operation == Operation.NONE && clickedCmd != null) {
				selectObjectAt(x, y);
			} else {
				// Clear selection if click is outside of any selected object
				// bounds
				if (clickedCmd == null) {
					if (selectedCommands != null && selectedCommands.size() > 0) {
						for (AbstractCommand cmd : selectedCommands) {
							cmd.setSelected(false);
						}
						selectedCommands.clear();
						pivotCommand = null;
						click = null;
						operation = Operation.NONE;
					}
				}
			}
		}
		ConsolePropertyManager.getInstance(instance).getConsolePanel().repaint();
		if (clickedCmd != null && operation == Operation.CONTEXT_MENU) {
			clickedCmd.openContextMenu();
			operation = Operation.NONE;
		}

	}

	private boolean contextMenuClicked(List<AbstractCommand> selectedCommands, int x, int y) {
		boolean retval = false;
		for (AbstractCommand c : selectedCommands) {
			if (c.getCurrentStatus() == Status.CONTEXT_MENU) {
				int cy = c.getY();
				int cx = c.getX();
				if (c.getContextMenuItems() != null)
					for (int i = 0; i < c.getContextMenuItems().size(); i++) {
						ContextMenuItem cm = c.getContextMenuItems().get(i);
						if (x - cx > 0 && x - cx < 100 && y - cy - i * 25 > 0 && y - cy - i * 25 < 25) {
							cm.getAction().run();
							retval = true;
						}
					}
				if (c.getCurrentStatus() == Status.CONTEXT_MENU)
					c.setCurrentStatus(Status.START);
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			}
		}

		return retval;
	}

	private void selectObjectAt(int x, int y) {
		AbstractCommand so = CommandManager.getDeaultInstance().getSmallestAreaCommandInRange(x, y, ConsolePropertyManager.SELECTION_RADIUS);
		if (so != null) {
			if (so.isSelected())
				so.setSelected(false);
			else
				so.setSelected(true);
			CommandManager.getDeaultInstance().fireCommandSelectionChanged(so);
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AbstractCommand cmd = CommandManager.getInstance(instance).getCommand();
		if (cmd != null)
			CommandManager.getInstance(instance).getCommand().mousePressed(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		logger.debug("MOUSE RELEASED");
		AbstractCommand cmd = CommandManager.getInstance(instance).getCommand();
		if (operation == Operation.CONTEXT_MENU) {
			if (cmd != null)
				cmd.openContextMenu();
			operation = Operation.NONE;
		} else if (cmd != null)
			cmd.mouseReleased(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		AbstractCommand cmd = CommandManager.getInstance(instance).getCommand();
		if (cmd != null) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x = (int) (mouseEvent.getX() / zx) + offset.x;
			y = (int) (mouseEvent.getY() / zx) + offset.y;

			logger.debug("mouseMoved: " + x + ", " + y);
			cmd.mouseDragged(x, y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
		logger.debug(evt);
		AbstractCommand command = CommandManager.getInstance(instance).getCommand();
		if (command != null) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x = (int) (evt.getX() / zx) + offset.x;
			y = (int) (evt.getY() / zx) + offset.y;

			logger.debug("mouseMoved: " + x + ", " + y);
			command.mouseMoved(x, y);
		}
		if (operation != null && !operation.equals(Operation.NONE) && selectedCommands != null && selectedCommands.size() > 0) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x = (int) (evt.getX() / zx) + offset.x;
			y = (int) (evt.getY() / zx) + offset.y;

			int dx = 0, dy = 0;

			switch (operation) {
			case LEFT:
			case TOP_LEFT:
			case BOTTOM_LEFT:
				dx = x - pivotCommand.getBounds().x;
				break;
			case MOVE:
				dx = x - (pivotCommand.getBounds().x + (pivotCommand.getBounds().width / 2));
				break;
			case RIGHT:
			case TOP_RIGHT:
			case BOTTOM_RIGHT:
				dx = x - (pivotCommand.getBounds().x + (pivotCommand.getBounds().width));
				break;
			default:
			}
			switch (operation) {
			case TOP:
			case TOP_LEFT:
			case TOP_RIGHT:
				dy = y - pivotCommand.getBounds().y;
				break;
			case MOVE:
				dy = y - (pivotCommand.getBounds().y + (pivotCommand.getBounds().height / 2));
				break;
			case BOTTOM:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				dy = y - (pivotCommand.getBounds().y + (pivotCommand.getBounds().height));
				break;
			default:
			}
			if (operation.equals(Operation.MOVE_CONNECTOR_EDGE)) {
				pivotCommand.move(operation, x, y);
			} else
				for (AbstractCommand cmd : selectedCommands) {
					logger.debug("MOVING x:" + cmd.getBounds().x);
					cmd.move(operation, dx, dy);
				}
			ConsolePropertyManager.getInstance(instance).getConsolePanel().repaint();
		}

	}

	@Override
	public void keyUpPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x -= offset.x;
			y -= offset.y + stepping;
			mouseMoved(new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
		}

	}

	@Override
	public void keyCommitPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x -= offset.x;
			y -= offset.y;
			MouseEvent evt = new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, 0, false);
			mousePressed(evt);
			mouseReleased(evt);
			mouseClicked(evt);
		}

	}

	@Override
	public void keyDownPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			y++;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x -= offset.x;
			y -= offset.y - stepping;
			mouseMoved(new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
		}

	}

	@Override
	public void keyLeftPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			x--;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x -= offset.x + stepping;
			y -= offset.y;
			mouseMoved(new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
		}

	}

	@Override
	public void keyRightPressed() {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command != null) {
			x++;
			command.mouseMoved(x, y);
		} else {
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x -= offset.x - stepping;
			y -= offset.y;
			mouseMoved(new MouseEvent(ConsolePropertyManager.getDefaultInstance().getConsolePanel(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing

	}

	private int stepping = 1;

	@Override
	public void keyPressed(KeyEvent e) {
		if (operation != null) {
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
		default:
			if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				stepping = e.getKeyCode() - KeyEvent.VK_0 +1;
			}
			if (e.getKeyCode() >= KeyEvent.VK_NUMPAD0 && e.getKeyCode() <= KeyEvent.VK_NUMPAD9) {
				stepping = e.getKeyCode() - KeyEvent.VK_NUMPAD0 + 1;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int ticks = e.getWheelRotation();
		if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK) {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			offset.setLocation(offset.x, offset.y + ticks * 100);
			ConsolePropertyManager.getDefaultInstance().setOffset(offset);
		} else if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			offset.setLocation(offset.x + ticks * 100, offset.y);
			ConsolePropertyManager.getDefaultInstance().setOffset(offset);
		} else {
			double scale = ConsolePropertyManager.getDefaultInstance().getScaleFactor();
			scale += 0.10 * ticks;
			ConsolePropertyManager.getDefaultInstance().setScaleFactor(scale);
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();

	}

}
