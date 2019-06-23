package com.samisari.cezmi.animator.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import com.samisari.cezmi.animator.core.Animation;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;
import com.samisari.cezmi.core.Operation;

public class PlayerMouseListener implements MouseListener, MouseMotionListener {
	PlayerPanel		panel;
	Player			player;
	Animation		anim;
	boolean			dragging			= false;
	Operation		currentOperation	= Operation.NONE;
	Point			lastClick;
	AbstractCommand	pivot;

	public PlayerMouseListener(PlayerPanel panel, Player player) {
		this.player = player;
		anim = player.getAnimation();
		this.panel = panel;
	}

	private Point transformClick(Point p, Point offset, double zoom) {
		p.x += offset.getX();
		p.y += offset.getY();
		p.x = (int) (p.x * zoom);
		p.y = (int) (p.y * zoom);
		return p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point click = transformClick(new Point(e.getX(), e.getY()), ConsolePropertyManager.getDefaultInstance().getOffset(), ConsolePropertyManager.getDefaultInstance().getScaleFactor());
		if (dragging) {
			dragging = false;
			currentOperation = Operation.NONE;
		} else {
			History history = anim.getCurrentView();
			List<AbstractCommand> list = history.getSelectedCommands();
			if (list.size() > 0) {
				for (AbstractCommand cmd : list) {
					Operation operation = cmd.getOperation(click);
					if (!dragging && operation != null && operation != Operation.NONE) {
						dragging = true;
						currentOperation = operation;
						pivot = cmd;
					}
				}
				lastClick = click;
			}
		}

		if (!dragging) {
			History history = anim.getCurrentView();
			List<AbstractCommand> list = history.getCommandsInRange(click.x, click.y, 5);
			if (list.size() == 0) {
				history.clearSelections();
			} else if (list.size() == 1) {
				list.get(0).setSelected(true);
			} else {
				AbstractCommand smallestAreaCommand = null;
				int minArea = Integer.MAX_VALUE;
				for (AbstractCommand cmd : list) {
					if (cmd.getArea() < minArea) {
						minArea = cmd.getArea();
						smallestAreaCommand = cmd;
					}
				}
				smallestAreaCommand.setSelected(true);
			}
		}
		panel.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point cursor = transformClick(new Point(e.getX(), e.getY()), ConsolePropertyManager.getDefaultInstance().getOffset(), ConsolePropertyManager.getDefaultInstance().getScaleFactor());
		if (currentOperation == Operation.CONTEXT_MENU) {
			History history = anim.getCurrentView();
			List<AbstractCommand> list = history.getSelectedCommands();
			if (list.size() > 0) {
				if (list.size() == 1) {
					AbstractCommand cmd = list.get(0);
					cmd.openContextMenu();
					return;
				} else {
					for (AbstractCommand cmd : list) {
						if (cmd.isInRange(cursor.x, cursor.y, AbstractCommand.SELECTION_MARKER_SIZE)) {
							cmd.openContextMenu();
							return;
						}
					}
				}

			}
		}
		if (dragging && currentOperation != Operation.NONE) {
			History history = anim.getCurrentView();
			List<AbstractCommand> list = history.getSelectedCommands();
			if (list.size() > 0) {
				Point pivotPoint = pivot.getOperationPoint(currentOperation);
				for (AbstractCommand cmd : list) {
					cmd.move(currentOperation, cursor.x - pivotPoint.x, cursor.y - pivotPoint.y);
				}
				panel.repaint();
			}
		}
	}

}
