package com.samisari.cezmi.component;

import java.awt.Point;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;

public class CmdPan extends AbstractCommand {
	private static final long	serialVersionUID	= -5334798116707651676L;
	private Point				pivot;

	@Override
	public void mouseClicked(int x, int y) {
		super.mouseClicked(x, y);
		if (getCurrentStatus() == Status.START) {
			setCurrentStatus(Status.DRAGGING);
			pivot = new Point(x, y);
		} else {
			CommandManager.getDeaultInstance().endCommand();
			setCurrentStatus(Status.START);
			CommandManager.getDeaultInstance().setCommand(this);
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		super.mouseMoved(x, y);
		if (getCurrentStatus() == Status.DRAGGING) {
			double zoom = ConsolePropertyManager.getDefaultInstance().getScaleFactor();
			Point offset = ConsolePropertyManager.getDefaultInstance().getOffset();
			//Point diff = new Point(pivot.x - (int) ((x - offset.x) * zoom) + offset.x, pivot.y - (int) ((y - offset.y) * zoom) + offset.y);
			Point diff = new Point((int) x - pivot.x, (int) y - pivot.y);
			System.out.println(offset);
			System.out.println(ConsolePropertyManager.getDefaultInstance().getScaleFactor());
			ConsolePropertyManager.getDefaultInstance().setOffset(new Point(offset.x + diff.x, offset.y + diff.y));
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

}
