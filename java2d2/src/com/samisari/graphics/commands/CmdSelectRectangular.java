package com.samisari.graphics.commands;

import java.awt.Rectangle;

import com.samisari.cezmi.component.CmdRectangle;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;
import com.samisari.cezmi.core.Status;

public class CmdSelectRectangular extends CmdRectangle {

	@Override
	public void mouseClicked(int x, int y) {
		// logger.debug("Mouse clicked: left=" + x + ", right=" + y);
		switch (getCurrentStatus()) {
		case START: {
			setX(x);
			setY(y);
			setWidth(0);
			setHeight(0);
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING: {
			setWidth(x-getX());
			setHeight(y - getY());
			selectObjectsInside(getX(),getY(),getWidth(),getHeight());
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			setCurrentStatus(Status.START);
		}
		}

	}

	private void selectObjectsInside(int x, int y, int w, int h) {
		History history = CommandManager.getDeaultInstance().getHistory();
		for (AbstractCommand c:history) {
			Rectangle b = c.getBounds();
			if (b.x>x && b.x+b.width < x+w && b.y>y && b.y+b.height<y+h) {
				c.setSelected(true);
			}
		}
		
	}

}
