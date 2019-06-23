package com.samisari.cezmi.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.HistoryListener;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;
import com.samisari.cezmi.util.Util;
import com.samisari.common.util.ImageTool;

public class CmdConnector extends AbstractCommand implements HistoryListener, PropertyChangeListener, Serializable {
	private static final long			serialVersionUID	= 1L;
	private static final Logger			logger				= Logger.getLogger(CmdConnector.class);
	private static final int			radius				= 5;
	private static final BufferedImage	imgro;
	static {
		imgro = ImageTool
				.getResourceAsImage(CmdConnector.class.getName().substring(0, CmdConnector.class.getName().lastIndexOf('.')).replaceAll("\\.", "/") + "/resources/arrowhead.png");
	}

	private String			sourceObjectId;
	private String			targetObjectId;

	private int				sourceConnectionEdge;				// Left/right/Top/Bottom
	private int				sourceConnectionDistance;			// Distance from top or left according
	// to edge
	private int				targetConnectionEdge;				// Left/right/Top/Bottom
	private int				targetConnectionDistance;			// Distance from top or left according
	// to edge
	private boolean			reshapeNoCrossingOthers;

	private List<Point>		points	= new ArrayList<Point>();
	private String			startText;
	private String			endText;
	private String			middleText;
	private transient Point	operationPoint;

	@Override
	public void mouseClicked(int x, int y) {
		switch (getCurrentStatus()) {
		case START: {
			AbstractCommand so = CommandManager.getDeaultInstance().getSmallestAreaCommandInRange(x, y, radius);
			if (so != null)
				if (so instanceof CmdConnectable) {
					setSourceObjectId(so.getId());
					setCurrentStatus(Status.OBJECT_1_SELECTED);
				}
			if (pivotIndex != null) {
				pivotIndex = null;
			}
		}
			break;
		case OBJECT_1_SELECTED: {
			AbstractCommand so = CommandManager.getDeaultInstance().getSmallestAreaCommandInRange(x, y, radius);

			if (sourceObjectId == null) {
				setCurrentStatus(Status.START);
				return;
			}
			if (so != null)
				if (so instanceof CmdConnectable)
					targetObjectId = so.getId();
				else {
					return;
				}
			deducePoints(false);
			getSourceObject().addPropertyChangeListener(this);
			getTargetObject().addPropertyChangeListener(this);
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			CommandManager.getDeaultInstance().getHistory().addListener(this);
			setCurrentStatus(Status.START);
		}
		default:
			break;
		}

	}

	private void deducePoints(boolean locked) {
		// if any of source object or target object does not exist dont do
		// anything
		if (getSourceObject() == null || getTargetObject() == null || getSourceObject().getBounds() == null || getTargetObject().getBounds() == null)
			return;

		// if both ends of connector touches source and target objects do not do
		// anything
		if (getPoints() != null && isConnected())
			return;

		Rectangle source = new Rectangle(getSourceObject().getBounds());
		Rectangle target = new Rectangle(getTargetObject().getBounds());
		AbstractCommand ancestor = getSourceObject();
		while ((ancestor = CommandManager.getDeaultInstance().getCommand(ancestor.getParentId())) != null) {
			source.x += ancestor.getBounds().x;
			source.y += ancestor.getBounds().y;
		}
		ancestor = getTargetObject();
		while ((ancestor = CommandManager.getDeaultInstance().getCommand(ancestor.getParentId())) != null) {
			target.x += ancestor.getBounds().x;
			target.y += ancestor.getBounds().y;
		}

		points.clear();
		if (source.x + source.width < target.x) {
			if (source.y + source.height < target.y) {
				points.add(new Point(source.x + source.width, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, target.y));
			} else if (target.y + target.height < source.y) {
				points.add(new Point(source.x + source.width, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, target.y + target.height));
			} else {
				points.add(new Point(source.x + source.width, source.y + source.height / 2));
				int midx = source.x + source.width + (target.x - (source.x + source.width)) / 2;
				points.add(new Point(midx, source.y + source.height / 2));
				points.add(new Point(midx, target.y + target.height / 2));
				points.add(new Point(target.x, target.y + target.height / 2));
			}
		} else if (target.x + target.width < source.x) {
			if (source.y + source.height < target.y) {
				points.add(new Point(source.x, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, target.y));
			} else if (target.y + target.height < source.y) {
				points.add(new Point(source.x, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, source.y + source.height / 2));
				points.add(new Point(target.x + target.width / 2, target.y + target.height));
			} else {
				points.add(new Point(source.x, source.y + source.height / 2));
				int midx = target.x + target.width + (source.x - (target.x + target.width)) / 2;
				points.add(new Point(midx, source.y + source.height / 2));
				points.add(new Point(midx, target.y + target.height / 2));
				points.add(new Point(target.x + target.width, target.y + target.height / 2));
			}
		} else {
			if (source.y + source.height < target.y) {
				points.add(new Point(source.x + source.width / 2, source.y + source.height));
				int midy = source.y + source.height + (target.y - (source.y + source.height)) / 2;
				points.add(new Point(source.x + source.width / 2, midy));
				points.add(new Point(target.x + target.width / 2, midy));
				points.add(new Point(target.x + target.width / 2, target.y));
			} else if (target.y + target.height < source.y) {
				points.add(new Point(source.x + source.width / 2, source.y));
				int midy = source.y + source.height + (target.y - (source.y + source.height)) / 2;
				points.add(new Point(source.x + source.width / 2, midy));
				points.add(new Point(target.x + target.width / 2, midy));
				points.add(new Point(target.x + target.width / 2, target.y + target.height));
			}

		}
	}

