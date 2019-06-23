package com.samisari.cezmi.component;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.HasComponents;
import com.samisari.cezmi.core.History;

public class CmdLayer<E extends AbstractCommand> extends CmdConnectableRectangle implements  Serializable {
	private static final long				serialVersionUID		= 1L;
	private String							zOrder;
	private boolean							visible					= true;
	private List<AbstractCommand>			components				= new ArrayList<>();
	protected Rectangle						originalBounds;
	protected HashMap<String, Rectangle>	originalChildrenBounds	= new HashMap<String, Rectangle>();

	public CmdLayer() {
		super();
		new CmdLayout().setContainer(this);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		super.run();
		components.addAll((List<E>) CommandManager.getDeaultInstance().getSelectedCommands());
		History h = CommandManager.getDeaultInstance().getHistory();
		for (AbstractCommand c : components) {
			if (c.isSelected())
				h.remove(c);
			c.setSelected(false);
		}
		computeBounds();
		// Give relative bounds to components
		for (AbstractCommand c : components) {
			c.setParentId(this.getId());
			c.getBounds().x -= getX();
			c.getBounds().y -= getY();
			originalChildrenBounds.put(c.getId(), c.getBounds());
			c.boundaryChanged(new Rectangle(0, 0, 0, 0), c.getBounds());
		}

		CommandManager.getDeaultInstance().getHistory().add(this);
		setVisible(true);
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private void computeBounds() {
		int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE, right = 0, bottom = 0;
		for (AbstractCommand component : components) {
			Rectangle b = component.getBounds();
			if (b != null) {
				if (b.x < left)
					left = b.x;
				if (b.y < top)
					top = b.y;
				if (b.x + b.width > right)
					right = b.x + b.width;
				if (b.y + b.height > bottom)
					bottom = b.y + b.height;

			}
		}
		setBounds(left, top, right - left, bottom - top);
		originalBounds = getBounds();

		boundaryChanged(getBounds(), getBounds());
	}

	@Override
	public void paint(Graphics g) {
		if (isSelected()) {
			super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
			super.paintSelection(g);
		}
		double tx = (double) (getX());
		double ty = (double) (getY());
		AffineTransform translate = AffineTransform.getTranslateInstance(tx, ty);
		if (visible) {
			if (components != null)
				for (AbstractCommand cmd : components) {
					cmd.setTransform(translate);
					cmd.paint(g);
					cmd.setTransform(null);
				}
		}
	}

	@Override
	public void boundaryChanged(Rectangle oldBounds, Rectangle newBounds) {
		super.boundaryChanged(oldBounds, newBounds);
		// Good for moving sucks when scaling
		//		if (originalChildrenBounds != null && !originalChildrenBounds.isEmpty())
		//			for (AbstractCommand c : components) {
		//				double scalex = ((double) newBounds.width) / ((double) originalBounds.width);
		//				double scaley = ((double) newBounds.height) / ((double) originalBounds.height);
		//				int x = (int) (((double) originalChildrenBounds.get(c.getId()).getX()) * scalex);
		//				int width = (int) (((double) originalChildrenBounds.get(c.getId()).getWidth()) * scalex);
		//				int y = (int) (((double) originalChildrenBounds.get(c.getId()).getY()) * scaley);
		//				int height = (int) (((double) originalChildrenBounds.get(c.getId()).getHeight()) * scaley);
		//				Rectangle newCBounds = new Rectangle(x, y, width, height);
		//				Rectangle oldCBounds = c.getBounds();
		//				c.setBounds(newCBounds);
		//				c.boundaryChanged(oldCBounds, newCBounds);
		//			}
	}

	@Override
	public void deserialise(Reader in) throws Exception {
		super.deserialise(in);
		originalBounds = new Rectangle(getBounds());
		for (AbstractCommand c : components) {
			originalChildrenBounds.put(c.getId(), new Rectangle(c.getBounds()));
		}

	}

	public String getzOrder() {
		return zOrder;
	}

	public void setzOrder(String zOrder) {
		this.zOrder = zOrder;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<AbstractCommand> getComponents() {
		return components;
	}

	public void setComponents(List<AbstractCommand> components) {
		this.components = components;
	}

	public void addComponent(AbstractCommand cmd) {
		components.add(cmd);
		originalChildrenBounds.put(cmd.getId(), new Rectangle(cmd.getBounds()));
		computeBounds();
	}

	public void removeComponent(AbstractCommand cmd) {
		components.remove(cmd);
		originalChildrenBounds.remove(cmd.getId());
		computeBounds();
	}

	public Rectangle getOriginalBounds() {
		return originalBounds;
	}

	public void setOriginalBounds(Rectangle originalBounds) {
		this.originalBounds = originalBounds;
	}

	public HashMap<String, Rectangle> getOriginalChildrenBounds() {
		return originalChildrenBounds;
	}

	public void setOriginalChildrenBounds(HashMap<String, Rectangle> originalChildrenBounds) {
		this.originalChildrenBounds = originalChildrenBounds;
	}
	@Override
	public AbstractCommand duplicate() {
		@SuppressWarnings("unchecked")
		CmdLayer<AbstractCommand> clone = (CmdLayer<AbstractCommand>)super.duplicate();
		History components= new History();
		for (AbstractCommand comp:clone.getComponents()) {
			components.add(comp.duplicate());
		}
		clone.setComponents(components);
		clone.setOriginalBounds(new Rectangle(getOriginalBounds()));
		return clone;
	}
}
