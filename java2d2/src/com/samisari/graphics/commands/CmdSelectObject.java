package com.samisari.graphics.commands;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;

public class CmdSelectObject extends AbstractCommand {
	private static final long	serialVersionUID	= 1747128654329125034L;
	public static final Logger	logger				= Logger.getLogger(CmdSelectObject.class);
	public static final int		radius				= 5;

	@Override
	public void run() {
		Component comp = ConsolePropertyManager.getDefaultInstance().getConsolePanel();
		comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (Status.START == getCurrentStatus()) {
			AbstractCommand so = CommandManager.getDeaultInstance().getSmallestAreaCommandInRange(x, y, radius);
			if (so != null) {
				if (so.isSelected())
					so.setSelected(false);
				else
					so.setSelected(true);
				CommandManager.getDeaultInstance().fireCommandSelectionChanged(so);
			}
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			setCurrentStatus(Status.END);
		}
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		return null;
	}
}
