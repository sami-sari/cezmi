package com.samisari.cezmi.component;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;

public class CmdLayout extends AbstractCommand implements PropertyChangeListener {
	private static final Logger					logger				= Logger.getLogger(CmdLayout.class);
	private static final long					serialVersionUID	= 1L;
	private CmdLayer<? extends AbstractCommand>	container;
	public PropertyChangeSupport				pcs;

	@Override
	public void run() {
		super.run();
		container.addPropertyChangeListener(this);
	}

	public void layout(Rectangle bounds) {
		Rectangle newBounds = bounds;
		Rectangle originalBounds = container.getOriginalBounds();
		HashMap<String, Rectangle> originalChildrenBounds = container.getOriginalChildrenBounds();
		if (bounds == null || originalBounds == null || originalChildrenBounds == null)
			return;
		for (AbstractCommand c : container.getComponents()) {
			logger.debug(c.getBounds());
			double scalex = ((double) newBounds.width) / ((double) originalBounds.width);
			double scaley = ((double) newBounds.height) / ((double) originalBounds.height);
			int x = (int) (((double) originalChildrenBounds.get(c.getId()).getX()) * scalex);
			int width = (int) (((double) originalChildrenBounds.get(c.getId()).getWidth()) * scalex);
			int y = (int) (((double) originalChildrenBounds.get(c.getId()).getY()) * scaley);
			int height = (int) (((double) originalChildrenBounds.get(c.getId()).getHeight()) * scaley);
			Rectangle newCBounds = new Rectangle(x, y, width, height);
			Rectangle oldCBounds = c.getBounds();
			c.setBounds(newCBounds);
			c.boundaryChanged(oldCBounds, newCBounds);
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt);
		layout(new Rectangle(container.getBounds()));
	}

	public CmdLayer<? extends AbstractCommand> getContainer() {
		return container;
	}

	public void setContainer(CmdLayer<? extends AbstractCommand> cmdLayer) {
		this.container = cmdLayer;
		run();
	}
}
