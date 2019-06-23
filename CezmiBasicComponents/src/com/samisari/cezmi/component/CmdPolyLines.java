package com.samisari.cezmi.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;
import com.samisari.common.util.ImageTool;

public class CmdPolyLines extends CmdFreehand implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6647901803784688520L;
	private static final Logger	logger				= Logger.getLogger(CmdPolyLines.class);
	private Color				color;
	private Point				endPoint;

	public CmdPolyLines() {
		super();
		setColor(ConsolePropertyManager.getDefaultInstance().getForegroundColor());

	}

	@Override
	public void mouseClicked(int x, int y) {
		switch (getCurrentStatus()) {
		case START: {
			endPoint = new Point(x, y);
			getVertices().add(new Point(x, y));
			setX(x);
			setY(y);
			setHeight(0);
			setWidth(0);
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case CONNECT: {
			Rectangle endBox = new Rectangle(getVertices().get(getVertices().size() - 1).x, getVertices().get(getVertices().size() - 1).y, 20, 20);
			if (endBox.contains(x, y)) {
				Component consolePanel = ConsolePropertyManager.getDefaultInstance().getConsolePanel();
				Cursor cursor = Cursor.getDefaultCursor();
				consolePanel.setCursor(cursor);
				setCurrentStatus(Status.START);
				CommandManager.getDeaultInstance().endCommand();
				CommandManager.getDeaultInstance().getHistory().add(this);
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
				break;
			} else {
				setCurrentStatus(Status.DRAGGING);
			}
		}
		case DRAGGING: {
			setX(Math.min(getX(), x));
			setY(Math.min(getY(), y));

			if (x < getX()) {
				getBounds().width = getWidth() + (getX() - x);
				getBounds().x = x;
			}
			if (x > getRight()) {
				getBounds().width = x - getX();
			}
			if (y < getY()) {
				getBounds().height = getHeight() + (getY() - y);
				getBounds().y = y;
			}
			if (y > getBottom()) {
				getBounds().height = y - getY();
			}

			getVertices().add(new Point(x, y));
			setCurrentStatus(Status.CONNECT);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();

		}
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus() == Status.DRAGGING) {
			logger.debug("MOVED (" + x + "," + y + ")");
			endPoint.x = x;
			endPoint.y = y;
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		super.paint(g);
		logger.debug("paint" + getColor());
		Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		if (isSelected())
			g.setColor(Color.RED);
		else
			g.setColor(getColor());

		if (getCurrentStatus().equals(Status.CONNECT)) {
			Point lastEnd = getVertices().get(getVertices().size() - 1);
			Rectangle goBox = new Rectangle(getVertices().get(getVertices().size() - 1).x, getVertices().get(getVertices().size() - 1).y, 20, 20);
			g.setColor(Color.green);
			g.fillRect(goBox.x, goBox.y, goBox.width, goBox.height);
			g.setColor(getColor());
			g.drawString("+", lastEnd.x + 2, lastEnd.y + 10);
		}
		g.setColor(tmp);
		super.afterPaint(g);
	}

	@Override
	public void run() {
		super.run();
		Component comp = ConsolePropertyManager.getDefaultInstance().getConsolePanel();
		Cursor cursor = ImageTool.createCursor("LineCursor", "com/samisari/graphics/cursors/DefaultCursor.png", 16, 16);
		comp.setCursor(cursor);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
