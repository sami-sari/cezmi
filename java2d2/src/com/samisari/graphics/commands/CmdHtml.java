package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;
import com.samisari.cezmi.util.Util;

public class CmdHtml extends AbstractCommand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5226672462820897803L;
	public static final Logger logger = Logger.getLogger(CmdHtml.class);
	private Integer x1, y1, x2, y2;
	private Color color;
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void mouseClicked(int x, int y) {
		logger.debug("Mouse clicked: x1="
				+ (x1 == null ? "NULL" : x1.toString()) + "x2="
				+ (x2 == null ? "NULL" : x2.toString()));
		switch (getCurrentStatus()) {
		case START: {
			x1 = new Integer(x);
			y1 = new Integer(y);
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING: {
			x2 = new Integer(x);
			y2 = new Integer(y);
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus().equals(Status.DRAGGING)) {
			x2 = new Integer(x);
			y2 = new Integer(y);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	@Override
	public void mousePressed(int x, int y) {
	}

	@Override
	public void mouseReleased(int x, int y) {
	}

	@Override
	public void paint(Graphics g) {
		Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		if (isSelected())
			g.setColor(Color.RED);
		else
			g.setColor(getColor());
		// g.setColor(getColor());
		Util.drawRect(g, x1, y1, x2, y2);
		g.setColor(tmp);
	}

	@Override
	public void redo() {
	}

	@Override
	public void run() {
		setColor(ConsolePropertyManager.getDefaultInstance().getForegroundColor());
	}

	@Override
	public void undo() {
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isInRange(int x, int y, int radius) {
		boolean retval = false;
		Point2D p1 = new Point2D.Float(Float.valueOf(x1), Float.valueOf(y1));
		Point2D p2 = new Point2D.Float(Float.valueOf(x2), Float.valueOf(y2));
		Point2D p = new Point2D.Float(Float.valueOf(x), Float.valueOf(y));
		Ellipse2D.Double circle = new Ellipse2D.Double(p1.getX(), p1.getY(), p2
				.getX(), p2.getY());
		if (circle.intersects(Double.valueOf(x - 5), Double.valueOf(y - 5),
				Double.valueOf(x + 5), Double.valueOf(y + 5)))
			retval = true;
		return retval;
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		// TODO Auto-generated method stub
		return null;
	}
}
