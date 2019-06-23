package com.samisari.cezmi.component;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;

public class CmdCircle extends AbstractCommand implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdCircle.class);
	private Color				color;
	private Color				fillColor;

	@Override
	public void mouseClicked(int x, int y) {
		switch (getCurrentStatus()) {
		case START: {
			setBounds(x, y, 0, 0);
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING: {
			int dim = Math.min(x - getX(), y - getY());
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			setBounds(getX(), getY(), dim, dim);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus().equals(Status.DRAGGING)) {
			int dim = Math.min(x - getX(), y - getY());
			setBounds(getX(), getY(), dim, dim);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		if (getHeight() > 0) {
			int r = Math.min(getHeight(), getWidth());
			Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
			if (isSelected())
				g.setColor(Color.RED);
			else
				g.setColor(getColor());
			if (fillColor != null) {
				g.setColor(fillColor);
				g.fillOval(getX(), getY(), r, r);
			} else {
				g.setColor(color);
				g.drawOval(getX(), getY(), r, r);
			}
			g.setColor(tmp);
		}
		super.afterPaint(g);
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

	/**
	 * Removed due to the assumption that operation points shall reside inside
	 * component boundaries
	 */

	// public boolean isInRange(int x, int y, int radius) {
	// boolean retval = false;
	// Point2D p1 = new Point2D.Float(Float.valueOf(getX()),
	// Float.valueOf(getY()));
	// Point2D p2 = new Point2D.Float(Float.valueOf(getRight()),
	// Float.valueOf(getBottom()));
	// Point2D p = new Point2D.Float(Float.valueOf(x), Float.valueOf(y));
	// Ellipse2D.Double circle = new Ellipse2D.Double(p1.getX(), p1.getY(),
	// p2.getX() - p1.getX(), p2.getY() - p1.getY());
	// if (circle.intersects(Double.valueOf(x - 5), Double.valueOf(y - 5), 5,
	// 5))
	// retval = true;
	// return retval;
	// }

	protected void paintSelection(Graphics g) {
		if (getBounds() != null) {
			Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
			g.setColor(SELECTED_COLOR);
			g.drawImage(RB_ICON, getBounds().x + getBounds().width - SELECTION_MARKER_SIZE, getBounds().y + getBounds().height - SELECTION_MARKER_SIZE, null);
			g.drawRect(getBounds().x + getBounds().width / 2 - SELECTION_MARKER_SIZE, getBounds().y + getBounds().height / 2 - SELECTION_MARKER_SIZE,
					SELECTION_MARKER_SIZE * 2, SELECTION_MARKER_SIZE * 2);
			g.setColor(tmp);
		}
	}

	@Override
	public CmdCircle duplicate() {
		CmdCircle clone = (CmdCircle) clone();
		return clone;

	}

	@Override
	public void move(Operation operation, int dx, int dy) {
		if (operation == Operation.BOTTOM_RIGHT) {
			int r = Math.min(getWidth() + dx, getHeight() + dy);
			dx = dy = Math.min(dx, dy);
			setBounds(getX(), getY(), r, r);
			return;
		}
		if (operation == Operation.MOVE)
			super.move(operation, dx, dy);
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

}
