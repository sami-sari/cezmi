package com.samisari.cezmi.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.ContextMenuAction;
import com.samisari.cezmi.core.ContextMenuItem;
import com.samisari.cezmi.core.Status;
import com.samisari.cezmi.util.Util;

public class CmdFreehand extends AbstractCommand implements Serializable {
	private static final long	serialVersionUID	= 1L;
	private static final Logger	logger				= Logger.getLogger(CmdFreehand.class);
	private List<Point>			vertices			= new ArrayList<>();
	private Color				color;
	private Rectangle			originalBounds;
	private List<Point>			originalVertices	= new ArrayList<>();
	private Point				pivot;

	@Override
	public void mouseClicked(int x, int y) {
		switch (getCurrentStatus()) {
		case START: {
			logger.debug("START (" + x + "," + y + ")");
			setX(x);
			setY(y);
			setHeight(0);
			setWidth(0);
			vertices.add(new Point(x, y));
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING: {
			logger.debug("DRAGGING (" + x + "," + y + ")");
			vertices.add(new Point(x, y));
			originalBounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
			reducePoints();
			for (Point v : vertices) {
				originalVertices.add(new Point(v.x, v.y));
			}
			setCurrentStatus(Status.CONNECT);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
			break;
		case CONNECT:
			logger.debug("CONNECT (" + x + "," + y + ")");

			if (isInRange(x, y, ConsolePropertyManager.SELECTION_RADIUS)) {
				Point last = vertices.get(0);
				originalVertices.add(new Point(last.x, last.y));
				vertices.add(new Point(last.x, last.y));
			}
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			setCurrentStatus(Status.START);
			break;

		case EDIT_POINTS: {
			int minDistance = Integer.MAX_VALUE;
			boolean selected = false;
			for (Point p : vertices) {
				int distance = Math.abs(p.y - y) + Math.abs(p.x - x);
				//				if ((p.x - x < 3 && p.x - x > -3) && (p.y - y < 3 && p.y - y > -3)) {
				//					pivot = p;
				//					selected=true;
				//					break;
				//				}
				if (distance < minDistance) {
					pivot = p;
					selected = true;
					minDistance = distance;
				}
			}

			//			if (!selected) {
			if (minDistance > 100) {
				CommandManager.getDeaultInstance().endCommand();
				setCurrentStatus(Status.START);
			}
		}
			break;
		default:
			break;
		}
	}

	public void mouseDragged(int x, int y) {
		if (pivot != null) {
			pivot.setLocation(x, y);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	private void reducePoints() {
		for (int i = 0; i < vertices.size() - 2;) {
			double dy = (vertices.get(i + 1).y - vertices.get(i).y);
			double dx = (vertices.get(i + 1).x - vertices.get(i).x);
			double m = dy / dx;
			double dy2 = (vertices.get(i + 2).y - vertices.get(i).y);
			double dx2 = (vertices.get(i + 2).x - vertices.get(i).x);
			double m2 = dy2 / dx2;
			if (m == m2) {
				vertices.remove(i + 1);
			}
			i++;

		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus() == Status.DRAGGING) {
			logger.debug("MOVED (" + x + "," + y + ")");
			String start = getBounds().toString();

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
			vertices.add(new Point(x, y));
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			if (!start.equals(getBounds().toString()))
				logger.debug(start + " >   " + getBounds().toString() + "" + x + "," + y);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());

		Graphics2D g2 = (Graphics2D) g;
		Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		if (getColor() == null) {
			color = tmp;
		}
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(2.0f));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		for (int i = 0; i < vertices.size() - 1; i++) {
			if (isSelected() || getCurrentStatus() == Status.EDIT_POINTS) {
				Util.drawCircle(g2, vertices.get(i), 2);
			}
			Util.drawLine(g, vertices.get(i), vertices.get(i + 1));
		}

		if (getCurrentStatus() == Status.EDIT_POINTS && pivot != null) {
			g.setColor(Color.RED);
			Util.drawCircle(g2, pivot, 2);
			g.setColor(tmp);

			//			Util.drawRect(g, getX(), getY(), 100, 25);
			Util.drawString(g, "Kapat", getX(), getY());
		}
		super.afterPaint(g);
	}

	@Override
	public void boundaryChanged(Rectangle oldBounds, Rectangle newBounds) {
		if (getCurrentStatus() == Status.START) {
			double dx = ((double) newBounds.x) - ((double) originalBounds.x);
			double dy = ((double) newBounds.y) - ((double) originalBounds.y);
			double sx = ((double) newBounds.width) / ((double) originalBounds.width);
			double sy = ((double) newBounds.height) / ((double) originalBounds.height);

			for (int i = 0; i < vertices.size(); i++) {
				Point o = originalVertices.get(i);
				Point v = vertices.get(i);
				double distx = (double) (o.x - originalBounds.x);
				double disty = (double) (o.y - originalBounds.y);
				v.x = originalBounds.x + (int) dx + (int) (distx * sx);
				v.y = originalBounds.y + (int) dy + (int) (disty * sy);
			}
		}
	}

	public List<Point> getVertices() {
		return vertices;
	}

	public void setVertices(List<Point> vertices) {
		this.vertices = vertices;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Rectangle getOriginalBounds() {
		return originalBounds;
	}

	public void setOriginalBounds(Rectangle originalBounds) {
		this.originalBounds = originalBounds;
	}

	public List<Point> getOriginalVertices() {
		return originalVertices;
	}

	public void setOriginalVertices(List<Point> originalVertices) {
		this.originalVertices = originalVertices;
	}

	@Override
	public AbstractCommand duplicate() {
		CmdFreehand copy = new CmdFreehand();
		copy.originalBounds = new Rectangle(getOriginalBounds());
		copy.setBounds(new Rectangle(getBounds()));
		copy.originalVertices = new ArrayList<Point>();
		copy.vertices = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			copy.vertices.add(new Point(vertices.get(i).x, vertices.get(i).y));
		}
		for (int i = 0; i < originalVertices.size(); i++) {
			copy.originalVertices.add(new Point(originalVertices.get(i).x, originalVertices.get(i).y));
		}
		copy.setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue()));
		return copy;
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();

	}

	@Override
	public List<ContextMenuItem> getContextMenuItems() {
		List<ContextMenuItem> menus = new ArrayList<ContextMenuItem>();
		ContextMenuItem item = new ContextMenuItem();
		item.setText("Sadeleştir");
		item.setAction(new ContextMenuAction() {

			@Override
			public void run() {
				double slope = Double.MAX_VALUE;
				for (int i = 1; i < getVertices().size();) {
					Point p0 = getVertices().get(i - 1);
					Point p1 = getVertices().get(i);
					if (Util.slope(p0, p1) == slope) {
						getVertices().remove(i);
					} else if (Util.distance(p1, p0) < 10.0) {
						getVertices().remove(i);
					} else {
						i++;
					}
				}
				getOriginalVertices().clear();
				for (Point p : getVertices()) {
					getOriginalVertices().add(new Point(p));
				}
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			}

		});
		ContextMenuItem i2 = new ContextMenuItem();
		i2.setText("Düzenle");
		i2.setAction(new ContextMenuAction() {

			@Override
			public void run() {
				CommandManager.getDeaultInstance().setCommand(CmdFreehand.this);
				setCurrentStatus(Status.EDIT_POINTS);
			}

		});
		menus.add(item);
		menus.add(i2);
		return menus;
	}
}
