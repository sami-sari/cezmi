package com.samisari.cezmi.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.samisari.common.util.bean.BeanUtils;

public abstract class AbstractCommand implements Serializable, Cloneable, ICommandConstants {
	private static final Logger						logger					= Logger.getLogger(AbstractCommand.class);
	private static final long						serialVersionUID		= -2314753607657866006L;
	private static long								sequence;

	private String									id;
	private String									parentId;
	private String									name;
	private Rectangle								bounds;
	private AffineTransform							transform;
	private double									rotationAngle;
	private double									z;
	private boolean									captureMouse			= false;
	private boolean									selected				= false;
	private transient Status						currentStatus			= Status.START;
	private transient List<PropertyChangeListener>	propertyChangeListeners	= new ArrayList<>();

	public AbstractCommand() {
		super();
		String className = getClass().getSimpleName();
		if (className.length() <= 3) {
			className = getClass().getSuperclass().getSimpleName();
		}
		setId(className.substring(3) + System.currentTimeMillis() + (sequence++));

	}

	public void mouseClicked(int x, int y) {
		// Default do nothing
	}

	public void mouseMoved(int x, int y) {
		// Default do nothing
	}

	public void mousePressed(int x, int y) {
		// Default do nothing
	}

	public void mouseReleased(int x, int y) {
		// Default do nothing
	}

	// Runs this when painting the object
	public void paint(Graphics g) {

	}

	public void beforePaint(Graphics g, double zoom, Point offset) {
		AffineTransform viewTransform = AffineTransform.getTranslateInstance(-offset.getX(), -offset.getY());
		viewTransform.scale(zoom, zoom);
		if (transform != null) {
			viewTransform.concatenate(transform);
		}
		((Graphics2D) g).setTransform(viewTransform);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	}

	public void afterPaint(Graphics g) {
		if (isSelected())
			paintSelection(g);
		if (currentStatus == Status.CONTEXT_MENU) {
			paintContextMenu(g);
		}
		if (transform != null) {
			try {
				AffineTransform inverse = transform.createInverse();
				((Graphics2D) g).setTransform(inverse);
			} catch (NoninvertibleTransformException e) {
				logger.error("Error while inverting transform", e);
			}
		}
	}

	// initial action executed as soon as the command is clicked
	public void run() {
	}

	public void redo() {
	}

