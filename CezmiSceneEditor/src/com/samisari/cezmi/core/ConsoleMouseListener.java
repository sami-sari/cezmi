package com.samisari.cezmi.core;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ConsoleMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final Logger		logger				= Logger.getLogger(ConsoleMouseListener.class);
	private Operation				operation			= Operation.NONE;
	private List<AbstractCommand>	selectedCommands	= new ArrayList<>();
	//	private Point					click;
	private AbstractCommand			pivotCommand;
	private String					instance;
	private int						x;
	private int						y;

	public ConsoleMouseListener(String key) {
		super();
		this.instance = key;
	}

	public ConsoleMouseListener() {
		this("DEFAULT");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		List<KeyListener> keyListeners = new ArrayList<>();
		logger.debug(e);
		Point click = fixEventPoint(e);
		x = click.x;
		y = click.y;

		AbstractCommand clickedCmd = null;
		try {
			for (KeyListener l : ConsolePropertyManager.getDefaultInstance().getConsolePanel().getKeyListeners()) {
				keyListeners.add(l);
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().removeKeyListener(l);
			}
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().setFocusable(true);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().addKeyListener(new ConsoleKeyListener());
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
									ConsolePropertyManager.getDefaultInstance().setActiveCommand(cmd);
									ConsolePropertyManager.getDefaultInstance().setActiveOp(tmp);
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
					if (clickedCmd.isCaptureMouse()) {
						clickedCmd.mouseClicked(x, y);
					} else {
						selectObjectAt(x, y);
					}
				} else {
					// Clear selection if click is outside of any selected object
					// bounds
					if (clickedCmd == null) {
						if (selectedCommands != null && selectedCommands.size() > 0) {
							for (AbstractCommand cmd : selectedCommands) {
								cmd.setSelected(false);
							}
							selectedCommands.clear();
							ConsolePropertyManager.getDefaultInstance().setActiveCommand(null);
							ConsolePropertyManager.getDefaultInstance().setActiveOp(Operation.NONE);
							pivotCommand = null;
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
		} finally {
			for (KeyListener l : keyListeners) {
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().addKeyListener(l);
			}
		}

	}

	private boolean contextMenuClicked(List<AbstractCommand> selectedCommands, int x, int y) {
		boolean retval = false;
		for (AbstractCommand c : selectedCommands) {
			if (c.getCurrentStatus() == Status.CONTEXT_MENU) {
				List<ContextMenuItem> cmi = c.getContextMenuItems();
				int cy = c.getY();
				int cx = c.getX();
				if (cmi != null)
					for (int i = 0; i < cmi.size(); i++) {
						ContextMenuItem cm = cmi.get(i);
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
		if (cmd != null) {
			Point p = fixEventPoint(e);
			cmd.mousePressed(p.x, p.y);
		}
	}

	Point fixEventPoint(MouseEvent e) {
		double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
		Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
		x = (int) (e.getX() / zx) + offset.x;
		y = (int) (e.getY() / zx) + offset.y;
		return new Point(x, y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AbstractCommand cmd = CommandManager.getInstance(instance).getCommand();
		if (operation == Operation.CONTEXT_MENU) {
			if (cmd != null)
				cmd.openContextMenu();
			operation = Operation.NONE;
		} else if (cmd != null) {
			Point point = fixEventPoint(e);
			cmd.mouseReleased(point.x, point.y);
		}
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		AbstractCommand cmd = CommandManager.getInstance(instance).getCommand();
		if (cmd != null) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			x = (int) (mouseEvent.getX() / zx);
			y = (int) (mouseEvent.getY() / zx);

			logger.debug("mouseMoved: " + x + ", " + y);
			cmd.mouseDragged(x, y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
		AbstractCommand command = CommandManager.getInstance(instance).getCommand();
		if (command != null) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x = (int) (evt.getX() / zx) + offset.x;
			y = (int) (evt.getY() / zx) + offset.y;

			logger.debug(command.getClass() + " mouseMoved: " + x + ", " + y);
			command.mouseMoved(x, y);
		}
		if (operation != null && !operation.equals(Operation.NONE) && selectedCommands != null && selectedCommands.size() > 0) {
			double zx = ConsolePropertyManager.getInstance(instance).getScaleFactor();
			Point offset = ConsolePropertyManager.getInstance(instance).getOffset();
			x = (int) (evt.getX() / zx) + offset.x;
			y = (int) (evt.getY() / zx) + offset.y;
			Rectangle transformedBounds = pivotCommand.getBounds();
			if (pivotCommand.getTransform() != null) {
				Point clck = new Point(x, y);
				Point trn = new Point();
				try {
					pivotCommand.getTransform().inverseTransform(clck, trn);
				} catch (NoninvertibleTransformException e) {
					logger.error(e);
				}
				x = trn.x;
				y = trn.y;
				logger.debug(clck.x + "," + clck.y + "->" + trn.x + "," + trn.y);
			}

			int dx = 0, dy = 0;

			switch (operation) {
			case LEFT:
			case TOP_LEFT:
			case BOTTOM_LEFT:
				dx = x - transformedBounds.x;
				break;
			case MOVE:
				dx = x - (transformedBounds.x + (transformedBounds.width / 2));
				break;
			case RIGHT:
			case TOP_RIGHT:
			case BOTTOM_RIGHT:
				dx = x - (transformedBounds.x + (transformedBounds.width));
				break;
			default:
			}
			switch (operation) {
			case TOP:
			case TOP_LEFT:
			case TOP_RIGHT:
				dy = y - transformedBounds.y;
				break;
			case MOVE:
				dy = y - (transformedBounds.y + (transformedBounds.height / 2));
				break;
			case BOTTOM:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				dy = y - (transformedBounds.y + (transformedBounds.height));
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
