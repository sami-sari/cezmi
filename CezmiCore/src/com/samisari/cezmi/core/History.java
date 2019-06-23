package com.samisari.cezmi.core;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class History extends ArrayList<AbstractCommand> implements Serializable, PropertyChangeListener {
	private static final long			serialVersionUID	= 1L;
	private static Logger				logger				= Logger.getLogger(History.class);
	List<HistoryListener>				listeners			= new ArrayList<HistoryListener>();
	HashMap<String, AbstractCommand>	idMap				= new HashMap<String, AbstractCommand>();
	private boolean						dirty				= false;

	public History() {
		super();
	}
	public History(List<AbstractCommand> commands) {
		this();
		addAll(commands);
		for (AbstractCommand c:commands) {
			idMap.put(c.getId(), c);
		}
	}

	public void addListener(HistoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public boolean add(AbstractCommand e) {
		boolean added = super.add(e);
		if (added) {
			idMap.put(e.getId(), e);
			for (HistoryListener listener : listeners) {
				listener.onInserted(e);
			}
		}
		return added;
	}

	@Override
	public void add(int index, AbstractCommand e) {
		super.add(index, e);
		idMap.put(e.getId(), e);
		for (HistoryListener listener : listeners) {
			listener.onInserted(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean addAll(Collection c) {
		boolean added = super.addAll(c);
		if (added) {
			for (AbstractCommand cmd : (Collection<AbstractCommand>) c) {
				idMap.put(cmd.getId(), cmd);
				for (HistoryListener listener : listeners) {
					listener.onInserted(c);
				}
			}
		}
		return added;
	}

	@Override
	public void clear() {
		for (AbstractCommand cmd : this) {
			idMap.remove(cmd.getId());
		}
		super.clear();
		for (HistoryListener listener : listeners) {
			listener.onModelChanged();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean addAll(int index, Collection c) {
		boolean added = super.addAll(index, c);
		if (added) {
			for (AbstractCommand cmd : (Collection<AbstractCommand>) c) {
				idMap.put(cmd.getId(), cmd);
			}
		}
		return added;
	}

	@Override
	public AbstractCommand remove(int index) {
		AbstractCommand removed = super.remove(index);
		if (removed != null) {
			idMap.remove(removed.getId());
			for (HistoryListener listener : listeners) {
				listener.onRemoved(removed);
			}
		}
		return removed;
	}

	@Override
	public boolean remove(Object c) {
		boolean removed = super.remove(c);
		if (removed) {
			idMap.remove(((AbstractCommand) c).getId());
			for (HistoryListener listener : listeners) {
				listener.onRemoved(c);
			}
		}
		return removed;
	}

	public void serialise(OutputStream out) {
		for (AbstractCommand c : this) {
			try {
				c.serialise(out);
			} catch (Exception e) {
				logger.warn(String.format("Class %1$s serialize edilemedi", c.getClass().getSimpleName()), e);
			}
		}
	}

	public void deserialise(InputStreamReader in) {
		int c;
		StringBuilder text = new StringBuilder();
		try {
			while ((c = in.read()) >= 0) {
				text.append((char) c);
				if (c == '>') {
					String className = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
					AbstractCommand command = (AbstractCommand) Class.forName(className).newInstance();
					consumeCommand(in, command);
					this.add(command);
					text = new StringBuilder();
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
			logger.error("Deserialize ederken hata alındı", e);
		}
	}

	private void consumeCommand(InputStreamReader in, AbstractCommand command) {
		try {
			command.deserialise(in);
			command.buildBounds();
		} catch (Exception e) {
			logger.error(String.format("%1$s komutu deserialize edilirken hata alındı", command.getClass().getSimpleName()), e);
		}
	}

	public void deserialise(Element elm) {
		NodeList children = elm.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element cmdElm = (Element) children.item(i);
				String className = cmdElm.getNodeName();
				AbstractCommand command = null;
				try {
					command = (AbstractCommand) Class.forName(className).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					logger.error("", e);
					continue;
				}
				command.deserialise(cmdElm);
				command.buildBounds();
				this.add(command);
			}

		}
	}

	public Rectangle getMaxBounds() {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		for (AbstractCommand c : this) {
			int newWidth = c.getBounds().x + c.getBounds().width;
			int newHeight = c.getBounds().y + c.getBounds().height;
			if (newWidth > width)
				width = newWidth;
			if (newHeight > height)
				height = newHeight;
		}
		return new Rectangle(x, y, width, height);
	}

	public AbstractCommand getCommand(String id) {
		AbstractCommand command = idMap.get(id);
		for (int i = 0; command == null && i < size(); i++) {
			if (get(i) instanceof HasComponents) {
				command = ((HasComponents) get(i)).getComponents().getCommand(id);
			}
		}
		return command;
	}

	public int getIndex(AbstractCommand cmd) {
		return indexOf(cmd);
	}

	public List<AbstractCommand> getCommandsInRange(int x, int y, int radius) {
		List<AbstractCommand> list = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			AbstractCommand command = get(i);
			if (command.isInRange(x, y, radius))
				list.add(command);
		}
		return list;
	}

	public List<AbstractCommand> getSelectedCommands() {
		List<AbstractCommand> list = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			AbstractCommand command = get(i);
			if (command.isSelected())
				list.add(command);
		}
		return list;
	}

	public void clearSelections() {
		for (int i = 0; i < size(); i++) {
			AbstractCommand command = get(i);
			if (command.isSelected())
				command.setSelected(false);
			;
		}
	}

	@Override
	public Object clone() {
		History h = new History();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serialise(out);
		byte[] bytes = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			logger.error("History kopyalanırken hata alındı", e);
		}
		h.deserialise(new InputStreamReader(new ByteArrayInputStream(bytes)));
		return h;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setDirty(true);

	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