	private boolean isConnected() {
		if (points != null && points.size() > 1) {
			Point lastPoint = points.get(points.size() - 1);
			Point firstPoint = points.get(0);
			Rectangle sr = getSourceObject().getBounds();
			Rectangle tr = getTargetObject().getBounds();
			Rectangle nsr = new Rectangle(sr.x - 1, sr.y - 1, sr.width + 2, sr.height + 2);
			Rectangle ntr = getTargetObject().getBounds();
			tr = new Rectangle(tr.x - 1, tr.y - 1, tr.width + 2, tr.height + 2);

			if (nsr.contains(firstPoint) && ntr.contains(lastPoint) && !sr.contains(firstPoint) && !tr.contains(lastPoint))
				return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus() == Status.OBJECT_1_SELECTED) {
			points.add(new Point(x, y));
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	public void drawLineArrow(Graphics g, Point lineStart, Point lineEnd) {
		Color tmp = g.getColor();
		g.setColor(Color.blue);
		double slope = Util.slope(lineStart, lineEnd);
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage img = new BufferedImage(imgro.getWidth(null), imgro.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D imgg = img.createGraphics();
		AffineTransform tx = AffineTransform.getRotateInstance(slope, 5.5, 5.5);
		imgg.drawImage(imgro, tx, null);

		int dx = (int) (6.0f * Math.cos(slope));
		int dy = (int) (6.0f * Math.sin(slope));
		g2d.drawImage(img, lineEnd.x - 5 - dx, lineEnd.y - 5 - dy, null);
		g2d.setColor(tmp);
	}

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());

		Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		g.setColor(Color.blue);

		for (int i = 0; i < points.size() - 1; i++)
			Util.drawLine(g, points.get(i), points.get(i + 1));
		int indx = points.size() - 1;
		if (indx > 1)
			drawLineArrow(g, points.get(indx - 1), points.get(indx));
		// if (points != null && points.size() > 1)
		//// if (getCurrentStatus() == Status.START) {
		// if (points.get(0).x < points.get(1).x)
		// g.drawString(getStartText() == null ? "" : getStartText(),
		// points.get(0).x + 4,
		// points.get(0).y - 2);
		// else
		// g.drawString(getStartText() == null ? "" : getStartText(),
		// points.get(1).x + 4,
		// points.get(1).y - 2);
		// if (points.get(indx).x < points.get(indx - 1).x)
		// g.drawString(getEndText() == null ? "" : getEndText(),
		// points.get(indx).x + 4,
		// points.get(indx).y - 2);
		// else
		// g.drawString(getEndText() == null ? "" : getEndText(),
		// points.get(indx - 1).x + 4,
		// points.get(indx - 1).y - 2);
		//// }
		paintTexts(g);
		g.setColor(tmp);
		// paintTexts(g);
		super.afterPaint(g);
	}

	private void paintTexts(Graphics g) {
		if (getStartText() != null && getPoints() != null && getPoints().size() > 1) {
			Point p1 = getPoints().get(0);
			Point p2 = getPoints().get(1);
			boolean vertical = (p1.x == p2.x);
			if (vertical) {
				boolean upward = p1.y < p2.y;
				if (upward) {
					g.drawString(getStartText(), p1.x + 3, p1.y - 5);
				} else {
					g.drawString(getStartText(), p1.x + 3, p1.y + 5);
				}
			} else {
				boolean left = p1.y < p2.y;
				if (left) {
					Rectangle sb = ((Graphics2D) g).getFontMetrics().getStringBounds(getStartText(), g).getBounds();
					g.drawString(getStartText(), p1.x + 3, p1.y - sb.height);
				} else {
					Rectangle sb = ((Graphics2D) g).getFontMetrics().getStringBounds(getStartText(), g).getBounds();
					g.drawString(getStartText(), p1.x - sb.width - 3, p1.y - sb.height - 2);

				}
			}
		}
		if (getEndText() != null && getPoints() != null && getPoints().size() > 1) {
			Point p1 = getPoints().get(getPoints().size() - 2);
			Point p2 = getPoints().get(getPoints().size() - 1);
			boolean vertical = (p1.x == p2.x);
			if (vertical) {
				boolean upward = p1.y < p2.y;
				if (upward) {
					g.drawString(getEndText(), p1.x + 3, p1.y - 5);
				} else {
					g.drawString(getEndText(), p1.x + 3, p1.y + 5);
				}
			} else {
				boolean left = p1.y < p2.y;
				if (left) {
					Rectangle sb = ((Graphics2D) g).getFontMetrics().getStringBounds(getEndText(), g).getBounds();
					g.drawString(getEndText(), p1.x + 3, p1.y - sb.height);
				} else {
					Rectangle sb = ((Graphics2D) g).getFontMetrics().getStringBounds(getEndText(), g).getBounds();
					g.drawString(getEndText(), p1.x - sb.width - 3, p1.y - sb.height - 2);

				}
			}
		}

	}

	@Override
	protected void paintSelection(Graphics g) {
		if (CommandManager.getDeaultInstance().getCommand(sourceObjectId) != null && CommandManager.getDeaultInstance().getCommand(targetObjectId) != null && points != null
				&& points.size() > 0) {
			Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
			g.setColor(SELECTED_COLOR);
			// Draw operation boxes at points
			for (Point p : points) {
				g.drawRect(p.x - SELECTION_MARKER_SIZE, p.y - SELECTION_MARKER_SIZE, SELECTION_MARKER_SIZE * 2, SELECTION_MARKER_SIZE * 2);
			}
			// Draw Operation boxes at edges
			for (int i = 0; i < points.size() - 1; i++) {
				Point p0 = points.get(i);
				Point p1 = points.get(i + 1);
				int epx = p0.x + ((p1.x - p0.x) / 2);
				int epy = p0.y + ((p1.y - p0.y) / 2);
				g.drawRect(epx - SELECTION_MARKER_SIZE, epy - SELECTION_MARKER_SIZE, SELECTION_MARKER_SIZE * 2, SELECTION_MARKER_SIZE * 2);
			}

			g.setColor(tmp);
		}
	}

	public static String getButtonName() {
		return "Con";
	}

	@Override
	public void propertyChange(PropertyChangeEvent property) {
		if (isConnected())
			return;
		deducePoints(isLocked());
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private boolean isLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isInRange(int x, int y, int radius) {
		boolean retval = false;
		for (int i = 0; i < points.size() - 1; i++) {
			Point2D p1 = new Point2D.Float(Float.valueOf(points.get(i).x), Float.valueOf(points.get(i).y));
			Point2D p2 = new Point2D.Float(Float.valueOf(points.get(i + 1).x), Float.valueOf(points.get(i + 1).y));
			Point2D p = new Point2D.Float(Float.valueOf(x), Float.valueOf(y));
			Line2D.Float line = new Line2D.Float(p1, p2);
			if (line.ptSegDist(p) < radius)
				retval = true;
		}
		return retval;
	}

	public Operation getOperation(Point click) {
		logger.debug("Click" + click);
		operationPoint = click;
		Point2D pclick = new Point2D.Float(Float.valueOf(click.x), Float.valueOf(click.y));
		for (int i = 0; i < points.size(); i++) {
			Point2D p = new Point2D.Float(Float.valueOf(points.get(i).x), Float.valueOf(points.get(i).y));
			if (p.distance(pclick) < SELECTION_MARKER_SIZE) {
				Point pi = points.get(i);
				if (Math.abs(pi.x - operationPoint.x) < SELECTION_MARKER_SIZE && Math.abs(pi.y - operationPoint.y) < SELECTION_MARKER_SIZE) {
					pivotIndex = new Integer[] { i };
				}
				return Operation.MOVE;
			}
		}
		for (int i = 0; i < points.size() - 1; i++) {
			Point p0 = points.get(i);
			Point p1 = points.get(i + 1);
			float epx = (float) (p0.x + ((p1.x - p0.x) / 2));
			float epy = (float) (p0.y + ((p1.y - p0.y) / 2));
			Point2D p = new Point2D.Float(epx, epy);
			if (p.distance(pclick) < SELECTION_MARKER_SIZE) {
				Point pi = points.get(i);
				Point pi1 = points.get(i + 1);
				Point edgePoint = new Point();
				edgePoint.x = pi.x + ((pi1.x - pi.x) / 2);
				edgePoint.y = pi.y + ((pi1.y - pi.y) / 2);
				if (Math.abs(edgePoint.x - operationPoint.x) < SELECTION_MARKER_SIZE && Math.abs(edgePoint.y - operationPoint.y) < SELECTION_MARKER_SIZE) {
					operationPoint.x = edgePoint.x;
					operationPoint.y = edgePoint.y;
					pivotIndex = new Integer[] { i, i + 1 };
				}
				return Operation.MOVE_CONNECTOR_EDGE;
			}
		}
		for (int i = 0; i < points.size() - 1; i++) {
			Point p0 = points.get(i);
			Point p1 = points.get(i + 1);
			Line2D l = new Line2D.Double(p0, p1);
			if (l.ptLineDist(click) < SELECTION_MARKER_SIZE) {
				points.add(i + 1, click);
				return Operation.MOVE;
			}
		}

		operationPoint = null;
		return null;
	}

	@Override
	public void morph(Operation operation, int ex, int ey) {
		if (operationPoint == null)
			return;
		for (Point p : points) {
			if (Math.abs(p.x - operationPoint.x) < SELECTION_MARKER_SIZE && Math.abs(p.y - operationPoint.y) < SELECTION_MARKER_SIZE) {
				operationPoint.x = p.x;
				operationPoint.y = p.y;
				break;
			}
		}
		int indx = points.indexOf(operationPoint);
		if (indx > -1 && indx < points.size() && operationPoint != null) {
			points.set(indx, new Point(operationPoint.x + ex, operationPoint.y + ey));
			operationPoint = points.get(indx);
		}

		if (points.get(0).equals(operationPoint)) {
			AbstractCommand sourceObject = CommandManager.getDeaultInstance().getCommand(sourceObjectId);
			Point p0 = points.get(0);
			Point ps = new Point(sourceObject.getBounds().x, sourceObject.getBounds().y);
			int ws = sourceObject.getBounds().width;
			int hs = sourceObject.getBounds().height;
			if (Math.abs(p0.x - ps.x) > 10 && Math.abs(p0.x - (ps.x + ws)) > 10 && p0.y != ps.y && p0.y != ps.y + hs) {
				points.add(0, new Point(ps.x, p0.y));
			}
		}
	}

	private Integer[] pivotIndex = null;

	public void move(Operation operation, int dx, int dy) {
		logger.info("Operation " + operation);
		logger.debug("Operation point " + operationPoint);
		logger.debug("d(" + dx + "," + dy + ")");
		logger.debug("Pivot index = " + (pivotIndex == null ? "NULL" : pivotIndex[0].intValue()) + "\n");

		if (operation == Operation.MOVE) {
			points.get(pivotIndex[0]).setLocation(dx, dy);
		} else if (operation == Operation.MOVE_CONNECTOR_EDGE) {
			Point p0 = points.get(pivotIndex[0]);
			Point p1 = points.get(pivotIndex[1]);
			if (p0.x == p1.x) {
				p0.setLocation(dx, p0.y);
				p1.setLocation(dx, p1.y);
			} else if (p0.y == p1.y) {
				p0.setLocation(p0.x, dy);
				p1.setLocation(p1.x, dy);
			}

		}
	}

	@Override
	public Point getOperationPoint(Operation operation) {

		return operationPoint;
	}

	private CmdConnectableRectangle getSourceObject() {
		return (CmdConnectableRectangle) CommandManager.getDeaultInstance().getCommand(sourceObjectId);
	}

	private CmdConnectableRectangle getTargetObject() {
		return (CmdConnectableRectangle) CommandManager.getDeaultInstance().getCommand(targetObjectId);
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public String getStartText() {
		return startText;
	}

	public void setStartText(String startText) {
		this.startText = startText;
	}

	public String getEndText() {
		return endText;
	}

	public void setEndText(String endText) {
		this.endText = endText;
	}

	public String getMiddleText() {
		return middleText;
	}

	public void setMiddleText(String middleText) {
		this.middleText = middleText;
	}

	public String getSourceObjectId() {
		return sourceObjectId;
	}

	public void setSourceObjectId(String sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
	}

	public String getTargetObjectId() {
		return targetObjectId;
	}

	public void setTargetObjectId(String targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

	@Override
	public void deserialise(Element elm) {
		super.deserialise(elm);
		// deducePoints();
		AbstractCommand source = getSourceObject();
		AbstractCommand target = getTargetObject();
		if (source != null && target != null) {
			source.addPropertyChangeListener(this);
			target.addPropertyChangeListener(this);
		}
		CommandManager.getDeaultInstance().getHistory().addListener(this);
	}

	@Override
	public void deserialise(Reader in) throws Exception {
		// TODO Auto-generated method stub
		super.deserialise(in);
		// deducePoints();
		AbstractCommand source = getSourceObject();
		AbstractCommand target = getTargetObject();
		if (source != null && target != null) {
			source.addPropertyChangeListener(this);
			target.addPropertyChangeListener(this);
		} else {
			System.out.println(getSourceObjectId());
		}
		CommandManager.getDeaultInstance().getHistory().addListener(this);
	}

	@Override
	public void onRemoved(Object o) {
		if (((AbstractCommand) o).getId().equals(getSourceObjectId()) || ((AbstractCommand) o).getId().equals(getTargetObjectId())) {
			CommandManager.getDeaultInstance().getHistory().remove(this);
		}

	}

	@Override
	public void onInserted(Object o) {
		if (((AbstractCommand) o).getId().equals(getSourceObjectId())) {
			AbstractCommand source = getSourceObject();
			source.addPropertyChangeListener(this);
		}
		if (((AbstractCommand) o).getId().equals(getTargetObjectId())) {
			AbstractCommand target = getTargetObject();
			target.addPropertyChangeListener(this);
		}

	}

	@Override
	public void onModelChanged() {
		AbstractCommand me = CommandManager.getDeaultInstance().getHistory().getCommand(this.getId());
		if (me != null) {
			AbstractCommand src = CommandManager.getDeaultInstance().getHistory().getCommand(this.getSourceObjectId());
			AbstractCommand tgt = CommandManager.getDeaultInstance().getHistory().getCommand(this.getTargetObjectId());
			if (src == null || tgt == null) {
				CommandManager.getDeaultInstance().getHistory().remove(this);
			}
		}

	}
}