	public void undo() {
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Status getCurrentStatus() {
		if (currentStatus == null) {
			currentStatus = Status.END;
		}
		return currentStatus;
	}

	public boolean isInRange(int x, int y, int radius) {
		boolean result = false;
		if (bounds != null) {
			if (transform != null) {
				Shape s = transform.createTransformedShape(bounds.getBounds2D());
				result = (s.contains(x, y));
			} else
				result = (x >= bounds.x - radius && x <= bounds.x + bounds.width + radius && y >= bounds.y - radius && y <= bounds.y + bounds.height + radius);
		}
		return result;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	protected void paintSelection(Graphics g) {
		if (bounds != null) {
			Color tmp = g.getColor();
			g.setColor(SELECTED_COLOR);
			g.drawImage(LT_ICON, bounds.x - SELECTION_MARKER_SIZE, bounds.y - SELECTION_MARKER_SIZE, null);
			g.drawImage(PROCESS_ICON, bounds.x + SELECTION_MARKER_SIZE, bounds.y - SELECTION_MARKER_SIZE, null);
			g.drawImage(RT_ICON, bounds.x + bounds.width - SELECTION_MARKER_SIZE, bounds.y - SELECTION_MARKER_SIZE, null);
			g.drawImage(LB_ICON, bounds.x - SELECTION_MARKER_SIZE, bounds.y + bounds.height - SELECTION_MARKER_SIZE, null);
			g.drawImage(RB_ICON, bounds.x + bounds.width - SELECTION_MARKER_SIZE, bounds.y + bounds.height - SELECTION_MARKER_SIZE, null);
			g.drawImage(T_ICON, bounds.x + bounds.width / 2 - SELECTION_MARKER_SIZE, bounds.y - 8, null);
			g.drawImage(B_ICON, bounds.x + bounds.width / 2 - SELECTION_MARKER_SIZE, bounds.y + bounds.height - 7, null);
			g.drawImage(L_ICON, bounds.x - 8, bounds.y + bounds.height / 2 - SELECTION_MARKER_SIZE, null);
			g.drawImage(R_ICON, bounds.x + bounds.width - SELECTION_MARKER_SIZE, bounds.y + bounds.height / 2 - SELECTION_MARKER_SIZE, null);
			g.drawRect(bounds.x + bounds.width / 2 - SELECTION_MARKER_SIZE, bounds.y + bounds.height / 2 - SELECTION_MARKER_SIZE, SELECTION_MARKER_SIZE * 2,
					SELECTION_MARKER_SIZE * 2);
			g.setColor(tmp);
		}
	}

	protected void setBounds(int left, int top, int widht, int height) {
		Rectangle oldBounds = bounds;
		bounds = new Rectangle(left, top, widht, height);
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
	}

	public Rectangle getBounds() {
		if (bounds == null) {
			bounds = new Rectangle();
		}
		return bounds;
	}

	public void buildBounds() {
		if (this.bounds == null)
			this.bounds = new Rectangle(0, 0, 0, 0);
	}

	public void setBounds(Rectangle bounds) {
		logger.debug("setBounds");
		Rectangle oldBounds = (this.bounds == null ? null : new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height));
		this.bounds = bounds;
		logger.debug("Firing boundary changed:" + getClass().getSimpleName() + ":" + (bounds == null ? "NULL" : bounds.toString()));
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
		// ConsolePropertyManager.getInstance().getConsolePanel().repaint();
	}

	public int getX() {
		return getBounds().x;
	}

	public int getY() {
		return getBounds().y;
	}

	public int getWidth() {
		return getBounds().width;
	}

	public int getHeight() {
		return getBounds().height;
	}

	public int getRight() {
		return getBounds().x + getBounds().width;
	}

	public int getBottom() {
		return getBounds().y + getBounds().height;
	}

	public void setX(int x) {
		logger.debug(getClass().getSimpleName() + " x=" + x);
		Rectangle oldBounds = new Rectangle(getBounds());
		getBounds().x = x;
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
	}

	public void setY(int y) {
		Rectangle oldBounds = new Rectangle(getBounds());
		getBounds().y = y;
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
	}

	public void setWidth(int width) {
		logger.debug(getClass().getSimpleName() + " width=" + width);
		Rectangle oldBounds = new Rectangle(getBounds());
		getBounds().width = width;
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
	}

	public void setHeight(int height) {
		Rectangle oldBounds = new Rectangle(getBounds());
		getBounds().height = height;
		firePropertyChange(new PropertyChangeEvent(this, "bounds", oldBounds, this.bounds));
	}

	public Point getOperationPoint(Operation operation) {
		Point p = new Point();
		switch (operation) {
		case LEFT:
			p.setLocation(bounds.getX(), bounds.getY() + (bounds.getHeight() / 2));
			break;
		case TOP_LEFT:
			p.setLocation(bounds.getX(), bounds.getY());
			break;
		case BOTTOM_LEFT:
			p.setLocation(bounds.getX(), bounds.getY() + bounds.getHeight());
			break;
		case RIGHT:
			p.setLocation(bounds.getX() + bounds.getWidth(), bounds.getY() + (bounds.getHeight() / 2));
			break;
		case TOP_RIGHT:
			p.setLocation(bounds.getX() + bounds.getWidth(), bounds.getY());
			break;
		case BOTTOM_RIGHT:
			p.setLocation(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
			break;
		case MOVE:
			p.setLocation(bounds.getX() + (bounds.getWidth() / 2), bounds.getY() + (bounds.getHeight() / 2));
			break;
		case TOP:
			p.setLocation(bounds.getX() + (bounds.getWidth() / 2), bounds.getY());
			break;
		case BOTTOM:
			p.setLocation(bounds.getX() + (bounds.getWidth() / 2), bounds.getY() + (bounds.getHeight()));
			break;
		default:
			p = null;

		}

		return p;

	}

	public void move(Operation operation, int dx, int dy) {
		move(operation, dx, dy, 1);
	}

	public void move(Operation operation, int dx, int dy, int movementStep) {
		Rectangle newBounds = new Rectangle(bounds);
		switch (operation) {
		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
			newBounds.x = bounds.x + dx;
			newBounds.width = bounds.width - dx;
			if (bounds.width < 0) {
				bounds.width = -bounds.width;
				bounds.x = bounds.x - bounds.width;
			}
			break;
		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			newBounds.width = bounds.width + dx;
			if (bounds.width < 0) {
				bounds.width = -bounds.width;
				bounds.x = bounds.x - bounds.width;
			}
			break;
		case MOVE:
			newBounds.x = bounds.x + dx;
			break;
		default:
		}
		switch (operation) {
		case TOP:
		case TOP_LEFT:
		case TOP_RIGHT:
			newBounds.y = bounds.y + dy;
			newBounds.height = bounds.height - dy;
			break;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			newBounds.height = bounds.height + dy;
			break;
		case MOVE:
			newBounds.y = bounds.y + dy;
			break;
		default:
		}
		if (movementStep > 1) {
			newBounds.x -= newBounds.x % 5;
			newBounds.y -= newBounds.y % 5;
			newBounds.width -= newBounds.width % 5;
			newBounds.height -= newBounds.height % 5;
		}
		boundaryChanged(bounds, newBounds);
		setBounds(newBounds.x, newBounds.y, newBounds.width, newBounds.height);

	}

	public void boundaryChanged(Rectangle oldBounds, Rectangle newBounds) {
		logger.debug("boundaryChanged height:" + oldBounds.height + ">" + newBounds.height + " - y:" + oldBounds.y + ">" + newBounds.y);
		logger.debug("boundaryChanged width:" + oldBounds.width + ">" + newBounds.width + " - x:" + oldBounds.x + ">" + newBounds.x);
		if (newBounds.width < 0) {
			newBounds.x = newBounds.x + newBounds.width;
			newBounds.width = newBounds.width * -1;
		}
		if (newBounds.height < 0) {
			newBounds.y = newBounds.y + newBounds.height;
			newBounds.height = newBounds.height * -1;
		}

	}

	public AbstractCommand duplicate() {
		AbstractCommand clone = clone();
		clone.currentStatus = Status.START;
		clone.selected = false;
		clone.setBounds(new Rectangle(this.getBounds()));
		clone.setName((getName() == null ? "" : getName()) + "-COPY");
		clone.setId(getClass().getSimpleName().substring(3) + System.nanoTime());
		return clone;
	}

	public void onDelete() {
	}

	public void mouseDragged(int x, int y) {
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeListeners.add(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeListeners.remove(l);
	}

	public void firePropertyChange(PropertyChangeEvent pc) {
		for (int i = 0; i < propertyChangeListeners.size(); i++) {
			PropertyChangeListener l = propertyChangeListeners.get(i);
			if (l == null) {
				propertyChangeListeners.remove(i);
				continue;
			}
			l.propertyChange(pc);
		}
	}

	public void setPropertyValue(String name, Object value) {
		BeanInfo classInfo;
		try {
			classInfo = Introspector.getBeanInfo(getClass(), Object.class, Introspector.USE_ALL_BEANINFO);
			PropertyDescriptor[] propertyInfoArr = classInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyInfo : propertyInfoArr) {
				String propertyName = propertyInfo.getName();
				if (name.equals(propertyName)) {
					setPropertyInfoValue(value, propertyInfo);
					return;
				}
			}
		} catch (Exception e) {
			logger.error(String.format("Eror setting class %1$s property %2$s", this.getClass().getSimpleName(), name), e);
		}
	}

	private void setPropertyInfoValue(Object value, PropertyDescriptor propertyInfo) throws IllegalAccessException, InvocationTargetException {
		if (propertyInfo.getPropertyType().isInstance(value))
			propertyInfo.getWriteMethod().invoke(this, value);
		else if (propertyInfo.getPropertyType().isAssignableFrom(value.getClass())) {
			propertyInfo.getWriteMethod().invoke(this, value);
		} else if (value instanceof String) {
			try {
				BeanUtils.string2Value(this, propertyInfo.getPropertyType(), propertyInfo.getWriteMethod(), (String) value);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
	}

	public List<CommandProperty> listProperties(String filter) {
		List<CommandProperty> properties = new ArrayList<>();
		BeanInfo classInfo;
		try {
			classInfo = Introspector.getBeanInfo(getClass(), Object.class, Introspector.IGNORE_ALL_BEANINFO);

			PropertyDescriptor[] propertyInfoArr = classInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyInfo : propertyInfoArr) {
				add2PropertyList(properties, propertyInfo);
			}
		} catch (Exception e) {
			logger.error("Error extracting properties", e);
		}
		return properties;
	}

	private void add2PropertyList(List<CommandProperty> properties, PropertyDescriptor propertyInfo) {
		String pName = null;
		Object pValue = null;
		try {
			String clas = propertyInfo.getReadMethod().getDeclaringClass().getSimpleName();
			pName = propertyInfo.getName();
			pValue = propertyInfo.getReadMethod().invoke(this);
			properties.add(new CommandProperty(pName, pValue, clas));
		} catch (Exception ei) {
			logger.warn("Can not retreive value of property " + pName + " of command class " + getClass().getSimpleName());
		}
	}

	public void serialise(OutputStream out) throws Exception {
		BeanUtils.serialiseObject(this, out);
	}

	public void deserialise(Reader in) throws Exception {
		String text = getComponentXml(in);
		text = text.replaceAll(">\n;<", ">\n<");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		/*
				DOMParser parser = new DOMParser();
				try {
					parser.parse(new InputSource(new ByteArrayInputStream(text.getBytes(Charset.forName("UTF8")))));
				} catch (SAXException se) {
					// TODO: log and return
					se.printStackTrace();
					return;
				}
				Document doc = parser.getDocument();
		 * 
		 */
		Document doc = db.parse(new InputSource(new ByteArrayInputStream(text.getBytes(Charset.forName("UTF8")))));
		Element elm = doc.getDocumentElement();
		deserialise(elm);
	}

	public void deserialise(Element elm) {
		BeanUtils.deserialise(this, elm);
	}

	private String getComponentXml(Reader in) throws IOException {
		int count = 1;
		boolean done = false;
		int c;
		StringBuilder buffer = new StringBuilder();
		String cmdEndTag = "</" + getClass().getName() + ">";
		String cmdStartTag = "<" + getClass().getName() + ">";
		logger.debug("PARSING class " + getClass().getSimpleName());
		while (!done) {
			done = (c = in.read()) >= 0;
			buffer.append((char) c);
			if (done)
				done = buffer.length() > cmdEndTag.length();
		}
		done = false;
		while ((c = in.read()) >= 0 && !done) {
			buffer.append((char) c);
			if (buffer.substring(buffer.length() - cmdStartTag.length()).equals(cmdStartTag))
				count++;
			if (buffer.substring(buffer.length() - cmdEndTag.length()).equals(cmdEndTag))
				count--;

			done = (count == 0);
		}

		String text = buffer.toString();
		text = cmdStartTag + text;
		return text;
	}

	public void morph(Operation operation, int ex, int ey) {

	}

	public Operation getOperation(Point click) {
		Rectangle transformedBounds = bounds;
		if (transform != null) {
			Shape shape = transform.createTransformedShape(bounds.getBounds2D());
			transformedBounds = shape.getBounds();
		}
		int l = transformedBounds.x;
		int r = transformedBounds.x + transformedBounds.width;
		int c = transformedBounds.x + transformedBounds.width / 2;
		int t = transformedBounds.y;
		int b = transformedBounds.y + transformedBounds.height;
		int m = transformedBounds.y + transformedBounds.height / 2;
		int x = click.x;
		int y = click.y;
		int s = AbstractCommand.SELECTION_MARKER_SIZE;

		// Context menu activator
		if (x >= l + s && x <= l + 3 * s)
			if (y >= t - s && y <= t + s) {
				return Operation.CONTEXT_MENU;
			}
		if (x >= l - s && x <= l + s) {
			if (y >= t - s && y <= t + s)
				return Operation.TOP_LEFT;
			else if (y >= m - s && y <= m + s)
				return Operation.LEFT;
			else if (y >= b - s && y <= b + s)
				return Operation.BOTTOM_LEFT;
			else
				return null;
		} else if (x >= c - s && x <= c + s) {
			if (y >= t - s && y <= t + s)
				return Operation.TOP;
			else if (y >= m - s && y <= m + s)
				return Operation.MOVE;
			else if (y >= b - s && y <= b + s)
				return Operation.BOTTOM;
			else
				return null;
		} else if (x >= r - s && x <= r + s) {
			if (y >= t - s && y <= t + s)
				return Operation.TOP_RIGHT;
			else if (y >= m - s && y <= m + s)
				return Operation.RIGHT;
			else if (y >= b - s && y <= b + s)
				return Operation.BOTTOM_RIGHT;
			else
				return null;
		}

		return null;
	}

	public int getArea() {
		return getBounds().height * getBounds().width;
	}

	public void openContextMenu() {
		logger.debug("Operation is Context Menu");
		currentStatus = Status.CONTEXT_MENU;
	}

	public void paintContextMenu(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.BLACK);
		List<ContextMenuItem> cmi = getContextMenuItems();
		if (cmi != null && !cmi.isEmpty()) {
			for (int i = 0; i < cmi.size(); i++) {
				ContextMenuItem item = cmi.get(i);
				g.drawRect(getX(), getY() + 25 * i, 100, 25);
				g.drawString(item.getText(), getX() + 20, getY() + 20 * (i + 1));
			}
		}
		g.setColor(color);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getBounds().x + "," + getBounds().y + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<ContextMenuItem> getContextMenuItems() {
		return null;
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public AbstractCommand clone() {
		try {
			return (AbstractCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error(e);
		}
		return null;
	}

	public double getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public void rotate(double angle) {
		AffineTransform back = AffineTransform.getTranslateInstance(-(getX() + getWidth() / 2.0), -(getY() + getHeight() / 2.0));
		AffineTransform rot = AffineTransform.getRotateInstance((angle / 180.0) * Math.PI);
		AffineTransform origin = AffineTransform.getTranslateInstance(getX() + getWidth() / 2.0, getY() + getHeight() / 2.0);

		origin.concatenate(rot);
		origin.concatenate(back);
		if (getTransform() == null)
			setTransform(origin);
		else {
			AffineTransform t = getTransform();
			origin.concatenate(t);
			setTransform(origin);
		}

	}

	public boolean isCaptureMouse() {
		return captureMouse;
	}

	public void setCaptureMouse(boolean captureMouse) {
		this.captureMouse = captureMouse;
	}
}
