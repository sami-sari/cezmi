package com.samisari.gui.table.propertiestable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.cezmi.core.ConsolePropertyManager;

public class PropertyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 7183418959488802317L;
	private static final Logger logger = Logger
			.getLogger(PropertyTableModel.class);
	private AbstractCommand command;
	private List<CommandProperty> properties;
	private String filter = null;

	public PropertyTableModel(final AbstractCommand command, String filter) {
		super();
		setFilter(filter);
		logger.debug("New property table model "
				+ (command == null ? "NULL" : command.getClass().getName()));
		addTableModelListener((e)-> {
				Object newValue = getValueAt(e.getFirstRow(), 1);
				String propertyName = (String) getValueAt(e.getFirstRow(), 0);
				try {
					Method propertySetter = null;
					String setterName = "set"
							+ propertyName.substring(0, 1).toUpperCase(
									Locale.ENGLISH) + propertyName.substring(1);
					Class<?> temp = command.getClass();
					while (propertySetter == null) {
						try {
							propertySetter = temp.getDeclaredMethod(setterName,
									newValue.getClass());
						} catch (NoSuchMethodException nsme) {
							try {
								if (newValue instanceof Integer) {
									propertySetter = temp.getDeclaredMethod(
											setterName, int.class);
								}
							} catch (NoSuchMethodException nsme1) {
							}
							temp = temp.getSuperclass();
						}
					}
					propertySetter.invoke(command, newValue);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				ConsolePropertyManager.getDefaultInstance().getConsolePanel()
						.repaint();
		});
		setCommand(command);
	}

	@Override
	public int getRowCount() {
		if (command == null)
			return 0;
		return properties.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (command == null)
			return null;
		CommandProperty property = properties.get(rowIndex);
		try {
			return columnIndex == 0 ? property.getName() : property.getSerializedForm();
		} catch (Exception e) {
			logger.error(null, e);
		}
		return null;
	}

	public void setCommand(AbstractCommand command) {
		this.command = command;
		if (command != null)
			properties = command.listProperties(filter);
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (command == null)
			return;
		CommandProperty property = properties.get(rowIndex);
		if (columnIndex != 0) {
			property.setValue(value);
		} else {
			property.setName((String) value);
		}
		//fireTableChanged(new TableModelEvent(this, rowIndex));
	}

	public AbstractCommand getCommand() {
		return command;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
